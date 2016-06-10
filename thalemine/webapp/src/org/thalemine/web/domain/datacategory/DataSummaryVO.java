package org.thalemine.web.domain.datacategory;

import java.util.ArrayList;
import java.util.List;

public class DataSummaryVO {
	
	private long dataSourceId;
	private long dataSetId;
	private int sortOrderId;
	private String rowType;
	private int parentDataSetId;
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
	private String geneCount;
	private String featureCount;
	private List <DataSummaryVO> categoryDetails = new ArrayList<DataSummaryVO>();
	private String units;
	private String pubTitle;
	
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
	public String getGeneCount() {
		return geneCount;
	}
	public void setGeneCount(String geneCount) {
		this.geneCount = geneCount;
	}
	public String getFeatureCount() {
		return featureCount;
	}
	public void setFeatureCount(String featureCount) {
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

	
	public String getRowType() {
		return rowType;
	}

	public void setRowType(String rowType) {
		this.rowType = rowType;
	}

	public int getParentDataSetId() {
		return parentDataSetId;
	}

	public void setParentDataSetId(int parentDataSetId) {
		this.parentDataSetId = parentDataSetId;
	}

	public void addCategoryDetail(DataSummaryVO detail){
		categoryDetails.add(detail);
	}

	public List<DataSummaryVO> getCategoryDetails() {
		return categoryDetails;
	}

	public void setCategoryDetails(List<DataSummaryVO> categoryDetails) {
		this.categoryDetails = categoryDetails;
	}

	
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getPubTitle() {
		return pubTitle;
	}

	public void setPubTitle(String pubTitle) {
		this.pubTitle = pubTitle;
	}

	@Override
	public String toString() {
		return "DataSummaryVO [dataSourceId=" + dataSourceId + ", dataSetId=" + dataSetId + ", sortOrderId="
				+ sortOrderId + ", rowType=" + rowType + ", parentDataSetId=" + parentDataSetId + ", categoryName="
				+ categoryName + ", dataSourceName=" + dataSourceName + ", dataSetName=" + dataSetName
				+ ", dataSetVersion=" + dataSetVersion + ", dataSetDescription=" + dataSetDescription
				+ ", dataSourceDescription=" + dataSourceDescription + ", dataSourceUrl=" + dataSourceUrl
				+ ", dataSetUrl=" + dataSetUrl + ", pubMedId=" + pubMedId + ", author=" + author + ", year=" + year
				+ ", geneCount=" + geneCount + ", featureCount=" + featureCount + ", categoryDetails="
				+ categoryDetails + ", units=" + units + ", pubTitle=" + pubTitle + "]";
	}
	

	
}
