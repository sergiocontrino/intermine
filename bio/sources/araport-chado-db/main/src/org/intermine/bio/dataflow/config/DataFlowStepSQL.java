package org.intermine.bio.dataflow.config;

import org.intermine.bio.datalineage.DataFlowStepType;

public class DataFlowStepSQL {

	private DataFlowStepType stepType;
	private String sql;
	
	public DataFlowStepSQL(){
		
	}
	
	public DataFlowStepSQL(DataFlowStepType stepType, String sql) {
		super();
		this.stepType = stepType;
		this.sql = sql;
	}
	
	public DataFlowStepType getStepType() {
		return stepType;
	}

	public void setStepType(DataFlowStepType stepType) {
		this.stepType = stepType;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
