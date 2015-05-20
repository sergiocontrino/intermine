package org.intermine.bio.item.processor;

import org.apache.log4j.Logger;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;


public class StockItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceStock, Item> {

	protected static final Logger log = Logger.getLogger(StockItemProcessor.class);
	
	private String targetClassName;
	
	private static final String ITEM_CLASSNAME = "Stock";
	
	public StockItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	

	@Override
	public Item process(SourceStock item) throws Exception {

		return createStock(item);

	}

	private Item createStock(SourceStock source) throws ObjectStoreException {

		log.info("Creating Item has started. Source Object:" + source);
		
		Item item = super.getService().createItem(ITEM_CLASSNAME);
		item.setAttribute("primaryIdentifier", source.getUniqueName());
		item.setAttribute("secondaryIdentifier", source.getName());
		item.setAttribute("name", source.getName());
		Item organismItem =
				super.getService().getOrganismItem(super.getService().getOrganism().getTaxonId());
		
		
		// stock.setAttribute("type", stockType);
		// stock.setAttribute("stockCenter", stockCenterUniqueName);
		item.setReference("organism", organismItem);
		super.getService().store(item);
		
		log.info("Target Item has been created. Target Object:" + item);
		
		getStockItems().put(source.getUniqueName(), item);
				
		return item;
	}
	
	public void setTargetClassName(String name){
		this.targetClassName = name;
	}
	
	public String getTargetClassName(){
		return this.targetClassName;
	}
}
