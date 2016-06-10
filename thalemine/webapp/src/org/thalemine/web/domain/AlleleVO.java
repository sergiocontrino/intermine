package org.thalemine.web.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.intermine.api.results.ResultElement;
import org.apache.log4j.Logger;


public class AlleleVO extends DomainVO{

	protected static final Logger log = Logger.getLogger(AlleleVO.class);
		
	private String primaryIdentifier;
	private String objectId;
	private String name;
	private String mutagen;
	private String alleleClass;
	private String sequenceAlterationType;
	private String mutationSite;
	private String inheritanceType;
	private String zygosity;
	private List<GeneModelVO> geneModels = new ArrayList<GeneModelVO>();
	private Set<GeneVO> genes = new TreeSet<GeneVO>();
	private List<GeneVO> geneList = new ArrayList<GeneVO>();

	public AlleleVO(){
		
	}
	
	public AlleleVO(List<ResultElement> resElement) {

		objectId = ((resElement.get(0) != null) && (resElement.get(0).getField() != null)) ? resElement.get(0)
				.getField().toString() : "&nbsp;";

		primaryIdentifier = ((resElement.get(1) != null) && (resElement.get(1).getField() != null)) ? resElement.get(1)
				.getField().toString() : "&nbsp;";

		name = ((resElement.get(2) != null) && (resElement.get(2).getField() != null)) ? resElement.get(2).getField()
				.toString() : "&nbsp;";
				
		if (name!=null){
			name = name.toLowerCase();
		}

		mutagen = ((resElement.get(3) != null) && (resElement.get(3).getField() != null)) ? resElement.get(3)
				.getField().toString() : "&nbsp;";

		mutagen = formatValue("mutagen",mutagen);
		
		sequenceAlterationType = ((resElement.get(4) != null) && (resElement.get(4).getField() != null)) ? resElement
				.get(4).getField().toString() : "&nbsp;";
				
		sequenceAlterationType = formatValue("sequenceAlterationType",sequenceAlterationType);

		mutationSite = ((resElement.get(5) != null) && (resElement.get(5).getField() != null)) ? resElement.get(5)
				.getField().toString() : "&nbsp;";
				
		mutationSite = formatValue("mutationSite",mutationSite);

		inheritanceType = ((resElement.get(6) != null) && (resElement.get(6).getField() != null)) ? resElement.get(6)
				.getField().toString() : "&nbsp;";
				
		inheritanceType = formatValue("inheritanceType",inheritanceType);

		alleleClass = ((resElement.get(7) != null) && (resElement.get(7).getField() != null)) ? resElement.get(7)
				.getField().toString() : "&nbsp;";
		
		alleleClass = formatValue("alleleClass",alleleClass);

	}
	
	public AlleleVO(String itemId, String name){
		this.objectId = itemId;
		this.name = name;
	}
	
	public AlleleVO(String itemId, String name, String zygosity){
		this.objectId = itemId;
		this.name = name;
		this.zygosity = zygosity;
	}
	
	public AlleleVO(String itemId, String name, String zygosity, String mutagen){
		this.objectId = itemId;
		this.name = name;
		this.zygosity = formatValue("zygosity", zygosity);
		this.mutagen = formatValue("mutagen", mutagen);
	}
	
	public AlleleVO(String itemId, String name, String zygosity, String mutagen, String inheritanceType, String alleleClass){
		this.objectId = itemId;
		this.name = name;
		this.zygosity = formatValue("zygosity", zygosity);
		this.mutagen = formatValue("mutagen", mutagen);
		this.inheritanceType = formatValue("inheritanceType ", inheritanceType);
		this.alleleClass = formatValue("alleleClass ", alleleClass);
	}
	
	public String formatValue(String type, String value){
		String result = value;
		
		log.debug("Value Object Type:" + type +";" + "Value:" + value);
		
		if (value!=null && !value.isEmpty()){
			if (type.equals("sequenceAlterationType") && value.equalsIgnoreCase("T-dna Insertion")){
				result = "insertion (T-DNA Insertion)";  
			}
			
			if (type.equals("mutagen") && value.equalsIgnoreCase("T Dna Insertion")){
				result = "T-DNA Insertion";  
			}
			
			if (value.equalsIgnoreCase("UNKNOWN")){
				result = "N/A";
			}
			
			if (value.equalsIgnoreCase("null")){
				result = "N/A";
			}
		}
		
		return result;
	}
	
	public String getZygosity() {
		return zygosity;
	}

	public void setZygosity(String zygosity) {
		this.zygosity = zygosity;
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

	public List<GeneModelVO> getGeneModels() {
		return geneModels;
	}

	public void setGeneModels(List<GeneModelVO> geneModels) {
		this.geneModels = geneModels;
	}
	
	public void addGeneVO(GeneVO gene){
		this.addGeneVO(gene);
	}

	public Set<GeneVO> getGenes() {
		return genes;
	}

	public void setGenes(Set<GeneVO> genes) {
		this.genes = genes;
	}

	public List<GeneVO> getGeneList() {
		return geneList;
	}

	public void setGeneList(List<GeneVO> geneList) {
		this.geneList = geneList;
	}

	@Override
	public String toString() {
		return "AlleleVO [primaryIdentifier=" + primaryIdentifier + ", objectId=" + objectId + ", name=" + name
				+ ", mutagen=" + mutagen + ", alleleClass=" + alleleClass + ", sequenceAlterationType="
				+ sequenceAlterationType + ", mutationSite=" + mutationSite + ", inheritanceType=" + inheritanceType
				+ ", zygosity=" + zygosity + ", geneModels=" + geneModels + ", genes=" + genes + ", geneList="
				+ geneList + "]";
	}
	
	
	
	

}
