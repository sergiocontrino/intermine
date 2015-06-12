package org.thalemine.web.domain;

import java.util.ArrayList;
import java.util.List;

import org.intermine.api.results.ResultElement;
import org.apache.log4j.Logger;

public class StockVO {

	protected static final Logger LOG = Logger.getLogger(StockVO.class);

	private String stockObjectId;
	private String genotypeObjectId;
	private String genotypeName;
	private String stockName;
	private String germplasmName;
	private String displayName;
	private String stockPrimaryIdentifier;
	private String genotypePrimaryIdentifier;
	private String germplasmPrimaryAccession;
	private String stockAccession;
	private String genotypeDisplayName;
	private List<StrainVO> backgrounds = new ArrayList<StrainVO>();
	private StrainVO strainAccession;
	private List<PhenotypeVO> phenotypes = new ArrayList<PhenotypeVO>();
	
	public StockVO() {

	}

	public StockVO(List<Object> list){
		init(list);
	}
	
	private void init(List<Object> list){
		
		this.genotypeObjectId = getElement(list, 0);
		this.genotypeDisplayName = getElement(list, 1);
		this.genotypeName = getElement(list, 2);
		this.genotypePrimaryIdentifier = getElement(list, 3);
		this.stockObjectId = getElement(list, 4);
		this.germplasmName = getElement(list, 5);
		this.stockPrimaryIdentifier = getElement(list, 6);
		this.stockName  = getElement(list, 7);
		this.germplasmPrimaryAccession = getElement(list, 8);
		this.stockAccession = getElement(list, 9);
		String backgroundAccessionId = getElement(list, 10);
		String backgroundAccessionAbbrName = getElement(list, 11);
		
		this.strainAccession = new StrainVO(backgroundAccessionId, backgroundAccessionAbbrName );
		
	}
	
	/*
	public StockVO(List<ResultElement> resElement) {

		// Genotype Attributes

		genotypeObjectId = ((resElement.get(0) != null) && (resElement.get(0).getField() != null)) ? resElement.get(0)
				.getField().toString() : "&nbsp;";

		genotypeDisplayName = ((resElement.get(1) != null) && (resElement.get(1).getField() != null)) ? resElement
				.get(1).getField().toString() : "&nbsp;";

		genotypeName = ((resElement.get(2) != null) && (resElement.get(2).getField() != null)) ? resElement.get(2)
				.getField().toString() : "&nbsp;";

		genotypePrimaryIdentifier = ((resElement.get(3) != null) && (resElement.get(3).getField() != null)) ? resElement
				.get(3).getField().toString()
				: "&nbsp;";

		// Stock Attributes
		stockObjectId = ((resElement.get(4) != null) && (resElement.get(4).getField() != null)) ? resElement.get(4)
				.getField().toString() : "&nbsp;";

		germplasmName = ((resElement.get(5) != null) && (resElement.get(5).getField() != null)) ? resElement.get(5)
				.getField().toString() : "&nbsp;";

		stockPrimaryIdentifier = ((resElement.get(6) != null) && (resElement.get(6).getField() != null)) ? resElement
				.get(6).getField().toString() : "&nbsp;";

		stockName = ((resElement.get(7) != null) && (resElement.get(7).getField() != null)) ? resElement.get(7)
				.getField().toString() : "&nbsp;";

		germplasmPrimaryAccession = ((resElement.get(8) != null) && (resElement.get(8).getField() != null)) ? resElement
				.get(8).getField().toString()
				: "&nbsp;";

		stockAccession = ((resElement.get(9) != null) && (resElement.get(9).getField() != null)) ? resElement.get(9)
				.getField().toString() : "&nbsp;";

		String backgroundAccessionId = ((resElement.get(10) != null) && (resElement.get(10).getField() != null)) ? resElement
				.get(10).getField().toString()
				: "&nbsp;";

		String backgroundAccessionAbbrName = ((resElement.get(11) != null) && (resElement.get(11).getField() != null)) ? resElement
				.get(11).getField().toString()
				: "&nbsp;";
				
		this.strainAccession = new StrainVO(backgroundAccessionId, backgroundAccessionAbbrName );
		
		
	}
	
	**/
		
