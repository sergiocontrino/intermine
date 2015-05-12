package org.intermine.bio.dataprocessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.intermine.bio.datalineage.DataFlowStep;

public class SQLTaskProcessor implements Callable<DataFlowStep>{
	
	private static final Logger log = Logger.getLogger(SQLTaskProcessor.class);
	private final static StopWatch timer = new StopWatch();
		
	private String sqlStmt;
	private String taskId;
	private int pageSize = 500;
	private PreparedStatement prepStmt;
	private Connection dataSource;
	private DataFlowStep step;
		
	
	public SQLTaskProcessor(String sqlStmt, String taskId, Connection datasource, DataFlowStep step) throws SQLException{
		this.sqlStmt = sqlStmt;
		this.taskId = taskId;
		this.dataSource = datasource;
		this.step = step;
	}
	
	public SQLTaskProcessor(String sqlStmt, String taskId, Connection datasource, int pageSize){
		this.sqlStmt = sqlStmt;
		this.taskId = taskId;
		this.pageSize = pageSize;
	}
	
	
	@Override
	public DataFlowStep call() throws Exception {
			
		PreparedStatement prepStmt = createPreparedStatement(this.sqlStmt);
		log.info("SQL STATEMENT:" + sqlStmt);
		
		//PreparedStatement prepStmt = getDataSource().prepareStatement(sqlStmt);
		
		//setStepStats(prepStmt);
		
		//SQLTask sqlTask = new SQLTask(prepStmt);
		
		//final Future<ResultSet> futureSqlTask = DataService
		//		.getDataServicePool().submit(sqlTask);
		
		//ResultSet resultSet = sqlTask.getResultSet(futureSqlTask, this.taskId);
		
		ResultSet resultSet = prepStmt.executeQuery();
		int rowCount = getResultSetRowCount(resultSet);
		this.step.setResultSet(resultSet);
		this.step.sourceRecordCount.setValue(rowCount);
		
		log.info("ROW COUNT:" + rowCount);
					
		return this.step;
	}

	
	
	
	private PreparedStatement createPreparedStatement(final String sqlStmt) throws SQLException{
		PreparedStatement prepStmt = this.dataSource.prepareStatement(sqlStmt, ResultSet.TYPE_SCROLL_INSENSITIVE,
        		ResultSet.CONCUR_READ_ONLY);
		
		return prepStmt;
		
	}
	
	private int getResultSetRowCount(ResultSet resultSet) throws SQLException{
		
		
		int size = 0;
        try {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        }
        catch(Exception ex) {
            return 0;
        }
        return size;
		
		
	}

	/*
	private void setStepStats(PreparedStatement prepStmt) throws SQLException{
		
		//this.step.sourceRecordCount.setValue(prepStmt.getUpdateCount());
		
	}	
		
  */
	
public DataFlowStep getResult(Future<DataFlowStep> sqlTask){
		
	
		DataFlowStep task = new DataFlowStep();
		
		while (true) {
			if (sqlTask.isDone()) {

				log.info("Task is executed:" + "Task ID : = " + taskId);
				break;
			}
			if (!sqlTask.isDone()) {
				try {
					task= sqlTask.get();
				} catch (InterruptedException e) {
					log.error("Error executing task: " + taskId);
				} catch (ExecutionException e) {
					log.error("Error executing task: " +taskId);
					e.printStackTrace();
				}

				log.info("Waiting for task being executed. Task ID : = "
						+ taskId);
			}

		}
			
		return task;
		
	}
}
	

