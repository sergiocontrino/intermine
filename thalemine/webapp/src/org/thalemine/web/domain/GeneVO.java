package org.thalemine.web.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneVO extends DomainVO{

	private String geneObjectId;
	private String geneName;
	
	private Map<String, GeneModelVO> geneModels = new HashMap<String, GeneModelVO>();
	
	public Map<String, GeneModelVO> getGeneModels() {
		return geneModels;
	}

	public void setGeneModels(Map<String, GeneModelVO> geneModels) {
		this.geneModels = geneModels;
	}

	public GeneVO(){
		
	}
	
	public GeneVO(List<Object> list) {
		init(list);
	}

	public GeneVO(String geneObjectId, String geneName) {
		
		this.geneObjectId = geneObjectId;
		this.geneName = geneName;
	}

	private void init(List<Object> list) {

		this.geneObjectId = getElement(list, 0);
		this.geneName = getElement(list, 3);

	}
	
	public String getGeneObjectId() {
		return geneObjectId;
	}
	public void setGeneObjectId(String geneObjectId) {
		this.geneObjectId = geneObjectId;
	}
	public String getGeneName() {
		return geneName;
	}
	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}
	
	
}
