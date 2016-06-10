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
	
	
	public GeneModelVO(String itemId, String name) {
		this.geneModelObjectId = itemId;
		this.geneModelName = name;
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
	
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj != null && getClass() == obj.getClass()) {

			GeneModelVO geneModel = (GeneModelVO) obj; // Classes are equal, downcast

			if (geneModelObjectId.equals(geneModel.getGeneObjectId()) && geneModelName.equals(geneModel.geneModelName)) { // Compare
																									// attributes
				return true;

			}
		}

		return false;
	}
	
	public int hashCode() {

		StringBuilder builder = new StringBuilder();

		if (this.geneModelObjectId != null) {

			builder.append(geneModelObjectId);

		}

		if (this.geneModelName != null) {

			builder.append(geneModelName);

		}

		final int prime = 31;
		int result = 1;

		if (builder != null && builder.length() > 0) {
			result = prime * result + builder.hashCode();
		} else {
			result = prime * result + 0;
		}

		return result;

	}

	@Override
	public String toString() {
		return "GeneModelVO [geneObjectId=" + geneObjectId + ", geneModelObjectId=" + geneModelObjectId
				+ ", alleleName=" + alleleName + ", geneName=" + geneName + ", geneModelName=" + geneModelName
				+ ", description=" + description + ", polymorphismSite=" + polymorphismSite + ", relationshipType="
				+ relationshipType + "]";
	}
	
	

}
