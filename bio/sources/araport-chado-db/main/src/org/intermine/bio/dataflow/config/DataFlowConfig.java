package org.intermine.bio.dataflow.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.intermine.bio.datalineage.CompletionStatus;
import org.intermine.bio.datalineage.DataFlowStep;
import org.intermine.bio.datalineage.DataFlowStepSQL;
import org.intermine.bio.datalineage.DataFlowStepType;
import org.intermine.bio.datalineage.ExecutionStatus;
import org.intermine.bio.datalineage.PriorityComparator;
import org.intermine.bio.datalineage.SourceCVComparator;
import org.intermine.bio.datalineage.StepAction;
import org.intermine.bio.datalineage.StepComparator;

public class DataFlowConfig {

	private static Map<Integer, DataFlowStep> dataFlowCVConfig = new TreeMap<Integer, DataFlowStep>();
	
	private static PriorityQueue<DataFlowStep> dataFlowTaskQueue = 
            new PriorityQueue<DataFlowStep>(20, new PriorityComparator());
	
	private DataFlowConfig() {

	}

	private static class DataFlowCVConfigHolder {

		public static final DataFlowConfig INSTANCE = new DataFlowConfig();
	}

	public static DataFlowConfig getInstance() {

		return DataFlowCVConfigHolder.INSTANCE;
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
			dataFlowCVConfig = new TreeMap<Integer, DataFlowStep>();
		}

		Map<Integer,StepAction> stepAction = new HashMap<Integer, StepAction>();
		stepAction.put(1, StepAction.CREATE_CV);
		
