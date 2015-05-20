package org.intermine.bio.domain.source;

public class SourceStock {

	private long stockId;
	private String name;
	private String uniqueName;
	private String description;
	private boolean isObsolete;
	private int stockTypeId;
	private int dbxrefId;
	private long tairObjectId;
	private int organismId;
	private int backgroundAccessionId;
	private int directBackgroundAccessionId;
	private int computedOrganismId;
	
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

	public int getComputedOrganismId() {
		return computedOrganismId;
	}

	public void setComputedOrganismId(int computedOrganismId) {
		this.computedOrganismId = computedOrganismId;
	}
	
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

	public boolean isObsolete() {
		return isObsolete;
	}

	public void setObsolete(boolean isObsolete) {
		this.isObsolete = isObsolete;
	}
	
	public int getStockTypeId() {
		return stockTypeId;
	}

	public void setStockTypeId(int stockTypeId) {
		this.stockTypeId = stockTypeId;
	}

	public int getDbxrefId() {
		return dbxrefId;
	}

	public void setDbxrefId(int dbxrefId) {
		this.dbxrefId = dbxrefId;
	}

	
	public long getTairObjectId() {
		return tairObjectId;
	}

	public void setTairObjectId(long tairObjectId) {
		this.tairObjectId = tairObjectId;
	}
	
	public int getOrganismId() {
		return organismId;
	}

	public void setOrganismId(int organismId) {
		this.organismId = organismId;
	}

	@Override
	public String toString() {
		return "Stock [stockId=" + stockId + ", name=" + name
				+ ", description=" + description + ", stockType=" +
				 ", isObsolete=" + isObsolete + ", stockTypeId=" + stockTypeId
				+ ", dbxrefId=" + dbxrefId + ", tairObjectId=" + tairObjectId
				+ ", organismId=" + organismId + "]";
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
		
}
