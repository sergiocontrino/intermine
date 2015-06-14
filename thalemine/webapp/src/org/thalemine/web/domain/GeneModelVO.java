package org.thalemine.web.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneModelVO extends DomainVO {

	private String geneObjectId;
	private String geneModelObjectId;
	private String alleleName;
	private String geneName;
	private String geneModelName;
	private String description;
	private String polymorphismSite;
	private String relationshipType;
	
	private Map<String,String> geneMap = new HashMap<String,String>(); 

	public static final String RELATIONSHIP_TYPE = "is an allele of";

	public GeneModelVO() {

	}

	public GeneModelVO(List<Object> list) {
		init(list);
	}

	private void init(List<Object> list) {

		this.geneObjectId = getElement(list, 0);
		this.geneModelObjectId = getElement(list, 1);
		this.alleleName = getElement(list, 2);
		this.geneName = getElement(list, 3);
		this.geneModelName = getElement(list, 4);
		this.description = getElement(list, 5);

		this.relationshipType = RELATIONSHIP_TYPE;

	}

	public String getAlleleName() {
		return alleleName;
	}

	public void setAlleleName(String alleleName) {
		this.alleleName = alleleName;
	}

	public String getGeneObjectId() {
		return geneObjectId;
	}

	public void setGeneObjectId(String geneObjectId) {
		this.geneObjectId = geneObjectId;
	}

	public String getGeneModelObjectId() {
		return geneModelObjectId;
	}

	public void setGeneModelObjectId(String geneModelObjectId) {
		this.geneModelObjectId = geneModelObjectId;
	}

	public String getGeneName() {
		return geneName;
	}

	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	public String getGeneModelName() {
		return geneModelName;
	}

	public void setGeneModelName(String geneModelName) {
		this.geneModelName = geneModelName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPolymorphismSite() {
		return polymorphismSite;
	}

	public void setPolymorphismSite(String polymorphismSite) {
		this.polymorphismSite = polymorphismSite;
	}

	public String getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}

	@Override
	public String toString() {
		return "GeneModelVO [geneObjectId=" + geneObjectId + ", geneModelObjectId=" + geneModelObjectId
				+ ", alleleName=" + alleleName + ", geneName=" + geneName + ", geneModelName=" + geneModelName
				+ ", description=" + description + ", polymorphismSite=" + polymorphismSite + ", relationshipType="
				+ relationshipType + "]";
	}
	
	

}
