package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
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

public class StockSynonymItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceStockSynonym, Item> {

	protected static final Logger log = Logger.getLogger(StockSynonymItemProcessor.class);

	private String targetClassName;

	private static final String DATASET_NAME = "TAIR Germplasm";
	private static final String ITEM_CLASSNAME = "Synonym";
	

	public StockSynonymItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceStockSynonym item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceStockSynonym source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;
		ItemHolder itemHolder = null;
		ItemHolder subjectItemHolder = null;
		
		
		int itemId = -1;

		try {
			log.debug("Creating Item has started. Source Object:" + source);

			subjectItemHolder = StockService.getStockItem(source.getGermplasmTairAccession());
			
			if (subjectItemHolder==null){
				exception = new Exception("SubjectItemHolder is Null! Source Record does not exists in the Service Lookup!");
				throw exception;
			}
								
			Item subjectItem = subjectItemHolder.getItem();
			
			if (subjectItem==null){
				exception = new Exception("Subject Item is Null! Source Record does not exists in the Service Lookup!");
				throw exception;
			}
			
			if (StringUtils.isBlank(source.getSynonymName())){
				exception = new Exception("Source Synonym Cannot Be Null!");
				throw exception;
			}
			
			if (StringUtils.isBlank(source.getSynonymType())){
				exception = new Exception("Source Synonym Type Cannot Be Null!");
				throw exception;
			}
			
			if (subjectItem!=null && !StringUtils.isBlank(source.getSynonymName())) {
				
				item = super.getService().createItem(ITEM_CLASSNAME);
				
				log.debug("Item place holder has been created: " + item);
				
				log.debug("Stock Synonym: " + source.getSynonymName());
				item.setAttribute("value", source.getSynonymName());
										
				if (subjectItem != null) {
					item.setReference("subject", subjectItem);
				}
				
				if (!StringUtils.isBlank(source.getSynonymType())) {
					log.debug("Synonym Type: " + source.getSynonymType());
					item.setAttribute("type", source.getSynonymType());
				}
				
			}else{
				log.debug("Skipping source record. Invalid entry:" + source);
			}
					
			itemId = super.getService().store(item);

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source + "Error:" + exception.getMessage());
			} else {
				log.debug("Target Item has been created. Target Object:" + item);
					
				itemHolder = new ItemHolder(item, itemId);
				
			}
		}
		
		if (itemHolder!=null && itemId!=-1) {
			
			//setDataSetItem(itemHolder);
			
		}
		
		return item;
	}

	public void setTargetClassName(String name) {
		this.targetClassName = name;
	}

	public String getTargetClassName() {
		return this.targetClassName;
	}

	
	
	private void setDataSetItem(ItemHolder item){
		
		Item dataSetItem = getDataSet();
		
		if (dataSetItem!=null && item!=null){
			DataSetService.addBionEntityItem(DATASET_NAME, item.getItem());
			
		}
		
	}

	private Item getDataSet(){
		return DataSetService.getDataSetItem(DATASET_NAME).getItem();
	}
}
