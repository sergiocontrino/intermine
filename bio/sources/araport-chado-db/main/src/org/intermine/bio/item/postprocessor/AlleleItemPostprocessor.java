package org.intermine.bio.item.postprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.data.service.AlleleFindService;
import org.intermine.bio.data.service.FindService;
import org.intermine.bio.data.service.GeneFindService;
import org.intermine.bio.data.service.ServiceLocator;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.dataloader.job.TaskletStep;
import org.intermine.bio.domain.source.SourceBackgroundStrain;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.bio.domain.source.SourceFeatureRelationship;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.postprocess.PostProcessUtil;
import org.intermine.bio.reader.BackgroundAccessionReader;
import org.intermine.bio.reader.GeneAlleleCollectionReader;
import org.intermine.bio.store.service.StoreService;
import org.intermine.dataconversion.ItemToObjectTranslator;
import org.intermine.item.domain.database.DatabaseItemReader;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.fastcollections.ObjectStoreFastCollectionsImpl;
import org.intermine.objectstore.translating.ObjectStoreTranslatingImpl;
import org.intermine.objectstore.translating.Translator;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;
import org.intermine.metadata.Model;
import org.intermine.model.InterMineObject;
import org.intermine.model.bio.*;

public class AlleleItemPostprocessor extends AbstractStep {

	protected static final Logger log = Logger.getLogger(AlleleItemPostprocessor.class);

	private static ChadoDBConverter service;

	protected TaskExecutor taskExecutor;

	MultiMap alleleGeneItemSet = new MultiValueMap();

	GeneFindService geneService = (GeneFindService) ServiceLocator.getService(ApplicationContext.GENE_SERVICE);
	AlleleFindService alleleService = (AlleleFindService) ServiceLocator.getService(ApplicationContext.ALLELE_SERVICE);

	public AlleleItemPostprocessor(ChadoDBConverter chadoDBConverter) {
		super();
		service = chadoDBConverter;
		alleleGeneItemSet.clear();
	}

	public AlleleItemPostprocessor getPostProcessor(String name, ChadoDBConverter chadoDBConverter,
			TaskExecutor taskExecutor

	) {

		AlleleItemPostprocessor processor = new AlleleItemPostprocessor(chadoDBConverter);
		processor.setName(name);

		processor.setTaskExecutor(taskExecutor);

		alleleGeneItemSet.clear();

		return processor;

	}

