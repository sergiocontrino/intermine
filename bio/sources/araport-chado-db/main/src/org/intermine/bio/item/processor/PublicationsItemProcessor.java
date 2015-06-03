package org.intermine.bio.item.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.PublicationService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.ApplicationContext;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class PublicationsItemProcessor extends DataSourceProcessor implements ItemProcessor<SourcePublication, Item> {

	protected static final Logger log = Logger.getLogger(PublicationsItemProcessor.class);

	private String targetClassName;

	private static final String ITEM_CLASSNAME = "Publication";

	public PublicationsItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourcePublication item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourcePublication source) throws ObjectStoreException {

		Exception exception = null;

		Item item = null;

		int itemId = -1;

		try {
			log.info("Creating Item has started. Source Object:" + source);

			item = super.getService().createItem(ITEM_CLASSNAME);

			log.info("Item place holder has been created: " + item);

			if (!StringUtils.isBlank(source.getPubAccessionNumber())) {
				log.info("PubMed Id: " + source.getPubAccessionNumber());
				item.setAttribute("pubMedId", source.getPubAccessionNumber());
			}

			if (!StringUtils.isBlank(source.getPubDOI())) {
				log.info("DOI: " + source.getPubDOI());
				item.setAttribute("doi", source.getPubDOI());
			}
			
			if (!StringUtils.isBlank(source.getPubTitle())) {
				log.info("Title: " + source.getPubTitle());
				item.setAttribute("title", source.getPubTitle());
			}
			
			if (!StringUtils.isBlank(source.getPubTitle())) {
				log.info("Journal: " + source.getPubSource());
				item.setAttribute("journal", source.getPubSource());
			}
			
			if (!StringUtils.isBlank(source.getPubIssue())) {
				log.info("Issue: " + source.getPubIssue());
				item.setAttribute("issue", source.getPubIssue());
			}

			if (!StringUtils.isBlank(source.getPubYear())) {
				log.info("Year: " + source.getPubYear());
				item.setAttribute("year", source.getPubYear());
			}

			if (!StringUtils.isBlank(source.getPubPages())) {
				log.info("Pages: " + source.getPubPages());
				item.setAttribute("pages", source.getPubPages());
			}
			
			if (!StringUtils.isBlank(source.getPubVolume())) {
				log.info("Volume: " + source.getPubVolume());
				item.setAttribute("volume", source.getPubVolume());
			}
			
			if (!StringUtils.isBlank(source.getPubFirstAuthor())) {
				log.info("First Author: " + source.getPubFirstAuthor());
				item.setAttribute("firstAuthor", source.getPubFirstAuthor());
			}
			
			if (!StringUtils.isBlank(source.getAbstractText())) {
				log.info("First Author: " + source.getAbstractText());
				item.setAttribute("firstAuthor", source.getAbstractText());
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
					PublicationService.addPublicationItem(source.getPubAccessionNumber(), itemHolder);
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
