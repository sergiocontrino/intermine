package org.thalemine.web.domain.datacategory;
import org.intermine.model.bio.DataSet;
import org.intermine.model.bio.DataSource;


public class DataSetSO {
	
	public DataSetSO (){
		
	}
	public DataSetSO(String name) {
		super();
		this.name = name;
	}

	private String name;
	private String url;
	private String releaseName;
	private Reference reference;
	private DataSet dataSet;
		
	private long geneCount;
	private long featureCount;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	public Reference getReference() {
		return reference;
	}
	public void setReference(Reference reference) {
		this.reference = reference;
	}
	public long getGeneCount() {
		return geneCount;
	}
	public void setGeneCount(long geneCount) {
		this.geneCount = geneCount;
	}
	public long getFeatureCount() {
		return featureCount;
	}
	public void setFeatureCount(long featureCount) {
		this.featureCount = featureCount;
	}
	
	
	public DataSet getDataSet() {
		return dataSet;
	}
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
	@Override
	public String toString() {
		return "DataSetSO [name=" + name + ", url=" + url + ", releaseName=" + releaseName + ", reference=" + reference
				+ ", geneCount=" + geneCount + ", featureCount=" + featureCount + "]";
	}

}
