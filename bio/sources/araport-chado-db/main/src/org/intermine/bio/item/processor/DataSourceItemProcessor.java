package org.intermine.bio.item.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.bio.chado.AlleleService;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.chado.DataSetService;
import org.intermine.bio.chado.DataSourceService;
import org.intermine.bio.chado.GenotypeService;
import org.intermine.bio.chado.OrganismService;
import org.intermine.bio.chado.PhenotypeService;
import org.intermine.bio.chado.PublicationService;
import org.intermine.bio.chado.StockService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataloader.job.AbstractStep;
import org.intermine.bio.dataloader.job.StepExecution;
import org.intermine.bio.dataloader.job.TaskExecutor;
import org.intermine.bio.dataloader.job.TaskletStep;
import org.intermine.bio.domain.source.SourceCVTerm;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.store.service.StoreService;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class DataSourceItemProcessor extends AbstractStep {

	protected static final Logger log = Logger.getLogger(DataSourceItemProcessor.class);

	private static ChadoDBConverter service;

	protected TaskExecutor taskExecutor;

	public DataSourceItemProcessor(ChadoDBConverter chadoDBConverter) {
		super();
		service = chadoDBConverter;

	}

	public DataSourceItemProcessor getPostProcessor(String name, ChadoDBConverter chadoDBConverter,
			TaskExecutor taskExecutor

	) {

		DataSourceItemProcessor processor = new DataSourceItemProcessor(chadoDBConverter);
		processor.setName(name);

		processor.setTaskExecutor(taskExecutor);

		return processor;

	}

	@Override
	protected void doExecute(StepExecution stepExecution) throws Exception {

		log.info("Running Task Let Step!  DataSource Item Processor " + getName());

		taskExecutor.execute(new Runnable() {

			public void run() {
				createDataSources();
			}
		});

	}

	@Override
	protected void doPostProcess(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub

	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	protected TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	private void createDataSources() {

		Item ncbiDataSourceItem = createDataSource("NCBI");
		Item ncbiDataSet = createDataSet(ncbiDataSourceItem, "PubMed to gene mapping", null, null, null);
		
		Item tairDataSourceItem = createDataSource("TAIR");
		String version = "TAIR Germplasm, October 2013";
		String url = "https://www.arabidopsis.org/";
		String description = "ABRC Germplasm/Seed Stock";
		Item germplasmDataSet = createDataSet(tairDataSourceItem, "TAIR Germplasm", version, url, description);
		
		
		String versionPoly = "TAIR Polymorphism, October 2013";
		String urlPoly = "https://www.arabidopsis.org/";
		String descriptionPoly = "TAIR Polymorphism";
		Item polyDataSet = createDataSet(tairDataSourceItem, "TAIR Polymorphism", versionPoly, urlPoly, descriptionPoly);
		
		String versionPheno = "TAIR Phenotypes, October 2013";
		String urlPheno = "https://www.arabidopsis.org/";
		String descriptionPheno = "TAIR Phenotypes";
		Item phenoDataSet = createDataSet(tairDataSourceItem, "TAIR Phenotypes", versionPheno, urlPheno, descriptionPheno);
		
		String versionEcotypes = "TAIR Ecotypes, October 2013";
		String urlEcotypes = "https://www.arabidopsis.org/";
		String descriptionEcotypes = "TAIR Ecotypes";
		Item ecotyTypesDataSet = createDataSet(tairDataSourceItem, "TAIR Ecotypes", versionPheno, urlPheno, descriptionPheno);

	}

	private Item createDataSource(String dataSourceName) {

		Item dataSourceItem = null;
		Exception exception = null;
		ItemHolder itemHolder = null;
		int itemId = -1;

		try {

			dataSourceItem = StoreService.getService().createItem("DataSource");
			dataSourceItem.setAttribute("name", dataSourceName);
			itemId = StoreService.getService().store(dataSourceItem);

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for datasource record:" + dataSourceName);
			} else {
				log.info("Target Item has been created. DataSource:" + dataSourceItem);

				itemHolder = new ItemHolder(dataSourceItem, itemId);

				if (itemHolder != null && itemId != -1) {

					DataSourceService.addDataSourceItem(dataSourceName, itemHolder);

				}

			}

		}

		return dataSourceItem;

	}

	private Item createDataSet(Item dataSource, String dataSetName, String version, String url, String description) {

		Item dataSetItem = null;
		Exception exception = null;
		ItemHolder itemHolder = null;
		int itemId = -1;

		if (dataSource != null && !StringUtils.isBlank(dataSetName)) {

			String datasourceRefId = dataSource.getIdentifier();

			dataSetItem = StoreService.getService().createItem("DataSet");
			dataSetItem.setAttribute("name", dataSetName);
			dataSetItem.setReference("dataSource", datasourceRefId);
			
			if (!StringUtils.isBlank(description)){
				
				dataSetItem.setAttribute("description", description);
			}
			
			if (!StringUtils.isBlank(version)){
				
				dataSetItem.setAttribute("version", version);
			}
			
			if (!StringUtils.isBlank(url)){
				
				dataSetItem.setAttribute("url", url);
			}

		}

		try {

			itemId = StoreService.getService().store(dataSetItem);

		} catch (ObjectStoreException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error storing item for dataset record:" + dataSetName);
			} else {
				log.info("Target Item has been created. DataSet:" + dataSetItem);

				itemHolder = new ItemHolder(dataSetItem, itemId);

				if (itemHolder != null && itemId != -1) {

					DataSetService.addDataSetItem(dataSetName, itemHolder);

				}

			}

		}

		return dataSetItem;

	}

}
