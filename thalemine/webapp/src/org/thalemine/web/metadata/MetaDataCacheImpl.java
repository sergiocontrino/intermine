package org.thalemine.web.metadata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.intermine.ObjectStoreInterMineImpl;
import org.intermine.objectstore.query.ConstraintSet;
import org.intermine.objectstore.query.ContainsConstraint;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.QueryCollectionReference;
import org.intermine.objectstore.query.QueryField;
import org.intermine.objectstore.query.QueryFunction;
import org.intermine.objectstore.query.QueryValue;
import org.intermine.objectstore.query.Results;
import org.intermine.objectstore.query.ResultsRow;
import org.intermine.objectstore.query.SimpleConstraint;
import org.thalemine.web.controller.DataCategoriesController;
import org.thalemine.web.domain.datacategory.Category;
import org.thalemine.web.domain.datacategory.DataSetSO;
import org.intermine.api.InterMineAPI;
import org.intermine.bio.util.Constants;
import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.Model;
import org.intermine.model.InterMineObject;
import org.intermine.model.bio.DataSet;
import org.intermine.model.bio.DataSource;
import org.intermine.model.bio.Gene;
import org.intermine.model.bio.SequenceFeature;

public class MetaDataCacheImpl {

	protected static final Logger log = Logger.getLogger(MetaDataCacheImpl.class);

	private static Map<String, Category> dataCategoryMap = null;

	public static void initDataCategories(ObjectStore os, InterMineAPI im)  throws ObjectStoreException, Exception {

		dataCategoryMap = new HashMap<String, Category>();
		
		getDataCategoryStat(im, "test",os);

	}

	public static synchronized Map<String, Category> getDataCategories(ObjectStore os, InterMineAPI im) throws ObjectStoreException, Exception {

		if (dataCategoryMap == null) {
			initDataCategories(os, im);
		}
		
		log.info("Initialization of Categories Summary has started.");
		log.info("Initialization of Categories Summary has completed." + "Data Categories Size: " + dataCategoryMap.size());
		
		return dataCategoryMap;
	}

