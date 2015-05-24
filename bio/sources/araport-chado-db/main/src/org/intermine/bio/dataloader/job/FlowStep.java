package org.intermine.bio.dataloader.job;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.item.domain.database.ParseException;
import org.intermine.item.domain.database.UnexpectedInputException;

public class FlowStep<I,O> extends AbstractStep{

	protected static final Logger log = Logger.getLogger(FlowStep.class);
    private final static StopWatch timer = new StopWatch();
    
	private DatabaseItemReader<? extends I> reader;
	
	private ItemProcessor<? super I, ? extends O> processor;
	
	private Step postProcessor;
	
	private TaskExecutor taskExecutor;
	
	@Override
	protected void open() throws Exception {
		
		if (reader !=null){
			
			log.info("Opening Cursor in FlowStep:" + this.getName());
			
			reader.open();
			
			log.info("Successfully Opened Cursor in FlowStep:" + this.getName());

		}else{
			log.error("Error Opening Cursor in FlowStep");
		}
	}
	
	@Override
	protected void doExecute(StepExecution stepExecution) throws Exception {
		
		taskExecutor.execute(new Runnable(){
			
			public void run(){
				log.info("Running Flow Step!" + getName());
				
				I item = null;
				
				try {
					
					while (reader.hasNext()) {
						
						item = reader.read();
						log.info("SQL" + reader.getSql());
						log.info("Current Item = " + item);
						log.info("Parameter values:" + reader.getParameterMap());
						
						if (item!=null){
							
							log.info("Item will be created: " + item);
							
							O processedItem = processor.process(item);
							
							log.info("Item has been created: " + processedItem);
							
						}
						
					}
					
				} catch (UnexpectedInputException e1) {
					log.error("Error: " + e1.getCause());
					e1.printStackTrace();
				} catch (ParseException e1) {
					
					log.error("Error:  " + e1.getCause());
					
				
					e1.printStackTrace();
				} catch (Exception e1) {
					
					log.error("Error: " + e1.getCause());
					
					e1.printStackTrace();
				}
				
				log.info("Flow Step has been completed!");
			}
		});
		
		
		
	}
	
	@Override
	protected void doPostProcess(StepExecution stepExecution) throws Exception {
		
		log.info("Evaluating PostProcessing!");
		
		if (postProcessor!=null){
			log.info("Running PostProcessing!");
			postProcessor.execute(stepExecution);
		} else{
			log.info("PostProcessor is Null");
		}
		
		log.info("Evaluating PostProcessing!");
	}
	
	public void setItemReader(DatabaseItemReader<? extends I> reader) {
		this.reader = reader;
	}
	
	public void setItemProcessor(ItemProcessor<? super I, ? extends O> processor) {
		this.processor = processor;
	}
	
	protected DatabaseItemReader<? extends I> getItemReader() {
		return reader;
	}
	
	protected ItemProcessor<? super I, ? extends O> getItemProcessor() {
		return processor;
	}
	
	public void setStepPostProcessor(Step postProcessor) {
		this.postProcessor = postProcessor;
	}
	

	protected Step getStepProcessor() {
		return postProcessor;
	}
	
	
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	protected TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	
}
