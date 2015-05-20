package org.intermine.bio.dataloader.job;

import java.util.ArrayList;
import java.util.List;

import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.domain.source.SourceStock;
import org.intermine.bio.reader.StockReader;
import org.intermine.item.domain.database.DatabaseItemReader;

public class Test extends DataSourceProcessor {

	public Test(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();

		SimpleJob job = new SimpleJob("job");
		Step step1 = new Support().createTestStep();
		Step step2 = new Support().createTestStep2();

		List<Step> steps = new ArrayList<Step>();

		steps.add(step1);
		steps.add(step2);

		job.setSteps(steps);
		// job.addStep(step1);
		// job.addStep(step2);

		System.out.println(job.getStepNames());

		JobParameters jobParameters = new Support().createJobParameters();
		JobInstance jobInstance = new Support().createJobInstance(job.getName(), jobParameters);
		JobExecution jobExecution = new Support().createJobExecution(jobInstance, jobParameters);

		// job.execute(jobExecution);

		jobLauncher.run(job, jobParameters);

		System.out.println("Job Test 1" + job);

		TaskExecutor taskExecutor = new SyncTaskExecutor();
		String stepName = "Test Step";

		// DatabaseItemReader<SourceStock> reader = new
		// StockReader().getStockReader(getConverter().getDatabase().getConnection());

		//FlowStepBuilder<Stock, Item> builder = new FlowStepBuilder<Stock, Item>().build(stepName, reader, processor,
			//	taskExecutor);

	}

}
