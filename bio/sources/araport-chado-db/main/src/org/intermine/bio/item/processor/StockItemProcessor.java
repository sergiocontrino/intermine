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

public class StockItemProcessor extends DataSourceProcessor implements ItemProcessor<SourceStock, Item> {

	protected static final Logger log = Logger.getLogger(StockItemProcessor.class);

	private String targetClassName;

	private static final String ITEM_CLASSNAME = "Stock";
	private static final String STOCK_ANNOTATION_CLASS_NAME = "StockAnnotation";
	private static final String CHOROMOSOMAL_CONSTITUTION_ANNOTATION_CLASS_NAME = "ChromosomalConstitutionAnnotation";
	private static final String GROWTH_CONSTITUTION_ANNOTATION_CLASS_NAME = "GrowthConditionAnnotation";

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

			log.info("Germplasm Name " + source.getName());
			item.setAttribute("primaryIdentifier", source.getName());

			log.info("Germplasm Accession " + source.getGermplasmTairAccession());
			item.setAttribute("secondaryIdentifier", source.getGermplasmTairAccession());

			log.info("Name   " + source.getName());
			item.setAttribute("name", source.getName());

			log.info("Germplasm Name   " + source.getName());
			item.setAttribute("germplasmName", source.getName());

			log.info("Display Name " + source.getDisplayName());
			item.setAttribute("displayName", source.getDisplayName());

			if (!StringUtils.isBlank(source.getStockName())) {
				log.info("Stock Name " + source.getStockName());
				item.setAttribute("stockName", source.getStockName());
			}

			if (!StringUtils.isBlank(source.getDescription())) {
				log.info("Stock Description " + source.getDescription());
				item.setAttribute("description", source.getDescription());
			}

			String strStockType = source.getStockType();
			log.info("String Stock Type: " + strStockType);

			Item stockType = CVService.getCVTermItem("germplasm_type", source.getStockType());
			log.info("Referenced Stock Type: " + stockType);

			if (stockType == null){
				stockType = CVService.getCVTermItem("germplasm_type", ApplicationContext.UNKNOWN);
			}

			if (stockType != null) {
				item.setReference("type", stockType);
			}
			
			Item stockCategory = CVService.getCVTermItem("stock_category", source.getStockCategory());
			
