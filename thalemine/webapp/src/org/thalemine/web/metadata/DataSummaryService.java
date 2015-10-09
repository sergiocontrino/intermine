package org.thalemine.web.metadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.intermine.objectstore.ObjectStoreException;
import org.thalemine.web.database.DBConnectionManager;
import org.thalemine.web.database.core.reader.DatabaseItemReader;
import org.thalemine.web.database.core.reader.ParseException;
import org.thalemine.web.database.core.reader.UnexpectedInputException;
import org.thalemine.web.database.domain.reader.DataSummaryReader;
import org.thalemine.web.domain.datacategory.DataSummaryVO;

public class DataSummaryService {

	private static final Logger log = Logger.getLogger(DataSummaryService.class);
	private static List<DataSummaryVO> results = null;
	private static List<DataSummaryVO> geneResults = null;
	private static List<DataSummaryVO> detailResults = null;
	private static String SQL;
	private static String SQLDETAIL;
	private static DatabaseItemReader<DataSummaryVO> reader;
	private static DatabaseItemReader<DataSummaryVO> rowDetailreader;

	private static class DataSummaryServiceHolder {

		public static final DataSummaryService INSTANCE = new DataSummaryService();

	}

	public static DataSummaryService getInstance(final String sqlQuery) {
		initialize(sqlQuery);
		return DataSummaryServiceHolder.INSTANCE;
	}

	public static DataSummaryService getInstance(final String sqlQuery, final String detailSQLQuery) {
		initialize(sqlQuery, detailSQLQuery);
		return DataSummaryServiceHolder.INSTANCE;
	}

	public static void initialize(final String sqlQuery) {
		SQL = sqlQuery;
	}

	public static void initialize(final String sqlQuery, final String detailSQLQuery) {
		SQL = sqlQuery;
		SQLDETAIL = detailSQLQuery;
	}

	public static synchronized List<DataSummaryVO> getSummary() throws ObjectStoreException, Exception {

		if (results == null) {
			createDataSummary();
		}

		log.info("Initialization of Categories Summary has started.");
		log.info("Initialization of Categories Summary has completed." + "Data Categories Size: " + results.size());

		return results;
	}

	public static synchronized List<DataSummaryVO> getGeneSummary() throws ObjectStoreException, Exception {

		if (geneResults == null) {
			createDataSummary();
		}

		return geneResults;
	}

	private static void doExecute() {

		try {

			doOpen();

			DataSummaryVO item = null;

			while (reader.hasNext()) {

				item = reader.read();

				if (item.getCategoryName().equals("Chromosomes") || item.getCategoryName().equals("Genes")) {
					geneResults.add(item);

				} else {
					results.add(item);
				}

				log.info("SQL" + reader.getSql());
				log.info("Current Item = " + item);
				log.info("Parameter values:" + reader.getParameterMap());

			}

		} catch (UnexpectedInputException e1) {
			log.error("Error: " + e1.getCause());
			e1.printStackTrace();
		} catch (ParseException e1) {

			log.error("Error:  " + e1.getCause());

			e1.printStackTrace();
		} catch (Exception e1) {

			log.error("Error: " + e1.getCause());

			e1.printStackTrace();
		}

		log.info("DataSet has been generated!");
	}

	private static void doExecuteDetails() {

		try {
			doOpenDetail();

			DataSummaryVO item = null;

			while (rowDetailreader.hasNext()) {

				item = rowDetailreader.read();

				log.info("SQL" + rowDetailreader.getSql());
				log.info("Current Item = " + item);
				log.info("Parameter values:" + rowDetailreader.getParameterMap());
				
				detailResults.add(item);

			}

		}

		catch (UnexpectedInputException e1) {
			log.error("Error: " + e1.getCause());
			e1.printStackTrace();
		} catch (ParseException e1) {

			log.error("Error:  " + e1.getCause());

			e1.printStackTrace();
		} catch (Exception e1) {

			log.error("Error: " + e1.getCause());

			e1.printStackTrace();
		}

		log.info("Detail DataSet has been generated! DataSet Size:" + detailResults.size());

	}

	public static void setReader() throws SQLException, Exception {

		Connection connection = DBConnectionManager.getConnection();
		DatabaseItemReader<DataSummaryVO> dbReader = new DataSummaryReader().getReader(connection);
		dbReader.setSql(SQL);
		reader = dbReader;

		DatabaseItemReader<DataSummaryVO> dbDetailreader = new DataSummaryReader().getReader(connection);
		dbDetailreader.setSql(SQLDETAIL);
		rowDetailreader = dbDetailreader;

	}

	private static void createDataSummary() throws SQLException, Exception {

		setReader();
		results = new ArrayList<DataSummaryVO>();
		detailResults = new ArrayList<DataSummaryVO>();
		geneResults = new ArrayList<DataSummaryVO>();
		doExecute();
		doExecuteDetails();
		doUpdateSummaryDataSet();

	}

	protected static void doOpen() throws Exception {

		if (reader != null) {

			log.info("Opening Cursor: DataSummaryService");

			reader.open();

			log.info("Successfully Opened Cursor in DataSummaryService");

		} else {
			log.error("Error Opening Cursor in DataSummaryService");
		}
	}

	protected static void doOpenDetail() throws Exception {

		if (rowDetailreader != null) {

			log.info("Opening Detail Cursor: DataSummaryService");

			rowDetailreader.open();

			log.info("Successfully Opened Detail Cursor in DataSummaryService");

		} else {
			log.error("Error Opening Deatil Cursor in DataSummaryService");
		}
	}

	private DataSummaryVO findDataSetByName(List<DataSummaryVO> list, String dataSetName) {

		DataSummaryVO element = null;

		for (DataSummaryVO item : list) {

			if (item.getDataSetName().equals(dataSetName)) {

				element = item;
				break;

			}
		}

		return element;
	}

	
	private static DataSummaryVO findDataSetById(List<DataSummaryVO> list, int dataSetId) {

		DataSummaryVO element = null;

		for (DataSummaryVO item : list) {

			if (item.getDataSetId() == (dataSetId)) {

				element = item;
				break;

			}
		}

		return element;
	}
	
	private static void doUpdateSummaryDataSet(){
		if (detailResults.size()> 0){
			doSummaryDataSet();
		}
	}
	
	private static void doSummaryDataSet(){
		
		for (DataSummaryVO item: detailResults){
			
			if (item.getParentDataSetId()!= 0){
				DataSummaryVO foundParentElement = findDataSetById(results, item.getParentDataSetId());
				
				if (foundParentElement!=null){
					int parentIndex = results.indexOf(foundParentElement);
				
					if (parentIndex > 0){
						DataSummaryVO parentElement = results.get(parentIndex);
						
						if (parentElement!= null){
							
							parentElement.addCategoryDetail(item);
						}
					}
				
				}
				
			}
			
		}
	}
}
