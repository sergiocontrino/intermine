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
		stock.setName(rs.getString("name"));
		stock.setUniqueName(rs.getString("uniquename"));
		stock.setDisplayName((rs.getString("display_name")));
		stock.setStockName(rs.getString("abrc_stock_name"));
		stock.setDescription(rs.getString("description"));
		stock.setStockType(rs.getString("stock_type"));
		stock.setGermplasmTairAccession(rs.getString("germplasm_accession"));
		stock.setStockTairAccession(rs.getString("stock_accession"));
	    stock.setStockCategory(rs.getString("stock_category"));
	    stock.setMutagen(rs.getString("mutagen"));
	    stock.setStockCenterComment(rs.getString("stock_center_comment"));
		
		return stock;
	}

}