		// Stock Type CV	
		DataFlowStepSQL stepStockTypeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.STOCK_TYPE_CV_SQL);
		
		DataFlowStep stepStockTypeCV = new DataFlowStep(DataFlowStepType.STOCK_TYPE_CV, "Stock Type CV");
		stepStockTypeCV.setStepSQL(stepStockTypeCVSQL);
		stepStockTypeCV.setStepAction(stepAction);
		stepStockTypeCV.setPriority(1);
		
		// Stock Category CV
		DataFlowStepSQL stepStockCategoryCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.STOCK_CATEGORY_CV_SQL);
		
		DataFlowStep stepStockCategoryCV = new DataFlowStep(DataFlowStepType.STOCK_CATEGORY_CV, "Stock Category CV");
		stepStockCategoryCV.setStepSQL(stepStockCategoryCVSQL);
		stepStockCategoryCV.setStepAction(stepAction);
		stepStockCategoryCV.setPriority(2);
		
		//Mutagen CV
		DataFlowStepSQL stepMutagenCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.MUTAGEN_CV_SQL);
		DataFlowStep stepMutagenCV = new DataFlowStep(DataFlowStepType.MUTAGEN_CV, "Mutagen CV");
		stepMutagenCV.setStepSQL(stepMutagenCVSQL);
		stepMutagenCV.setStepAction(stepAction);
		stepMutagenCV.setPriority(3);

		//Inheritance Mode CV
		DataFlowStepSQL stepInheritanceModeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.INHERITANCEMODE_CV_SQL);
		
		DataFlowStep stepInheritanceCV = new DataFlowStep(DataFlowStepType.INHERITANCEMODE_CV, "Inheritance CV");
		stepInheritanceCV.setStepSQL(stepInheritanceModeCVSQL);
		stepInheritanceCV.setStepAction(stepAction);
		stepInheritanceCV.setPriority(4);
		
		// Mutation Site

		DataFlowStepSQL stepMutationSiteCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.MUTATION_SITE_CV_SQL);

		DataFlowStep stepMutationSiteCV = new DataFlowStep(DataFlowStepType.MUTATION_SITE_CV, "Mutation Site CV");
		stepMutationSiteCV.setStepSQL(stepMutationSiteCVSQL);
		stepMutationSiteCV.setStepAction(stepAction);
		stepMutationSiteCV.setPriority(5);
		
		// Zygosity Type
		DataFlowStepSQL stepZygosityTypeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.ZYGOSITY_TYPE_CV_SQL);

		DataFlowStep stepZygosityCV = new DataFlowStep(DataFlowStepType.ZYGOSITY_TYPE_CV, "Zygosity Type CV");
		stepZygosityCV.setStepSQL(stepZygosityTypeCVSQL);
		stepZygosityCV.setStepAction(stepAction);
		stepZygosityCV.setPriority(6);
		
		//Contact Type
		DataFlowStepSQL stepContactTypeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.CONTACT_TYPE_CV_SQL);
		
		DataFlowStep stepContactCV = new DataFlowStep(DataFlowStepType.CONTACT_TYPE_CV, "Contact Type CV");
		stepStockTypeCV.setStepSQL(stepContactTypeCVSQL);
		stepStockTypeCV.setStepAction(stepAction);
		stepStockTypeCV.setPriority(7);
		
		// Attribution Type
		DataFlowStepSQL stepAttributionTypeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.ATTRIBUTIONTYPE_CV_SQL);
		
		DataFlowStep stepAttributionCV = new DataFlowStep(DataFlowStepType.ATTRIBUTIONTYPE_CV, "Attribution Type CV");
		stepAttributionCV.setStepSQL(stepAttributionTypeCVSQL);
		stepAttributionCV.setStepAction(stepAction);
		stepAttributionCV.setPriority(8);

		//Sequence Alteration Type
		DataFlowStepSQL stepSequenceAlterationTypeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.SEQUENCE_ALTERATION_TYPE_CV_SQL);
		
		DataFlowStep stepSequenceAlterationTypeCV = new DataFlowStep(DataFlowStepType.SEQUENCE_ALTERATION_TYPE_CV, "Sequence Alteration Type CV");
		stepSequenceAlterationTypeCV.setStepSQL(stepSequenceAlterationTypeCVSQL);
		stepSequenceAlterationTypeCV.setStepAction(stepAction);
		stepSequenceAlterationTypeCV.setPriority(9);
		

		// Strain Type CV
		DataFlowStepSQL stepStrainTypeCVSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.STRAIN_TYPE_CV_SQL);
		
		DataFlowStep stepStrainTypeCV = new DataFlowStep(DataFlowStepType.STRAIN_TYPE_CV, "Strain Type CV");
		stepSequenceAlterationTypeCV.setStepSQL(stepStrainTypeCVSQL);
		stepSequenceAlterationTypeCV.setStepAction(stepAction);
		stepSequenceAlterationTypeCV.setPriority(10);
		
		
		dataFlowCVConfig.put(stepStockTypeCV.getPriority(), stepStockTypeCV);
		dataFlowCVConfig.put(stepStockCategoryCV.getPriority(), stepStockCategoryCV);
		
		dataFlowCVConfig.put(stepMutagenCV.getPriority(), stepMutagenCV);
		dataFlowCVConfig.put(stepInheritanceCV.getPriority(), stepInheritanceCV);
		
		dataFlowCVConfig.put(stepMutationSiteCV.getPriority(), stepMutationSiteCV);
		
		dataFlowCVConfig.put(stepZygosityCV.getPriority(), stepZygosityCV);
		
		dataFlowCVConfig.put(stepContactCV.getPriority(), stepContactCV);
		
		dataFlowCVConfig.put(stepAttributionCV.getPriority(), stepAttributionCV);
		
		dataFlowCVConfig.put(stepSequenceAlterationTypeCV.getPriority(), stepSequenceAlterationTypeCV);
		
		dataFlowCVConfig.put(stepStrainTypeCV.getPriority(), stepStrainTypeCV);
		
		dataFlowCVConfig.put(stepStockCategoryCV.getPriority(), stepStockCategoryCV);
		
		dataFlowCVConfig.put(stepStockCategoryCV.getPriority(), stepStockCategoryCV);
	

	}

	public static void initDataFlowConfig() {
		if (dataFlowTaskQueue == null) {
			dataFlowTaskQueue =  new PriorityQueue<DataFlowStep>(20, new PriorityComparator());
			
		}

		
		for (Entry<Integer, DataFlowStep> entry : dataFlowCVConfig.entrySet()) {

			DataFlowStep step = entry.getValue();
			dataFlowTaskQueue.add(step);

		}
		
		// Strain 
		DataFlowStepSQL stepStrainSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.STRAIN_TYPE_CV_SQL);
				
		DataFlowStep stepStrain = new DataFlowStep(DataFlowStepType.STRAIN, "Strain");
				
		Map<Integer,StepAction> strainStepAction = new HashMap<Integer, StepAction>();
		strainStepAction.put(1, StepAction.CREATE_STRAIN);
				
		stepStrain.setStepSQL(stepStrainSQL);
		stepStrain.setStepAction(strainStepAction);
		stepStrain.setPriority(11);
				
		dataFlowTaskQueue.add(stepStrain);
				
		
		// Stock 
		DataFlowStepSQL stepStockSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.STOCK_SQL);
		
		DataFlowStep stepStock = new DataFlowStep(DataFlowStepType.STOCK, "Stock");
		
		Map<Integer,StepAction> stockStepAction = new HashMap<Integer, StepAction>();
		stockStepAction.put(1, StepAction.CREATE_STOCK);
		
		stepStock.setStepSQL(stepStockSQL);
		stepStock.setStepAction(stockStepAction);
		stepStock.setPriority(12);
		
		dataFlowTaskQueue.add(stepStock);
		
		// Allele
		DataFlowStepSQL stepAlleleSQL = new DataFlowStepSQL(SourceDataFlowTaskContainer.ALLELE_SQL);
		
		DataFlowStep stepAllele = new DataFlowStep(DataFlowStepType.ALLELE, "Allele");
		
		Map<Integer,StepAction> alleleStepAction = new HashMap<Integer, StepAction>();
		alleleStepAction.put(1, StepAction.CREATE_ALLELE);
		
		stepAllele.setStepSQL(stepAlleleSQL);
		stepAllele.setStepAction(alleleStepAction);
		stepAllele.setPriority(14);
		
		dataFlowTaskQueue.add(stepAllele);
		
		// Genotype 
		DataFlowStepSQL stepGenotypeSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.GENOTYPE_SQL);
		
		DataFlowStep stepGenotype = new DataFlowStep(DataFlowStepType.GENOTYPE, "Genotype");
		
		Map<Integer,StepAction> genoTypeStepAction = new HashMap<Integer, StepAction>();
		genoTypeStepAction.put(1, StepAction.CREATE_GENOTYPE);
		
		stepGenotype.setStepSQL(stepGenotypeSQL);
		stepGenotype.setStepAction(genoTypeStepAction);
		stepGenotype.setPriority(15);
		
		dataFlowTaskQueue.add(stepGenotype);
		
        // Stock Center
		DataFlowStepSQL stepStockCenterSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.STOCK_CENTER_SQL);

		Map<Integer,StepAction> stockCenterAction = new HashMap<Integer, StepAction>();
		DataFlowStep stepStockCenter = new DataFlowStep(DataFlowStepType.STOCK_CENTER, "Stock Center");
		stockCenterAction.put(1, StepAction.CREATE_STOCK_CENTER);
		
		stepStockCenter.setStepSQL(stepStockCenterSQL);
		stepStockCenter.setStepAction(stockCenterAction);
		stepStockCenter.setPriority(16);
		
		dataFlowTaskQueue.add(stepStockCenter);
		
		// Publication
		
		DataFlowStepSQL stepPublicationSQL = new DataFlowStepSQL(
				SourceDataFlowTaskContainer.PUBLICATION_SQL);

		Map<Integer,StepAction> publicationAction = new HashMap<Integer, StepAction>();
		DataFlowStep stepPublication = new DataFlowStep(DataFlowStepType.PUBLICATION, "Publication");
		publicationAction.put(1, StepAction.CREATE_PUBLICATION);
		
		stepPublication.setStepSQL(stepPublicationSQL);
		stepPublication.setStepAction(publicationAction);
		stepPublication.setPriority(17);
		
		dataFlowTaskQueue.add(stepPublication);
		
		}

	
	public Map<Integer, DataFlowStep> getDataFlowCVConfig(){
		return dataFlowCVConfig;
	}
	
	public PriorityQueue<DataFlowStep> getDataFlowTasks(){
		return dataFlowTaskQueue;
	}
}
