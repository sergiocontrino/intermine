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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.intermine.dataconversion.ItemWriter;
import org.intermine.metadata.Model;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.sql.Database;
import org.intermine.xml.full.Item;

/**
 *
 * @author
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

    // barId, pi
    private Map<Integer, String> experimentLabMap = new HashMap<Integer, String>();
    //pi, item Id
    private Map<String, String> labIdRefMap = new HashMap<String, String>();
    //barId, item Id
    private Map<Integer, String> experimentIdRefMap = new HashMap<Integer, String>();
    //barId, item Id
    private Map<Integer, String> sampleIdRefMap = new HashMap<Integer, String>();

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
        processSampleData(connection);
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
//    		Integer sampleId = new Integer(res.getInt(6));

    		String experimentRefId = createExperiment(experimentBarId, title,
    				pi, affiliation, address);
    		experimentIdRefMap.put(experimentBarId, experimentRefId);
//    		processExpressionDetails(expressionRefId, geneRefId1, geneRefId2, pubString,
//    				expressionsDetectionMI, expressionsTypeMI);
    	}
    	res.close();
//
//        Set<Integer> exp = experimentLabMap.keySet();
//        Iterator<Integer> i  = exp.iterator();
//        while (i.hasNext()) {
//            Integer thisExp = i.next();
//            String prov = submissionLabMap.get(thisExp);
//            String project = submissionProjectMap.get(thisExp);
//
//            if (labIdMap.containsKey(prov)) {
//                continue;
//            }
//            LOG.debug("PROV: " + prov);
//            Item lab = getChadoDBConverter().createItem("Lab");
//            lab.setAttribute("surname", prov);
//            lab.setReference("project", projectIdRefMap.get(project));
//
//            Integer intermineObjectId = getChadoDBConverter().store(lab);
//            storeInLabMaps(lab, prov, intermineObjectId);
//        }
//        LOG.info("created " + labIdMap.size() + " labs");
//        LOG.info("PROCESS TIME labs: " + (System.currentTimeMillis() - bT) + " ms");


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
    	LOG.info("PROCEXP");
    	Item experiment = createItem("Experiment");
    	experiment.setAttribute("experimentBarId", experimentBarId.toString());
    	experiment.setAttribute("title", title);
    	experiment.setAttribute("category", EXPERIMENT_CATEGORY);

    	String labRefId;
    	// check if lab already stored
    	if (!labIdRefMap.containsValue(pi)) {
    		labRefId=createLab(pi, affiliation, address);
    		labIdRefMap.put(pi, labRefId);
    	}
//    	if (!experimentLabMap.containsValue(pi)) {
//    		labRefId=createLab(pi, affiliation, address);
//    	}

//    	experimentLabRefMap.put(experimentBarId, labRefId);

		experimentLabMap.put(experimentBarId, pi);
//		experiment.setReference("lab", experimentLabRefMap.get(experimentBarId));
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

    private String createSampleData(Integer sampleBarId,
    		String probeSet, Double signal, String call, Double pValue)
    				throws ObjectStoreException {

    	// needed to compensate for missing experiments for some smples
    	String sampleIdRef = sampleIdRefMap.get(sampleBarId);

    	if (sampleIdRef == null) {
    		LOG.warn("Orphaned sample: id=" + sampleBarId +
    				". The eperiment for this sample is missing.");
    		return "orphaned sample";
    	}


    	Item sampleData = createItem("SampleData");
    	sampleData.setAttribute("probeSet", probeSet);
    	sampleData.setAttribute("signal", signal.toString());
    	if (call!=null) {
    		sampleData.setAttribute("call", call);
    	}
    	sampleData.setAttribute("pValue", pValue.toString());

		sampleData.setReference("sample", sampleIdRef);
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
        Statement stmt = connection.createStatement();
        LOG.info("PREQ");
    	String query =
    			"select p.proj_id, p.proj_res_area, p.proj_pi, p.proj_pi_inst, "
    			+ " p.proj_pi_addr "
    			+ " from proj_info p;";
//		"select p.proj_id, p.proj_res_area, p.proj_pi, p.proj_pi_inst, "
//		+ " p.proj_pi_addr, s.sample_id "
//		+ " from proj_info p, sample_biosource_info s "
//		+ " where p.proj_id=s.proj_id;";
        ResultSet res = stmt.executeQuery(query);
        LOG.info("POSTQ");
        return res;
    }

    /**
     * Return the expressions from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the expressions
     */
    protected ResultSet getSamples(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        LOG.info("PREQs");
    	String query =
    	"SELECT p.proj_id, sb.sample_id, sb.sample_bio_name, sb.sample_alias, "
    	+ "sg.sample_desc, sg.sample_ctrl, sg.sample_repl, sg.sample_file_name "
    	+ "FROM sample_biosource_info sb, sample_general_info sg, proj_info p "
    	+ "WHERE sb.sample_id=sg.sample_id "
    	+ "AND p.proj_id=sb.proj_id;";
    	ResultSet res = stmt.executeQuery(query);
        LOG.info("POSTQS");
        return res;
    }

    /**
     * Return the expressions from the bar-expressions table
     * This is a protected method so that it can be overridden for testing.
     * @param connection the bar database connection
     * @throws SQLException if there is a problem while querying
     * @return the expressions
     */
    protected ResultSet getSampleData(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        LOG.info("PREQs");
    	String query =
    			"SELECT sample_id, data_probeset_id, data_signal, "
    			+ "data_call, data_p_val "
    			+ "FROM sample_data;";
    	ResultSet res = stmt.executeQuery(query);
        LOG.info("POSTQS");
        return res;
    }


	/*
    private String processExpression(String geneRefId1, String geneRefId2,
    		Integer quality, Integer index, Double pcc, String pubString,
    		String expressionsDetectionMI, String expressionsTypeMI) throws ObjectStoreException {

    	Item expression = createItem("Expression");
    	expression.setReference("gene1", geneRefId1);
    	expression.setReference("gene2", geneRefId2);
    	store(expression);
    	return expression.getIdentifier();
    }

    private String processExpressionDetails(String expressionRefId, String geneRefId1,
    		String geneRefId2, String pubString, String
    		expressionsDetectionMI, String expressionsTypeMI)
    		throws ObjectStoreException {
    	Item detail = createItem("ExpressionDetail");
//    	detail.setAttribute("type", EXPRESSION_TYPE);
    	if (StringUtils.isNotEmpty(expressionsTypeMI)) {
    		detail.setReference("relationshipType", getTerm(expressionsTypeMI));
    	}
    	detail.addToCollection("dataSets", getDataSourceItem(getDataSetTitle(TAXON_ID)));
        detail.addToCollection("allInteractors", geneRefId1);
        detail.addToCollection("allInteractors", geneRefId2);
        if (StringUtils.isNotEmpty(expressionsDetectionMI + pubString)) {
        	detail.setReference("experiment", getExperiment(pubString, expressionsDetectionMI));
        }
        detail.setReference("expression", expressionRefId);
        store(detail);
        return detail.getIdentifier();
    }
*/

