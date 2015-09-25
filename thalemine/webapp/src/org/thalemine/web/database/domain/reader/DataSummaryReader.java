package org.thalemine.web.database.domain.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.thalemine.web.database.core.reader.DatabaseItemReader;
import org.thalemine.web.database.rowmapper.DataSetRowMapper;
import org.thalemine.web.database.rowmapper.DataSummaryRowMapper;
import org.thalemine.web.domain.datacategory.DataSetTO;
import org.thalemine.web.domain.datacategory.DataSummaryVO;


public class DataSummaryReader {
	
	private String sql;
	
	public DataSummaryReader(){
		
	}

public DatabaseItemReader<DataSummaryVO> getReader(Connection con){
		
	DatabaseItemReader<DataSummaryVO> reader = new DatabaseItemReader<DataSummaryVO>();
	
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public DataSummaryRowMapper getRowMapper(){
		return new DataSummaryRowMapper();
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
