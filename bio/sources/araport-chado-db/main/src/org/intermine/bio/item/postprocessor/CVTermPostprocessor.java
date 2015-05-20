package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.dataloader.job.TaskletStep;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class CVTermPostprocessor extends  AbstractStep  {

	protected static final Logger log = Logger.getLogger(CVTermPostprocessor.class);
	
	private static ChadoDBConverter service;
	
	protected TaskExecutor taskExecutor;
	
	public CVTermPostprocessor(ChadoDBConverter chadoDBConverter) {
		super();
		service = chadoDBConverter;
		
	}

	public CVTermPostprocessor getPostProcessor(String name, ChadoDBConverter chadoDBConverter, TaskExecutor taskExecutor

	) {

		CVTermPostprocessor processor = new CVTermPostprocessor(chadoDBConverter);
		processor.setName(name);

		processor.setTaskExecutor(taskExecutor);

		return processor;

	}

	@Override
	protected void doExecute(StepExecution stepExecution) throws Exception {
		
		log.info("Running Task Let Step!  CVTermPostprocessor " + getName());
		
		
		taskExecutor.execute(new Runnable() {

			public void run() {
				
				Map<String, Item> items = CVService.getCVItemSet();

				for (Map.Entry<String, Item> item : items.entrySet()) {

					String cv = item.getKey();

					Collection collection = (Collection) item.getValue();

					List terms = new ArrayList(collection);

					Item cvItem = CVService.getCVItemMap().get(cv);
					
									
					ReferenceList referenceList = new ReferenceList();
					referenceList.setName("terms");
					
					List<String> termRefs = new ArrayList<String>();
					termRefs.clear();
										
					for (Object term : terms) {
			
						Item cvTermItem = (Item) term;
						termRefs.add(cvTermItem.getIdentifier());

						log.info("CV Key:" + cv + " ; cvItem:" + cvTermItem);

					}
					
					cvItem.setCollection("terms", termRefs);
					
						}

				log.info("CV Map Item Size =" + items.size());

				log.info("Tasklet Task has Completed! " + getName());
			}
		});

  
		
	}

	@Override
	protected void doPostProcess(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	protected TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	
}
