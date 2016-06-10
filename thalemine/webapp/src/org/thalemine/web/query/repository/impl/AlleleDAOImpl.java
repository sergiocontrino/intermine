package org.thalemine.web.query.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.metadata.Model;
import org.intermine.model.InterMineObject;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.PathQuery;
import org.thalemine.web.domain.GeneVO;
import org.thalemine.web.domain.StockGenotypeVO;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.webservice.client.core.ServiceFactory;
import org.intermine.webservice.client.services.QueryService;
import org.thalemine.web.query.StockQueryService;
import org.thalemine.web.query.repository.AbstractDAO;
import org.thalemine.web.query.repository.AbstractRepository;
import org.thalemine.web.query.repository.AlleleDAO;
import org.thalemine.web.query.repository.PublicationDAO;
import org.thalemine.web.query.repository.QueryRepository;
import org.thalemine.web.query.repository.QueryResult;
import org.thalemine.web.query.repository.StockDAO;
import org.thalemine.web.service.Verifiable;
import org.thalemine.web.service.core.IRepositoryManager;
import org.thalemine.web.utils.UtilService;

public class AlleleDAOImpl implements QueryRepository, AlleleDAO, Verifiable {

	private static final String PRIMARY_IDENTIFIER_CONSTRAINT = "Allele.primaryIdentifier";
	private static final String OBJECT_IDENTIFIER_CONSTRAINT = "Allele.id";

	private IRepositoryManager repository;

	protected static final Logger log = Logger.getLogger(AlleleDAOImpl.class);
	
	public AlleleDAOImpl(){
		super();
	}
	

	@Override
	public QueryResult getGenotype(Object item) throws Exception {

		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getGenotypeQuery(item);
			queryResult = new QueryResultImpl(this.repository, query);
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {

				log.error("Error occured while retrieving result set for query:" + "; Query:" + query);
				log.error("Error:" + exception.getMessage() + ";Cause:" + exception.getCause());
				exception.printStackTrace();

			} else {
				log.info("Successfully retrieved resultset for query." + "; Query:" + query);
			}
		}

