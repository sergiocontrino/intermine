package org.thalemine.web.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.thalemine.web.domain.AlleleSummaryVO;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.GeneModelVO;
import org.thalemine.web.domain.GeneVO;
import org.thalemine.web.domain.PhenotypeVO;
import org.thalemine.web.domain.StockAnnotationVO;
import org.thalemine.web.domain.StockAvailabilityVO;
import org.thalemine.web.domain.StockGenotypeVO;
import org.thalemine.web.domain.StockGrowthRequirementsVO;
import org.thalemine.web.domain.StrainVO;
import org.thalemine.web.domain.builder.AlleleVOBuilder;
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

public class AlleleServiceImpl extends AbstractService implements AlleleService {

	protected static final Logger log = Logger.getLogger(AlleleServiceImpl.class);

	@Override
	public List<GeneVO> getGenes(Object item) throws Exception {

		ArrayList<GeneVO> resultList = new ArrayList<GeneVO>();

		if (item == null) {
			throw new Exception("Allele cannot be null.");
		}

		QueryResult queryResult = this.alleleDao.getGenes(item);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			log.debug("Gene Item:" + currentItem);

			String geneObjectId = getElement(currentItem, 0);
			String geneName = getElement(currentItem, 2);

			GeneVO resultItem = new GeneVO(geneObjectId, geneName);

			resultList.add(resultItem);

			log.debug("Gene Result Item:" + resultItem);

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

	@Override
	public List<GeneVO> getGenes(String itemId) throws Exception {
		ArrayList<GeneVO> resultList = new ArrayList<GeneVO>();

		if (itemId == null) {
			throw new Exception("Allele cannot be null.");
		}

		QueryResult queryResult = this.alleleDao.getGenes(itemId);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			log.debug("Gene Item:" + currentItem);

			String geneObjectId = getElement(currentItem, 0);
			String geneName = getElement(currentItem, 2);

			GeneVO resultItem = new GeneVO(geneObjectId, geneName);

			resultList.add(resultItem);

			log.debug("Gene Result Item:" + resultItem);

		}

		return resultList;

	}

	@Override
	public List<AlleleVO> getAllelesByGene(String itemId) throws Exception {
		ArrayList<AlleleVO> resultList = new ArrayList<AlleleVO>();

		if (itemId == null) {
			throw new Exception("Gene cannot be null.");
		}

		QueryResult queryResult = this.alleleDao.getAllelesByGene(itemId);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			log.debug("Allele Item:" + currentItem);

			AlleleVOBuilder builder = new AlleleVOBuilder();
			AlleleVO item = builder.buildbyObjectList(currentItem);

			resultList.add(item);
			log.debug("Allele:" + item);

		}

		return resultList;
	}

	@Override
	public List<GeneModelVO> getGeneModels(String itemId) throws Exception {

		ArrayList<GeneModelVO> resultList = new ArrayList<GeneModelVO>();

		QueryResult queryResult = this.alleleDao.getGeneModels(itemId);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		while (iterator.hasNext()) {

			List<Object> currentItem = iterator.next();

			log.debug("Gene Model Item:" + currentItem);

			GeneModelVO resultItem = new GeneModelVO(currentItem);

			resultList.add(resultItem);

			log.debug("Gene Model Result Item:" + resultItem);

		}

		return resultList;
	}

	@Override
	public List<AlleleSummaryVO> getAlleleSummary(String itemId) throws Exception {
		List<AlleleSummaryVO> resultList = new ArrayList<AlleleSummaryVO>();

		QueryResult queryResult = this.alleleDao.getAlleleSummary(itemId);
		Iterator<List<Object>> iterator = queryResult.getResultItems();

		int rowCount = 0;

		while (iterator.hasNext() && rowCount < 1) {

			List<Object> currentItem = iterator.next();

			log.debug("Allele Summary Item:" + currentItem);

			AlleleSummaryVO alleleItemVO = new AlleleSummaryVO(currentItem);
			resultList.add(alleleItemVO);

			log.debug("Allele Summary Result Item:" + alleleItemVO);

			rowCount++;

		}

		return resultList;
	}

	@Override
	public String toString() {
		return ServiceConfig.ALLELE_SERVICE_CLASS;
	}
}
