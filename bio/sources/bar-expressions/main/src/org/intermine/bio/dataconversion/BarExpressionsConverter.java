package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2011 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.dataconversion.ItemWriter;
import org.intermine.metadata.Model;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.sql.Database;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

/**
 *
 * @author sc
 */
public class BarExpressionsConverter extends BioDBConverter
{
    private static final Logger LOG =
        Logger.getLogger(BarExpressionsConverter.class);
    private static final String DATASET_TITLE = "Expressions data set";
    private static final String EXPERIMENT_CATEGORY = "hormone";
    private static final String DATA_SOURCE_NAME = "atgenexp_hormone";
    private static final int TAXON_ID = 3702;
    private Map<String, String> genes = new HashMap<String, String>();
    private Map<String, String> publications = new HashMap<String, String>();
    private Map<String, String> terms = new HashMap<String, String>();
    private static final String PUBMED_PREFIX = "PubMed";

    private static final List<String> PROPERTY_TYPES =
            Arrays.asList(
                    "stock",
                    "geneticVar",
                    "tissue",
                    "diseased",
                    "growthCondition",
                    "growthStage",
                    "timePoint");



    //pi, item Id
    private Map<String, String> labIdRefMap = new HashMap<String, String>();
    //barId, item Id
    private Map<Integer, String> experimentIdRefMap = new HashMap<Integer, String>();
    //barId, item Id
    private Map<Integer, String> sampleIdRefMap = new HashMap<Integer, String>();
    //probeset, item Id
    private Map<String, String> probeIdRefMap = new HashMap<String, String>();
    //propertyType.propertyValue, item Id
    private Map<String, String> propertyIdRefMap = new HashMap<String, String>();
    //propertyType.propertyValue, objectId
    private Map<String, Integer> propertyIdMap = new HashMap<String, Integer>();

    /**
     * Construct a new BarExpressionsConverter.
     * @param database the database to read from
     * @param model the Model used by the object store we will write to with the ItemWriter
     * @param writer an ItemWriter used to handle Items created
     */
    public BarExpressionsConverter(Database database, Model model, ItemWriter writer) {
        super(database, model, writer, DATA_SOURCE_NAME, DATASET_TITLE);
    }

    /**
     * {@inheritDoc}
     */
    public void process() throws Exception {

    	Connection connection = null;

        if (getDatabase() == null) {
            // no Database when testing and no connection needed
            connection = null;
        } else {
        	// a database has been initialised from properties starting with db.bar-expressions
            connection = getDatabase().getConnection();
        }

        processExperiments(connection);
        processSamples(connection);
        processSampleProperties(connection);
//        processSampleData(connection);
    }

    private void processExperiments(Connection connection)
        throws SQLException, ObjectStoreException {
        ResultSet res = getExperiments(connection);
    	while (res.next()) {
    		Integer experimentBarId = new Integer(res.getInt(1));
    		String title = res.getString(2);
    		String pi = res.getString(3);
    		String affiliation = res.getString(4);
    		String address = res.getString(5);

    		String experimentRefId = createExperiment(experimentBarId, title,
    				pi, affiliation, address);
    		experimentIdRefMap.put(experimentBarId, experimentRefId);
    	}
    	res.close();
    }

    private void processSamples(Connection connection)
            throws SQLException, ObjectStoreException {
            ResultSet res = getSamples(connection);
        	while (res.next()) {
        		Integer experimentBarId = new Integer(res.getInt(1));
        		Integer sampleBarId = new Integer(res.getInt(2));
        		String name = res.getString(3);
        		String alias = res.getString(4);
        		String description = res.getString(5);
        		String control = res.getString(6);
        		String replication = res.getString(7);
        		String file = res.getString(8);

        		String sampleRefId = createSample(experimentBarId,
        				sampleBarId, name, alias, description, control,
        				replication, file);
        		sampleIdRefMap.put(sampleBarId, sampleRefId);
        	}
        	res.close();
//    		LOG.info("sampleIdRefMap: " + sampleIdRefMap.keySet());
    }

    private void processSampleProperties(Connection connection)
            throws SQLException, ObjectStoreException {
            ResultSet res = getSampleProperties(connection);
        	while (res.next()) {
        		Integer sampleBarId = new Integer(res.getInt(1));
        		String stock = res.getString(2);
        		String geneticVar = res.getString(3);
        		String tissue = res.getString(4);
        		String diseased = res.getString(5);
        		String growthCondition = res.getString(6);
        		String growthStage = res.getString(7);
        		String timePoint = res.getString(8);

        		String sampleRefId = createSampleProperties(sampleBarId,
        				stock, geneticVar, tissue, diseased,
        				growthCondition, growthStage, timePoint);
        	}
        	res.close();
    }

