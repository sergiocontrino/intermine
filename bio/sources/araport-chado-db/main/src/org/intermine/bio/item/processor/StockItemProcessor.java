package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
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

		Exception exception = null;

		Item item = null;

		try {
			log.info("Creating Item has started. Source Object:" + source);

			item = super.getService().createItem(ITEM_CLASSNAME);

			log.info("Item place holder has been created: " + item);

			log.info("Germplasm Accession " + source.getGermplasmTairAccession());
			item.setAttribute("primaryIdentifier", source.getGermplasmTairAccession());

			log.info("Stock/Germplasm Id " + source.getStockCategory() + " Id:" + source.getStockId());
			item.setAttribute("secondaryIdentifier", StringUtils.capitalize(source.getStockCategory()) + " Id:"
					+ source.getStockId());

			log.info("Name   " + source.getName());
			item.setAttribute("name", source.getName());

			log.info("Germplasm Name   " + source.getName());
			item.setAttribute("germplasmName", source.getName());

			log.info("Display Name " + source.getDisplayName());
			item.setAttribute("displayName", source.getDisplayName());

			log.info("Stock Name " + source.getStockName());
			item.setAttribute("stockName", source.getStockName());

			log.info("Stock Description " + source.getDescription());
			item.setAttribute("description", source.getDescription());

			String strStockType = source.getStockType();
			log.info("String Stock Type: " + strStockType);

			Item stockType = CVService.getCVTermItem("germplasm_type", source.getStockType());
			log.info("Referenced Stock Type: " + stockType);

			item.setReference("type", stockType);

			Item stockCategory = CVService.getCVTermItem("stock_category", source.getStockCategory());
			log.info("Referenced Stock Category: " + stockCategory);

			if (stockCategory != null) {
				item.setReference("stockCategory", stockCategory);
			}

			if (!StringUtils.isBlank(source.getMutagen())) {
				Item mutagen = CVService.getCVTermItem("mutagen_type", source.getMutagen());
				log.info("Referenced Mutagen: " + mutagen);
				if (mutagen != null) {
					item.setReference("mutagen", mutagen);
				}

			}

			log.info("Stock Center Comment: " + source.getStockCenterComment());
			if (!StringUtils.isBlank(source.getStockCenterComment())) {
				item.setAttribute("stockCenterComment", source.getStockCenterComment());
			}

			Item organismItem = super.getService().getOrganismItem(super.getService().getOrganism().getTaxonId());

			if (organismItem != null) {
				item.setReference("organism", organismItem);
			}

			super.getService().store(item);

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for source record:" + source);
			} else {
				log.info("Target Item has been created. Target Object:" + item);

				getStockItems().put(source.getGermplasmTairAccession(), item);
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
