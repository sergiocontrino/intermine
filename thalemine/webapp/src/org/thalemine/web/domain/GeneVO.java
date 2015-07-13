package org.thalemine.web.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.thalemine.web.constants.ComparisonConstants;

public class GeneVO extends DomainVO implements Comparable<GeneVO>{

	private String geneObjectId;
	private String geneName;

	private Set<GeneModelVO> geneModels = new TreeSet<GeneModelVO>();

	public Set<GeneModelVO> getGeneModels() {
		return geneModels;
	}

	public void setGeneModels(Set<GeneModelVO> geneModels) {
		
		this.geneModels = geneModels;
	}

	public GeneVO() {

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

	public void addGeneModel(GeneModelVO geneModel){
		this.geneModels.add(geneModel);
	}
	
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj != null && getClass() == obj.getClass()) {

			GeneVO gene = (GeneVO) obj; // Classes are equal, downcast

			if (geneObjectId.equals(gene.getGeneObjectId()) && geneName.equals(gene.geneName)) { // Compare
																									// attributes
				return true;

			}
		}

		return false;
	}

	public int hashCode() {

		StringBuilder builder = new StringBuilder();

		if (this.geneObjectId != null) {

			builder.append(geneObjectId);

		}

		if (this.geneName != null) {

			builder.append(geneName);

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
	public int compareTo(GeneVO that) {
		
	    int comparison = this.geneObjectId.compareTo(that.geneObjectId);
	    if (comparison != ComparisonConstants.EQUAL) return comparison;
	    
	    comparison = this.geneName.compareTo(that.geneName);
	    
	    if (comparison != ComparisonConstants.EQUAL) return comparison;
	    
		return ComparisonConstants.EQUAL;
	}

	@Override
	public String toString() {
		return "GeneVO [geneObjectId=" + geneObjectId + ", geneName=" + geneName + ", geneModels=" + geneModels + "]";
	}
	
	

}
