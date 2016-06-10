package org.thalemine.web.domain;

import java.util.List;

public class StockAnnotationVO extends DomainVO{
	
	private String objectId;
	private String stockPrimaryIdentifier;
	private String mutagen;
	private String aneploid;
	private String ploidy;
	private String growthConditions;
	private String growthTemperature;
	private String mutant;
	private String transgene;
	private String naturalVariant;
	
	public StockAnnotationVO(){
		
	}
	
	public StockAnnotationVO(List<Object> list){
		init(list);
	}
	
	private void init(List<Object> list){
		
		this.objectId = getElement(list, 0);
		this.stockPrimaryIdentifier = getElement(list, 1);
		this.mutagen = formatValue("mutagen", getElement(list, 2));
		this.aneploid = getElement(list, 3);
		this.ploidy = getElement(list, 4);
		this.growthConditions = getElement(list, 5);
		this.mutant = getElement(list, 6);
		this.transgene = getElement(list, 7);
		this.naturalVariant = getElement(list, 8);		
		
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
	public String getMutagen() {
		return mutagen;
	}
	public void setMutagen(String mutagen) {
		this.mutagen = mutagen;
	}
	public String getAneploid() {
		return aneploid;
	}
	public void setAneploid(String aneploid) {
		this.aneploid = aneploid;
	}
	public String getPloidy() {
		return ploidy;
	}
	public void setPloidy(String ploidy) {
		this.ploidy = ploidy;
	}
	public String getGrowthConditions() {
		return growthConditions;
	}
	public void setGrowthConditions(String growthConditions) {
		this.growthConditions = growthConditions;
	}
	public String getGrowthTemperature() {
		return growthTemperature;
	}
	public void setGrowthTemperature(String growthTemperature) {
		this.growthTemperature = growthTemperature;
	}
	public String getTransgene() {
		return transgene;
	}
	public void setTransgene(String transgene) {
		this.transgene = transgene;
	}
	public String getNaturalVariant() {
		return naturalVariant;
	}
	public void setNaturalVariant(String naturalVariant) {
		this.naturalVariant = naturalVariant;
	}
	
	public String getMutant() {
		return mutant;
	}

	public void setMutant(String mutant) {
		this.mutant = mutant;
	}

	@Override
	public String toString() {
		return "StockAnnotationVO [objectId=" + objectId + ", stockPrimaryIdentifier=" + stockPrimaryIdentifier
				+ ", mutagen=" + mutagen + ", aneploid=" + aneploid + ", ploidy=" + ploidy + ", growthConditions="
				+ growthConditions + ", growthTemperature=" + growthTemperature + ", mutant=" + mutant + ", transgene="
				+ transgene + ", naturalVariant=" + naturalVariant + "]";
	}
	
	
	

}
