package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.dataloader.job.TaskletStep;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.store.service.StoreService;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class StockItemPostprocessor extends AbstractStep {

	protected static final Logger log = Logger.getLogger(StockItemPostprocessor.class);

	private static ChadoDBConverter service;

	protected TaskExecutor taskExecutor;

	public StockItemPostprocessor(ChadoDBConverter chadoDBConverter) {
		super();
		service = chadoDBConverter;

	}

	public StockItemPostprocessor getPostProcessor(String name, ChadoDBConverter chadoDBConverter,
			TaskExecutor taskExecutor

	) {

		StockItemPostprocessor processor = new StockItemPostprocessor(chadoDBConverter);
		processor.setName(name);

		processor.setTaskExecutor(taskExecutor);

		return processor;

	}

	@Override
	protected void doExecute(StepExecution stepExecution) throws Exception {

		log.info("Running Task Let Step!  CVTermPostprocessor " + getName());

		taskExecutor.execute(new Runnable() {

			public void run() {
				createStockStrainCollection();
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

	private void createStockStrainCollection() {
		Map<String, Item> items = OrganismService.getStrainItemSet();

		log.debug("Total Count of Entities to Process:" + items.size());

		for (Map.Entry<String, Item> item : items.entrySet()) {

			String strain = item.getKey();

			log.info("Processing Strain: " + strain);

			Collection<Item> collection = (Collection<Item>) item.getValue();

			List terms = new ArrayList(collection);

			Item strainItem = OrganismService.getStrainMap().get(strain).getItem();
			ItemHolder itemHolder = OrganismService.getStrainMap().get(strain);

			log.debug("Total Count of Entities to Process:" + collection.size());

			Exception exception = null;

			ReferenceList referenceList = new ReferenceList();
			referenceList.setName("stocks");

			try {

				StoreService.storeCollection(collection, itemHolder, referenceList.getName());

			} catch (ObjectStoreException e) {
				exception = e;
			} catch (Exception e) {
				exception = e;

			} finally {
				if (exception != null) {
					log.error("Error storing stock/strain collection for strain:" + strain + "; Error:"
							+ exception.getMessage());
				} else {
					log.debug("Stock/Strain Collection successfully stored." + itemHolder.getItem() + ";"
							+ "Collection size:" + collection.size());
				}
			}

		}

		log.debug("Strain Map Item Size =" + items.size());

		log.debug("Tasklet Task has Completed! " + getName());

	}

}
