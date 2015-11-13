package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class StrainItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceStrain, Item> {

	protected static final Logger log = Logger.getLogger(StrainItemProcessor.class);

	private String targetClassName;

	private static final String ITEM_CLASSNAME = "Strain";
	private static final String STRAIN_TYPE_CLASS_NAME = "organism_type";

	public StrainItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceStrain item) throws Exception {

		return createTargetItem(item);

	}

	private Item createTargetItem(SourceStrain source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;

		try {
			log.info("Creating Item has started. Source Object:" + source);

			item = super.getService().createItem(ITEM_CLASSNAME);

			log.info("Item place holder has been created: " + item);
			
			if (StringUtils.isBlank(source.getAccessionAbbreviation())) {
				Exception e = new Exception("Strain Accession Primary Identifier cannot be null! Skipping Source Record:" + source);
				throw e;
			}

			if (StringUtils.isBlank(source.getOrganismType())) {
				Exception e = new Exception("Strain Accession OrganismType cannot be null! Skipping Source Record:" + source);
				throw e;
			}
			
			log.info("Strain Accession: " + source.getAccessionAbbreviation());
			item.setAttribute("primaryIdentifier", source.getAccessionAbbreviation());

			log.info("Organism Id: " + source.getOrganismId() + " Id:" + source.getOrganismId());
			
			item.setAttribute("secondaryIdentifier",
					StringUtils.capitalize(source.getOrganismType()) + " Id:" + source.getOrganismId());

			if (!StringUtils.isBlank(source.getAccessionOriginalName())){
				
				log.info("Name   " + source.getAccessionOriginalName());
				item.setAttribute("name", source.getAccessionOriginalName());
			}else
			{
				log.info("Name   " + source.getAccessionAbbreviation());
				item.setAttribute("name", source.getAccessionAbbreviation());
     		}
			
			log.info("Accession Abbreviation: " + source.getAccessionAbbreviation());
			item.setAttribute("abbreviationName", source.getAccessionAbbreviation());
			
			log.info("Accession Number: " + source.getAccessionNumber());
			
			if (!StringUtils.isBlank(source.getAccessionNumber())){
				
				log.info("Accession Number: " + source.getAccessionNumber());
				item.setAttribute("accessionNumber", source.getAccessionNumber());
			}
	
			log.info("Habitat: " + source.getHabitat());
			
			if (!StringUtils.isBlank(source.getHabitat())){
				
				log.info("Habitat: " + source.getHabitat());
				item.setAttribute("habitat", source.getHabitat());
			}
			
			
			if (!StringUtils.isBlank(source.getGeoLocation())){
				
				log.info("Geo Location: " + source.getGeoLocation());
				item.setAttribute("geoLocation", source.getGeoLocation());
			}

			String strStrainType = source.getOrganismType();
			log.info("Strain Type: " + strStrainType);

			Item strainType = CVService.getCVTermItem(STRAIN_TYPE_CLASS_NAME, source.getOrganismType());
			log.info("Referenced Strain Type: " + strainType);

			if (strainType == null) {
				strainType = CVService.getCVTermItem(STRAIN_TYPE_CLASS_NAME, ApplicationContext.UNKNOWN);
			}
			
			log.info("Referenced Strain Type: " + strainType);
			
			if (strainType !=null) {
				log.info("Setting Strain Type: " + strainType);
				item.setReference("type", strainType);
			}

			Item organismItem = super.getService().getOrganismItem(super.getService().getOrganism().getTaxonId());

			if (organismItem != null) {
				item.setReference("organism", organismItem);
			}
			
			int itemId = super.getService().store(item);
			
			ItemHolder itemHolder = new ItemHolder(item, itemId);

			if (itemHolder!=null){
				OrganismService.addStrainItem(source.getAccessionAbbreviation(), itemHolder);
			}


		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source + "Error:" + exception.getMessage());
			} else {
				log.info("Target Item has been created. Target Object:" + item);

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
