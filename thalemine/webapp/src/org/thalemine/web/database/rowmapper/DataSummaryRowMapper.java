package org.thalemine.web.database.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.thalemine.web.database.jdbc.core.RowMapper;
import org.thalemine.web.domain.datacategory.DataSetTO;
import org.thalemine.web.domain.datacategory.DataSummaryVO;

public class DataSummaryRowMapper implements RowMapper<DataSummaryVO> {

	private static final Logger log = Logger.getLogger(DataSummaryRowMapper.class);

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
		String data_source_description = rs.getString("datasource_description");
		String data_set_description = rs.getString("dataset_name");
				
		if (StringUtils.isNotBlank(data_source_description) && data_set_description.equals("PubMed to gene mapping")){
			StringBuilder pubDataSource = new StringBuilder();
			pubDataSource.append(data_source_description + "\n");
			pubDataSource.append(" ;TAIR/The Arabidopsis Information Resource " +"\n");
			pubDataSource.append(" ; UniProt");
			item.setDataSourceDescription(pubDataSource.toString());
		}
					
		item.setDataSetDescription(rs.getString("dataset_description"));
		item.setDataSetUrl(rs.getString("dataset_url"));
		item.setDataSetVersion(rs.getString("dataset_version"));
		item.setPubMedId(rs.getString("pubmed_id"));
		item.setYear(rs.getString("year"));

		String name = rs.getString("authors");
		String year = rs.getString("year");

		String processedName = processAuthors(name, year);
		log.info("Processed Name:" + processedName);
		item.setAuthor(processedName);

		item.setGeneCount(rs.getString("gene_count"));
		
		if (item.getCategoryName().equals("Genes")){
			item.setFeatureCount(rs.getString("gene_count"));
		}else{
			item.setFeatureCount(rs.getString("feature_count"));
		}

		return item;
	}

	String processAuthors(String name, String year) {
		String result = name;

		if (!StringUtils.isBlank(name)) {
			if (!(name.contains("Initiative") || name.contains("Consortium"))) {
				String[] result1 = StringUtils.split(name, " ");
				log.info("Result 1: " + result1);

				String result2 = "";
				if (result1.length > 0) {
					result2 = result1[0];
					log.info("Result 2: " + result2);
				}

				if (!StringUtils.isBlank(result2)) {
					result = result2;
					log.info("Result 3: " + result + " et al");
				}
				result = result + " et al ";

				if (!StringUtils.isBlank(year)) {
					result = result + "., ";

					return result;
				}

			}

			if (!StringUtils.isBlank(year)) {
				result = result + ", ";
			}

			log.info("Result:" + result);
		}

		return result;
	}

}
