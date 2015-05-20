package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class StockRowMapper implements RowMapper<SourceStock> {

	@Override
	public SourceStock mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceStock stock = new SourceStock();
		stock.setStockId(rs.getLong("stock_id"));
		stock.setName(rs.getString("stock_name"));
		stock.setUniqueName(rs.getString("stock_uniquename"));
		stock.setDescription(rs.getString("stock_description"));
				
		return stock;
	}

}