//    private String getExperiment(String pubString, String expressionsDetectionMI)
//    		throws ObjectStoreException {
//        Item experiment = createItem("ExpressionExperiment");
//        // some publications don't start with PubMed, what are those?
//        if (StringUtils.isNotEmpty(pubString) && pubString.startsWith(PUBMED_PREFIX)
//        		&& pubString.length() >  PUBMED_PREFIX.length() + 1) {
//        	String pubRefId = getPublication(pubString);
//        	if (StringUtils.isNotEmpty(pubRefId)) {
//        		experiment.setReference("publication", pubRefId);
//        	}
//        }
//        if (StringUtils.isNotEmpty(expressionsDetectionMI)) {
//        	experiment.addToCollection("expressionDetectionMethods",
//        			getTerm(expressionsDetectionMI));
//        }
//        store(experiment);
//        return experiment.getIdentifier();
//    }

//    private String getPublication(String pubString) throws ObjectStoreException {
//        String pubMedId =  pubString.substring(PUBMED_PREFIX.length());
//
//    	// why do some look like this?
//    	// PubMed18849490                  +
//
//        String pubRefId = publications.get(pubMedId);
//        if (pubRefId != null) {
//        	return pubRefId;
//        }
//        Item publication = createItem("Publication");
//        publication.setAttribute("pubMedId", pubMedId);
//        store(publication);
//        pubRefId = publication.getIdentifier();
//        publications.put(pubMedId, pubRefId);
//        return pubRefId;
//    }

    private String getGene(String identifier)
    		throws ObjectStoreException {
    	String geneRefId = genes.get(identifier);

    	// we've already seen this gene, don't store again
    	if (geneRefId != null) {
		return geneRefId;
    	}

    	// create new gene
    	Item gene = createItem("Gene");
    	gene.setReference("organism", getOrganismItem(TAXON_ID));
    	gene.setAttribute("primaryIdentifier", identifier);

    	// put in our map
	geneRefId=gene.getIdentifier();
    	genes.put(identifier, geneRefId);

    	// store to database
    	store(gene);

    	return gene.getIdentifier();
    }

    private String getTerm(String identifier) throws ObjectStoreException {
    	String refId = terms.get(identifier);
    	if (refId != null) {
    		return refId;
    	}
    	Item item = createItem("ExpressionTerm");
    	item.setAttribute("identifier", "MI:" + identifier);
    	terms.put(identifier, item.getIdentifier());
   		store(item);
    	return item.getIdentifier();
    }

    /**
     * Default implementation that makes a data set title based on the data source name.
     * {@inheritDoc}
     */
    @Override
    public String getDataSetTitle(int taxonId) {
        return DATA_SOURCE_NAME + " expressions data set";
    }

}