    private void processSampleData(Connection connection)
            throws SQLException, ObjectStoreException {
            ResultSet res = getSampleData(connection);
        	while (res.next()) {
        		Integer sampleBarId = new Integer(res.getInt(1));
        		String probeSet = res.getString(2);
        		Double signal = res.getDouble(3);
        		String call = res.getString(4);
        		Double pValue = res.getDouble(5);

        		String sampleDataRefId = createSampleData(sampleBarId,
        				probeSet, signal, call, pValue);
        	}
        	res.close();
    }


    private String createExperiment(Integer experimentBarId, String title,
    		String pi, String affiliation, String address)
    				throws ObjectStoreException {
    	Item experiment = createItem("Experiment");
    	experiment.setAttribute("experimentBarId", experimentBarId.toString());
    	experiment.setAttribute("title", title);
    	experiment.setAttribute("category", EXPERIMENT_CATEGORY);

    	// check if lab already stored
    	if (!labIdRefMap.containsKey(pi)) {
    		LOG.info("LAB: " + pi);
    		String labRefId=createLab(pi, affiliation, address);
    		labIdRefMap.put(pi, labRefId);
    	}

    	experiment.setReference("lab", labIdRefMap.get(pi));
		store(experiment);
    	return experiment.getIdentifier();
    }

	private String createLab(String pi, String affiliation, String address)
			throws ObjectStoreException {
		Item lab = createItem("Lab");
		lab.setAttribute("name", pi);
		lab.setAttribute("affiliation", affiliation);
		lab.setAttribute("address", address);
		store(lab);
		return lab.getIdentifier();
	}

    private String createSample(Integer experimentBarId, Integer sampleBarId,
    		String name, String alias, String description,
    		String control, String replication, String file)
    				throws ObjectStoreException {
    	Item sample = createItem("Sample");
    	sample.setAttribute("barId", sampleBarId.toString());
    	sample.setAttribute("name", name);
    	sample.setAttribute("alias", alias);
    	sample.setAttribute("description", description);
    	sample.setAttribute("control", control);
    	sample.setAttribute("replication", replication);
    	sample.setAttribute("file", file);

		sample.setReference("experiment", experimentIdRefMap.get(experimentBarId));
		store(sample);
    	return sample.getIdentifier();
    }

    private String createSampleProperties(Integer sampleBarId,
    		String stock, String geneticVar, String tissue, String diseased,
    		String growthCondition, String growthStage, String timePoint)
    				throws ObjectStoreException {

    	// create list of values
    	List<String> PROPERTY_VALUES =
                Arrays.asList(
                        stock,
                        geneticVar,
                        tissue,
                        diseased,
                        growthCondition,
                        growthStage,
                        timePoint);
    	// needed to compensate for missing experiments for some samples
    	String sampleIdRef = sampleIdRefMap.get(sampleBarId);

    	if (sampleIdRef == null) {
    		LOG.info("SAMPLE: " + sampleBarId + " has no refs.");
    		return "properties: orphaned sample";
    	}

    	int i=0;
    	for (String p: PROPERTY_TYPES){
    		String name = PROPERTY_TYPES.get(i);
    		String value = PROPERTY_VALUES.get(i);
    		if (value == null || value.isEmpty()) {
        		LOG.info("SAMPLE " + sampleBarId +
        				": empty prop value for " + p);
        		i++;
    			continue;
    		}
    		String propertyRefId = propertyIdRefMap.
    				get(name.concat(value));
    		if (propertyRefId == null) {
    			// prop not yet seen: store it
    			Item property = createItem("SampleProperty");
    			property.setAttribute("name", name);
    			property.setAttribute("value", value);
    			property.addToCollection("samples", sampleIdRef);
    			Integer propObjId = store(property);

    			propertyRefId=property.getIdentifier();
    			propertyIdRefMap.put(name.concat(value), propertyRefId);
    			propertyIdMap.put(name.concat(value), propObjId);
        		LOG.info("SAMPLE " + sampleBarId + ": created property "
        		+ p + " - "+ value);
    		} else {
 			// setting ref if prop already in..
    			Integer propObjId=propertyIdMap.get(name.concat(value));
        		LOG.info("SAMPLE " + sampleBarId + ": adding property "
        		+ p + " - "+ value + " to collection");

                ReferenceList collection = new ReferenceList();
                collection.setName("samples");
                collection.addRefId(sampleIdRef);
    			store(collection, propObjId);
            }
			i++;
    	}
    	return sampleIdRef;
    }


