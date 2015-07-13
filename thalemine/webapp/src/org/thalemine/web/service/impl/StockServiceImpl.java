package org.thalemine.web.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;
import org.thalemine.web.domain.AlleleVO;
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
import org.thalemine.web.query.repository.AlleleDAO;
import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.query.repository.GenotypeDAO;
import org.thalemine.web.query.repository.PhenotypeDAO;
import org.thalemine.web.query.repository.PublicationDAO;
import org.thalemine.web.query.repository.QueryResult;
import org.thalemine.web.query.repository.StockDAO;
import org.thalemine.web.service.AlleleService;
import org.thalemine.web.service.StockService;
import org.thalemine.web.service.core.ServiceConfig;
import org.thalemine.web.service.core.ServiceManager;

public class StockServiceImpl extends AbstractService implements StockService {

	protected static final Logger log = Logger.getLogger(StockServiceImpl.class);

	@Override
	public List<StrainVO> getNaturalAccession(Object item) throws Exception {
		Exception exception = null;

		List<StrainVO> result = new ArrayList<StrainVO>();

		if (item == null) {
			throw new Exception("Stock cannot be null.");
		}

		QueryResult queryResult = this.stockDao.getNaturalAccession(item);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		int rowCount = 0;

		while (iterator.hasNext() && rowCount < 1) {

			List<Object> currentItem = iterator.next();

			StrainVO resultItem = new StrainVO(currentItem, true);

			result.add(resultItem);

			log.info("Accession Result Item:" + resultItem);

			rowCount++;

		}

		return result;

	}

	@Override
	public StockGrowthRequirementsVO getGrowthRequirements(Object item) throws Exception {

		StockGrowthRequirementsVO result = null;

		if (item == null) {
			throw new Exception("Stock cannot be null.");
		}

		QueryResult queryResult = this.stockDao.getGrowthRequirements(item);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		int rowCount = 0;

		log.info("Getting Stock Growth Requirements Item:" + item);

		while (iterator.hasNext() && rowCount < 1) {

			List<Object> currentItem = iterator.next();

			result = new StockGrowthRequirementsVOBuilder().build(currentItem);

			log.info("Stock Growth Requirements Result Item:" + result);

			rowCount++;

		}

		return result;

	}

	@Override
	public List<StockAvailabilityVO> getStockAvailability(String itemId) throws Exception{
		List<StockAvailabilityVO> result = new ArrayList<StockAvailabilityVO>();
				
		if (itemId == null) {
			log.error("Stock cannot be null");
			throw new Exception("Stock cannot be null.");
		}

		QueryResult queryResult = this.stockDao.getStockAvailability(itemId);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			StockAvailabilityVO resultItem = new StockAvailabilityVO(currentItem);

			log.debug("Stock Availability Item:" + resultItem);

			result.add(resultItem);

		}

