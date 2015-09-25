package org.thalemine.web.database.domain.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.thalemine.web.database.core.reader.DatabaseItemReader;
import org.thalemine.web.database.rowmapper.DataSetRowMapper;
import org.thalemine.web.domain.datacategory.DataSetTO;


public class DataSetReader {
	
	private String sql;
	
	public DataSetReader(){
		
	}

public DatabaseItemReader<DataSetTO> getReader(Connection con){
		
	DatabaseItemReader<DataSetTO> reader = new DatabaseItemReader<DataSetTO>();
	
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public DataSetRowMapper getRowMapper(){
		return new DataSetRowMapper();
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
