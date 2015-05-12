package org.intermine.bio.datalineage;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DataFlowStep extends DataSetStats implements DataLineage {

	private static final Logger log = Logger.getLogger(DataFlowStep.class);
	private DataFlowStepType type;
	private ResultSet resultSet;
	
	public DataFlowStep(){
		super();
	}
	
	public DataFlowStep(DataFlowStepType type, String stepName) {
		super(stepName);
		this.type = type;
	
	}

	public DataFlowStepType getType() {
		return type;
	}

	public void setType(DataFlowStepType type) {
		this.type = type;
	}

	public String toString() {
		return "Data Flow Step: " + " Priority: " + this.getType().getPriority() + "; Type: "
				+ this.getType().getValue() + "; Name: " + this.getName();
	}

	public ResultSet getResultSet(){
		return this.resultSet;
	}
	
	public void setResultSet(ResultSet resultSet){
		this.resultSet = resultSet;
	}
		
	public int getResultSetRowCount() throws SQLException{
		
		
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
	
}
