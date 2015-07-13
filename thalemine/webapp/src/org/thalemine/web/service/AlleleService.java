package org.thalemine.web.service;

import java.util.List;

import org.intermine.model.InterMineObject;
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


public interface AlleleService extends BusinessService,  Verifiable {
	
	public List<GeneVO> getGenes(Object item) throws Exception;
	public List<GeneVO> getGenes(String itemId) throws Exception;
	public List<AlleleVO> getAllelesByGene(String itemId) throws Exception;
	public List<GeneModelVO> getGeneModels(String itemId) throws Exception;
	public List<AlleleSummaryVO> getAlleleSummary(String itemId) throws Exception;	
	
}