    private String createSampleData(Integer sampleBarId,
    		String probeSet, Double signal, String call, Double pValue)
    				throws ObjectStoreException {

    	// needed to compensate for missing experiments for some samples
    	String sampleIdRef = sampleIdRefMap.get(sampleBarId);

    	if (sampleIdRef == null) {
    		LOG.warn("Orphaned sample: id=" + sampleBarId +
    				". The experiment for this sample is missing.");
    		return "orphaned sample";
    	}

    	String probeRefId = probeIdRefMap.get(probeSet);
    	if (probeRefId == null) {

    	Item probe = createItem("Probe");
    	probe.setAttribute("name", probeSet);
    	store(probe);
    	probeRefId=probe.getIdentifier();
    	probeIdRefMap.put(probeSet, probeRefId);
    	}


    	Item sampleData = createItem("Expression");
//    	sampleData.setAttribute("probeSet", probeSet);
    	sampleData.setAttribute("signal", signal.toString());
    	if (call!=null) {
    		sampleData.setAttribute("call", call);
    	}
    	sampleData.setAttribute("pValue", pValue.toString());

		sampleData.setReference("sample", sampleIdRef);
		sampleData.setReference("probe", probeRefId);
		store(sampleData);
    	return sampleData.getIdentifier();
    }


    /**
     * Return the expressions from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the expressions
     */
    protected ResultSet getExperiments(Connection connection) throws SQLException {
    	String query =
    			"SELECT p.proj_id, p.proj_res_area, p.proj_pi, "
    			+ " p.proj_pi_inst, p.proj_pi_addr "
    			+ " FROM proj_info p;";
        return doQuery(connection, query, "getExperiments");
    }

    /**
     * Return the samples from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the samples
     */
    protected ResultSet getSamples(Connection connection) throws SQLException {
    	String query =
    	"SELECT p.proj_id, sb.sample_id, sb.sample_bio_name, sb.sample_alias, "
    	+ "sg.sample_desc, sg.sample_ctrl, sg.sample_repl, sg.sample_file_name "
    	+ "FROM sample_biosource_info sb, sample_general_info sg, proj_info p "
    	+ "WHERE sb.sample_id=sg.sample_id "
    	+ "AND p.proj_id=sb.proj_id;";
        return doQuery(connection, query, "getSamples");
    }

    /**
     * Return the samples from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the samples
     */
    protected ResultSet getSampleProperties(Connection connection) throws SQLException {
    	String query =
    			"SELECT sample_id, sample_stock_code, sample_genetic_var, "
    			+ "sample_tissue, sample_diseased, sample_growth_condition, "
    			+ "sample_growth_stage,sample_time_point "
    			+ "FROM sample_biosource_info;";
        return doQuery(connection, query, "getSampleProperties");
    }

    /**
     * Return the expressions from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the expressions
     */
    protected ResultSet getSampleData(Connection connection) throws SQLException {
    	String query =
    			"SELECT sample_id, data_probeset_id, data_signal, "
    			+ "data_call, data_p_val "
    			+ "FROM sample_data;";
//		+ "FROM sample_data limit 1000;";
        return doQuery(connection, query, "getSampleData");
    }


    /**
     * Default implementation that makes a data set title based on the data source name.
     * {@inheritDoc}
     */
    @Override
    public String getDataSetTitle(int taxonId) {
        return DATA_SOURCE_NAME + " expressions data set";
    }



    /**
     * method to wrap the execution of a query with log info)
     * @param connection
     * @param query
     * @param comment for not logging
     * @return the result set
     * @throws SQLException
     */
    private ResultSet doQuery(Connection connection, String query, String comment)
        throws SQLException {
        // see ModEncodeMetaDataProcessor
    	LOG.info("executing: " + query);
        long bT = System.currentTimeMillis();
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(query);
        LOG.info("QUERY TIME " + comment + ": " + (System.currentTimeMillis() - bT) + " ms");
        return res;
    }

}
