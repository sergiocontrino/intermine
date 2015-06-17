package org.thalemine.web.domain;

import java.util.ArrayList;
import java.util.List;

public class StockGenotypeVO extends DomainVO {
	
	private String stockObjectId;
	private String stockName;
	private String genotypeObjectId;
	private String genotypeName;
	private List<AlleleVO> alleles;
	private List<GeneVO> genes;
	private List<GeneModelVO> geneModels = new ArrayList<GeneModelVO>();
	
	
	public StockGenotypeVO(){
		
	}
	
	
	public StockGenotypeVO(List<Object> list){
		init(list);
	}
	
	private void init (List<Object> list){
		
		this.stockObjectId = getElement(list, 0);
		this.stockName = getElement(list, 1);
		this.genotypeObjectId= getElement(list, 2);
		this.genotypeName = getElement(list, 3);
					
	}
	

	public String getStockObjectId() {
		return stockObjectId;
	}
	public void setStockObjectId(String stockObjectId) {
		this.stockObjectId = stockObjectId;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
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


	public List<AlleleVO> getAlleles() {
		return alleles;
	}


	public void setAlleles(List<AlleleVO> alleles) {
		this.alleles = alleles;
	}


	public List<GeneVO> getGenes() {
		return genes;
	}


	public void setGenes(List<GeneVO> genes) {
		this.genes = genes;
	}

	
	public List<GeneModelVO> getGeneModels() {
		return geneModels;
	}


	public void setGeneModels(List<GeneModelVO> geneModels) {
		this.geneModels = geneModels;
	}


	@Override
	public String toString() {
		return "StockGenotypeVO [stockObjectId=" + stockObjectId + ", stockName=" + stockName + ", genotypeObjectId="
				+ genotypeObjectId + ", genotypeName=" + genotypeName + ", alleles=" + alleles + ", genes=" + genes
				+ ", geneModels=" + geneModels + "]";
	}

	
		
	

}
