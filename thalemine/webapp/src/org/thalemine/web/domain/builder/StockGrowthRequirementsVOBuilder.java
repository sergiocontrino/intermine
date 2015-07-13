package org.thalemine.web.domain.builder;

import java.util.List;

import org.apache.log4j.Logger;
import org.intermine.api.results.ResultElement;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.DomainVO;
import org.thalemine.web.domain.StockGrowthRequirementsVO;

public class StockGrowthRequirementsVOBuilder extends DomainVO {

	protected static final Logger log = Logger.getLogger(StockGrowthRequirementsVOBuilder.class);
	private final StockGrowthRequirementsVO valueObject;
	
	public StockGrowthRequirementsVOBuilder(){
		this.valueObject = new StockGrowthRequirementsVO();
	}
	
	public StockGrowthRequirementsVO build(List<Object> element) {

		initByObjectList(element);
		return this.valueObject;
			
	}
	
	private void initByObjectList(List<Object> element){
		
		this.valueObject.setObjectId(getElement(element, 0));
		this.valueObject.setStockPrimaryIdentifier(getElement(element, 1));
		this.valueObject.setGrowthRequirements(getElement(element, 2));
		
		log.debug("Value Object:" + this.valueObject);
		
	}
	
}
