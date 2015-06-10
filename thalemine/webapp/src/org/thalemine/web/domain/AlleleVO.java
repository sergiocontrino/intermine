package org.thalemine.web.domain;

import java.util.List;
import org.intermine.api.results.ResultElement;

public class AlleleVO {
	
	private String primaryIdentifier;
	private String objectId;
	private String name;
	private String mutagen;
	private String alleleClass;
	private String sequenceAlterationType;
	private String mutationSite;
	private String inheritanceType;
	
	

	public AlleleVO(List<ResultElement> resElement){
		
	name = ((resElement.get(0)!=null) && (resElement.get(0).getField()!= null))?
                resElement.get(0).getField().toString():"&nbsp;";
                
    mutagen = ((resElement.get(1)!=null) && (resElement.get(1).getField()!= null))?
                resElement.get(1).getField().toString():"&nbsp;";
                
   // sequenceAlterationType = ((resElement.get(2)!=null) && (resElement.get(2).getField()!= null))?
     //           resElement.get(2).getField().toString():"&nbsp;";
                
		
	}
	
	public String getPrimaryIdentifier() {
		return primaryIdentifier;
	}




	public void setPrimaryIdentifier(String primaryIdentifier) {
		this.primaryIdentifier = primaryIdentifier;
	}




	public String getObjectId() {
		return objectId;
	}




	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getMutagen() {
		return mutagen;
	}




	public void setMutagen(String mutagen) {
		this.mutagen = mutagen;
	}




	public String getAlleleClass() {
		return alleleClass;
	}




	public void setAlleleClass(String alleleClass) {
		this.alleleClass = alleleClass;
	}




	public String getSequenceAlterationType() {
		return sequenceAlterationType;
	}




	public void setSequenceAlterationType(String sequenceAlterationType) {
		this.sequenceAlterationType = sequenceAlterationType;
	}




	public String getMutationSite() {
		return mutationSite;
	}




	public void setMutationSite(String mutationSite) {
		this.mutationSite = mutationSite;
	}




	public String getInheritanceType() {
		return inheritanceType;
	}




	public void setInheritanceType(String inheritanceType) {
		this.inheritanceType = inheritanceType;
	}




	@Override
	public String toString() {
		return "AlleleVO [primaryIdentifier=" + primaryIdentifier + ", objectId=" + objectId + ", name=" + name
				+ ", mutagen=" + mutagen + ", alleleClass=" + alleleClass + ", sequenceAlterationType="
				+ sequenceAlterationType + ", mutationSite=" + mutationSite + ", inheritanceType=" + inheritanceType
				+ "]";
	}
	
	

}
