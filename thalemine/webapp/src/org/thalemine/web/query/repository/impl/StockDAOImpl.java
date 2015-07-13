package org.thalemine.web.query.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.metadata.Model;
import org.intermine.model.InterMineObject;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.PathQuery;
import org.thalemine.web.domain.StockGenotypeVO;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.webservice.client.core.ServiceFactory;
import org.intermine.webservice.client.services.QueryService;
import org.thalemine.web.query.StockQueryService;
import org.thalemine.web.query.repository.AbstractDAO;
import org.thalemine.web.query.repository.AbstractRepository;
import org.thalemine.web.query.repository.QueryRepository;
import org.thalemine.web.query.repository.QueryResult;
import org.thalemine.web.query.repository.StockDAO;
import org.thalemine.web.service.Verifiable;
import org.thalemine.web.service.core.IRepositoryManager;
import org.thalemine.web.utils.UtilService;

public class StockDAOImpl implements QueryRepository, StockDAO {

	private static final String PRIMARY_IDENTIFIER_CONSTRAINT = "Stock.primaryIdentifier";
	private static final String OBJECT_IDENTIFIER_CONSTRAINT = "Stock.id";

	private IRepositoryManager repository;

	protected static final Logger log = Logger.getLogger(StockDAOImpl.class);

	public StockDAOImpl() {
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

	private PathQuery getStockAvailabilityQuery(String itemId) throws Exception {
		
		PathQuery query = new PathQuery(getModel());
		query.addViews("Stock.id", "Stock.primaryAccession", "Stock.stockAvailabilities.stockDisplayNumber",
				"Stock.stockAvailabilities.stockCenter.name", "Stock.stockAvailabilities.stockNumber",
				"Stock.stockAvailabilities.stockCenter.stockObjectUrlPrefix");

		// Add orderby
		query.addOrderBy("Stock.stockAvailabilities.stockCenter.name", OrderDirection.ASC);

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;
		
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

	private PathQuery getGrowthRequirementsQuery(Object item) throws Exception {

		PathQuery query = new PathQuery(getModel());

		query.addViews("Stock.stockAnnotation.id", "Stock.primaryIdentifier",
				"Stock.stockAnnotation.growthCondition.specialGrowthConditions");

		setConstraint(query, item);

		return query;

	}

	private PathQuery getStockPhenotypesQuery(Object item) throws Exception {

		PathQuery query = new PathQuery(getModel());

		query.addViews("Stock.primaryIdentifier", "Stock.genotypes.primaryIdentifier",
				"Stock.genotypes.phenotypesObserved.id", "Stock.genotypes.phenotypesObserved.description");

		// Add orderby
		query.addOrderBy("Stock.primaryIdentifier", OrderDirection.ASC);

		setConstraint(query, item);

		return query;

	}

	private PathQuery getMutagenChromosomalConstitutionQuery(Object item) throws Exception {

		PathQuery query = new PathQuery(getModel());

		query.addViews("Stock.stockAnnotation.id", "Stock.primaryIdentifier", "Stock.mutagen.name",
				"Stock.stockAnnotation.chromosomalConstitution.aneploidChromosome",
				"Stock.stockAnnotation.chromosomalConstitution.ploidy");

		query.setOuterJoinStatus("Stock.stockAnnotation.chromosomalConstitution", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.mutagen", OuterJoinStatus.OUTER);

		setConstraint(query, item);

		return query;

	}

	private PathQuery getNaturalAccessionQuery(Object item) throws Exception {

		PathQuery query = new PathQuery(getModel());

		query.addViews("Stock.accession.id", "Stock.accession.abbreviationName", "Stock.accession.infraspecificName",
				"Stock.accession.habitat", "Stock.accession.geoLocation");

		setConstraint(query, item);

		return query;

	}

	private PathQuery getBackgroundAccessionsQuery(String stockId){
		PathQuery query = new PathQuery(getModel());

		query.addViews("Stock.backgroundAccessions.id", "Stock.backgroundAccessions.abbreviationName");

		query.addConstraint(Constraints.eq("Stock.id", stockId));

		return query;
	}
	
	private PathQuery getStockGenotypeGeneticContextQuery(String stockId, String genotypeId) throws Exception {
		PathQuery query = new PathQuery(getModel());

		query.addViews("Stock.genotypes.alleles.id", "Stock.genotypes.alleles.primaryIdentifier",
				"Stock.genotypes.alleles.alleleGeneZygosities.zygosity.name", "Stock.genotypes.alleles.mutagen.name",
				"Stock.genotypes.alleles.inheritanceMode.name", "Stock.genotypes.alleles.alleleClass.name");

		query.setOuterJoinStatus("Stock.genotypes.alleles.alleleGeneZygosities", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.genotypes.alleles.alleleGeneZygosities.zygosity", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.genotypes.alleles.mutagen", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.genotypes.alleles.inheritanceMode", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.genotypes.alleles.alleleClass", OuterJoinStatus.OUTER);

		query.addConstraint(Constraints.eq("Stock.id", stockId));
		query.addConstraint(Constraints.eq("Stock.genotypes.id", genotypeId));

		return query;
	}

	private PathQuery getPhenotypesByGeneticItemQuery(String itemId, String stockId, String itemClass) {
		PathQuery query = new PathQuery(getModel());

		if (StringUtils.isNotBlank(itemClass)){
			if (itemClass.equalsIgnoreCase("gene")){
				query = getPhenotypesByGeneItemQuery(itemId,stockId);
			}else if(itemClass.equalsIgnoreCase("allele")){
				query = getPhenotypesByAlleleItemQuery(itemId,stockId);
			}
		}
		
		return query;
	}
	
	private PathQuery getPhenotypesByGeneItemQuery(String geneId, String stockId) {
		PathQuery query = new PathQuery(getModel());
		
		query.addViews("Gene.affectedAlleles.genotypes.phenotypesObserved.id",
				"Gene.affectedAlleles.genotypes.phenotypesObserved.description",
				"Gene.affectedAlleles.primaryIdentifier", "Gene.affectedAlleles.genotypes.name",
				"Gene.affectedAlleles.genotypes.phenotypesObserved.primaryIdentifier",
				"Gene.affectedAlleles.genotypes.stocks.primaryIdentifier", "Gene.primaryIdentifier");

		// Add orderby
		query.addOrderBy("Gene.affectedAlleles.genotypes.phenotypesObserved.primaryIdentifier", OrderDirection.ASC);

		// Filter the results with the following constraints:
		query.addConstraint(Constraints.eq("Gene.id", geneId), "A");
		query.addConstraint(Constraints.eq("Gene.affectedAlleles.genotypes.stocks.id", stockId),
				"B");
		// Specify how these constraints should be combined.
		query.setConstraintLogic("A and B");
		return query;
	}
	
	private PathQuery getPhenotypesByAlleleItemQuery(String alleleId, String stockId) {
		PathQuery query = new PathQuery(getModel());
		
		query.addViews("Allele.genotypes.phenotypesObserved.id", "Allele.genotypes.phenotypesObserved.description",
				"Allele.primaryIdentifier", "Allele.genotypes.name",
				"Allele.genotypes.phenotypesObserved.primaryIdentifier", "Allele.genotypes.stocks.primaryIdentifier");

		// Add orderby
		query.addOrderBy("Allele.genotypes.phenotypesObserved.primaryIdentifier", OrderDirection.ASC);

		// Filter the results with the following constraints:
		query.addConstraint(Constraints.eq("Allele.id", alleleId), "A");
		query.addConstraint(Constraints.eq("Allele.genotypes.stocks.id", stockId), "B");
		// Specify how these constraints should be combined.
		query.setConstraintLogic("A and B");
		
		return query;
	}
	
	private PathQuery getStocksByGeneticItemQuery(String itemId, String itemClass) {
		PathQuery query = new PathQuery(getModel());

		if (StringUtils.isNotBlank(itemClass)){
			if (itemClass.equalsIgnoreCase("gene")){
				query = getStocksByGeneQuery(itemId);
			}else if(itemClass.equalsIgnoreCase("allele")){
				query = getStocksByAlleleQuery(itemId);
			}
		}
		
		return query;
	}

	private PathQuery getStocksByGeneQuery(String geneId) {
		PathQuery query = new PathQuery(getModel());

		query.addViews("Gene.affectedAlleles.genotypes.id", "Gene.affectedAlleles.genotypes.displayName",
				"Gene.affectedAlleles.genotypes.name", "Gene.affectedAlleles.genotypes.primaryIdentifier",
				"Gene.affectedAlleles.genotypes.stocks.id", "Gene.affectedAlleles.genotypes.stocks.germplasmName",
				"Gene.affectedAlleles.genotypes.stocks.primaryIdentifier",
				"Gene.affectedAlleles.genotypes.stocks.stockName",
				"Gene.affectedAlleles.genotypes.stocks.primaryAccession",
				"Gene.affectedAlleles.genotypes.stocks.stockAccession",
				"Gene.affectedAlleles.genotypes.stocks.accession.id",
				"Gene.affectedAlleles.genotypes.stocks.accession.abbreviationName");

		// Add orderby
		query.addOrderBy("Gene.affectedAlleles.genotypes.stocks.germplasmName", OrderDirection.ASC);

		// Outer Joins
		// Show all information about these relationships if they exist, but do
		// not require that they exist.
		query.setOuterJoinStatus("Gene.affectedAlleles.genotypes.stocks.accession", OuterJoinStatus.OUTER);

		query.addConstraint(Constraints.eq("Gene.id", geneId));
		return query;
	}

	private PathQuery getStocksByAlleleQuery(String alleleId) {
		PathQuery query = new PathQuery(getModel());

		query.addViews("Allele.genotypes.id", "Allele.genotypes.displayName", "Allele.genotypes.name",
				"Allele.genotypes.primaryIdentifier", "Allele.genotypes.stocks.id",
				"Allele.genotypes.stocks.germplasmName", "Allele.genotypes.stocks.primaryIdentifier",
				"Allele.genotypes.stocks.stockName", "Allele.genotypes.stocks.primaryAccession",
				"Allele.genotypes.stocks.stockAccession", "Allele.genotypes.stocks.accession.id",
				"Allele.genotypes.stocks.accession.abbreviationName");

		// Add orderby
		query.addOrderBy("Allele.genotypes.stocks.germplasmName", OrderDirection.ASC);

		// Outer Joins
		// Show all information about these relationships if they exist, but do
		// not require that they exist.
		query.setOuterJoinStatus("Allele.genotypes.stocks.accession", OuterJoinStatus.OUTER);

		query.addConstraint(Constraints.eq("Allele.id", alleleId));
		
		return query;
	}

	private PathQuery getPhenotypeGeneticContextQuery(String itemId) throws Exception {
		PathQuery query = new PathQuery(getModel());
		
		query.addViews("Stock.id", 
				"Stock.primaryIdentifier", 
				"Stock.genotypes.id", 
				"Stock.genotypes.name",
				"Stock.genotypes.phenotypesObserved.id"
				);
		
		// Add orderby
		query.addOrderBy("Stock.primaryIdentifier", OrderDirection.ASC);

		// Filter the results with the following constraints:
		query.addConstraint(Constraints.eq("Stock.genotypes.phenotypesObserved.id", itemId));
		
		return query;
		
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

	@Override
	public QueryResult getGrowthRequirements(Object item) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getGrowthRequirementsQuery(item);
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
	public QueryResult getStockPhenotypes(Object item) throws Exception {

		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getStockPhenotypesQuery(item);
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
	public QueryResult getMutagenChromosomalConstitution(Object item) throws Exception {

		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getMutagenChromosomalConstitutionQuery(item);
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
	public QueryResult getNaturalAccession(Object item) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getNaturalAccessionQuery(item);
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
	public QueryResult getStockGenotypeGeneticContext(String stockId, String genotypeId) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getStockGenotypeGeneticContextQuery(stockId, genotypeId);
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
	public QueryResult getStocksByGeneticItem(String geneId, String itemClass) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getStocksByGeneticItemQuery(geneId, itemClass);
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
	public QueryResult getBackgroundAccessions(String itemId) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getBackgroundAccessionsQuery(itemId);
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
	public QueryResult getPhenotypesbyGeneticItem(String itemId, String stockId, String itemClass) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getPhenotypesByGeneticItemQuery(itemId, stockId, itemClass);
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
	public QueryResult getPhenotypeGeneticContext(String itemId) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getPhenotypeGeneticContextQuery(itemId);
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
	public QueryResult getStockAvailability(String itemId) throws Exception {
		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getStockAvailabilityQuery(itemId);
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
