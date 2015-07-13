package org.intermine.bio.item.processor;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.ChadoCV;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataconversion.DataSourceProcessor;
import org.intermine.bio.dataflow.config.DataFlowConfig;
import org.intermine.bio.item.ItemProcessor;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.bio.domain.source.*;


public class CVTermProcessor extends DataSourceProcessor implements ItemProcessor<SourceCVTerm, Item> {

	protected static final Logger log = Logger.getLogger(CVTermProcessor.class);
	
	private String targetClassName;
	private String parentTargetClassName;


	public CVTermProcessor(ChadoDBConverter chadoDBConverter) {
		super(chadoDBConverter);
	}

	

	@Override
	public Item process(SourceCVTerm item) throws Exception {

		return createItem(item);

	}

	private Item createItem(SourceCVTerm source) throws ObjectStoreException {

		log.info("Creating Item has started. Source Object:" + source);
		
		String cv_name = source.getCvName();
		String cv_item_class_name = DataFlowConfig.getChadoCVMap().get(cv_name).getTargetClassName();

		log.info("Chado CV Name:" + cv_name + ";Target CV Class Name:" + cv_item_class_name);
		
		String cv_term_name = source.getCvTermName();
		String cvterm_item_class_name = DataFlowConfig.getChadoCVTermClassMap().get(cv_name);

		log.info("Chado CV Term Name:" + cv_term_name + ";Target CV Term Class Name:" + cvterm_item_class_name);

		Item item = null;

		Exception exception = null;

		try {
			if (!StringUtils.isBlank(cvterm_item_class_name) && (!StringUtils.isBlank(cv_term_name))) {

				log.info("Passed Validation Criteria. Creating Target Item...");

				String sourceString = cv_term_name;
				String parsedSourceString = StringUtils.replace(sourceString , "_", " ");
				
				parsedSourceString = WordUtils.capitalize(parsedSourceString);
				
				String identifier = source.getDbName() + ":" + cv_term_name;
				item = super.getService().createItem(cvterm_item_class_name);
				item.setAttribute("identifier",identifier);
				item.setAttribute("name",parsedSourceString);
				item.setAttribute("uniqueName", cv_term_name);

				Item vocabularyRef = CVService.getCVItemMap().get(cv_name).getItem();
				
				String referenceName = "vocabulary";
				
				item.setReference(referenceName, vocabularyRef);
				
				super.getService().store(item);

				if (item!=null){
					CVService.addCVTermItem(cv_name, cv_term_name, item);
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
	
	public void setTargetClassName(String name){
		this.targetClassName = name;
	}
	
	public String getTargetClassName(){
		return this.targetClassName;
	}
	

	public String getParentTargetClassName() {
		return parentTargetClassName;
	}



	public void setParentTargetClassName(String parentTargetClassName) {
		this.parentTargetClassName = parentTargetClassName;
	}

}
