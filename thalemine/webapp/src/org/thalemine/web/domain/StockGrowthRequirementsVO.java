package org.thalemine.web.domain;

import java.util.List;

public class StockGrowthRequirementsVO extends DomainVO {
	
	private String objectId;
	private String stockPrimaryIdentifier;
	private String growthRequirements;
	
	
	public StockGrowthRequirementsVO(){
		
	}

	public StockGrowthRequirementsVO(List<Object> list) {
		init(list);
	}
	
	public StockGrowthRequirementsVO(String objectId){
		
	}
	
	private void init(List<Object> list) {

		this.objectId = getElement(list, 0);
		this.stockPrimaryIdentifier = getElement(list, 1);
		this.growthRequirements = getElement(list, 2);
		
	}

	public String getObjectId() {
		return objectId;
	}


	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}


	public String getStockPrimaryIdentifier() {
		return stockPrimaryIdentifier;
	}


	public void setStockPrimaryIdentifier(String stockPrimaryIdentifier) {
		this.stockPrimaryIdentifier = stockPrimaryIdentifier;
	}


	public String getGrowthRequirements() {
		return growthRequirements;
	}


	public void setGrowthRequirements(String growthRequirements) {
		this.growthRequirements = growthRequirements;
	}

	@Override
	public String toString() {
		return "StockGrowthRequirementsVO [objectId=" + objectId + ", stockPrimaryIdentifier=" + stockPrimaryIdentifier
				+ ", growthRequirements=" + growthRequirements + "]";
	}
	
	

}
