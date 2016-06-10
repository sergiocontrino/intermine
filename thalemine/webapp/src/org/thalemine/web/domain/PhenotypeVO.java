package org.thalemine.web.domain;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class PhenotypeVO {

	protected static final Logger log = Logger.getLogger(PhenotypeVO.class);
	
	private String objectId;
	private String description;
	private List<PublicationVO> publications = new ArrayList<PublicationVO>();
	
	public PhenotypeVO(){
		
	}
	
	public PhenotypeVO(String objectId,String description){
		this.objectId = objectId;
		this.description =description;
	}


	public PhenotypeVO(List<Object> list) {
		init(list);
	}

	private void init(List<Object> list) {

		this.objectId = getElement(list, 0);
		this.description = getElement(list, 1);

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
	
	private String getElement(List<Object> list, int index) {

		String element = ((list.get(index) != null) && (list.get(index) != null)) ? list.get(index).toString()
				: "&nbsp;";

		return element;

	}
	
}
