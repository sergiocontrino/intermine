package org.thalemine.web.domain;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class AlleleSummaryVO extends DomainVO {
	
	private String objectId;
	private String primaryIdentifier;
	private String mutagen;
	private String mutationSite;
	private String sequenceAlterationType;
	private String alleleClass;
	private String inheritanceType;
	
	protected static final Logger log = Logger.getLogger(AlleleSummaryVO .class);
	
	public AlleleSummaryVO()
	{
		
	}
	
	public  AlleleSummaryVO(List<Object> list) {
		init(list);
	}

	private void init(List<Object> list) {

		this.objectId = getElement(list, 0);
		this.primaryIdentifier = getElement(list, 1);
		this.mutagen =  formatValue("mutagen", getElement(list, 2));	
		this.mutationSite = formatValue("mutationSite",getElement(list, 3));
		this.sequenceAlterationType  = formatValue("sequenceAlterationType",getElement(list, 4));
		this.alleleClass = formatValue("alleleClass",getElement(list, 5));	
		this.inheritanceType = formatValue("inheritanceType",getElement(list, 6));

	}
	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getMutagen() {
		return mutagen;
	}
	public void setMutagen(String mutagen) {
		this.mutagen = mutagen;
	}
	public String getMutationSite() {
		return mutationSite;
	}
	public void setMutationSite(String mutationSite) {
		this.mutationSite = mutationSite;
	}
	public String getSequenceAlterationType() {
		return sequenceAlterationType;
	}
	public void setSequenceAlterationType(String sequenceAlterationType) {
		this.sequenceAlterationType = sequenceAlterationType;
	}
	public String getAlleleClass() {
		return alleleClass;
	}
	public void setAlleleClass(String alleleClass) {
		this.alleleClass = alleleClass;
	}
	public String getInheritanceType() {
		return inheritanceType;
	}
	public void setInheritanceType(String inheritanceType) {
		this.inheritanceType = inheritanceType;
	}
	
	public String getPrimaryIdentifier() {
		return primaryIdentifier;
	}

	public void setPrimaryIdentifier(String primaryIdentifier) {
		this.primaryIdentifier = primaryIdentifier;
	}


	@Override
	public String toString() {
		return "AlleleSummaryVO [objectId=" + objectId + ", primaryIdentifier=" + primaryIdentifier + ", mutagen="
				+ mutagen + ", mutationSite=" + mutationSite + ", sequenceAlterationType=" + sequenceAlterationType
				+ ", alleleClass=" + alleleClass + ", inheritanceType=" + inheritanceType + "]";
	}
	
	
}
