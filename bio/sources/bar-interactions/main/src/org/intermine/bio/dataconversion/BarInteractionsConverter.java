package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2011 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.intermine.bio.dataconversion.BioDBConverter;
import org.intermine.dataconversion.ItemWriter;
import org.intermine.metadata.Model;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.sql.Database;
import org.intermine.xml.full.Item;
import org.xml.sax.SAXException;

/**
 * 
 * @author Julie Sullivan
 */
public class BarInteractionsConverter extends BioDBConverter
{
    private static final String DATASET_TITLE = "Interactions data set";
    private static final String DATA_SOURCE_NAME = "BAR";
    private static final int TAXON_ID = 3702;
    private Map<String, String> genes = new HashMap<String, String>();
    private Map<String, String> publications = new HashMap<String, String>();
    private Map<String, String> terms = new HashMap<String, String>();
    private static final String INTERACTION_TYPE = "physical";
    private static final String PUBMED_PREFIX = "PubMed";

    /**
     * Construct a new BarInteractionsConverter.
     * @param database the database to read from
     * @param model the Model used by the object store we will write to with the ItemWriter
     * @param writer an ItemWriter used to handle Items created
     */
    public BarInteractionsConverter(Database database, Model model, ItemWriter writer) {
        super(database, model, writer, DATA_SOURCE_NAME, DATASET_TITLE);
    }

    /**
     * {@inheritDoc}
     */
    public void process() throws Exception {
    	// a database has been initialised from properties starting with db.bar-interactions
    	Connection connection = getDatabase().getConnection();

    	// process data with direct SQL queries on the source database, for example:
    	String query = "select protein1, protein2, quality, index, pcc, bind_id, " +
    			"interactions_detection_mi, interactions_detection, interactions_type_mi, " +
    			"interactions_type from interactions;";

    	Statement stmt = connection.createStatement();
    	ResultSet res = stmt.executeQuery(query);
    	while (res.next()) {
    		String gene1 = res.getString("protein1");
    		String gene2 = res.getString("protein2");
    		Integer quality = new Integer(res.getInt("quality"));
    		Integer index = new Integer(res.getInt("index"));
    		// is this a confidence score?
    		Double pcc = new Double(res.getDouble("pcc"));
    		String pubString = res.getString("bind_id");
    		String interactionsDetectionMI = res.getString("interactions_detection_mi");
//    		String interactionsDetection = res.getString("interactions_detection");
    		String interactionsTypeMI = res.getString("interactions_type_mi");
//    		String interactionsType = res.getString("interactions_type");

    		// strings that represent the stored gene
    		String geneRefId1 = getGene(gene1);
    		String geneRefId2 = getGene(gene2);
    		
    		processInteraction(geneRefId1, geneRefId2, quality, index, pcc, 
    				pubString, interactionsDetectionMI, interactionsTypeMI);
    		
    	}   
    	res.close();
    }
    
    private void processInteraction(String geneRefId1, String geneRefId2, Integer quality, 
    		Integer index, Double pcc, String pubString, String interactionsDetectionMI, 
    		String interactionsTypeMI) throws ObjectStoreException {
    	
    	Item interaction = createItem("Interaction");
    	interaction.setReference("gene1", geneRefId1);
    	interaction.setReference("gene2", geneRefId2);
    	interaction.addToCollection("details", getDetails(geneRefId1, geneRefId2, pubString, 
    			interactionsDetectionMI, interactionsTypeMI));
    	store(interaction);
    }
    
    private String getDetails(String geneRefId1, String geneRefId2, String pubString, String 
    		interactionsDetectionMI, String interactionsTypeMI) 
    		throws ObjectStoreException {
    	Item detail = createItem("InteractionDetail");
    	detail.setAttribute("type", INTERACTION_TYPE);
    	detail.setReference("relationshipType", getTerm(interactionsTypeMI));
    	detail.addToCollection("dataSets", getDataSourceItem(getDataSetTitle(TAXON_ID)));
        detail.addToCollection("allInteractors", geneRefId1);
        detail.addToCollection("allInteractors", geneRefId2);
        detail.setReference("experiment", getExperiment(pubString, interactionsDetectionMI));
        return detail.getIdentifier();
    }
    
    private String getExperiment(String pubString, String interactionsDetectionMI) 
    		throws ObjectStoreException {
        Item experiment = createItem("InteractionExperiment");
        experiment.setReference("publication", getPublication(pubString));
        experiment.setReference("interactionDetectionMethods", getTerm(interactionsDetectionMI));
        store(experiment);
        return experiment.getIdentifier();
    }
    
    private String getPublication(String pubString) throws ObjectStoreException {
        String pubMedId =  pubString.substring(PUBMED_PREFIX.length());
        String pubRefId = publications.get(pubMedId);
        if (pubRefId != null) {
        	return pubRefId;
        }
        Item publication = createItem("Publication");
        publication.setAttribute("pubMedId", pubMedId);
        store(publication);
        pubRefId = publication.getIdentifier();
        publications.put(pubMedId, pubRefId);
        return pubRefId;
    }

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
    	Item item = createItem("InteractionTerm");
    	item.setAttribute("identifier", identifier);
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
        return DATA_SOURCE_NAME + " interactions data set";
    }

}
