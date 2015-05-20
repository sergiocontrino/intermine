package org.intermine.bio.item.processor;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.DataFlowConfig;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;

public class CVItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceCV, Item> {

	protected static final Logger log = Logger.getLogger(CVItemProcessor.class);

	private String targetClassName;

	public CVItemProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	@Override
	public Item process(SourceCV item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceCV source) throws ObjectStoreException {

		log.info("Creating Item has started. Source Object:" + source);

		String cv_name = source.getName();
		String item_class_name = DataFlowConfig.getChadoCVMap().get(cv_name);

		log.info("Chado CV Name:" + cv_name + ";Target CV Class Name:" + item_class_name);

		Item item = null;

		Exception exception = null;

		try {
			if (!StringUtils.isBlank(item_class_name) && (!StringUtils.isBlank(cv_name))) {

				log.info("Passed Validation Criteria. Creating Target Item...");

				String sourceString = source.getName();
				String parsedSourceString = StringUtils.replace(sourceString , "_", " ");
				
				parsedSourceString = WordUtils.capitalize(parsedSourceString);
				
				item = super.getService().createItem(item_class_name);
				item.setAttribute("name",parsedSourceString);
				item.setAttribute("uniqueName", cv_name);
				item.setAttribute("url", "https://www.arabidopsis.org/");

				super.getService().store(item);

				if (item!=null){
					CVService.addCVItem(cv_name, item);
				}


			}

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error occurred during item creation. Source Item:" + source);
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
