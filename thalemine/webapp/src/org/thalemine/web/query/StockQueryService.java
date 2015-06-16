package org.thalemine.web.query;

import org.apache.log4j.Logger;
import org.intermine.web.logic.session.SessionMethods;
import org.intermine.web.util.URLGenerator;
import org.thalemine.web.domain.PhenotypeVO;
import org.thalemine.web.domain.PublicationVO;
import org.thalemine.web.domain.StockAnnotationVO;
import org.thalemine.web.domain.StockAvailabilityVO;
import org.thalemine.web.domain.StockVO;
import org.thalemine.web.domain.StrainVO;
import org.thalemine.web.utils.QueryServiceLocator;
import org.thalemine.web.utils.WebApplicationContextLocator;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang.StringUtils.repeat;

import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.PathQuery;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.metadata.Model;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.core.ServiceFactory;
import org.intermine.webservice.client.services.QueryService;
import org.intermine.model.bio.Gene;
import org.intermine.model.bio.Allele;
import org.intermine.model.InterMineObject;

public class StockQueryService implements Service {

	protected static final Logger log = Logger.getLogger(StockQueryService.class);

	private static String className = "Stock";
	private static String SERVICE_URL;
	private static final String PUBLICATION_SERVICE = "PublicationQueryService";
	private static ServiceFactory factory;

	private static PublicationQueryService publicationService;

	public PublicationQueryService getPublicationService() {
		return publicationService;
	}

	public void setPublicationService(PublicationQueryService publicationService) {
		this.publicationService = publicationService;
	}

	private StockQueryService() {

	}

	private static class StockQueryServiceHolder {

		public static final StockQueryService INSTANCE = new StockQueryService();

	}

	public static StockQueryService getInstance(HttpServletRequest request) {

		SERVICE_URL = WebApplicationContextLocator.getServiceUrl(request);
		factory = new ServiceFactory(SERVICE_URL);

		publicationService = (PublicationQueryService) QueryServiceLocator.getService(PUBLICATION_SERVICE, request);

		return StockQueryServiceHolder.INSTANCE;
	}

	public void init(HttpServletRequest request){
		
		SERVICE_URL = WebApplicationContextLocator.getServiceUrl(request);
		factory = new ServiceFactory(SERVICE_URL);

		log.info("Service URL:" + SERVICE_URL);

	}
	
	
	public static String getSERVICE_URL() {
		return SERVICE_URL;
	}

	public static void setSERVICE_URL(String sERVICE_URL) {
		SERVICE_URL = sERVICE_URL;
	}

	public static ServiceFactory getFactory() {
		return factory;
	}

	public static void setFactory(ServiceFactory factory) {
		StockQueryService.factory = factory;
	}

	@Override
	public String getClassName() {
		return className;
	}

	private PathQuery getStocksByGeneQuery(Gene item) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

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

