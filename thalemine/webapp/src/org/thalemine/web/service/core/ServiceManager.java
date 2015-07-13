package org.thalemine.web.service.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.thalemine.web.exception.ComponentInitializationException;
import org.thalemine.web.exception.ServiceException;
import org.thalemine.web.exception.ServiceInitializationException;
import org.thalemine.web.injection.SystemServiceSetter;
import org.thalemine.web.query.repository.AlleleDAO;
import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.query.repository.GenotypeDAO;
import org.thalemine.web.query.repository.PhenotypeDAO;
import org.thalemine.web.query.repository.PublicationDAO;
import org.thalemine.web.query.repository.StockDAO;
import org.thalemine.web.query.repository.impl.GenotypeDAOImpl;
import org.thalemine.web.query.repository.impl.PhenotypeDAOImpl;
import org.thalemine.web.query.repository.impl.PublicationDAOImpl;
import org.thalemine.web.query.repository.impl.StockDAOImpl;
import org.thalemine.web.service.BusinessService;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.StockService;
import org.thalemine.web.service.AlleleService;
import org.thalemine.web.service.builder.BusinessServiceBuilder;
import org.thalemine.web.service.impl.AlleleServiceImpl;
import org.thalemine.web.service.impl.StockServiceImpl;
import org.thalemine.web.utils.ClassUtils;

public final class ServiceManager implements IServiceManager {

	private static final String TAG = "ServiceManager";
	private static final Logger log = Logger.getLogger(ServiceManager.class);
	private static HashMap<String, BusinessService> serviceCache = new HashMap<String, BusinessService>();

	private ServiceRegistry registry;
	private IComponentManager componentManager = ComponentManager.getInstance();
	
	private ServiceManager(){
		
	}
	
	private static class ServiceManagerHolder {

		public static final ServiceManager INSTANCE = new ServiceManager();

	}

	public static ServiceManager getInstance() {

		return ServiceManagerHolder.INSTANCE;
		
	}



	@Override
	public BusinessService getService(String name) throws ServiceException, ServiceInitializationException {
		try {

			if (!hasService(name)) {
				addService(name);
			} else {
				BusinessService service = serviceCache.get(name);
				return service;
			}

		} catch (ServiceException e) {
			log.error(TAG + " :error in getService");
		}
		return null;
	}

	@Override
	public void addService(String name) throws ServiceException, ServiceInitializationException {
		Exception exception = null;

		try {
			if (!hasService(name)) {
				log.info("Addding Service by Name: " + name);
				lookup(name);
			}
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error(TAG + " service add error" + exception.getMessage() + " " + name);
				exception.printStackTrace();
				throw new ServiceInitializationException("Adding Service Error: " + name);
			} else {
				log.info(TAG  + "Service has been successfully added. " + name);
			}
		}

	}

	@Override
	public void validateService(String name) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void initServices() throws ServiceInitializationException {

		Exception exception = null;

		try {
			String serviceClass = this.registry.getEntry(ServiceConfig.STOCK_SERVICE);
			log.info("Addding Service:" + serviceClass);
			addService(serviceClass);

			serviceClass = this.registry.getEntry(ServiceConfig.ALLELE_SERVICE);
			log.info("Adding Service:" + serviceClass);
			addService(serviceClass);
			
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error(TAG + " service initialization errors" + exception.getMessage());
				exception.printStackTrace();
				throw new ServiceInitializationException(TAG + " services initialization errors");
			} else {
				log.info(TAG  + "Services has been successfully initialized.");
			}
		}

	}

	@Override
	public String[] listServices() throws ServiceInitializationException {
		ArrayList<String> services = new ArrayList<String>();

		String[] array = new String[services.size()];
		services.toArray(array);
		return array;

	}

	@Override
	public BusinessService lookup(String name) throws Exception {

		String serviceClass = this.registry.getEntry(name);
		BusinessService businessService = null;

		if (StringUtils.isNotBlank(serviceClass)) {

			log.info("Looking up and creating a new " + serviceClass + " service");

			StockDAO stockDAO = (StockDAO) this.componentManager.getComponent(ComponentConfig.STOCK_DAO);
			PublicationDAO publicationDAO = (PublicationDAO) this.componentManager
					.getComponent(ComponentConfig.PUBLICATION_DAO);

			GenotypeDAO genotypeDAO = (GenotypeDAO) this.componentManager.getComponent(ComponentConfig.GENOTYPE_DAO);
			PhenotypeDAO phenotypeDAO = (PhenotypeDAO) this.componentManager
					.getComponent(ComponentConfig.PHENOTYPE_DAO);

			AlleleDAO alleleDAO = (AlleleDAO) this.componentManager
					.getComponent(ComponentConfig.ALLELE_DAO);
			
			if (ServiceConfig.STOCK_SERVICE.equalsIgnoreCase(serviceClass)) {

				if (stockDAO != null && publicationDAO != null && genotypeDAO != null && phenotypeDAO != null) {

					businessService = (StockService) new BusinessServiceBuilder().build(new StockServiceImpl(), stockDAO, publicationDAO,
							genotypeDAO, phenotypeDAO, alleleDAO);
				}

			}
			
			if (ServiceConfig.ALLELE_SERVICE.equalsIgnoreCase(serviceClass)) {

				if (stockDAO != null && publicationDAO != null && genotypeDAO != null && phenotypeDAO != null) {

					businessService = (AlleleService) new BusinessServiceBuilder().build(new AlleleServiceImpl(), stockDAO, publicationDAO,
							genotypeDAO, phenotypeDAO, alleleDAO);
				}

			}
			
			if (businessService != null) {
				
				businessService.validateState();
				serviceCache.put(serviceClass, businessService);
	
			}
		}

		return null;

	}

	@Override
	public boolean hasService(String name) {

		boolean result = false;

		if (serviceCache != null && serviceCache.size() > 0) {
			if (serviceCache.containsKey(name)) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public void setRegistry(ServiceRegistry registry) {

		this.registry = registry;

	}

	@Override
	public void setComponentManager(IComponentManager manager) {

		this.componentManager = manager;

	}

	@Override
	public void validateState() throws Exception {

		Exception exception = null;

		if (this.componentManager == null) {
			log.error(TAG + " Component Manager must not be null!");
			exception = new Exception(" Component Manager must not be null!");
			exception.printStackTrace();
			throw exception;
		}

		if (this.registry == null || this.registry.getServiceRegistry().size() == 0) {
			log.error(TAG + " Service Registry must not be null! and Components must be set.");
			exception = new Exception(" Service Registry must not be null! and Components must be set.");
			exception.printStackTrace();
			throw exception;
		}

	}

	@Override
	public void initialize() throws Exception {

		validateState();
		initServices();

	}

}
