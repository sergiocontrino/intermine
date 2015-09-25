package org.thalemine.web.domain.datacategory;

public class DataSummaryVO {
	
	private long dataSourceId;
	private long dataSetId;
	private int sortOrderId;
	private String categoryName;
	private String dataSourceName;
	private String dataSetName;
	private String dataSetVersion;
	private String dataSetDescription;
	private String dataSourceDescription;
	private String dataSourceUrl;
	private String dataSetUrl;
	private String pubMedId;
	private String author;
	private String year;
	private int geneCount;
	private int featureCount;
	
	public DataSummaryVO(){
		
	}
	
	public long getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(long dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public long getDataSetId() {
		return dataSetId;
	}
	public void setDataSetId(long dataSetId) {
		this.dataSetId = dataSetId;
	}
	public int getSortOrderId() {
		return sortOrderId;
	}
	public void setSortOrderId(int sortOrderId) {
		this.sortOrderId = sortOrderId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getDataSetName() {
		return dataSetName;
	}
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}
	
	public String getDataSetDescription() {
		return dataSetDescription;
	}
	public void setDataSetDescription(String dataSetDescription) {
		this.dataSetDescription = dataSetDescription;
	}
	public String getDataSourceDescription() {
		return dataSourceDescription;
	}
	public void setDataSourceDescription(String dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}
	public String getDataSetUrl() {
		return dataSetUrl;
	}
	public void setDataSetUrl(String dataSetUrl) {
		this.dataSetUrl = dataSetUrl;
	}
	public String getPubMedId() {
		return pubMedId;
	}
	public void setPubMedId(String pubMedId) {
		this.pubMedId = pubMedId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getGeneCount() {
		return geneCount;
	}
	public void setGeneCount(int geneCount) {
		this.geneCount = geneCount;
	}
	public int getFeatureCount() {
		return featureCount;
	}
	public void setFeatureCount(int featureCount) {
		this.featureCount = featureCount;
	}
	
	public String getDataSourceUrl() {
		return dataSourceUrl;
	}

	public void setDataSourceUrl(String dataSourceUrl) {
		this.dataSourceUrl = dataSourceUrl;
	}

	public String getDataSetVersion() {
		return dataSetVersion;
	}

	public void setDataSetVersion(String dataSetVersion) {
		this.dataSetVersion = dataSetVersion;
	}

	@Override
	public String toString() {
		return "DataSummaryVO [dataSourceId=" + dataSourceId + ", dataSetId=" + dataSetId + ", sortOrderId="
				+ sortOrderId + ", categoryName=" + categoryName + ", dataSourceName=" + dataSourceName
				+ ", dataSetName=" + dataSetName + ", dataSetVersion=" + dataSetVersion + ", dataSetDescription="
				+ dataSetDescription + ", dataSourceDescription=" + dataSourceDescription + ", dataSourceUrl="
				+ dataSourceUrl + ", dataSetUrl=" + dataSetUrl + ", pubMedId=" + pubMedId + ", author=" + author
				+ ", year=" + year + ", geneCount=" + geneCount + ", featureCount=" + featureCount + "]";
	}
	

	

}