	private static Map<String, Category> readCategorySummaries(ObjectStore os) {
		long startTime = System.currentTimeMillis();
		dataCategoryMap = new HashMap<String, Category>();

		long totalTime = System.currentTimeMillis() - startTime;

		return dataCategoryMap;
	}
	
	
	private static Query getCategoryQuery(InterMineAPI im, String categoryName
			) {
	
		Model model = im.getModel();
		
		Query outerQuery = new Query();
		QueryClass dataSourceCls = new QueryClass(DataSource.class);
		QueryClass dataSetCls = new QueryClass(DataSet.class);
		
		ConstraintSet outerQueryMainCS = new ConstraintSet(ConstraintOp.AND);
		ConstraintSet outerQueryCS = new ConstraintSet(ConstraintOp.OR);
		
		outerQuery.setDistinct(true);

		// outer query from clause
		outerQuery.addFrom(dataSourceCls);
		outerQuery.addFrom(dataSetCls);
		
		outerQuery.addToSelect(dataSourceCls);
		outerQuery.addToSelect(dataSetCls);
		
		// dataset count
		QueryFunction count = new QueryFunction();
		outerQuery.addToSelect(count);
		
		outerQuery.addToGroupBy(new QueryField(dataSourceCls, "id"));
		outerQuery.addToGroupBy(new QueryField(dataSetCls, "id"));
		
		QueryCollectionReference dataSetCollection = new QueryCollectionReference(dataSourceCls, "dataSets");
		outerQueryMainCS.addConstraint(new ContainsConstraint(dataSetCollection, ConstraintOp.CONTAINS,
				dataSetCls));
		
		outerQuery.setConstraint(outerQueryMainCS);
				
		return outerQuery;
		
	}
	
	
	private static Query getGeneQuery(InterMineAPI im, String categoryName, InterMineObject object
			) {
	
		Model model = im.getModel();
		
		Query query = new Query();
		QueryClass dataSetCls = new QueryClass(DataSet.class);
		QueryClass geneCls = new QueryClass(Gene.class);
		
		ConstraintSet queryCS = new ConstraintSet(ConstraintOp.AND);
				
		query.setDistinct(true);

		// outer query from clause
		query.addFrom(dataSetCls);
		query.addFrom(geneCls);
		
		query.addToSelect(dataSetCls);
			
		// gene count
		QueryFunction count = new QueryFunction();
		query.addToSelect(count);
		
		//.query.addToSelect(geneCls);
		
		query.addToGroupBy(new QueryField(dataSetCls, "id"));
		
		QueryField dsId = new QueryField(dataSetCls, "id");
		SimpleConstraint sc = new SimpleConstraint(dsId, ConstraintOp.EQUALS,
                new QueryValue(object.getId()));
		
		queryCS.addConstraint(sc);
        
		QueryCollectionReference geneCollection = new QueryCollectionReference(object, "bioEntities");
		queryCS.addConstraint(new ContainsConstraint(geneCollection, ConstraintOp.CONTAINS,
				geneCls));
		
		QueryCollectionReference geneClassCollection = new QueryCollectionReference(dataSetCls,
				"bioEntities");
		queryCS.addConstraint(new ContainsConstraint(geneClassCollection, ConstraintOp.CONTAINS,
				geneCls));
					
		query.setConstraint(queryCS);
				
		return query;
		
	}
	
	
	private static Query getFeatureQuery(InterMineAPI im, String categoryName, InterMineObject object
			) {
	
		Model model = im.getModel();
		
		Query query = new Query();
		QueryClass dataSetCls = new QueryClass(DataSet.class);
		QueryClass sequenceFeatureCls = new QueryClass(SequenceFeature.class);
		
		ConstraintSet queryCS = new ConstraintSet(ConstraintOp.AND);
				
		query.setDistinct(true);

		// outer query from clause
		query.addFrom(dataSetCls);
		query.addFrom(sequenceFeatureCls);
		
		query.addToSelect(dataSetCls);
			
		// gene count
		QueryFunction count = new QueryFunction();
		query.addToSelect(count);
		
		//.query.addToSelect(geneCls);
		
		query.addToGroupBy(new QueryField(dataSetCls, "id"));
		
		QueryField dsId = new QueryField(dataSetCls, "id");
		SimpleConstraint sc = new SimpleConstraint(dsId, ConstraintOp.EQUALS,
                new QueryValue(object.getId()));
		
		queryCS.addConstraint(sc);
        
		QueryCollectionReference featureCollection = new QueryCollectionReference(object, "bioEntities");
		queryCS.addConstraint(new ContainsConstraint(featureCollection, ConstraintOp.CONTAINS,
				sequenceFeatureCls));
		
		QueryCollectionReference featureClassCollection = new QueryCollectionReference(dataSetCls,
				"bioEntities");
		queryCS.addConstraint(new ContainsConstraint(featureClassCollection, ConstraintOp.CONTAINS,
				sequenceFeatureCls));
					
		query.setConstraint(queryCS);
				
		return query;
		
	}
	private static Set<DataSource> getDataCategoryStat(InterMineAPI im, final String categoryName, ObjectStore os) throws ObjectStoreException, Exception {
		
		Set<DataSource> result = new HashSet<DataSource>();
		long startTime = System.currentTimeMillis();
		Exception exception = null;

		Query query = getCategoryQuery(im, categoryName);
		Iterator<?> iterator =getResultSetIterator(query, os);

		int itemCount = 0;
		
		try {

			if (query == null) {
				exception = new Exception("Query cannot be null.");
				throw exception;
			}

			log.info("Input Query: " + query.toString() + "Idl Query: " + query.getIqlQuery());

			while (iterator.hasNext()) {

				ResultsRow item = (ResultsRow) iterator.next();
				DataSource dataSource = (DataSource) item.get(0);

				String dataCategoryName = dataSource.getName();
				log.info("Current DataSource Item: = " + dataSource);
				log.info("Data Category Name: = " + dataCategoryName);
				
				Category category = getCategory(dataCategoryName);

				itemCount++;

				log.info(" Current Item Count:" + itemCount);

				Object countObject = (Object) item.get(2);
				
				InterMineObject dataSetObject = (InterMineObject) item.get(1);
				DataSet dataSet = (DataSet) dataSetObject;
				
				log.info("Count Data Set Object: = " + countObject);

				Long count = (Long) countObject;
								
				DataSetSO dataSetSO = getDataSetStat(im, "test", os, dataSetObject);
				category.addDataSet(dataSetSO);
				
				result.add(dataSource);

			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error occurred while executing Data Summary Query." + " ; Message: " + exception.getMessage()
						+ "; Cause: " + exception.getCause());
				throw exception;
			} else {
				log.info("Data Summary Query has successfully completed. " + "; Result Set Size: " + result.size()
						);
			}
		}

			
		return result;
		
	}
	
	
	private static DataSetSO getDataSetStat(InterMineAPI im, final String categoryName, ObjectStore os, InterMineObject object) throws ObjectStoreException, Exception {
		
		DataSet dataSet = (DataSet) object;
		
		String dataSetName = dataSet.getName();
		DataSetSO dataSetSO = new DataSetSO(dataSetName);
		
		long geneCount = getDataSetGeneCount(im,categoryName,os, object);
		long featureCount = getDataSetFeatureCount(im,categoryName,os, object);
		dataSetSO.setGeneCount(geneCount);
		dataSetSO.setFeatureCount(featureCount);
			
		return dataSetSO;
		
		
	}
	
	
	private static long getDataSetGeneCount(InterMineAPI im, final String categoryName, ObjectStore os, InterMineObject object) throws ObjectStoreException, Exception {
		
		Set <DataSet> result = new HashSet<DataSet>();
		long startTime = System.currentTimeMillis();
		Exception exception = null;
		long resultCount = 0;

		Query query = getGeneQuery(im, categoryName, object);
		Iterator<?> iterator =getResultSetIterator(query, os);

		int itemCount = 0;

		try {

			if (query == null) {
				exception = new Exception("Query cannot be null.");
				throw exception;
			}

			log.info("Input Query: " + query.toString() + "Idl Query: " + query.getIqlQuery());

			while (iterator.hasNext()) {

				ResultsRow item = (ResultsRow) iterator.next();
				DataSet dataSetItem = (DataSet) item.get(0);

				log.info("Current DataSet Item: = " + dataSetItem);

				itemCount++;

				log.info(" Current DataSet Count:" + itemCount);

				//Object countObject = (Object) item.get(1);
				resultCount = (Long) item.get(1);
				
				log.info("Gene Count Object: = " + resultCount);
								
				result.add(dataSetItem);
				
			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error occurred while executing Data Set Summary Query." + " ; Message: " + exception.getMessage()
						+ "; Cause: " + exception.getCause());
				throw exception;
			} else {
				log.info("Data Set Summary Query has successfully completed. " + "; Result Set Size: " + result.size()
						);
			}
		}
		
		return resultCount;
	}
	
	