		query.addConstraint(Constraints.eq("Gene.id", item.getId().toString()));
		return query;

	}

	private PathQuery getStocksByAlleleQuery(Allele item) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

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

		query.addConstraint(Constraints.eq("Allele.id", item.getId().toString()));
		return query;

	}

	public String getServiceUrl() {
		return SERVICE_URL;
	}

	public Iterator<List<Object>> getResultSet(PathQuery query) {

		QueryService service = factory.getQueryService();
		return service.getRowListIterator(query);

	}
	
	
	public List<StockAvailabilityVO> getStockAvailability(InterMineObject item) throws Exception{
		
		List<StockAvailabilityVO> result = new ArrayList<StockAvailabilityVO>() ;
		Exception exception = null;
		
		PathQuery query = null;
		
		if (item==null){
			throw new Exception("Stock cannot be null.");
		}
		
		String itemId = item.getId().toString();
		
		query = getStockAvailabilityQuery(itemId);
		
		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);
		
		log.info("Getting Stock Availability Item:" + item);
		
		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			StockAvailabilityVO resultItem = new StockAvailabilityVO(currentItem);

			log.info("Stock Availability Item:" + resultItem);
			
			result.add(resultItem);
			

		}
		
		return result;
		
		
	}
	
	
	public StockAnnotationVO getStockAnnotation(InterMineObject item) throws Exception{
		
		StockAnnotationVO result = null;
		Exception exception = null;
		
		PathQuery query = null;
		
		if (item==null){
			throw new Exception("Stock cannot be null.");
		}
		
		String itemId = item.getId().toString();
		
		query = getAnnotationQuery(itemId);
		
		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);
		
		int rowCount = 0;
		
		log.info("Getting Stock Annotation Item:" + item);
		
		while (resultSetIterator.hasNext() && rowCount < 1) {

			List<Object> currentItem = resultSetIterator.next();

			result = new StockAnnotationVO(currentItem);

			log.info("Stock Annotation Result Item:" + result);
			
			rowCount++;

		}
		
		return result;
		
	}
	
	public List<StrainVO> getAccession(InterMineObject item) throws Exception{
		
		Exception exception = null;
		
		List<StrainVO> resultList = new ArrayList<StrainVO>();
		PathQuery query = null;
		
		if (item==null){
			throw new Exception("Stock cannot be null.");
		}
		
		String itemId = item.getId().toString();
		
		query = getAccessionsQuery(itemId);
		
		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);
		
		int rowCount = 0;
		
		while (resultSetIterator.hasNext() && rowCount < 1) {

			List<Object> currentItem = resultSetIterator.next();

			StrainVO resultItem = new StrainVO(currentItem, true);

			resultList.add(resultItem);

			log.info("Accession Result Item:" + resultItem);
			
			rowCount++;

		}
		
		return resultList;
		
	}

	public List<StockVO> getStocks(InterMineObject item, String objectClassName) throws Exception{

		ArrayList<StockVO> resultList = new ArrayList<StockVO>();

		PathQuery query = null;

		Gene gene = null;
		Allele allele = null;

		if (objectClassName.equals("Gene")) {
			gene = (Gene) item;
			query = getStocksByGeneQuery(gene);
			log.info("Getting Stocks by Gene:" + gene.getPrimaryIdentifier());
		} else if (objectClassName.equals("Allele")) {
			allele = (Allele) item;
			query = getStocksByAlleleQuery(allele);
			log.info("Getting Stocks by Gene:" + allele.getPrimaryIdentifier());
		} else {
			throw new Exception("Unknown Object Type.");
		}

		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			StockVO resultItem = new StockVO(currentItem);

			// Stock Background Accessions
			List<StrainVO> bgAccessions = new ArrayList<StrainVO>();
			bgAccessions = getBackgroundAccessions(resultItem);
			resultItem.setBackgrounds(bgAccessions);

			// Phenotypes
			List<PhenotypeVO> phenotypes = new ArrayList<PhenotypeVO>();
			phenotypes = getPhenotypes(item, resultItem, objectClassName);
			resultItem.setPhenotypes(phenotypes);

			resultList.add(resultItem);

			log.info("Stock Result Item:" + resultItem);

		}

		return resultList;

	}

	private QueryService getQueryService() {
		return factory.getQueryService();
	}


	private PathQuery getAccessionsQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Stock.accession.id", "Stock.accession.abbreviationName",
				"Stock.accession.infraspecificName", "Stock.accession.habitat",
						"Stock.accession.geoLocation"
				
				);

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;

	}
	
	
	private PathQuery getStockAvailabilityQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews(
				"Stock.id",
				"Stock.primaryAccession",
				"Stock.stockAvailabilities.stockDisplayNumber",
				"Stock.stockAvailabilities.stockCenter.name",
				"Stock.stockAvailabilities.stockNumber",
                "Stock.stockAvailabilities.stockCenter.stockObjectUrlPrefix");
			

        // Add orderby
        query.addOrderBy("Stock.stockAvailabilities.stockCenter.name", OrderDirection.ASC);

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;

	}
	
	private PathQuery getAnnotationQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews(
				"Stock.stockAnnotation.id",
				"Stock.primaryIdentifier",
				"Stock.mutagen.name",
				"Stock.stockAnnotation.chromosomalConstitution.aneploidChromosome",
                "Stock.stockAnnotation.chromosomalConstitution.ploidy",
                "Stock.stockAnnotation.growthCondition.specialGrowthConditions",
                "Stock.stockAnnotation.mutant",
                "Stock.stockAnnotation.transgene",
                "Stock.stockAnnotation.naturalVariant");
			
		// Outer Joins
        // Show all information about these relationships if they exist, but do not require that they exist.
        query.setOuterJoinStatus("Stock.stockAnnotation.growthCondition", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Stock.stockAnnotation", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Stock.stockAnnotation.chromosomalConstitution", OuterJoinStatus.OUTER);
        query.setOuterJoinStatus("Stock.mutagen", OuterJoinStatus.OUTER);

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;

	}
	
	private PathQuery getBackGroundAccessionsQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Stock.backgroundAccessions.id", "Stock.backgroundAccessions.abbreviationName");

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;

	}
	
	public List<StrainVO> getBackgroundAccessions(StockVO item) {
		List<StrainVO> result = new ArrayList<StrainVO>();

		PathQuery query = getBackGroundAccessionsQuery(item.getStockObjectId());

		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			StrainVO resultItem = new StrainVO(currentItem);

			result.add(resultItem);

			log.info("Background Accession Result Item:" + resultItem);

		}

		return result;
	}

	private PathQuery getPhenotypesByGeneStockQuery(Gene geneItem, StockVO stockItem) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Gene.affectedAlleles.genotypes.phenotypesObserved.id",
				"Gene.affectedAlleles.genotypes.phenotypesObserved.description",
				"Gene.affectedAlleles.primaryIdentifier", "Gene.affectedAlleles.genotypes.name",
				"Gene.affectedAlleles.genotypes.phenotypesObserved.primaryIdentifier",
				"Gene.affectedAlleles.genotypes.stocks.primaryIdentifier", "Gene.primaryIdentifier");

		// Add orderby
		query.addOrderBy("Gene.affectedAlleles.genotypes.phenotypesObserved.primaryIdentifier", OrderDirection.ASC);

		// Filter the results with the following constraints:
		query.addConstraint(Constraints.eq("Gene.id", geneItem.getId().toString()), "A");
		query.addConstraint(Constraints.eq("Gene.affectedAlleles.genotypes.stocks.id", stockItem.getStockObjectId()),
				"B");
		// Specify how these constraints should be combined.
		query.setConstraintLogic("A and B");

		return query;

	}

	private PathQuery getPhenotypesByAlleleStockQuery(Allele alleleItem, StockVO stockItem) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Allele.genotypes.phenotypesObserved.id",
				"Allele.genotypes.phenotypesObserved.description",
				"Allele.primaryIdentifier", "Allele.genotypes.name",
				"Allele.genotypes.phenotypesObserved.primaryIdentifier",
				"Allele.genotypes.stocks.primaryIdentifier");

		// Add orderby
		query.addOrderBy("Allele.genotypes.phenotypesObserved.primaryIdentifier", OrderDirection.ASC);

		// Filter the results with the following constraints:
		query.addConstraint(Constraints.eq("Allele.id", alleleItem.getId().toString()), "A");
		query.addConstraint(Constraints.eq("Allele.genotypes.stocks.id", stockItem.getStockObjectId()),
				"B");
		// Specify how these constraints should be combined.
		query.setConstraintLogic("A and B");

		return query;

	}

	public List<PhenotypeVO> getPhenotypes(Gene geneItem, StockVO stockItem) {

		List<PhenotypeVO> result = new ArrayList<PhenotypeVO>();

		PathQuery query = getPhenotypesByGeneStockQuery(geneItem, stockItem);

		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			PhenotypeVO resultItem = new PhenotypeVO(currentItem);

			if (resultItem.getObjectId() != null && publicationService != null) {

				List<PublicationVO> publications = new ArrayList<PublicationVO>();

				publications = publicationService.getPhenotypePublications(resultItem);
				resultItem.setPublications(publications);
				log.info("Phenotype:" + resultItem + ";Publication Size:" + publications.size());

			}

			result.add(resultItem);

			log.info("Phenotype Result Item:" + resultItem);

		}

		return result;

	}

	public List<PhenotypeVO> getPhenotypes(InterMineObject item, StockVO stockItem, String objectClassName) {

		List<PhenotypeVO> result = new ArrayList<PhenotypeVO>();

		try {
		
		Gene gene = null;
		Allele allele = null;
		PathQuery query = null;

		if (objectClassName.equals("Gene")) {
			gene = (Gene) item;
			query = getPhenotypesByGeneStockQuery(gene, stockItem);
			log.info("Getting Phenotypes by Gene:" + gene.getPrimaryIdentifier());
		} else if (objectClassName.equals("Allele")) {
			allele = (Allele) item;
			query = getPhenotypesByAlleleStockQuery(allele, stockItem);
			log.info("Getting Phenotypes by Allele:" + allele.getPrimaryIdentifier());
		} else{
			throw new Exception("Unknown Object Type.");
		}

		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			PhenotypeVO resultItem = new PhenotypeVO(currentItem);

			if (resultItem.getObjectId() != null && publicationService != null) {

				List<PublicationVO> publications = new ArrayList<PublicationVO>();

				publications = publicationService.getPhenotypePublications(resultItem);
				resultItem.setPublications(publications);
				log.info("Phenotype:" + resultItem + ";Publication Size:" + publications.size());

			}

			result.add(resultItem);

			log.info("Phenotype Result Item:" + resultItem);

		}

		} catch(Exception e){
			log.error("Error getting pehotypes for:" + item + ";" + ";Error:" + e.getMessage() + ";Cause:" + e.getCause());
		}
		return result;

	}
}
