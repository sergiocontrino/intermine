package org.intermine.bio.datalineage;

public enum DataFlowStepType {
	
	ORGANISM(0,"Organism"),
	STOCK_TYPE_CV(1, "Stock Type"),
	STOCK_CLASS_CV(2, "Stock Class"),
	MUTAGEN_CV(3, "Mutagen"),
	ALLELE_CLASS_CV (4, "Allele Class"),
	INHERITANCEMODE_CV (5, "Inheritance Mode"),
	MUTATION_SITE_CV (6, "Mutation Site"),
	ZYGOCITY_TYPE_CV (7, "Zygosity Type"),
	CONTACT_TYPE_CV (8, "Contact Type"),
	ATTRIBUTIONTYPE_CV (9, "Attribution Type"),
	SEQUENCE_ALTERATION_TYPE_CV(10, "Sequence Alteration Type"),
	STRAIN_TYPE_CV (11, "Strain Type"),
	STOCK (12, "Stock"),
	ALLELE(14, "Allele"),
	GENOTYPE(15, "Genotype"),
	PUBLICATION(16, "Publication"),
	ALL (20, "All");
	
	private int priority;
	private String name;

	private DataFlowStepType(int priority, String name) {
		this.priority = priority;
		this.name = name;
	}

	public String getValue() {
		return this.name;
	}
	
	public int getPriority(){
		return this.priority;
	}
}
