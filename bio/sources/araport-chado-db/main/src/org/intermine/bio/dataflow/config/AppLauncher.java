package org.intermine.bio.dataflow.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.ExecutionContext;
import org.intermine.bio.dataloader.job.FlowStep;
import org.intermine.bio.dataloader.job.FlowStepBuilder;
import org.intermine.bio.dataloader.job.JobExecution;
import org.intermine.bio.dataloader.job.JobInstance;
import org.intermine.bio.dataloader.job.JobParameters;
import org.intermine.bio.dataloader.job.SimpleJob;
import org.intermine.bio.dataloader.job.SimpleJobLauncher;
import org.intermine.bio.dataloader.job.SyncTaskExecutor;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.dataloader.job.TaskletStep;
import org.intermine.bio.dataloader.util.IdGenerator;
import org.intermine.bio.domain.source.SourceCV;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.item.postprocessor.CVTermPostprocessor;
import org.intermine.bio.item.processor.CVItemProcessor;
import org.intermine.bio.item.processor.CVTermProcessor;
import org.intermine.bio.item.processor.StockItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.reader.CVReader;
import org.intermine.bio.reader.CVTermReader;
import org.intermine.bio.reader.StockReader;
import org.intermine.bio.dataloader.job.Step;
import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.xml.full.Item;

public class AppLauncher {


	protected static final Logger log = Logger.getLogger(AppLauncher.class);
	
	private static ChadoDBConverter service;
	
	private final static String JOB_NAME = "Stock Loading Job";
	private static SimpleJob job = new SimpleJob(JOB_NAME);
	
	private final static SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
	
	private final static TaskExecutor taskExecutor = new SyncTaskExecutor();
	
	private static List<Step> steps = new ArrayList<Step>();

	private static class AppLauncherHolder {
		
	public static final AppLauncher INSTANCE = new AppLauncher();		
		
	}
	
	public static AppLauncher getInstance(ChadoDBConverter chadoDBConverter) {
		service = chadoDBConverter;
		return AppLauncherHolder.INSTANCE;
	}
	

	public static void initialize(ChadoDBConverter chadoDBConverter) {

		log.info("Initializing Launcher has started...");
		
		service = chadoDBConverter;
		createSteps();
		setJob();
		
		DataFlowConfig.initialize();
		
		log.info("Initialization has completed.");
		
	}

	private static void createSteps() {

		// Stock Step Config
		StockItemProcessor processor1 = new StockItemProcessor(service);
		DatabaseItemReader<SourceStock> reader1 = new StockReader().getStockReader(service.getConnection());
		String stepName1 = "Stock Loading Step";
		FlowStep<SourceStock, Item> stockStep = new FlowStepBuilder<SourceStock, Item>().build(stepName1, reader1, processor1,
				taskExecutor);
		
		// CV Step Config
		CVItemProcessor processor2 = new CVItemProcessor(service);
		DatabaseItemReader<SourceCV> reader2 = new CVReader().getReader(service.getConnection());
		String stepName2 = "CV Loading Step";
		FlowStep<SourceCV, Item> cvStep = new FlowStepBuilder<SourceCV, Item>().build(stepName2, reader2, processor2,
				taskExecutor);
		
		// CVTerm Config
		
		CVTermProcessor processor3 = new CVTermProcessor(service);
		DatabaseItemReader<SourceCVTerm> reader3 = new CVTermReader().getReader(service.getConnection());
		String stepName3 = "CVTerm Loading Step";
		Step cvTermPostprocessor = new CVTermPostprocessor(service).getPostProcessor("CVTerm PostProcessor", service, taskExecutor);
		
		FlowStep<SourceCVTerm, Item> cvTermStep = new FlowStepBuilder<SourceCVTerm, Item>().build(stepName3, reader3, processor3,
				taskExecutor);
		
		cvTermStep.setStepPostProcessor(cvTermPostprocessor);
		
		// CV - CVTerm Collection   
		
		steps.add(stockStep);
		steps.add(cvStep);
		steps.add(cvTermStep);
	}

	private static SimpleJob setJob() {

		job.setSteps(steps);

		return job;
	}

	private static JobExecution createJobExecution(JobInstance jobInstance, JobParameters jobParameters) {

		JobExecution jobExecution = new JobExecution(jobInstance, jobParameters);
		ExecutionContext executionContext = new ExecutionContext();
		jobExecution.setExecutionContext(executionContext);
		jobExecution.setLastUpdated(new Date(System.currentTimeMillis()));

		return jobExecution;
	}

	private static JobInstance createJobInstance(String jobName, JobParameters jobParameters) {

		Long jobId = IdGenerator.randLong();

		JobInstance jobInstance = new JobInstance(jobId, jobName);
		jobInstance.incrementVersion();

		return jobInstance;
	}

	private static JobParameters createJobParameters() {
		return new JobParameters();
	}
	
	public static void run(){
		
		JobParameters jobParameters = createJobParameters();
		
		log.info("Launching Loading Job...");
		
		jobLauncher.run(job, jobParameters);
		
		log.info("Loading Job has been completed.");
		
		Map<String, ItemHolder> items = CVService.getCVItemMap();
		
		for (Map.Entry<String, ItemHolder> item : items.entrySet()){
			
			String cv = item.getKey();
			String cvItemId = item.getValue().getItem().getIdentifier();
			
			Item cvItem = item.getValue().getItem();
			log.info("CV Key:" + cv + "; cvItemId:" + cvItemId + ";" + "cvItem = " + cvItem + ";" + cvItem.getCollection("terms"));
			
		}
		
		log.info("CV Map Item Size =" + items.size());
		
	}
}
