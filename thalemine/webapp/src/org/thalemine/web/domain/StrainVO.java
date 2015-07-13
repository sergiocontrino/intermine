package org.thalemine.web.domain;

import java.util.List;

import org.intermine.api.results.ResultElement;
import org.apache.log4j.Logger;

public class StrainVO extends DomainVO {

	protected static final Logger LOG = Logger.getLogger(StrainVO.class);

	private String objectId;
	private String primaryIdentifier;
	private String name;
	private String abbreviationName;
	private String infraspecificName;
	private String habitat;

	private String geoLocation;

	public StrainVO() {

	}

	public StrainVO(String objectId, String abbreviationName) {
		this.objectId = objectId;
		this.abbreviationName = abbreviationName;
	}

	public StrainVO(List<Object> list) {
		init(list);
	}

	public StrainVO(List<Object> list, boolean allFields) {
		initAccession(list);
	}
	
	private void init(List<Object> list) {

		this.objectId = getElement(list, 0);
		this.abbreviationName = getElement(list, 1);

	}

	
	private void initAccession(List<Object> list) {

		this.objectId = getElement(list, 0);
		this.abbreviationName = getElement(list, 1);
		this.infraspecificName = getElement(list, 2);
		this.habitat = getElement(list, 3);
		this.geoLocation = getElement(list, 4);

	}
	
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
				+ ", abbreviationName=" + abbreviationName + ", infraspecificName=" + infraspecificName + ", habitat="
				+ habitat + ", geoLocation=" + geoLocation + "]";
	}

	public String getHabitat() {
		return habitat;
	}

	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

}
