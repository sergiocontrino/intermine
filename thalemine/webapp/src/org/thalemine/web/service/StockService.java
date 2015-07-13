package org.thalemine.web.service;

import java.util.List;

import org.intermine.model.InterMineObject;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.PhenotypeVO;
import org.thalemine.web.domain.StockAnnotationVO;
import org.thalemine.web.domain.StockAvailabilityVO;
import org.thalemine.web.domain.StockGenotypeVO;
import org.thalemine.web.domain.StockGrowthRequirementsVO;
import org.thalemine.web.domain.StockVO;
import org.thalemine.web.domain.StrainVO;


public interface StockService extends BusinessService,  Verifiable {
	
	public List<StrainVO> getNaturalAccession(Object item) throws Exception;
	public StockGrowthRequirementsVO getGrowthRequirements(Object item) throws Exception;
	public List<StockAvailabilityVO> getStockAvailability(String itemId) throws Exception;
	public List<PhenotypeVO> getStockPhenotypes(Object item) throws Exception;
	public StockAnnotationVO getMutagenChromosomalConstitution(Object item) throws Exception;
	public List<StockGenotypeVO> getStockGenotypes(Object item) throws Exception;
	public List<AlleleVO> getStockGenotypeGeneticContext(String stockId, String genotypeId) throws Exception;
	public List<StockVO> getStocksbyGeneticItem(String itemId, String itemClass) throws Exception;
	public List<StrainVO> getBackgroundAccessions(String itemId) throws Exception;
	public List<PhenotypeVO> getPhenotypesbyGeneticItem(String itemId, String stockId, String itemClass) throws Exception;
	public List<StockGenotypeVO> getPhenotypeGeneticContext(String itemId) throws Exception;
	
	
}
