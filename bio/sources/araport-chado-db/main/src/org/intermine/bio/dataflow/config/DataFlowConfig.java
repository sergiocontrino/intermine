package org.intermine.bio.dataflow.config;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.intermine.bio.datalineage.DataFlowStep;
import org.intermine.bio.datalineage.DataFlowStepType;
import org.intermine.bio.datalineage.SourceCVComparator;
import org.intermine.bio.datalineage.StepComparator;

public class DataFlowConfig {

	private static Map<SourceCV, DataFlowStepSQL> dataFlowCVConfig = new TreeMap<SourceCV, DataFlowStepSQL>(
			new SourceCVComparator());
	private static Map<DataFlowStepType, DataFlowStepSQL> dataFlowConfig = new TreeMap<DataFlowStepType, DataFlowStepSQL>(
			new StepComparator());

	private DataFlowConfig() {

	}

	private static class SourceDataFlowCVConfigHolder {

		public static final DataFlowConfig INSTANCE = new DataFlowConfig();
	}

	public static DataFlowConfig getInstance() {

		return SourceDataFlowCVConfigHolder.INSTANCE;
	}

	static {
		init();
	}

	public static void init() {

		initCVConfig();
		initDataFlowConfig();

	}

	public static void initCVConfig() {
		if (dataFlowCVConfig == null) {
			dataFlowCVConfig = new TreeMap<SourceCV, DataFlowStepSQL>();
		}
		
		DataFlowStepSQL stepStockTypeCV = new DataFlowStepSQL(DataFlowStepType.STOCK_TYPE_CV, 
				SourceDataFlowTaskContainer.STOCK_TYPE_CV_SQL);
		
		DataFlowStepSQL stepStockCategoryCV = new DataFlowStepSQL(DataFlowStepType.STOCK_CATEGORY_CV, 
				SourceDataFlowTaskContainer.STOCK_CATEGORY_CV_SQL);
		
		DataFlowStepSQL stepMutagenCV = new DataFlowStepSQL(DataFlowStepType.MUTAGEN_CV, 
				SourceDataFlowTaskContainer.MUTAGEN_CV_SQL);
			
		DataFlowStepSQL stepInheritanceModeCV = new DataFlowStepSQL(DataFlowStepType.INHERITANCEMODE_CV, 
				SourceDataFlowTaskContainer.INHERITANCEMODE_CV_SQL);
		
		DataFlowStepSQL stepMutationSiteCV = new DataFlowStepSQL(DataFlowStepType.MUTATION_SITE_CV, 
				SourceDataFlowTaskContainer.MUTATION_SITE_CV_SQL);
		
		DataFlowStepSQL stepZygosityTypeCV = new DataFlowStepSQL(DataFlowStepType.ZYGOSITY_TYPE_CV, 
				SourceDataFlowTaskContainer.ZYGOSITY_TYPE_CV_SQL);
		
		DataFlowStepSQL stepContactTypeCV = new DataFlowStepSQL(DataFlowStepType.CONTACT_TYPE_CV, 
				SourceDataFlowTaskContainer.CONTACT_TYPE_CV_SQL);
		
		DataFlowStepSQL stepAttributionTypeCV = new DataFlowStepSQL(DataFlowStepType.ATTRIBUTIONTYPE_CV, 
				SourceDataFlowTaskContainer.ATTRIBUTIONTYPE_CV_SQL);
		
		DataFlowStepSQL stepSequenceAlterationTypeCV = new DataFlowStepSQL(DataFlowStepType.SEQUENCE_ALTERATION_TYPE_CV, 
				SourceDataFlowTaskContainer.SEQUENCE_ALTERATION_TYPE_CV_SQL);
		
		DataFlowStepSQL stepStrainTypeCV = new DataFlowStepSQL(DataFlowStepType.STRAIN_TYPE_CV, 
				SourceDataFlowTaskContainer.STRAIN_TYPE_CV_SQL);
		
		dataFlowCVConfig.put(SourceCV.STOCK_TYPE_CV, stepStockTypeCV);
		dataFlowCVConfig.put(SourceCV.STOCK_CATEGORY_CV, stepStockCategoryCV);
		dataFlowCVConfig.put(SourceCV.MUTAGEN_CV, stepMutagenCV);
		
		dataFlowCVConfig.put(SourceCV.ALLELE_CLASS_CV, stepMutagenCV);
		
		dataFlowCVConfig.put(SourceCV.INHERITANCEMODE_CV, stepInheritanceModeCV);
		dataFlowCVConfig.put(SourceCV.MUTATION_SITE_CV, stepMutationSiteCV);
		dataFlowCVConfig.put(SourceCV.ZYGOSITY_TYPE_CV, stepZygosityTypeCV);
		dataFlowCVConfig.put(SourceCV.CONTACT_TYPE_CV, stepContactTypeCV);
		dataFlowCVConfig.put(SourceCV.ATTRIBUTIONTYPE_CV, stepAttributionTypeCV);
		dataFlowCVConfig.put(SourceCV.SEQUENCE_ALTERATION_TYPE_CV, stepSequenceAlterationTypeCV);
		dataFlowCVConfig.put(SourceCV.STRAIN_TYPE_CV, stepStrainTypeCV);
	
		
	}

	public static void initDataFlowConfig() {
		if (dataFlowConfig == null) {
			dataFlowConfig = new TreeMap<DataFlowStepType, DataFlowStepSQL>();
		}
		
		
		for (Entry<SourceCV, DataFlowStepSQL> entry : dataFlowCVConfig.entrySet()) {
	        
			DataFlowStepSQL step = entry.getValue();
				dataFlowConfig.put(step.getStepType(), step);
					    
		   }
		
		
		DataFlowStepSQL stepAllele = new DataFlowStepSQL(DataFlowStepType.ALLELE, 
				SourceDataFlowTaskContainer.ALLELE_SQL);
		
		DataFlowStepSQL stepGenotype = new DataFlowStepSQL(DataFlowStepType.GENOTYPE, 
				SourceDataFlowTaskContainer.GENOTYPE_SQL);
		
		DataFlowStepSQL stepStockCenter = new DataFlowStepSQL(DataFlowStepType.STOCK_CENTER, 
				SourceDataFlowTaskContainer.STOCK_CENTER_SQL);
			
		DataFlowStepSQL stepStock = new DataFlowStepSQL(DataFlowStepType.STOCK, 
				SourceDataFlowTaskContainer.STOCK_SQL);
		
		DataFlowStepSQL stepPublication = new DataFlowStepSQL(DataFlowStepType.PUBLICATION, 
				SourceDataFlowTaskContainer.PUBLICATION_SQL);
		
		
		dataFlowConfig.put(DataFlowStepType.ALLELE, stepAllele);
		dataFlowConfig.put(DataFlowStepType.GENOTYPE, stepGenotype);
		dataFlowConfig.put(DataFlowStepType.STOCK_CENTER, stepStockCenter);
		dataFlowConfig.put(DataFlowStepType.STOCK, stepStock);
		dataFlowConfig.put(DataFlowStepType.PUBLICATION, stepPublication);
		
						
	}
}