			if (stockCategory == null){
				stockCategory = CVService.getCVTermItem("mutagen_type", ApplicationContext.UNKNOWN);
			}
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

			}else{
				Item mutagen = CVService.getCVTermItem("mutagen_type", ApplicationContext.UNKNOWN);
				
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

			Item stockAnnotationItem = createStockAnnotation(source, item);

			item.setReference("stockAnnotation", stockAnnotationItem);
			
			Item accessionRef = null;
			
			log.info("Strain Accession: " + source.getAcessionName());
			
			if (!StringUtils.isBlank(source.getAcessionName())){
				log.info("Strain Accession Map: " + OrganismService.getStrainMap().get(source.getAcessionName()).getItem());
			}
			
			
			if (!StringUtils.isBlank(source.getAcessionName())) {
				
				accessionRef = OrganismService.getStrainMap().get(source.getAcessionName()).getItem();
				
				String referenceName = "accession";
				
				log.info("Setting Strain Accession for Stock: " + accessionRef + " ; " + source.getAcessionName() + ";" + source.getName());
				
				if (accessionRef!=null){
					item.setReference(referenceName, accessionRef);
				}
				
				
				if (item!=null && (accessionRef!=null)){
					OrganismService.addStockItem(source.getAcessionName(), source.getGermplasmTairAccession(), item);
				}
				
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

	private Item createStockAnnotation(SourceStock source, Item stockItem) {

		Item stockAnnotationItem = null;
		Exception exception = null;

		try {
			stockAnnotationItem = super.getService().createItem(STOCK_ANNOTATION_CLASS_NAME);

			log.info("Stock Mutant: "  + "Stock: " + source.getName() + " ; " + source.getIsMutant());
			
			log.info("Stock: " + source.getName() + " ; Mathches: " + source.getIsMutant().matches("true"));
			
			if (!StringUtils.isBlank(source.getIsMutant())) {
				
				log.info("Mutant: " + source.getName() + " ; " + source.getIsMutant());
				
				
				if (source.getIsMutant().equals("true")) {
					
					log.info("Setting Mutant to True");
					stockAnnotationItem.setAttribute("mutant", "Yes");
				}else
				{
					log.info("Setting Mutant to False");
					stockAnnotationItem.setAttribute("mutant", "No");
				}
			}

			if (!StringUtils.isBlank(source.getIsTransgene())) {
				if (source.getIsTransgene().equals("true")) {
					stockAnnotationItem.setAttribute("transgene", "Yes");
				} else {
					stockAnnotationItem.setAttribute("transgene", "No");
				}
			}

			if (!StringUtils.isBlank(source.getIsNaturalVarinat())) {
				if (source.getIsNaturalVarinat().equals("true")) {
					stockAnnotationItem.setAttribute("naturalVariant", "Yes");
				} else {
					stockAnnotationItem.setAttribute("naturalVariant", "No");
				}
			}

			stockAnnotationItem.setReference("stock", stockItem);

			Item chromosomalAnnotation = createChromosomalAnnotation(source, stockAnnotationItem);

			if (chromosomalAnnotation != null) {
				stockAnnotationItem.setReference("chromosomalConstitution", chromosomalAnnotation);
			}

			Item growthCondition = createGrowthConditionAnnotation(source, stockAnnotationItem);

			if (growthCondition != null) {
				stockAnnotationItem.setReference("growthCondition", growthCondition);
			}

			super.getService().store(stockAnnotationItem);

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing stock annotation item for source record:" + source);
			} else {
				log.info("Stock Annotation Item has been created. Target Object:" + stockAnnotationItem);

			}
		}

		return stockAnnotationItem;
	}

	private Item createChromosomalAnnotation(SourceStock source, Item annotationItem) {

		Item chromosomalAnnotationItem = null;
		Exception exception = null;

		try {
			chromosomalAnnotationItem = super.getService().createItem(CHOROMOSOMAL_CONSTITUTION_ANNOTATION_CLASS_NAME);

			if (!StringUtils.isBlank(source.getIsAneploidChromosome())) {
				if (source.getIsAneploidChromosome().equals("true")) {
					chromosomalAnnotationItem.setAttribute("aneploidChromosome", "Yes");
				}
				{
					chromosomalAnnotationItem.setAttribute("aneploidChromosome", "No");
				}
			}

			if (!StringUtils.isBlank(source.getPloidy())) {

				chromosomalAnnotationItem.setAttribute("ploidy", source.getPloidy());

			}

			chromosomalAnnotationItem.setReference("stockAnnotation", annotationItem);

			super.getService().store(chromosomalAnnotationItem);
		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing Chromosomal Annotation item for source record:" + source);
			} else {
				log.info("Chromosomal Annotation Item has been created. Target Object:" + chromosomalAnnotationItem);

			}
		}

		return chromosomalAnnotationItem;
	}

	private Item createGrowthConditionAnnotation(SourceStock source, Item annotationItem) {

		Item growthAnnotationItem = null;
		Exception exception = null;

		try {
			growthAnnotationItem = super.getService().createItem(GROWTH_CONSTITUTION_ANNOTATION_CLASS_NAME);

			if (!StringUtils.isBlank(source.getSpecialGrowthConditions())) {
				growthAnnotationItem.setAttribute("specialGrowthConditions", source.getSpecialGrowthConditions());
			}

			if (!StringUtils.isBlank(source.getGrowthTemperature())) {

				growthAnnotationItem.setAttribute("growthTemperature", source.getGrowthTemperature());

			}

			if (!StringUtils.isBlank(source.getDurationOfGrowth())) {

				growthAnnotationItem.setAttribute("durationOfGrowth", source.getDurationOfGrowth());

			}

			growthAnnotationItem.setReference("stockAnnotation", annotationItem);
			super.getService().store(growthAnnotationItem);
		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Growth Condition Annotation Item item for source record:" + source);
			} else {
				log.info("Growth Condition Annotation Item has been created. Target Object:" + growthAnnotationItem);

			}
		}

		return growthAnnotationItem;
	}
}
