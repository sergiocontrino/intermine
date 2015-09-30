package org.thalemine.web.metadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.objectstore.ObjectStoreException;
import org.thalemine.web.database.DBConnectionManager;
import org.thalemine.web.database.core.reader.DatabaseItemReader;
import org.thalemine.web.database.core.reader.ParseException;
import org.thalemine.web.database.core.reader.UnexpectedInputException;
import org.thalemine.web.database.domain.reader.DataSetReader;
import org.thalemine.web.database.domain.reader.DataSummaryReader;
import org.thalemine.web.domain.datacategory.DataSetTO;
import org.thalemine.web.domain.datacategory.DataSummaryVO;
import org.thalemine.web.utils.FileUtils;

public class DataSummaryService {
	
	private static final Logger log = Logger.getLogger(DataSummaryService.class);
	private static List<DataSummaryVO> results = null;
	private static List<DataSummaryVO> geneResults = null;
	private static String SQL;
	private static DatabaseItemReader<DataSummaryVO> reader;
		
	private static class DataSummaryServiceHolder {

		public static final DataSummaryService INSTANCE = new DataSummaryService();
		
	}

	public static DataSummaryService getInstance(final String sqlQuery) {
		initialize(sqlQuery);
		return  DataSummaryServiceHolder.INSTANCE;
	}
	
	public static void initialize(final String sqlQuery){
		SQL = sqlQuery;
	}

	public static synchronized List<DataSummaryVO> getSummary() throws ObjectStoreException, Exception {

		if (results == null) {
			createDataSummary();
		}
		
		log.info("Initialization of Categories Summary has started.");
		log.info("Initialization of Categories Summary has completed." + "Data Categories Size: " + results.size());
		
		return results;
	}
	
	public static synchronized List<DataSummaryVO> getGeneSummary() throws ObjectStoreException, Exception {

		if (geneResults == null) {
			createDataSummary();
		}
		
		return geneResults;
	}

	private static void doExecute(){
		
		try {
			
			doOpen();
			
			DataSummaryVO item = null;
						
			while (reader.hasNext()) {
				
				item = reader.read();
				
				if (item.getCategoryName().equals("Genome Assembly") || item.getCategoryName().equals("Genes")) {
					geneResults.add(item);
					
				}else{
					results.add(item);
				}
				
				log.info("SQL" + reader.getSql());
				log.info("Current Item = " + item);
				log.info("Parameter values:" + reader.getParameterMap());
							
			}
			
		} catch (UnexpectedInputException e1) {
			log.error("Error: " + e1.getCause());
			e1.printStackTrace();
		} catch (ParseException e1) {
			
			log.error("Error:  " + e1.getCause());
			
		
			e1.printStackTrace();
		} catch (Exception e1) {
			
			log.error("Error: " + e1.getCause());
			
			e1.printStackTrace();
		}
		
		log.info("DataSet has been generated!");
	}
		
	
	public static void  setReader() throws SQLException, Exception {
		
		Connection connection = DBConnectionManager.getConnection();
		DatabaseItemReader<DataSummaryVO> dbReader = new DataSummaryReader().getReader(connection);
		dbReader.setSql(SQL);
		reader = dbReader;
		
	}

	private static void createDataSummary() throws SQLException, Exception{
		
		setReader();
		results = new ArrayList<DataSummaryVO>();
		geneResults = new ArrayList<DataSummaryVO>();
		doExecute();
	}


	protected static void doOpen() throws Exception {
		
		if (reader !=null){
			
			log.info("Opening Cursor: DataSummaryService");
			
			reader.open();
			
			log.info("Successfully Opened Cursor in DataSummaryService");

		}else{
			log.error("Error Opening Cursor in DataSummaryService");
		}
	}
	
	
}
