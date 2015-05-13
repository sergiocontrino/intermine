package org.intermine.bio.dataflow.config;

import org.intermine.bio.utils.sql.FileUtils;

public interface SourceDataFlowTaskContainer {

	static final String MUTAGEN_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String MUTAGEN_CV_SQL = FileUtils
			.getSqlFileContents(MUTAGEN_CV_SQL_PATH);
		
	static final String STOCK_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String STOCK_TYPE_CV_SQL = FileUtils
			.getSqlFileContents(STOCK_TYPE_CV_SQL_PATH);
		
	static final String STOCK_CATEGORY_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String STOCK_CATEGORY_CV_SQL = FileUtils
			.getSqlFileContents(STOCK_CATEGORY_CV_SQL_PATH);
	
	static final String STRAIN_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String STRAIN_TYPE_CV_SQL = FileUtils
			.getSqlFileContents(STRAIN_TYPE_CV_SQL_PATH);
		
	static final String STOCK_DS_SQL_PATH = "/sql/stock_dataset.sql";
	static final String STOCK_DS_SQL = FileUtils
			.getSqlFileContents(STOCK_DS_SQL_PATH);
	
	static final String ALLELE_CLASS_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String ALLELE_CLASS_CV_SQL = FileUtils
			.getSqlFileContents(ALLELE_CLASS_CV_SQL_PATH);
	
	static final String SEQUENCE_ALTERATION_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String SEQUENCE_ALTERATION_TYPE_CV_SQL = FileUtils
			.getSqlFileContents(ALLELE_CLASS_CV_SQL_PATH);

	
	static final String INHERITANCEMODE_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String INHERITANCEMODE_CV_SQL = FileUtils
			.getSqlFileContents(INHERITANCEMODE_CV_SQL_PATH);

	static final String MUTATION_SITE_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String MUTATION_SITE_CV_SQL = FileUtils
			.getSqlFileContents(MUTATION_SITE_CV_SQL_PATH);
	
	static final String ZYGOSITY_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String ZYGOSITY_TYPE_CV_SQL = FileUtils
			.getSqlFileContents(ZYGOSITY_TYPE_CV_SQL_PATH);
	
	static final String CONTACT_TYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String CONTACT_TYPE_CV_SQL = FileUtils
			.getSqlFileContents(ZYGOSITY_TYPE_CV_SQL_PATH);
	
	static final String ATTRIBUTIONTYPE_CV_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String ATTRIBUTIONTYPE_CV_SQL = FileUtils
			.getSqlFileContents(ZYGOSITY_TYPE_CV_SQL_PATH);
	
	static final String ALLELE_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String ALLELE_SQL = FileUtils
			.getSqlFileContents(ALLELE_SQL_PATH);
	
	static final String GENOTYPE_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String GENOTYPE_SQL = FileUtils
			.getSqlFileContents(GENOTYPE_SQL_PATH);
			
	static final String PUBLICATION_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String PUBLICATION_SQL = FileUtils
			.getSqlFileContents(PUBLICATION_SQL_PATH);
	
	static final String STOCK_CENTER_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String STOCK_CENTER_SQL = FileUtils
			.getSqlFileContents(STOCK_CENTER_SQL_PATH);
	
	static final String STOCK_SQL_PATH = "/sql/stock_dataset.sql";
	
	static final String STOCK_SQL = FileUtils
			.getSqlFileContents(STOCK_SQL_PATH);
	
}
