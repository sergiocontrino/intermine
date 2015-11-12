package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class BackgroundAccessionStockItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceBackgroundStrain, Item> {

	protected static final Logger log = Logger.getLogger(BackgroundAccessionStockItemProcessor.class);

	private String targetClassName;

	public BackgroundAccessionStockItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceBackgroundStrain item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceBackgroundStrain source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;
		Item itemStock = null;

		try {
			log.info("Creating Mapping for background Accession has started. Source Object:" + source);
			
			if (OrganismService.getStrainMap().containsKey(source.getBackgroundAccessionName())){
				item = OrganismService.getStrainMap().get(source.getBackgroundAccessionName()).getItem();
			}
			
			if (item!=null){
				if 	(DataSourceProcessor.getStockItems().containsKey(source.getStockUniqueAccession())) {
					itemStock = DataSourceProcessor.getStockItems().get(source.getStockUniqueAccession());
				}
			}
			
			if (item!=null && itemStock!=null){
				OrganismService.addBgStockItem(source.getBackgroundAccessionName(), source.getStockUniqueAccession(), itemStock);
			}
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error creating background accession record:" + source + "; Error:" + exception.getMessage());
			} else {
				log.info("Target Item has been mapped.");
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
