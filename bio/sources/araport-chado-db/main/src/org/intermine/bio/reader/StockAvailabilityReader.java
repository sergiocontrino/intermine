package org.intermine.bio.reader;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.bio.dataflow.config.SourceDataFlowTaskContainer;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.domain.source.SourceStockAvailability;
import org.intermine.bio.domain.source.SourceStockSynonym;
import org.intermine.bio.item.ItemReader;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.rowmapper.StockAvailabilityRowMapper;
import org.intermine.bio.rowmapper.StockRowMapper;
import org.intermine.bio.rowmapper.StockSynonymRowMapper;

public class StockAvailabilityReader {
	
	public StockAvailabilityReader(){
		
	}

public DatabaseItemReader<SourceStockAvailability> getReader(Connection con){
		
	DatabaseItemReader<SourceStockAvailability> reader = new DatabaseItemReader<SourceStockAvailability>();
	
		reader.setSql(SourceDataFlowTaskContainer.STOCK_AVAILABILITY_SQL);
		reader.setDataSource(con);
		reader.setRowMapper(getRowMapper());
		
		return reader;
	}
	
	
	public 
	StockAvailabilityRowMapper getRowMapper(){
		return new 
				StockAvailabilityRowMapper();
	}
	
}
