package org.thalemine.web.database.domain.reader;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.thalemine.web.utils.FileUtils;


public class DataReaderConfig {
	
	public static final String DATASET_SQL_PATH = "/WEB-INF/sql/dataset.sql";
	public static final String DATASET_SUMMARY_SQL_PATH = "/WEB-INF/sql/data_summary.sql";
	public static final String DATASET_DETAILS_SQL_PATH = "/WEB-INF/sql/pub_category_details.sql";
	
	private static final Logger log = Logger.getLogger(DataReaderConfig.class);
	
	private static class DataReaderConfigHolder {

		public static final DataReaderConfig INSTANCE = new DataReaderConfig();

	}

	public static DataReaderConfig getInstance() {
		
		return DataReaderConfigHolder.INSTANCE;
	}
	
	public String buildSQL(ServletContext context, final String path){
		
		InputStream in = context.getResourceAsStream(path);
		String result = FileUtils.getSqlFileContents(in);
		
		log.info("SQL text: " + result);
		
		return result;
		
		
	}

	

}