	@Override
	protected void doExecute(StepExecution stepExecution) throws Exception {

		log.info("Running Task Let Step!  AleleItemPostprocessor " + getName());

		taskExecutor.execute(new Runnable() {

			public void run() {
				createAlleleGenotypeCollection();
				//createAlleleGeneCollection();
				processAlleleGeneCollection();
				saveAlleleGeneCollection();
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

	private void createAlleleGenotypeCollection() {

		Map<String, Item> items = AlleleService.getAlleleItemSet();

		for (Map.Entry<String, Item> item : items.entrySet()) {

			String allele = item.getKey();

			log.info("Processing Allele: " + allele);

			Collection<Item> collection = (Collection<Item>) item.getValue();

			List terms = new ArrayList(collection);

			ItemHolder itemHolder = AlleleService.getAlleleMap().get(allele);

			ReferenceList referenceList = new ReferenceList();
			referenceList.setName("genotypes");
			try {

				StoreService.storeCollection(collection, itemHolder, referenceList.getName());

				log.info("Genotype Collection successfully stored." + itemHolder.getItem() + ";" + "Collection size:"
						+ collection.size());

			} catch (ObjectStoreException e) {
				log.error("Error storing genotypes collection for allele:" + allele);
			}

		}

	}

	private void createAlleleGeneCollection() {

		log.info("Gene/Allele Collection processing has started...");

		DatabaseItemReader<SourceFeatureRelationship> reader = new GeneAlleleCollectionReader().getReader(service
				.getConnection());

		Exception exception = null;

		SourceFeatureRelationship lastItem = null;
		SourceFeatureRelationship currentItem = null;
		boolean save = true;

		try {

			log.info("Opening Reader: Gene/Allele Collection ... ");

			reader.open();

			log.info("Reader: Gene/Allele Collection successfully opened. ");

			while (reader.hasNext()) {

				currentItem = reader.read();
				log.info("SQL" + reader.getSql());
				log.info("Current Item = " + currentItem);
				log.info("Parameter values:" + reader.getParameterMap());

				ItemHolder alleleItem = AlleleService.getAlleleItem(currentItem.getObjectUniqueAccession());

				addGeneItem(currentItem.getSubjectUniqueAccession(), alleleItem);

				if (lastItem != null
						&& !currentItem.getSubjectUniqueAccession().equals(lastItem.getSubjectUniqueAccession())) {

					log.info("Storing allele/gene collection: " + lastItem);

					log.info("Current Gene/Allele Collection Size:" + alleleGeneItemSet.size());

					saveGenesCollection(lastItem);
				}

				lastItem = currentItem;
			}

			if (lastItem != null) {

				log.info("Storing last item allele/gene collection: " + lastItem);

				saveGenesCollection(lastItem);
			}

		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error occured during Gene/Allele Collection processing");
			} else {
				log.info("Creation of Gene/Allele Collection has successfully completed.");
			}
		}

	}

	private void addGeneItem(String geneName, ItemHolder item) {
		alleleGeneItemSet.put(geneName, item.getItem());
	}

	private void saveGenesCollection(SourceFeatureRelationship item) {

		String itemName = item.getSubjectUniqueAccession();

		log.info("Gene Item Name:" + itemName);

		Map<String, SourceFeatureRelationship> items = alleleGeneItemSet;

		Collection<SourceFeatureRelationship> collection = null;

		Collection<Item> collection1 = null;

		log.info("Processing Allele/Gene Collection: Gene:" + itemName + ";" + item.getSubjectUniqueName());

		if (alleleGeneItemSet.containsKey(itemName)) {

			collection = (Collection<SourceFeatureRelationship>) alleleGeneItemSet.get(itemName);
			collection1 = (Collection<Item>) alleleGeneItemSet.get(itemName);

			log.info("Collection Size:" + collection.size());

			log.info("Allele Collection Size:" + collection1.size());
		}

		Set genes = new HashSet();

		if (collection1 != null && collection1.size() > 0) {
			List<SourceFeatureRelationship> terms = new ArrayList<SourceFeatureRelationship>(collection);

			InterMineObject objectGene = null;

			log.info("Affected Gene: " + item.getSubjectUniqueName());

			try {
				log.info("Gene Service using Service Locator:" + "ATMG00030");
				objectGene = geneService.findbyObjectbyId("ATMG00030");

				int objectGeneId = objectGene.getId();
				log.info("Object Gene Id:" + objectGeneId);

				// InterMineObject testGene =
				// StoreService.getObjectStoreWriter().getObjectById(objectGene.getId());

			} catch (Exception e) {
				log.info("Error:" + e.getMessage());
			}

			int itemId = objectGene.getId();

			ReferenceList referenceList = new ReferenceList();
			referenceList.setName("affectedAlleles");

			try {

				StoreService.storeCollection(collection1, itemId, referenceList.getName());

				log.info("Affected Alleles Collection successfully stored." + objectGene + ";" + "Collection size:"
						+ collection1.size());

			} catch (ObjectStoreException e) {
				log.error("Error storing alleles collection for gene:" + objectGene);
			}

		}

	}

	public Item createGene(SourceFeatureRelationship source) throws ObjectStoreException {

		Item item = null;

		if (AlleleService.getGeneItem(source.getSubjectUniqueName()) == null) {

			item = StoreService.getService().createItem("Gene");

			log.info("Item place holder has been created: " + item);

			log.info("Gene Unique Name " + source.getSubjectUniqueName());
			item.setAttribute("primaryIdentifier", source.getSubjectUniqueName());

			int itemId = StoreService.getService().store(item);

			ItemHolder itemHolder = new ItemHolder(item, itemId);

			if (itemHolder != null && itemId != -1) {
				AlleleService.addGeneItem(source.getSubjectUniqueName(), itemHolder);
			}

		} else {
			item = AlleleService.getGeneItem(source.getSubjectUniqueName()).getItem();
		}

		return item;
	}

	private void createGeneAlleleCollection(SourceFeatureRelationship source) {

		Exception exception = null;

		Item item = null;

		try {
			log.info("Creating Item has started. Source Object:" + source);

			Item geneItem = createGene(source);
			ItemHolder alleleItemHolder = AlleleService.getAlleleItem(source.getObjectUniqueAccession());

			if (alleleItemHolder != null && geneItem != null) {

				AlleleService.addAlleleGeneItem(source.getObjectUniqueAccession(), source.getSubjectUniqueName(),
						geneItem);

			}

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error adding allele to the gene/allele item set" + source);
			} else {
				log.info("Gene has been successfully added to the gene/allele item set." + " Allele:"
						+ source.getObjectUniqueAccession() + "/" + source.getObjectUniqueName() + " Gene:"
						+ source.getSubjectUniqueAccession() + "/" + source.getSubjectUniqueName());

			}
		}

	}
	
	private void processAlleleGeneCollection() {

		log.info("Gene/Allele Collection processing has started...");

		DatabaseItemReader<SourceFeatureRelationship> reader = new GeneAlleleCollectionReader().getReader(service
				.getConnection());

		Exception exception = null;

		SourceFeatureRelationship lastItem = null;
		SourceFeatureRelationship currentItem = null;
		boolean save = true;

		try {

			log.info("Opening Reader: Gene/Allele Collection ... ");

			reader.open();

			log.info("Reader: Gene/Allele Collection successfully opened. ");

			while (reader.hasNext()) {

				currentItem = reader.read();
				log.info("SQL" + reader.getSql());
				log.info("Current Item = " + currentItem);
				log.info("Parameter values:" + reader.getParameterMap());

				createGeneAlleleCollection(currentItem);
				
			}

		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error occured during Gene/Allele Collection processing");
			} else {
				log.info("Creation of Gene/Allele Collection has successfully completed.");
			}
		}

	}

	private void saveAlleleGeneCollection() {

		Map<String, Item> items = AlleleService.getAlleleGeneItemSet();

		for (Map.Entry<String, Item> item : items.entrySet()) {

			String allele = item.getKey();

			log.info("Processing Allele: " + allele);

			Collection<Item> collection = (Collection<Item>) item.getValue();

			List terms = new ArrayList(collection);

			ItemHolder itemHolder = AlleleService.getAlleleMap().get(allele);

			ReferenceList referenceList = new ReferenceList();
			referenceList.setName("affectedGenes");
			try {

				StoreService.storeCollection(collection, itemHolder, referenceList.getName());

				log.info("Gene Collection successfully stored." + itemHolder.getItem() + ";" + "Collection size:"
						+ collection.size());

			} catch (ObjectStoreException e) {
				log.error("Error storing affected genes collection for allele:" + allele);
			}

		}

	}
}
