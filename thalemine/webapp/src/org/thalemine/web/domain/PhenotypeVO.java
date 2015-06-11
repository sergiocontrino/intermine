package org.thalemine.web.domain;


import java.util.ArrayList;
import java.util.List;

import org.intermine.api.results.ResultElement;
import org.apache.log4j.Logger;

public class PhenotypeVO {

	protected static final Logger LOG = Logger.getLogger(PhenotypeVO.class);
	
	private String objectId;
	private String description;
	private List<PublicationVO> publications = new ArrayList<PublicationVO>();
	
	public PhenotypeVO(){
		
	}
	
	public PhenotypeVO(String objectId,String description){
		this.objectId = objectId;
		this.description =description;
	}


	public PhenotypeVO(List<ResultElement> resElement) {


			objectId = ((resElement.get(0) != null) && (resElement.get(0).getField() != null)) ? resElement.get(0)
					.getField().toString() : "&nbsp;";

			description = ((resElement.get(1) != null) && (resElement.get(1).getField() != null)) ? resElement
					.get(1).getField().toString() : "&nbsp;";
	}
	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<PublicationVO> getPublications() {
		return publications;
	}
	public void setPublications(List<PublicationVO> publications) {
		this.publications = publications;
	}
	
	public String toString(){
		
		if (description!=null){
			return description;
		}else{
			return "&nbsp;";
		}
	}
	
}
