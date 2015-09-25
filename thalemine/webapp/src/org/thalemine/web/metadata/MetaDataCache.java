package org.thalemine.web.metadata;

import java.util.List;

public interface MetaDataCache {

	public static final String GENE= "Gene";
	public static final String PROTEIN = "Proteins";
	public static final String HOMOLOGY = "Homology";
	public static final String GENE_ONTOLOGY = "Gene Ontology";
	public static final String INTERACTIONS = "Gene Ontology";
	public static final String DATABASE = "db.production";
	
	public void initDataCategories();

	
}