		return result;

	}

	@Override
	public void validateState() throws Exception {

		if (this.stockDao == null) {

			throw new IllegalStateException("Stock DAO must not be null!");
		}

		if (this.publicationDao == null) {

			throw new IllegalStateException("Publication DAO Factory must not be null!");
		}

		if (this.genotypeDao == null) {

			throw new IllegalStateException("Genotype DAO Factory must not be null!");
		}

		if (this.alleleDao == null) {

			throw new IllegalStateException("Allele DAO Factory must not be null!");
		}

		if (this.phenotypeDao == null) {

			throw new IllegalStateException("Phenotype DAO Factory must not be null!");
		}

	}

	@Override
	public String toString() {
		return ServiceConfig.STOCK_SERVICE_CLASS;
	}

	@Override
	public List<PhenotypeVO> getStockPhenotypes(Object item) throws Exception {

		List<PhenotypeVO> result = new ArrayList<PhenotypeVO>();

		if (item == null) {
			log.error("Stock cannot be null");
			throw new Exception("Stock cannot be null.");
		}

		QueryResult queryResult = this.stockDao.getStockPhenotypes(item);

		Iterator<List<Object>> iterator = queryResult.getResultItems();

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

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

			log.debug("Phenotype Stock Result Item:" + resultItem);

		}

		return result;

	}

	@Override
	public StockAnnotationVO getMutagenChromosomalConstitution(Object item) throws Exception {

		StockAnnotationVO result = null;
		
		if (item == null) {
			
			log.error("Stock cannot be null");
			throw new Exception("Stock cannot be null.");
		}

		QueryResult queryResult = this.stockDao.getMutagenChromosomalConstitution(item);

		Iterator<List<Object>> iterator = queryResult.getResultItems();

		int rowCount = 0;

		log.debug("Getting Mutagen/Chromosomsal Constitution Item:" + item);

		while (iterator.hasNext() && rowCount < 1) {

			List<Object> currentItem = iterator.next();

			result = new StockAnnotationVOBuilder().buildByMutagenChromosomalConstitution(currentItem);

			log.debug("Mutagen/Chromosomsal Constitution Result Item:" + result);

			rowCount++;

		}

		return result;

	}

	@Override
	public List<StockGenotypeVO> getStockGenotypes(Object item) throws Exception {

		List<StockGenotypeVO> result = new ArrayList<StockGenotypeVO>();
		
		if (item == null) {
			log.error("Stock cannot be null");
			throw new Exception("Stock cannot be null.");
		}

		QueryResult queryResult = this.stockDao.getGenotype(item);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		log.debug("Getting Stock Genotype Item:" + item);

		AlleleService alleleService = (AlleleService) ServiceManager.getInstance().getService(
				ServiceConfig.ALLELE_SERVICE);

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			StockGenotypeVO resultItem = new StockGenotypeVO(currentItem);

			List<AlleleVO> alleles = new ArrayList<AlleleVO>();

			if (resultItem.getStockObjectId() != null && resultItem.getGenotypeObjectId() != null) {

				alleles = getStockGenotypeGeneticContext(resultItem.getStockObjectId(),
						resultItem.getGenotypeObjectId());
			}

			for (AlleleVO alleleItem : alleles) {

				log.debug("Allele Item:" + alleleItem);
				List<GeneVO> genes = new ArrayList<GeneVO>();

				genes = alleleService.getGenes(alleleItem.getObjectId());
				alleleItem.setGeneList(genes);

				log.debug("Allele Item:" + alleleItem + "Genes List Size:" + alleleItem.getGeneList().size());

			}

			if (alleles.size() > 0) {
				resultItem.setAlleles(alleles);
			}

			log.info("Comepleted Stock Genotype Item:" + resultItem);

			result.add(resultItem);

		}

		return result;

	}

	@Override
	public List<AlleleVO> getStockGenotypeGeneticContext(String stockId, String genotypeId) throws Exception {

		List<AlleleVO> result = new ArrayList<AlleleVO>();
		
		if (stockId == null || genotypeId == null) {
			log.error("Stock/Genotype Id cannot be null.");
			throw new Exception("Stock/Genotype Id cannot be null.");
		}

		QueryResult queryResult = this.stockDao.getStockGenotypeGeneticContext(stockId, genotypeId);

		Iterator<List<Object>> iterator = queryResult.getResultItems();

		log.debug("Getting Allele Item for: " + ";StockId:" + stockId + " ;GenotypeId:" + genotypeId);

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

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

				AlleleVO resultItem = new AlleleVO(alleleId, alleleName, zygosity, mutagen, inheritanceType,
						alleleClass);

				log.debug("Allele Item:" + resultItem);

				result.add(resultItem);

			}

		}

		return result;

	}

	@Override
	public List<StockVO> getStocksbyGeneticItem(String itemId, String itemClass) throws Exception {
		ArrayList<StockVO> resultList = new ArrayList<StockVO>();

		QueryResult queryResult = this.stockDao.getStocksByGeneticItem(itemId, itemClass);

		Iterator<List<Object>> iterator = queryResult.getResultItems();

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			StockVO resultItem = new StockVO(currentItem);

			// Stock Background Accessions
			List<StrainVO> bgAccessions = new ArrayList<StrainVO>();
			bgAccessions = getBackgroundAccessions(resultItem.getStockObjectId());
			resultItem.setBackgrounds(bgAccessions);

			// Phenotypes
			List<PhenotypeVO> phenotypes = new ArrayList<PhenotypeVO>();
			phenotypes = getPhenotypesbyGeneticItem(itemId, resultItem.getStockObjectId(), itemClass);
			resultItem.setPhenotypes(phenotypes);

			resultList.add(resultItem);

			log.debug("Stock Result Item:" + resultItem);

		}

		return resultList;

	}

	@Override
	public List<StrainVO> getBackgroundAccessions(String itemId) throws Exception {

		List<StrainVO> result = new ArrayList<StrainVO>();

		QueryResult queryResult = this.stockDao.getBackgroundAccessions(itemId);

		Iterator<List<Object>> iterator = queryResult.getResultItems();

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			StrainVO resultItem = new StrainVO(currentItem);

			result.add(resultItem);

			log.debug("Background Accession Result Item:" + resultItem);

		}

		return result;
	}

	@Override
	public List<PhenotypeVO> getPhenotypesbyGeneticItem(String itemId, String stockId, String itemClass)
			throws Exception {

		List<PhenotypeVO> result = new ArrayList<PhenotypeVO>();
		QueryResult queryResult = this.stockDao.getPhenotypesbyGeneticItem(itemId, stockId, itemClass);

		Iterator<List<Object>> iterator = queryResult.getResultItems();

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			PhenotypeVO resultItem = new PhenotypeVO(currentItem);

			if (resultItem.getObjectId() != null) {

				List<PublicationVO> publications = new ArrayList<PublicationVO>();
				resultItem.setPublications(publications);

				log.debug("Phenotype:" + resultItem + ";Publication Size:" + publications.size());

			}

			result.add(resultItem);

			log.debug("Phenotype Result Item:" + resultItem);

		}

		return result;
	}

	@Override
	public List<StockGenotypeVO> getPhenotypeGeneticContext(String itemId) throws Exception {

		List<StockGenotypeVO> result = new ArrayList<StockGenotypeVO>();

		QueryResult queryResult = this.stockDao.getPhenotypeGeneticContext(itemId);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		if (itemId == null) {
			throw new Exception("Phenotype cannot be null.");
		}

		AlleleService alleleService = (AlleleService) ServiceManager.getInstance().getService(
				ServiceConfig.ALLELE_SERVICE);

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			StockGenotypeVO resultItem = new StockGenotypeVO(currentItem);

			List<AlleleVO> alleles = new ArrayList<AlleleVO>();

			if (resultItem.getStockObjectId() != null && resultItem.getGenotypeObjectId() != null) {

				alleles = getStockGenotypeGeneticContext(resultItem.getStockObjectId(),
						resultItem.getGenotypeObjectId());

			}

			for (AlleleVO alleleItem : alleles) {

				List<GeneVO> genes = new ArrayList<GeneVO>();
				genes = alleleService.getGenes(alleleItem.getObjectId());
				alleleItem.setGeneList(genes);

				log.debug("Allele Item:" + alleleItem + "Genes List Size:" + alleleItem.getGeneList().size());

			}

			if (alleles.size() > 0) {
				resultItem.setAlleles(alleles);
			}

			log.info("Genotype Item:" + resultItem);

			result.add(resultItem);

		}

		return result;
	}

}
