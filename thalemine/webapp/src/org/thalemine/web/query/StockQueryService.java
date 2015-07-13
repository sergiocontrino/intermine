package org.thalemine.web.query;

import org.apache.log4j.Logger;
import org.intermine.web.logic.session.SessionMethods;
import org.intermine.web.util.URLGenerator;
import org.thalemine.web.context.WebApplicationContextLocator;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.GeneModelVO;
import org.thalemine.web.domain.GeneVO;
import org.thalemine.web.domain.PhenotypeVO;
import org.thalemine.web.domain.PublicationVO;
import org.thalemine.web.domain.StockAnnotationVO;
import org.thalemine.web.domain.StockAvailabilityVO;
import org.thalemine.web.domain.StockGenotypeVO;
import org.thalemine.web.domain.StockGrowthRequirementsVO;
import org.thalemine.web.domain.StockVO;
import org.thalemine.web.domain.StrainVO;
import org.thalemine.web.domain.builder.StockAnnotationVOBuilder;
import org.thalemine.web.domain.builder.StockGrowthRequirementsVOBuilder;
import org.thalemine.web.utils.QueryServiceLocator;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	public void init(HttpServletRequest request) {

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

	public StockGrowthRequirementsVO getStockGrowthRequirements(InterMineObject item) throws Exception {

		StockGrowthRequirementsVO result = null;
		Exception exception = null;

		PathQuery query = null;

		if (item == null) {
			throw new Exception("Stock cannot be null.");
		}

		String itemId = item.getId().toString();

		query = getGrowthRequirementsQuery(itemId);

		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		int rowCount = 0;

		log.info("Getting Stock Annotation Item:" + item);

		while (resultSetIterator.hasNext() && rowCount < 1) {

			List<Object> currentItem = resultSetIterator.next();

			result = new StockGrowthRequirementsVOBuilder().build(currentItem);

			log.info("Stock Growth Requirements Result Item:" + result);

			rowCount++;

		}

		return result;

	}

	public List<StockGenotypeVO> getPhenotypeGeneticContext(InterMineObject item) throws Exception {
		
		List<StockGenotypeVO> result = new ArrayList<StockGenotypeVO>();
		Exception exception = null;

		PathQuery query = null;

		if (item == null) {
			throw new Exception("Stock cannot be null.");
		}

		String itemId = item.getId().toString();
		
		query = getPhenotypesObservedInQuery(itemId);
		
		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		log.info("Getting Genotype Item:" + item);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			StockGenotypeVO resultItem = new StockGenotypeVO(currentItem);

			List<AlleleVO> alleles = new ArrayList<AlleleVO>();

			if (resultItem.getStockObjectId() != null && resultItem.getGenotypeObjectId() != null) {

				alleles = getAllelebyStockGenotype(resultItem.getStockObjectId(), resultItem.getGenotypeObjectId());

			}

			for (AlleleVO alleleItem : alleles) {

				List<GeneVO> genes = new ArrayList<GeneVO>();
				genes = getGenes(alleleItem.getObjectId());
				alleleItem.setGeneList(genes);

				log.info("Allele Item:" + alleleItem + "Genes size:" + alleleItem.getGeneList().size());

			}

			if (alleles.size() > 0) {
				resultItem.setAlleles(alleles);
			}

			log.info("Genotype Item:" + resultItem);

			result.add(resultItem);

		}

		return result;
		
	}
	
	public List<StockGenotypeVO> getStockGenotypes(InterMineObject item) throws Exception {

		List<StockGenotypeVO> result = new ArrayList<StockGenotypeVO>();
		Exception exception = null;

		PathQuery query = null;

		if (item == null) {
			throw new Exception("Stock cannot be null.");
		}

		String itemId = item.getId().toString();

		query = getGenotypeQuery(itemId);

		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		log.info("Getting Genotype Item:" + item);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			StockGenotypeVO resultItem = new StockGenotypeVO(currentItem);

			List<AlleleVO> alleles = new ArrayList<AlleleVO>();

			if (resultItem.getStockObjectId() != null && resultItem.getGenotypeObjectId() != null) {

				alleles = getAllelebyStockGenotype(resultItem.getStockObjectId(), resultItem.getGenotypeObjectId());

			}

			for (AlleleVO alleleItem : alleles) {

				List<GeneVO> genes = new ArrayList<GeneVO>();
				genes = getGenes(alleleItem.getObjectId());
				alleleItem.setGeneList(genes);

				log.info("Allele Item:" + alleleItem + "Genes size:" + alleleItem.getGeneList().size());

			}

			if (alleles.size() > 0) {
				resultItem.setAlleles(alleles);
			}

			log.info("Genotype Item:" + resultItem);

			result.add(resultItem);

		}

		return result;

	}

	public List<GeneModelVO> getGeneModels(String itemId) throws Exception {

		ArrayList<GeneModelVO> resultList = new ArrayList<GeneModelVO>();

		PathQuery query = null;

		log.info("Allele Source Item:" + itemId);

		query = getGeneModelsByAlleleIdQuery(itemId);

		QueryService service = factory.getQueryService();
		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			log.info("Gene Model Item:" + currentItem);

			GeneModelVO resultItem = new GeneModelVO(currentItem);

			resultList.add(resultItem);

			log.info("Gene Model Result Item:" + resultItem);

		}

		return resultList;

	}

	public List<GeneVO> getGenes(String itemId) throws Exception {

		ArrayList<GeneVO> resultList = new ArrayList<GeneVO>();

		PathQuery query = null;

		log.info("Allele Source Item:" + itemId);

		query = getGenesByAlleleIdQuery(itemId);

		QueryService service = factory.getQueryService();
		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			log.info("Gene Item:" + currentItem);

			String geneObjectId = getElement(currentItem, 0);
			String geneName = getElement(currentItem, 2);

			GeneVO resultItem = new GeneVO(geneObjectId, geneName);

			resultList.add(resultItem);

			log.info("Gene Result Item:" + resultItem);

		}

		return resultList;

	}

	private String getElement(List<Object> list, int index) {

		String element = ((list.get(index) != null) && (list.get(index) != null)) ? list.get(index).toString()
				: "&nbsp;";

		if (element != null && ((element.equalsIgnoreCase("unknown") || (element.equalsIgnoreCase("null"))))) {
			element = "&nbsp;";
		}

		if (element == null) {
			element = "&nbsp;";
		}
		return element;

	}

	private List<AlleleVO> getAllelebyStockGenotype(String stockId, String genotypeId) throws Exception {

		List<AlleleVO> result = new ArrayList<AlleleVO>();

		Exception exception = null;
		PathQuery query = null;

		if (stockId == null || genotypeId == null) {
			throw new Exception("Stock/Genotype Id cannot be null.");
		}

		query = getAlleleQuerybyStockGenotype(stockId, genotypeId);

		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		log.info("Getting Allele Item:" + ";StockId:" + stockId + ";GenotypeId:" + genotypeId);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			String alleleId = null;
			String alleleName = null;
			String zygosity = null;
			String mutagen = null;
			String inheritanceType = null;
			String alleleClass = null;

			if (currentItem.get(0) != null) {
				alleleId = currentItem.get(0).toString();
			}

			if (currentItem.get(1) != null) {
				alleleName = currentItem.get(1).toString().toLowerCase();
			}

			if (currentItem.get(2) != null) {
				zygosity = currentItem.get(2).toString();
			} else {
				zygosity = "&nbsp;";
			}

			if (zygosity.equalsIgnoreCase("null")) {
				zygosity = "&nbsp;";
			}

			if (currentItem.get(3) != null) {
				mutagen = currentItem.get(3).toString();
			} else {
				mutagen = "N/A";
			}

			if (mutagen.equalsIgnoreCase("null")) {
				mutagen = "&nbsp;";
			}
			
			if (currentItem.get(4) != null) {
				inheritanceType = currentItem.get(4).toString();
			} else {
				inheritanceType = "N/A;";
			}
			
			if (currentItem.get(5) != null) {
				alleleClass = currentItem.get(5).toString();
			} else {
				alleleClass = "N/A;";
			}


			if (alleleId != null && alleleName != null) {

				AlleleVO resultItem = new AlleleVO(alleleId, alleleName, zygosity, mutagen, inheritanceType, alleleClass);

				log.info("Allele Item:" + resultItem);

				result.add(resultItem);

			}

		}

		return result;

	}

	public List<StockAvailabilityVO> getStockAvailability(InterMineObject item) throws Exception {

		List<StockAvailabilityVO> result = new ArrayList<StockAvailabilityVO>();
		Exception exception = null;

		PathQuery query = null;

		if (item == null) {
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

	public StockAnnotationVO getStockAnnotation(InterMineObject item) throws Exception {

		StockAnnotationVO result = null;
		Exception exception = null;

		PathQuery query = null;

		if (item == null) {
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

	public StockAnnotationVO getMutagenChromosomalConstitution(InterMineObject item) throws Exception {

		StockAnnotationVO result = null;
		Exception exception = null;

		PathQuery query = null;

		if (item == null) {
			throw new Exception("Stock cannot be null.");
		}

		String itemId = item.getId().toString();

		query = getMutagenChromosomalConstitutionQuery(itemId);

		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		int rowCount = 0;

		log.info("Getting Mutagen/Chromosomsal Constitution Item:" + item);

		while (resultSetIterator.hasNext() && rowCount < 1) {

			List<Object> currentItem = resultSetIterator.next();

			result = new StockAnnotationVOBuilder().buildByMutagenChromosomalConstitution(currentItem);

			log.info("Mutagen/Chromosomsal Constitution Result Item:" + result);

			rowCount++;

		}

		return result;

	}

	public List<StrainVO> getAccession(InterMineObject item) throws Exception {

		Exception exception = null;

		List<StrainVO> resultList = new ArrayList<StrainVO>();
		PathQuery query = null;

		if (item == null) {
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

	public List<StockVO> getStocks(InterMineObject item, String objectClassName) throws Exception {

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

		query.addViews("Stock.accession.id", "Stock.accession.abbreviationName", "Stock.accession.infraspecificName",
				"Stock.accession.habitat", "Stock.accession.geoLocation"

		);

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;

	}

	private PathQuery getStockAvailabilityQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Stock.id", "Stock.primaryAccession", "Stock.stockAvailabilities.stockDisplayNumber",
				"Stock.stockAvailabilities.stockCenter.name", "Stock.stockAvailabilities.stockNumber",
				"Stock.stockAvailabilities.stockCenter.stockObjectUrlPrefix");

		// Add orderby
		query.addOrderBy("Stock.stockAvailabilities.stockCenter.name", OrderDirection.ASC);

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;

	}

	private PathQuery getAnnotationQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Stock.stockAnnotation.id", "Stock.primaryIdentifier", "Stock.mutagen.name",
				"Stock.stockAnnotation.chromosomalConstitution.aneploidChromosome",
				"Stock.stockAnnotation.chromosomalConstitution.ploidy",
				"Stock.stockAnnotation.growthCondition.specialGrowthConditions", "Stock.stockAnnotation.mutant",
				"Stock.stockAnnotation.transgene", "Stock.stockAnnotation.naturalVariant");

		// Outer Joins
		// Show all information about these relationships if they exist, but do
		// not require that they exist.
		query.setOuterJoinStatus("Stock.stockAnnotation.growthCondition", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.stockAnnotation", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.stockAnnotation.chromosomalConstitution", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.mutagen", OuterJoinStatus.OUTER);

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;

	}

	private PathQuery getMutagenChromosomalConstitutionQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);
		query.addViews("Stock.stockAnnotation.id", "Stock.primaryIdentifier", "Stock.mutagen.name",
				"Stock.stockAnnotation.chromosomalConstitution.aneploidChromosome",
				"Stock.stockAnnotation.chromosomalConstitution.ploidy");

		query.setOuterJoinStatus("Stock.stockAnnotation.chromosomalConstitution", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.mutagen", OuterJoinStatus.OUTER);

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;

	}

	private PathQuery getGrowthRequirementsQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Stock.stockAnnotation.id", "Stock.primaryIdentifier",
				"Stock.stockAnnotation.growthCondition.specialGrowthConditions");

		query.addConstraint(Constraints.eq("Stock.id", itemId));

		return query;

	}

	private PathQuery getGenotypeQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Stock.id", "Stock.primaryIdentifier", "Stock.genotypes.id", "Stock.genotypes.name");

		query.addConstraint(Constraints.eq("Stock.id", itemId));
		return query;

	}
	
	private PathQuery getPhenotypesObservedInQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

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

	private PathQuery getAlleleQuerybyStockGenotype(String stockId, String genotypeId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Stock.genotypes.alleles.id", "Stock.genotypes.alleles.primaryIdentifier",
				"Stock.genotypes.alleles.alleleGeneZygosities.zygosity.name", "Stock.genotypes.alleles.mutagen.name",
				"Stock.genotypes.alleles.inheritanceMode.name",
				"Stock.genotypes.alleles.alleleClass.name"
				);

		query.setOuterJoinStatus("Stock.genotypes.alleles.alleleGeneZygosities", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.genotypes.alleles.alleleGeneZygosities.zygosity", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.genotypes.alleles.mutagen", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.genotypes.alleles.inheritanceMode", OuterJoinStatus.OUTER);
		query.setOuterJoinStatus("Stock.genotypes.alleles.alleleClass", OuterJoinStatus.OUTER);

		query.addConstraint(Constraints.eq("Stock.id", stockId));
		query.addConstraint(Constraints.eq("Stock.genotypes.id", genotypeId));

		return query;

	}

	private PathQuery getGeneModelsByAlleleIdQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Allele.affectedGenes.id", "Allele.affectedGenes.transcripts.id", "Allele.primaryIdentifier",
				"Allele.affectedGenes.primaryIdentifier", "Allele.affectedGenes.transcripts.primaryIdentifier",
				"Allele.affectedGenes.transcripts.curatorSummary");

		// Outer Joins
		// Show all information about these relationships if they exist, but do
		// not require that they exist.
		query.setOuterJoinStatus("Allele.affectedGenes.transcripts", OuterJoinStatus.OUTER);
		query.addConstraint(Constraints.eq("Allele.id", itemId));

		return query;

	}

	private PathQuery getGenesByAlleleIdQuery(String itemId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Allele.affectedGenes.id", "Allele.primaryIdentifier", "Allele.affectedGenes.primaryIdentifier");

		// Outer Joins
		// Show all information about these relationships if they exist, but do
		// not require that they exist.
		query.addConstraint(Constraints.eq("Allele.id", itemId));

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

		query.addViews("Allele.genotypes.phenotypesObserved.id", "Allele.genotypes.phenotypesObserved.description",
				"Allele.primaryIdentifier", "Allele.genotypes.name",
				"Allele.genotypes.phenotypesObserved.primaryIdentifier", "Allele.genotypes.stocks.primaryIdentifier");

		// Add orderby
		query.addOrderBy("Allele.genotypes.phenotypesObserved.primaryIdentifier", OrderDirection.ASC);

		// Filter the results with the following constraints:
		query.addConstraint(Constraints.eq("Allele.id", alleleItem.getId().toString()), "A");
		query.addConstraint(Constraints.eq("Allele.genotypes.stocks.id", stockItem.getStockObjectId()), "B");
		// Specify how these constraints should be combined.
		query.setConstraintLogic("A and B");

		return query;

	}

	private PathQuery getPhenotypesByStockIdQuery(String stockId) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews("Stock.primaryIdentifier", "Stock.genotypes.primaryIdentifier",
				"Stock.genotypes.phenotypesObserved.id", "Stock.genotypes.phenotypesObserved.description");

		// Add orderby
		query.addOrderBy("Stock.primaryIdentifier", OrderDirection.ASC);

		// Filter the results with the following constraints:
		query.addConstraint(Constraints.eq("Stock.id", stockId));

		return query;

	}

	public List<PhenotypeVO> getStockPhenotypes(InterMineObject item) throws Exception {

		List<PhenotypeVO> result = new ArrayList<PhenotypeVO>();

		if (item == null) {
			throw new Exception("Stock cannot be null.");
		}

		String itemId = item.getId().toString();

		PathQuery query = getPhenotypesByStockIdQuery(itemId);

		QueryService service = factory.getQueryService();

		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);

		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();

			String objectId = null;
			String description = null;

			if (currentItem.get(2) != null) {
				objectId = currentItem.get(2).toString();
			}

			if (currentItem.get(3) != null) {
				description = currentItem.get(3).toString();
			}

			PhenotypeVO resultItem = new PhenotypeVO(objectId, description);
			result.add(resultItem);

			log.info("Phenotype Stock Result Item:" + resultItem);

		}

		return result;

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
			} else {
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

		} catch (Exception e) {
			log.error("Error getting pehotypes for:" + item + ";" + ";Error:" + e.getMessage() + ";Cause:"
					+ e.getCause());
		}
		return result;

	}
}
