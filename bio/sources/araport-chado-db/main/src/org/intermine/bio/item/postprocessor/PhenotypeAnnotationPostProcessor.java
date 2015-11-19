package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.PublicationService;
import org.intermine.bio.chado.StockService;
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

public class PhenotypeAnnotationPostProcessor extends AbstractStep {

	protected static final Logger log = Logger.getLogger(PhenotypeAnnotationPostProcessor.class);

	private static ChadoDBConverter service;

	protected TaskExecutor taskExecutor;

	public PhenotypeAnnotationPostProcessor(ChadoDBConverter chadoDBConverter) {
		super();
		service = chadoDBConverter;

	}

	public PhenotypeAnnotationPostProcessor getPostProcessor(String name, ChadoDBConverter chadoDBConverter,
			TaskExecutor taskExecutor

	) {

		PhenotypeAnnotationPostProcessor processor = new PhenotypeAnnotationPostProcessor(chadoDBConverter);
		processor.setName(name);

		processor.setTaskExecutor(taskExecutor);

		return processor;

	}

	@Override
	protected void doExecute(StepExecution stepExecution) throws Exception {

		log.info("Running Task Let Step!  PhenotypeAnnotationPostprocessor " + getName());

		taskExecutor.execute(new Runnable() {

			public void run() {
				
				createStockPhenotypeAnnotationCollection();
				createGenotypePhenotypeAnnotationCollection();
				createStockPhenotypeAnnotationCollection();
				
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

	private void createPhenotypePhenotypeAnnotationCollection() {

		Map<String, Item> items = PhenotypeService.getPhenotypeAnnotationItemSet();

		log.debug("Total Count of Phenotype/Phenotype Collections to Process:" + items.size());

		for (Map.Entry<String, Item> item : items.entrySet()) {

			String phenotype = item.getKey();

			log.info("Processing Phenotype: " + phenotype);

			Exception exception = null;

			ItemHolder itemHolder = null;
			Item phenotypeItem = null;

			int collectionSize = 0;

			try {

				if (phenotype == null) {
					Exception e = new Exception("Phenotype Cannot Null! Skipping Record Processing! " + phenotype);
					throw e;
				}

				Collection<Item> collection = (Collection<Item>) item.getValue();

				if (collection == null) {
					Exception e = new Exception("Collection Cannot Null! Skipping Record  !" + item);
					throw e;
				}

				List<Item> collectionItems = new ArrayList<Item>(collection);
				collectionSize = collection.size();

				itemHolder = PhenotypeService.getPhenotypeItem(phenotype);

				if (itemHolder == null) {
					Exception e = new Exception("Phenotype Item Holder Cannot Be Null! Skipping Record Processing!"
							+ itemHolder);
					throw e;
				}

				phenotypeItem = itemHolder.getItem();

				if (phenotypeItem == null) {
					Exception e = new Exception("Phenotype Item Cannot Be Null! Skipping Record Processing!"
							+ phenotypeItem);
					throw e;
				}

				log.info("Collection Holder: " + phenotype);

				log.info("Total Count of Entities to Process:" + collectionItems.size());

				ReferenceList referenceList = new ReferenceList();
				referenceList.setName("phenotypeAnnotations");

				StoreService.storeCollection(collection, itemHolder, referenceList.getName());

			} catch (Exception e) {
				exception = e;
			} finally {
				if (exception != null) {
					log.error("Error storing Phenotype/Phenotype Annotation Collection for a phenotype:" + phenotype
							+ "; Error:" + exception.getMessage());
				} else {
					log.info("Phenotype/Phenotype Annotation Collection successfully stored." + phenotypeItem + ";"
							+ "Collection size:" + collectionSize);
				}
			}

		}

	}

	private void createGenotypePhenotypeAnnotationCollection() {

		Map<String, Item> items = GenotypeService.getPhenotypeAnnotationItemSet();

		log.debug("Total Count of Genotype/Phenotype Collections to Process:" + items.size());

		for (Map.Entry<String, Item> item : items.entrySet()) {

			String genotype = item.getKey();

			log.info("Processing Genotype: " + genotype);

			Exception exception = null;

			ItemHolder itemHolder = null;
			Item genotypeItem = null;

			int collectionSize = 0;

			try {

			if (genotype == null) {
					Exception e = new Exception("Genotype Cannot Null! Skipping Record Processing! " + genotype);
					throw e;
				}

				Collection<Item> collection = (Collection<Item>) item.getValue();

				if (collection == null) {
					Exception e = new Exception("Collection Cannot Null! Skipping Record  !" + item);
					throw e;
				}

				List<Item> collectionItems = new ArrayList<Item>(collection);
				collectionSize = collection.size();

				itemHolder = GenotypeService.getGenotypeItem(genotype);

				if (itemHolder == null) {
					Exception e = new Exception("Genotype Item Holder Cannot Be Null! Skipping Record Processing!"
							+ itemHolder);
					throw e;
				}

				genotypeItem = itemHolder.getItem();

				if (genotypeItem == null) {
					Exception e = new Exception("Genotype Item Cannot Be Null! Skipping Record Processing!"
							+ genotypeItem);
					throw e;
				}

				log.info("Collection Holder: " + genotype);

				log.info("Total Count of Entities to Process:" + collectionItems.size());

				ReferenceList referenceList = new ReferenceList();
				referenceList.setName("genotypephenotypeAnnotations");

				StoreService.storeCollection(collection, itemHolder, referenceList.getName());

			} catch (Exception e) {
				exception = e;
			} finally {
				if (exception != null) {
					log.error("Error storing Genotype/Phenotype Annotation Collection for a genotype:" + genotype
							+ "; Error:" + exception.getMessage());
				} else {
					log.info("Genotype/Phenotype Annotation Collection successfully stored." + genotypeItem + ";"
							+ "Collection size:" + collectionSize);
				}
			}

		}

	}

	private void createStockPhenotypeAnnotationCollection() {

		Map<String, Item> items = StockService.getPhenotypeAnnotationItemSet();

		log.debug("Total Count of Stock/Phenotype Collections to Process:" + items.size());

		for (Map.Entry<String, Item> item : items.entrySet()) {

			String stock = item.getKey();

			log.info("Processing Stock: " + stock);

			Exception exception = null;

			ItemHolder itemHolder = null;
			Item stockItem = null;

			int collectionSize = 0;

			try {

				if (stock == null) {
					Exception e = new Exception("Genotype Cannot Null! Skipping Record Processing! " + stock);
					throw e;
				}

				Collection<Item> collection = (Collection<Item>) item.getValue();

				if (collection == null) {
					Exception e = new Exception("Collection Cannot Null! Skipping Record  !" + item);
					throw e;
				}

				List<Item> collectionItems = new ArrayList<Item>(collection);
				collectionSize = collection.size();

				itemHolder = StockService.getStockItem(stock);

				if (itemHolder == null) {
					Exception e = new Exception("Stock Item Holder Cannot Be Null! Skipping Record Processing!"
							+ itemHolder);
					throw e;
				}

				stockItem = itemHolder.getItem();

				if (stockItem == null) {
					Exception e = new Exception("Stock Item Cannot Be Null! Skipping Record Processing!" + stockItem);
					throw e;
				}

				log.info("Collection Holder: " + stock);

				log.info("Total Count of Entities to Process:" + collectionItems.size());

				ReferenceList referenceList = new ReferenceList();
				referenceList.setName("stockphenotypeAnnotations");

				StoreService.storeCollection(collection, itemHolder, referenceList.getName());

			} catch (Exception e) {
				exception = e;
			} finally {
				if (exception != null) {
					log.error("Error storing Genotype/Phenotype Annotation Collection for a genotype:" + stock
							+ "; Error:" + exception.getMessage());
				} else {
					log.info("Stock/Phenotype Annotation Collection successfully stored." + stockItem + ";"
							+ "Collection size:" + collectionSize);
				}
			}

		}

	}

	
}
