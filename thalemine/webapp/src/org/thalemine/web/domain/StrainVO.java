package org.thalemine.web.domain;

public class StrainVO {

private String objectId;
private String primaryIdentifier;
private String name;
private String abbreviationName;

private StrainVO(){
	
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
			+ ", abbreviationName=" + abbreviationName + "]";
}

	
}
