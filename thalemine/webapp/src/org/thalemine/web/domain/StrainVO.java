package org.thalemine.web.domain;

import java.util.List;

import org.intermine.api.results.ResultElement;
import org.apache.log4j.Logger;

public class StrainVO {

	protected static final Logger LOG = Logger.getLogger(StrainVO.class);

	private String objectId;
	private String primaryIdentifier;
	private String name;
	private String abbreviationName;

	public StrainVO() {

	}

	public StrainVO(String objectId, String abbreviationName) {
		this.objectId = objectId;
		this.abbreviationName = abbreviationName;
	}

	public StrainVO(List<Object> list) {
		init(list);
	}

	private void init(List<Object> list) {

		this.objectId = getElement(list, 0);
		this.abbreviationName = getElement(list, 1);

	}

	/**
	 * public StrainVO(List<ResultElement> resElement) {
	 * 
	 * 
	 * objectId = ((resElement.get(0) != null) && (resElement.get(0).getField()
	 * != null)) ? resElement.get(0) .getField().toString() : "&nbsp;";
	 * 
	 * abbreviationName = ((resElement.get(1) != null) &&
	 * (resElement.get(1).getField() != null)) ? resElement
	 * .get(1).getField().toString() : "&nbsp;"; }
	 */

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getPrimaryIdentifier() {
		return primaryIdentifier;
	}

	public void setPrimaryIdentifier(String primaryIdentifier) {
		this.primaryIdentifier = primaryIdentifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviationName() {
		return abbreviationName;
	}

	public void setAbbreviationName(String abbreviationName) {
		this.abbreviationName = abbreviationName;
	}

	@Override
	public String toString() {
		return "StrainVO [objectId=" + objectId + ", primaryIdentifier=" + primaryIdentifier + ", name=" + name
				+ ", abbreviationName=" + abbreviationName + "]";
	}

	private String getElement(List<Object> list, int index) {

		String element = ((list.get(index) != null) && (list.get(index) != null)) ? list.get(index).toString()
				: "&nbsp;";

		return element;

	}

}
