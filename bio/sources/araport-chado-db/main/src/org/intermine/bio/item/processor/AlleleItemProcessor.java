package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class AlleleItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceAllele, Item> {

	protected static final Logger log = Logger.getLogger(AlleleItemProcessor.class);

	private String targetClassName;

	private static final String ITEM_CLASSNAME = "Allele";
	private static final String SEQUENCE_ALTERATION_CLASS_NAME = "SequenceAlterationType";

	public AlleleItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceAllele item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceAllele source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;

		int itemId = -1;

		try {
			log.info("Creating Item has started. Source Object:" + source);

			item = super.getService().createItem(ITEM_CLASSNAME);

			log.info("Item place holder has been created: " + item);

			log.info("Allele Unique Name " + source.getAlleleUniqueName());
			item.setAttribute("primaryIdentifier", source.getAlleleUniqueName());

			log.info("Allele Accession " + source.getAlleleUniqueAccession());
			item.setAttribute("secondaryIdentifier", source.getAlleleUniqueAccession());

			log.info("Name   " + source.getAlleleName());
			item.setAttribute("name", source.getAlleleName());

			Item sequenceAlterationType = null;

			if (!StringUtils.isBlank(source.getSequenceAlterationType())) {
				log.info("Sequence Altreation Type: " + source.getSequenceAlterationType());

				sequenceAlterationType = CVService.getCVTermItem("polymorphism_type",
						source.getSequenceAlterationType());
				log.info("Referenced Sequence Alteration Type: " + sequenceAlterationType);
			}

			if (sequenceAlterationType == null) {
				sequenceAlterationType = CVService.getCVTermItem("polymorphism_type", ApplicationContext.UNKNOWN);
			}

			if (sequenceAlterationType != null) {
				item.setReference("sequenceAlterationType", sequenceAlterationType);
			}

			if (!StringUtils.isBlank(source.getDescription())) {
				log.info("Allele Description " + source.getDescription());
				item.setAttribute("description", source.getDescription());
			}

			Item alleleClass = null;

			alleleClass = CVService.getCVTermItem("allele_mode_type", source.getAlleleClass());

			if (!StringUtils.isBlank(source.getAlleleClass())) {
				log.info("Allele Class: " + source.getAlleleClass());

				alleleClass = CVService.getCVTermItem("alleleClass", source.getAlleleClass());

				log.info("Referenced Allele Class: " + alleleClass);
			}

			if (alleleClass == null) {
				alleleClass = CVService.getCVTermItem("alleleClass", ApplicationContext.UNKNOWN);
			}

			if (alleleClass != null) {
				item.setReference("alleleClass", alleleClass);
			}

			Item mutagen = null;

			if (!StringUtils.isBlank(source.getMutagen())) {

				mutagen = CVService.getCVTermItem("mutagen_type", source.getMutagen());
				log.info("Referenced Mutagen: " + mutagen);

			}

			if (mutagen == null) {
				mutagen = CVService.getCVTermItem("mutagen_type", ApplicationContext.UNKNOWN);
			}

			if (mutagen != null) {
				item.setReference("mutagen", mutagen);
			}

			Item inheritanceMode = null;

			if (!StringUtils.isBlank(source.getInheritanceType())) {

				inheritanceMode = CVService.getCVTermItem("inheritance_type", source.getInheritanceType());
				log.info("Referenced Inheritance Mode: " + inheritanceMode);
			}

			if (inheritanceMode == null) {
				inheritanceMode = CVService.getCVTermItem("inheritance_type", ApplicationContext.UNKNOWN);
			}

			if (inheritanceMode != null) {
				item.setReference("inheritanceMode", inheritanceMode);
			}

			Item mutationSite = null;

			if (!StringUtils.isBlank(source.getMutationSite())) {

				mutationSite = CVService.getCVTermItem("mutation_site_type", source.getMutationSite());
				log.info("Referenced Mutation Site: " + mutationSite);
			}

			if (mutationSite != null) {
				item.setReference("mutationSite", mutationSite);
			}

			Item organismItem = super.getService().getOrganismItem(super.getService().getOrganism().getTaxonId());

			if (organismItem != null) {
				item.setReference("organism", organismItem);
			}

			if (!StringUtils.isBlank(source.getWildType())) {
				log.info("Wild Type: " + source.getWildType());
				item.setAttribute("wildType", source.getWildType());
			}

			itemId = super.getService().store(item);
						
		
		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source);
			} else {
				log.info("Target Item has been created. Target Object:" + item);

				ItemHolder itemHolder = new ItemHolder(item, itemId);

				if (itemHolder != null && itemId != -1) {
					AlleleService.addAleleItem(source.getAlleleUniqueAccession(), itemHolder);
				}
				
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

	
}