	public String getStockObjectId() {
		return stockObjectId;
	}

	public void setStockObjectId(String stockObjectId) {
		this.stockObjectId = stockObjectId;
	}

	public String getGenotypeObjectId() {
		return genotypeObjectId;
	}

	public void setGenotypeObjectId(String genotypeObjectId) {
		this.genotypeObjectId = genotypeObjectId;
	}

	public String getGenotypeName() {
		return genotypeName;
	}

	public void setGenotypeName(String genotypeName) {
		this.genotypeName = genotypeName;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getGermplasmName() {
		return germplasmName;
	}

	public void setGermplasmName(String germplasmName) {
		this.germplasmName = germplasmName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getStockPrimaryIdentifier() {
		return stockPrimaryIdentifier;
	}

	public void setStockPrimaryIdentifier(String stockPrimaryIdentifier) {
		this.stockPrimaryIdentifier = stockPrimaryIdentifier;
	}

	public String getGenotypePrimaryIdentifier() {
		return genotypePrimaryIdentifier;
	}

	public void setGenotypePrimaryIdentifier(String genotypePrimaryIdentifier) {
		this.genotypePrimaryIdentifier = genotypePrimaryIdentifier;
	}

	public String getGermplasmPrimaryAccession() {
		return germplasmPrimaryAccession;
	}

	public void setGermplasmPrimaryAccession(String germplasmPrimaryAccession) {
		this.germplasmPrimaryAccession = germplasmPrimaryAccession;
	}

	public String getStockAccession() {
		return stockAccession;
	}

	public void setStockAccession(String stockAccession) {
		this.stockAccession = stockAccession;
	}

	public String getGenotypeDisplayName() {
		return genotypeDisplayName;
	}

	public void setGenotypeDisplayName(String genotypeDisplayName) {
		this.genotypeDisplayName = genotypeDisplayName;
	}

	public List<StrainVO> getBackgrounds() {
		return backgrounds;
	}

	public void setBackgrounds(List<StrainVO> backgrounds) {
		this.backgrounds = backgrounds;
	}

	public StrainVO getStrainAccession() {
		return strainAccession;
	}

	public void setStrainAccession(StrainVO strainAccession) {
		this.strainAccession = strainAccession;
	}

	public List<PhenotypeVO> getPhenotypes() {
		return phenotypes;
	}

	public void setPhenotypes(List<PhenotypeVO> phenotypes) {
		this.phenotypes = phenotypes;
	}
	
	public String getBackground(){
		
		String result = null;
		
		StringBuilder strBuilder= new StringBuilder();
		
		for (StrainVO item:backgrounds){
			strBuilder.append(item.getAbbreviationName());
			
			if (backgrounds.size() > 1) {
				strBuilder.append(";");
			}
		}
		
		if (strBuilder.length() > 0){
			result = strBuilder.toString(); 
		}else{
			result = "&nbsp;"; 
		}
		
		return result;
	}

	@Override
	public String toString() {
		return "StockVO [stockObjectId=" + stockObjectId + ", genotypeObjectId=" + genotypeObjectId + ", genotypeName="
				+ genotypeName + ", stockName=" + stockName + ", germplasmName=" + germplasmName + ", displayName="
				+ displayName + ", stockPrimaryIdentifier=" + stockPrimaryIdentifier + ", genotypePrimaryIdentifier="
				+ genotypePrimaryIdentifier + ", germplasmPrimaryAccession=" + germplasmPrimaryAccession
				+ ", stockAccession=" + stockAccession + ", genotypeDisplayName=" + genotypeDisplayName
				+ ", backgrounds=" + backgrounds + ", strainAccession=" + strainAccession + ", phenotypes="
				+ phenotypes + "]";
	}
	
	
	private String getElement(List<Object> list, int index){
		
		String element = ((list.get(index) != null) && (list.get(index) != null)) ? list.get(index)
				.toString() : "&nbsp;";
		
		return element;
		
	}
}
