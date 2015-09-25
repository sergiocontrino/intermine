package org.thalemine.web.database.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.thalemine.web.database.jdbc.core.RowMapper;
import org.thalemine.web.domain.datacategory.DataSetTO;
import org.thalemine.web.domain.datacategory.DataSummaryVO;


public class DataSummaryRowMapper implements RowMapper<DataSummaryVO> {

	@Override
	public DataSummaryVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DataSummaryVO item = new DataSummaryVO();
		item.setCategoryName(rs.getString("category_name"));
		item.setDataSourceId(rs.getLong("datasource_id"));
		item.setDataSetId(rs.getLong("dataset_id"));
		item.setDataSourceName(rs.getString("datasource_name"));
		item.setDataSourceDescription(rs.getString("datasource_description"));
		item.setDataSourceUrl(rs.getString("datasource_url"));
		item.setDataSetName(rs.getString("dataset_name"));
		item.setDataSetDescription(rs.getString("dataset_description"));
		item.setDataSetUrl(rs.getString("dataset_url"));
		item.setDataSetVersion(rs.getString("dataset_version"));
		item.setPubMedId(rs.getString("pubmed_id"));
		item.setYear(rs.getString("year"));
		item.setAuthor(rs.getString("authors"));
		item.setGeneCount(rs.getInt("gene_count"));
		item.setFeatureCount(rs.getInt("feature_count"));
	
		return item;
	}

}
