package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class PhenotypeGeneticContextItemProcessor extends DataSourceProcessor implements
		ItemProcessor<SourcePhenotypeGeneticContext, Item> {

	protected static final Logger log = Logger.getLogger(PhenotypeGeneticContextItemProcessor.class);

	private String targetClassName;

	private static final String ALLELE_ITEM_CLASSNAME = "Allele";
	private static final String GENOTYPE_ITEM_CLASSNAME = "Genotype";

	public PhenotypeGeneticContextItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourcePhenotypeGeneticContext item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourcePhenotypeGeneticContext source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;

		try {
			log.info("Creating Item has started. Source Object:" + source);

			item = getItembyGeneticFeatureType(source);

			log.info("Phenotype Unique Accession: " + source.getPhenotypeUniqueAccession());

			if (!StringUtils.isBlank(source.getPhenotypeDescription())) {
				log.info("Phenotype Description:" + source.getPhenotypeDescription());
			}

			if (item != null) {
				addToCollection(source, item);
			}else
				new Exception("Phenotype/Genetic Feature Item is Null. Skipping source record." + source);

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error adding to a phenotype/genetic context collection item for source record: " + source
						+ ";Error occured:" + exception.getMessage());
			} else {

				log.info("Source Record has been successfully added to a phenotype/genetic context collection: "
						+ item);

			}
		}
		return item;
	}

	public void setTargetClassName(String name) {
		this.targetClassName = name;
	}

	public String getTargetClassName() {
		return this.targetClassName;
	}

	private void addAllelePhenotypeItem(SourcePhenotypeGeneticContext source, Item item) {

		PhenotypeService.addPhenotypeAlleleItem(source.getPhenotypeUniqueAccession(), item);

	}

	private void addGenotypePhenotypeItem(SourcePhenotypeGeneticContext source, Item item) {

		PhenotypeService.addPhenotypeGenotypeItem(source.getPhenotypeUniqueAccession(), item);
	}

	private Item getItembyGeneticFeatureType(SourcePhenotypeGeneticContext source) {

		Item item = null;

		boolean status = false;

		if (source.getGeneticFeatureType().equals("allele")) {
			
			item = AlleleService.getAlleleItem(source.getEntityUniqueAccession()).getItem();
			status = true;
			

		} else if (source.getGeneticFeatureType().equals("genotype"))

		{

			ItemHolder itemHolder = null;
			itemHolder = GenotypeService.getGenotypeItem(source.getEntityUniqueAccession());
			
			if (itemHolder!=null){
				item = GenotypeService.getGenotypeItem(source.getEntityUniqueAccession()).getItem();	
			}
			status = true;

		}

		if (status == true) {
			log.info("Item place holder has been obtained: " + item + "; Source record:" + source);
		} else {
			log.error("Unknown feature type to associate with a phenotype. Skipping row." + " Source Record:" + source);
		}

		return item;
	}

	private void addToCollection(SourcePhenotypeGeneticContext source, Item item) {

		boolean status = false;

		if (source.getGeneticFeatureType().equals("allele")) {

			addAllelePhenotypeItem(source, item);
			status = true;

		} else if (source.getGeneticFeatureType().equals("genotype"))

		{
			addGenotypePhenotypeItem(source, item);
			status = true;

		}

		if (status == true) {
			log.info("Item has been added to a phenotype/genetic context collection " + item);
		} else {
			log.error("Unknown feature type to associate with a phenotype. Skipping row." + " Source Record:" + source);
		}

	}
}
