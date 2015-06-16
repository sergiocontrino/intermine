package org.thalemine.web.domain;

import java.util.List;

public class StockAvailabilityVO extends DomainVO{
	
	private String stockObjectId;
	private String externalStockObjectUrlPrefix;
	private String stockName;
	private String stockAccession;
	private String stockCenter;
	private String stockNumber;

	public StockAvailabilityVO(){
		
	}
	
	public StockAvailabilityVO(List<Object> list){
		init(list);
	}
	
	private void init(List<Object> list){
		
		this.stockObjectId = getElement(list, 0);
		this.stockAccession = getElement(list, 1);
		this.stockName = getElement(list, 2);
		this.stockCenter = getElement(list, 3);
		this.stockNumber = getElement(list, 4);
		this.externalStockObjectUrlPrefix = getElement(list, 5);
		
		
	}

	
	
	
	public String getStockObjectId() {
		return stockObjectId;
	}

	public void setStockObjectId(String stockObjectId) {
		this.stockObjectId = stockObjectId;
	}

	public String getExternalStockObjectUrlPrefix() {
		return externalStockObjectUrlPrefix;
	}

	public void setExternalStockObjectUrlPrefix(String externalStockObjectUrlPrefix) {
		this.externalStockObjectUrlPrefix = externalStockObjectUrlPrefix;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockAccession() {
		return stockAccession;
	}

	public void setStockAccession(String stockAccession) {
		this.stockAccession = stockAccession;
	}

	public String getStockCenter() {
		return stockCenter;
	}

	public void setStockCenter(String stockCenter) {
		this.stockCenter = stockCenter;
	}

	
	public String getStockNumber() {
		return stockNumber;
	}

	public void setStockNumber(String stockNumber) {
		this.stockNumber = stockNumber;
	}
	
	@Override
	public String toString() {
		return "StockAvailabilityVO [stockObjectId=" + stockObjectId + ", externalStockObjectUrlPrefix="
				+ externalStockObjectUrlPrefix + ", stockName=" + stockName + ", stockAccession=" + stockAccession
				+ ", stockCenter=" + stockCenter + "]";
	}


}