private static long getDataSetFeatureCount(InterMineAPI im, final String categoryName, ObjectStore os, InterMineObject object) throws ObjectStoreException, Exception {
		
		Set <DataSet> result = new HashSet<DataSet>();
		long startTime = System.currentTimeMillis();
		Exception exception = null;
		long resultCount = 0;

		Query query = getFeatureQuery(im, categoryName, object);
		Iterator<?> iterator =getResultSetIterator(query, os);

		int itemCount = 0;

		try {

			if (query == null) {
				exception = new Exception("Query cannot be null.");
				throw exception;
			}

			log.info("Input Query: " + query.toString() + "Idl Query: " + query.getIqlQuery());

			while (iterator.hasNext()) {

				ResultsRow item = (ResultsRow) iterator.next();
				DataSet dataSetItem = (DataSet) item.get(0);

				log.info("Current DataSet Item: = " + dataSetItem);

				itemCount++;

				log.info(" Current DataSet Count:" + itemCount);

				//Object countObject = (Object) item.get(1);
				resultCount = (Long) item.get(1);
				
				log.info("Feature Count Object: = " + resultCount);
								
				result.add(dataSetItem);
				
			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error occurred while executing Data Set Summary Query." + " ; Message: " + exception.getMessage()
						+ "; Cause: " + exception.getCause());
				throw exception;
			} else {
				log.info("Data Set Summary Query has successfully completed. " + "; Result Set Size: " + result.size()
						);
			}
		}
		
		return resultCount;
	}
	private static Iterator<?> getResultSetIterator(final Query query, ObjectStore os) throws ObjectStoreException {

		log.info("In getResultSetIterator start");

		Results res = os.execute(query, 5000, true, false, true);

		if (res != null) {
			log.info("Result Set Size:" + res.size());
		}

		log.info("In getResultSetIterator end");
		return res.iterator();
	}
	
	
	private static Category addCategory (String name) {
		 
		boolean result = false;
		Category category = null;
		
		 if (!dataCategoryMap.containsKey(name)){
			 
			 category = createCategory(name);
			 dataCategoryMap.put(name, category);
		 }
		 
		 return category;
	}
	
	private static Category createCategory(String name){
		Category category = new Category(name);
		return category;
	}
	
	private static Category getCategory(String name){
		
		if (!dataCategoryMap.containsKey(name)){
			Category category = addCategory(name);
			return category;
		 }else{
			 Category category = dataCategoryMap.get(name);
			 return category;
		 }
	}

}
