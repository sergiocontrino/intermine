package org.intermine.bio.domain.source;

public class SourceStock {

	private long stockId;
	private String name;
	private String uniqueName;
	private String displayName;
	private String stockName;
	private String description;
	private int dbxrefId;
	private String stockType;
	private String germplasmTairAccession;
	private String stockTairAccession;
	private String mutagen;
	private String stockCategory;
	private String stockCenterComment;
	private int organismId;
	private int backgroundAccessionId;
	private int directBackgroundAccessionId;

	
	public SourceStock() {

	}

	public SourceStock(long stockId, String name, String description
			) {

		this.stockId = stockId;
		this.name = name;
		this.description = description;
	
	}
	
	public long getStockId() {
		return stockId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDbxrefId() {
		return dbxrefId;
	}

	public void setDbxrefId(int dbxrefId) {
		this.dbxrefId = dbxrefId;
	}

	
	public String getTairObjectId() {
		return germplasmTairAccession;
	}

	public void setTairObjectId(String tairObjectId) {
		this.germplasmTairAccession = tairObjectId;
	}
	
	public int getOrganismId() {
		return organismId;
	}

	public void setOrganismId(int organismId) {
		this.organismId = organismId;
	}


	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public void setStockId(long stockId) {
		this.stockId = stockId;
	}

	
	public int getBackgroundAccessionId() {
		return backgroundAccessionId;
	}

	public void setBackgroundAccessionId(int backgroundAccessionId) {
		this.backgroundAccessionId = backgroundAccessionId;
	}
	
	public int getDirectBackgroundAccessionId() {
		return directBackgroundAccessionId;
	}

	public void setDirectBackgroundAccessionId(int directBackgroundAccessionId) {
		this.directBackgroundAccessionId = directBackgroundAccessionId;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getGermplasmTairAccession() {
		return germplasmTairAccession;
	}

	public void setGermplasmTairAccession(String germplasmTairAccession) {
		this.germplasmTairAccession = germplasmTairAccession;
	}

	public String getStockTairAccession() {
		return stockTairAccession;
	}

	public void setStockTairAccession(String stockTairAccession) {
		this.stockTairAccession = stockTairAccession;
	}

	public String getMutagen() {
		return mutagen;
	}

	public void setMutagen(String mutagen) {
		this.mutagen = mutagen;
	}

	public String getStockCategory() {
		return stockCategory;
	}

	public void setStockCategory(String stockCategory) {
		this.stockCategory = stockCategory;
	}

	public String getStockCenterComment() {
		return stockCenterComment;
	}

	public void setStockCenterComment(String stockCenterComment) {
		this.stockCenterComment = stockCenterComment;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String toString() {
		return "SourceStock [stockId=" + stockId + ", name=" + name + ", uniqueName=" + uniqueName + ", displayName="
				+ displayName + ", stockName=" + stockName + ", description=" + description + ", dbxrefId=" + dbxrefId
				+ ", stockType=" + stockType + ", germplasm_tair_accession=" + germplasmTairAccession
				+ ", stock_tair_accession=" + stockTairAccession + ", mutagen=" + mutagen + ", stockCategory="
				+ stockCategory + ", stockCenterComment=" + stockCenterComment + ", organismId=" + organismId
				+ ", backgroundAccessionId=" + backgroundAccessionId + ", directBackgroundAccessionId="
				+ directBackgroundAccessionId + "]";
	}
	
	
		
}