		return queryResult;
	}

	private PathQuery getGenotypeQuery(Object item) throws Exception {

		PathQuery query = new PathQuery(getModel());
		query.addViews("Stock.id", "Stock.primaryIdentifier", "Stock.genotypes.id", "Stock.genotypes.name");

		setConstraint(query, item);

		return query;
	}

	public void setConstraint(PathQuery query, Object item) throws Exception {

		String primaryIdentifier = UtilService.getObjectIdentifier(item);

		if ((item != null) && UtilService.isInterMineObject(item)) {
			query.addConstraint(Constraints.eq(OBJECT_IDENTIFIER_CONSTRAINT, primaryIdentifier));
		} else {
			query.addConstraint(Constraints.eq(PRIMARY_IDENTIFIER_CONSTRAINT, primaryIdentifier));
		}

	}

	@Override
	public void setRepository(IRepositoryManager repository) {
		this.repository = repository;
	}

	private Model getModel() {
		return repository.getFactory().getModel();
	}

	@Override
	public void validateState() throws Exception {

		if (this.repository == null) {

			throw new IllegalStateException("Repository must not be null!");
		}

		if (this.repository.getFactory() == null) {

			throw new IllegalStateException("Repository Service Factory must not be null!");
		}

		if (StringUtils.isBlank(this.repository.getSystemService().getServiceEndPoint())) {
			throw new IllegalStateException("Repository Service Endpoint must not be null!");
		}

	}

	private PathQuery getGeneModelsQuery(String itemId){
		PathQuery query = new PathQuery(getModel());
		
		query.addViews(
				"Allele.affectedGenes.id",
				"Allele.affectedGenes.transcripts.id",
				"Allele.primaryIdentifier",
				"Allele.affectedGenes.primaryIdentifier",
				"Allele.affectedGenes.transcripts.primaryIdentifier",
				 "Allele.affectedGenes.transcripts.curatorSummary"
				);
		
		// Outer Joins
				// Show all information about these relationships if they exist, but do
				// not require that they exist.
		query.setOuterJoinStatus("Allele.affectedGenes.transcripts", OuterJoinStatus.OUTER);

		
		query.addConstraint(Constraints.eq("Allele.id", itemId));
		
		return query;
		
	}
	
	private PathQuery getAlleleSummaryQuery(String itemId){
		PathQuery query = new PathQuery(getModel());
		
		query.addViews(
				"Allele.id",
				"Allele.primaryIdentifier",
				"Allele.mutagen.name",
				"Allele.mutationSite.name",
				"Allele.sequenceAlterationType.name",
				"Allele.alleleClass.name",
				"Allele.inheritanceMode.name"
				);
		
		// Outer Joins
        // Show all information about these relationships if they exist, but do not require that they exist.
        query.setOuterJoinStatus("Allele.inheritanceMode", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Allele.mutagen", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Allele.mutationSite", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Allele.sequenceAlterationType", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Allele.alleleClass", OuterJoinStatus.OUTER);
		
		query.addConstraint(Constraints.eq("Allele.id", itemId));
		return query;
		
	}
	
	
	private PathQuery getGenesQuery(Object item) throws Exception {

		PathQuery query = new PathQuery(getModel());

		query.addViews("Allele.affectedGenes.id", "Allele.primaryIdentifier", "Allele.affectedGenes.primaryIdentifier");
		
		setConstraint(query, item);

		return query;
	}

	private PathQuery getGenesQuery(String itemId) throws Exception {

		PathQuery query = new PathQuery(getModel());

		query.addViews("Allele.affectedGenes.id", "Allele.primaryIdentifier", "Allele.affectedGenes.primaryIdentifier");

		query.addConstraint(Constraints.eq("Allele.id", itemId));

		return query;
	}
	
	private PathQuery getAllelesQuery(String itemId) throws Exception {

		PathQuery query = new PathQuery(getModel());

		// Select the output columns:
		 
        query.addViews("Gene.affectedAlleles.id",
        			   "Gene.affectedAlleles.primaryIdentifier",
        			   "Gene.affectedAlleles.name",
                       "Gene.affectedAlleles.mutagen.name",
                       "Gene.affectedAlleles.sequenceAlterationType.name", 
                       "Gene.affectedAlleles.mutationSite.name",
                       "Gene.affectedAlleles.inheritanceMode.name",
                       "Gene.affectedAlleles.alleleClass.name"
                       
        		);
        
     // Add orderby
   	    query.addOrderBy("Gene.affectedAlleles.name", OrderDirection.ASC);
	    	        
        
	 // Outer Joins
        // Show all information about these relationships if they exist, but do not require that they exist.
        query.setOuterJoinStatus("Gene.affectedAlleles.mutagen", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Gene.affectedAlleles.sequenceAlterationType", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Gene.affectedAlleles.mutationSite", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Gene.affectedAlleles.inheritanceMode", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Gene.affectedAlleles.alleleClass", OuterJoinStatus.OUTER);
        
	    query.addConstraint(Constraints.eq("Gene.id",itemId));
	    return query;

	}
	
	@Override
	public QueryResult getGenes(Object item) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query =  getGenesQuery(item);
			queryResult = new QueryResultImpl(this.repository, query);
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {

				log.error("Error occured while retrieving result set for query:" + "; Query:" + query);
				log.error("Error:" + exception.getMessage() + ";Cause:" + exception.getCause());
				exception.printStackTrace();

			} else {
				log.info("Successfully retrieved resultset for query." + "; Query:" + query);
			}
		}

		return queryResult;

	}


	@Override
	public QueryResult getGenes(String itemId) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query =  getGenesQuery(itemId);
			queryResult = new QueryResultImpl(this.repository, query);
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {

				log.error("Error occured while retrieving result set for query:" + "; Query:" + query);
				log.error("Error:" + exception.getMessage() + ";Cause:" + exception.getCause());
				exception.printStackTrace();

			} else {
				log.info("Successfully retrieved resultset for query." + "; Query:" + query);
			}
		}

		return queryResult;

	}


	@Override
	public QueryResult getAllelesByGene(String itemId) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query =  getAllelesQuery(itemId);
			queryResult = new QueryResultImpl(this.repository, query);
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {

				log.error("Error occured while retrieving result set for query:" + "; Query:" + query);
				log.error("Error:" + exception.getMessage() + ";Cause:" + exception.getCause());
				exception.printStackTrace();

			} else {
				log.info("Successfully retrieved resultset for query." + "; Query:" + query);
			}
		}

		return queryResult;

	}


	@Override
	public QueryResult getGeneModels(String itemId) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query =  getGeneModelsQuery(itemId);
			queryResult = new QueryResultImpl(this.repository, query);
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {

				log.error("Error occured while retrieving result set for query:" + "; Query:" + query);
				log.error("Error:" + exception.getMessage() + ";Cause:" + exception.getCause());
				exception.printStackTrace();

			} else {
				log.info("Successfully retrieved resultset for query." + "; Query:" + query);
			}
		}

		return queryResult;

	}


	@Override
	public QueryResult getAlleleSummary(String itemId) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query =  getAlleleSummaryQuery(itemId);
			queryResult = new QueryResultImpl(this.repository, query);
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {

				log.error("Error occured while retrieving result set for query:" + "; Query:" + query);
				log.error("Error:" + exception.getMessage() + ";Cause:" + exception.getCause());
				exception.printStackTrace();

			} else {
				log.info("Successfully retrieved resultset for query." + "; Query:" + query);
			}
		}

		return queryResult;

	}

}
