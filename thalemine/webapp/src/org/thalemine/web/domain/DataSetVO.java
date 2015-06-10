package org.thalemine.web.domain;

import java.util.List;

import org.intermine.api.results.ResultElement;
import org.apache.log4j.Logger;

public class DataSetVO {

	protected static final Logger LOG = Logger.getLogger(DataSetVO.class);

	private String dataSetName;
	private String dataSetUrl;
	private String dataSourceName;
	private String objectId;

	public DataSetVO(List<ResultElement> resElement) {

		objectId = ((resElement.get(0) != null) && (resElement.get(0).getField() != null)) ? resElement.get(0)
				.getField().toString() : "&nbsp;";

		dataSetName = ((resElement.get(1) != null) && (resElement.get(1).getField() != null)) ? resElement.get(1)
				.getField().toString() : "&nbsp;";

		dataSourceName = ((resElement.get(2) != null) && (resElement.get(2).getField() != null)) ? resElement.get(2)
				.getField().toString() : "&nbsp;";

		dataSetUrl = ((resElement.get(3) != null) && (resElement.get(3).getField() != null)) ? resElement.get(3)
				.getField().toString() : "&nbsp;";

	}

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	public String getDataSetUrl() {
		return dataSetUrl;
	}

	public void setDataSetUrl(String dataSetUrl) {
		this.dataSetUrl = dataSetUrl;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

}
