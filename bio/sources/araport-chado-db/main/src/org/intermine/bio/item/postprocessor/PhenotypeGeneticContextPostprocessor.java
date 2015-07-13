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

public class PhenotypeGeneticContextPostprocessor extends AbstractStep {

	protected static final Logger log = Logger.getLogger(PhenotypeGeneticContextPostprocessor.class);

	private static ChadoDBConverter service;

	protected TaskExecutor taskExecutor;

	public PhenotypeGeneticContextPostprocessor(ChadoDBConverter chadoDBConverter) {
		super();
		service = chadoDBConverter;

	}

	public PhenotypeGeneticContextPostprocessor getPostProcessor(String name, ChadoDBConverter chadoDBConverter,
			TaskExecutor taskExecutor

	) {

		PhenotypeGeneticContextPostprocessor processor = new PhenotypeGeneticContextPostprocessor(chadoDBConverter);
		processor.setName(name);

		processor.setTaskExecutor(taskExecutor);

		return processor;

	}

	@Override
	protected void doExecute(StepExecution stepExecution) throws Exception {

		log.info("Running Task Let Step!  PhenotypeGeneticContextPostprocessor " + getName());

		taskExecutor.execute(new Runnable() {

			public void run() {
				createAllelePhenotypeCollection();
				createGenotypePhenotypeCollection();
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

	
	private void createAllelePhenotypeCollection() {

		Map<String, Item> items = PhenotypeService.getPhenotypeAlleleItemSet();

		for (Map.Entry<String, Item> item : items.entrySet()) {

			String phenotype = item.getKey();

			log.info("Processing Phenotype: " + phenotype);

			Collection<Item> collection = (Collection<Item>) item.getValue();

			List<Item> collectionItems = new ArrayList<Item>(collection);

			ItemHolder itemHolder = PhenotypeService.getPhenotypeMap().get(phenotype);

			log.info("Collection Holder: " + phenotype);

			for (Item member : collectionItems) {

				log.info("Member of Collection: " + member + "; " + " Collection Holder:" + phenotype);
			}

			ReferenceList referenceList = new ReferenceList();
			referenceList.setName("observedIn");
			try {

				StoreService.storeCollection(collection, itemHolder, referenceList.getName());

				log.info("Phenotype/Allele Collection successfully stored." + itemHolder.getItem() + ";"
						+ "Collection size:" + collection.size());

			} catch (ObjectStoreException e) {
				log.error("Error storing allele collection for a phenotype:" + phenotype);
			}

		}

	}

	private void createGenotypePhenotypeCollection() {

		Map<String, Item> items = PhenotypeService.getPhenotypeGenotypeItemSet();

		for (Map.Entry<String, Item> item : items.entrySet()) {

			String phenotype = item.getKey();

			log.info("Processing Phenotype: " + phenotype);

			Collection<Item> collection = (Collection<Item>) item.getValue();

			List<Item> collectionItems = new ArrayList<Item>(collection);

			ItemHolder itemHolder = PhenotypeService.getPhenotypeMap().get(phenotype);

			log.info("Collection Holder: " + phenotype);

			for (Item member : collectionItems) {

				log.info("Member of Collection: " + member + "; " + " Collection Holder:" + phenotype);
			}

			ReferenceList referenceList = new ReferenceList();
			referenceList.setName("observedIn");
			try {

				StoreService.storeCollection(collection, itemHolder, referenceList.getName());

				log.info("Phenotype/Genotype Collection successfully stored." + itemHolder.getItem() + ";"
						+ "Collection size:" + collection.size());

			} catch (ObjectStoreException e) {
				log.error("Error storing genotype collection for a phenotype:" + phenotype);
			}

		}

	}
}
