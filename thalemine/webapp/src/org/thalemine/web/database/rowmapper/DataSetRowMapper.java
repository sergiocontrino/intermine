package org.thalemine.web.database.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.thalemine.web.database.jdbc.core.RowMapper;
import org.thalemine.web.domain.datacategory.DataSetTO;


public class DataSetRowMapper implements RowMapper<DataSetTO> {

	@Override
	public DataSetTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DataSetTO item = new DataSetTO();
		
		item.setName(rs.getString("name"));
		return item;
	}

}
