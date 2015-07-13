package org.thalemine.web.service.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.thalemine.web.context.WebApplicationLauncher;
import org.thalemine.web.exception.ComponentException;
import org.thalemine.web.exception.ComponentInitializationException;
import org.thalemine.web.exception.ServiceException;
import org.thalemine.web.injection.SystemServiceSetter;
import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.query.repository.StockDAO;
import org.thalemine.web.query.repository.impl.StockDAOImpl;
import org.thalemine.web.service.BusinessService;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.Initializable;
import org.thalemine.web.service.StockService;
import org.thalemine.web.service.builder.DAOBuilder;
import org.thalemine.web.service.builder.RepositoryBuilder;
import org.thalemine.web.service.impl.StockServiceImpl;
import org.thalemine.web.query.repository.PublicationDAO;
import org.thalemine.web.query.repository.AlleleDAO;
import org.thalemine.web.query.repository.GenotypeDAO;
import org.thalemine.web.query.repository.PhenotypeDAO;
import org.thalemine.web.query.repository.impl.PublicationDAOImpl;
import org.thalemine.web.query.repository.impl.AlleleDAOImpl;
import org.thalemine.web.query.repository.impl.GenotypeDAOImpl;
import org.thalemine.web.query.repository.impl.PhenotypeDAOImpl;

public class ComponentManager implements IComponentManager {

	private static final String TAG = "ComponentManager";
	private static final Logger log = Logger.getLogger(ComponentManager.class);
	private static HashMap<String, GeneralDAO> componentCache = new HashMap<String, GeneralDAO>();

	private InfrastructureService systemService;
	private ComponentRegistry registry;
	private IRepositoryManager repository;

	private ComponentManager() {
		this.registry = new ComponentRegistry();
	}
	
	private static class ComponentManagerHolder {

		public static final ComponentManager INSTANCE = new ComponentManager();

	}

	public static ComponentManager getInstance() {

		return ComponentManagerHolder.INSTANCE;
		
	}

	@Override
	public GeneralDAO getComponent(String name) throws Exception, ComponentInitializationException {
		try {

			if (!hasComponent(name)) {
				addComponent(name);
			}

			GeneralDAO component = componentCache.get(name);
			return component;

		} catch (ComponentInitializationException e) {
			log.error(TAG + ":error in getComponent");
		}
		return null;
	}

	@Override
	public void addComponent(String name) throws Exception, ComponentInitializationException {

		Exception exception = null;

		try {
			if (!hasComponent(name)) {
				log.info("Component not found in the component registry. Adding Component:" + name);
				lookup(name);
			}
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error(TAG + " Component add error" + exception.getMessage() + " " + name);
				exception.printStackTrace();
				throw new ComponentInitializationException("Adding Component Error: " + name);
			} else {
				log.info(TAG + " Component has been successfully added. " + name);
			}
		}
	}

	@Override
	public void validateComponent(String name) throws Exception, ComponentInitializationException {

		Exception exception = null;

		try {
			GeneralDAO generalDAO = getComponent(name);
			generalDAO.validateState();
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error(TAG + " Component Validation has failed:" + name + "; Message:" + exception.getMessage());
			} else {
				log.info(TAG  + "Component has been successfully validated. " + name);
			}
		}

	}

	@Override
	public void initComponents() throws Exception, ComponentInitializationException {

		Exception exception = null;

		log.info(TAG + " Initializing Components has started...");
		
		try {
			String componentClass = this.registry.getEntry(ComponentConfig.STOCK_DAO);
			log.info("Component: " + componentClass);
			addComponent(componentClass);

			componentClass = this.registry.getEntry(ComponentConfig.PUBLICATION_DAO);
			log.info("Component: " + componentClass);
			addComponent(componentClass);

			componentClass = this.registry.getEntry(ComponentConfig.GENOTYPE_DAO);
			log.info("Component: " + componentClass);
			addComponent(componentClass);

			componentClass = this.registry.getEntry(ComponentConfig.ALLELE_DAO);
			log.info("Component: " + componentClass);
			addComponent(componentClass);

			componentClass = this.registry.getEntry(ComponentConfig.PHENOTYPE_DAO);
			log.info("Component: " + componentClass);
			addComponent(componentClass);

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error(TAG + " components initialization errors" + exception.getMessage());
				exception.printStackTrace();
				throw new ComponentInitializationException(TAG + " components initialization errors");
			} else {
				log.info(TAG  + " Components has been successfully initialized");
			}
		}

	}

	@Override
	public String[] listComponents() throws ComponentInitializationException {

		ArrayList<String> components = new ArrayList<String>();

		String[] array = new String[components.size()];
		components.toArray(array);
		return array;
	}

	@Override
	public GeneralDAO lookup(String name) throws Exception {

		String componentClass = this.registry.getEntry(name);

		log.info("Component Lookup by Name: " + componentClass);

		GeneralDAO generalDAO = null;
		Exception exception = null;
		
		try {

			if (StringUtils.isNotBlank(componentClass)) {

				log.info("Looking up and creating a new " + componentClass + " component");

				if (ComponentConfig.STOCK_DAO.equalsIgnoreCase(componentClass)) {

					generalDAO = (StockDAO) new DAOBuilder().build(new StockDAOImpl(), this.repository);	

				}
				
				if (ComponentConfig.PUBLICATION_DAO.equalsIgnoreCase(componentClass)) {

					generalDAO = (PublicationDAO) new DAOBuilder().build(new PublicationDAOImpl(), this.repository);

				}
				
				if (ComponentConfig.ALLELE_DAO.equalsIgnoreCase(componentClass)) {

					generalDAO = (AlleleDAO) new DAOBuilder().build(new AlleleDAOImpl(), this.repository);

				}
				
				if (ComponentConfig.GENOTYPE_DAO.equalsIgnoreCase(componentClass)) {

					generalDAO = (GenotypeDAO) new DAOBuilder().build(new GenotypeDAOImpl(), this.repository);

				}
				
				if (ComponentConfig.PHENOTYPE_DAO.equalsIgnoreCase(componentClass)) {

					generalDAO = (PhenotypeDAO) new DAOBuilder().build(new PhenotypeDAOImpl(), this.repository);

				}
			}

			
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null || generalDAO == null) {
				log.error(TAG + " component lookup error" + exception.getMessage() + " " + componentClass);
				exception.printStackTrace();
				throw new ComponentInitializationException("Initializing Component Error: " + componentClass);
			}

			if (StringUtils.isNotBlank(componentClass) && generalDAO != null) {

				generalDAO.setRepository(this.repository);
				generalDAO.validateState();
				componentCache.put(componentClass, generalDAO);
				return generalDAO;
			}
		}

		return null;

	}

	@Override
	public boolean hasComponent(String name) {

		boolean result = false;

		if (componentCache != null && componentCache.size() > 0) {
			if (componentCache.containsKey(name)) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public void initialize() throws Exception {

		validateState();
		initComponents();

	}

	@Override
	public void validateState() throws Exception {

		Exception exception = null;
		
		log.info("Validating Component Manager has started.");

		if (this.systemService == null) {
			log.error(TAG + " System Service must not be null!");
			exception = new Exception(" System Service must not be null!");
			exception.printStackTrace();
			throw exception;
		}

		if (this.repository == null) {
			log.error(TAG + " Repository must not be null!");
			exception = new Exception(" Repository must not be null!");
			exception.printStackTrace();
			throw exception;
		}
		
		if (this.registry == null || this.registry.getComponentRegistry().size() == 0) {
			log.error(TAG + " Component Registry must not be null! and Components must be set.");
			exception = new Exception(" Component Registry must not be null! and Components must be set.");
			exception.printStackTrace();
			throw exception;
		}

		this.systemService.validateState();
		
		log.info("Validating Component Manager has completed.");

	}

	@Override
	public void setSystemService(InfrastructureService service) throws Exception {

		this.systemService = service;

	}

	@Override
	public void setRegistry(ComponentRegistry registry) {

		this.registry = registry;

	}

	@Override
	public void setRepository(IRepositoryManager repository) {
		
		this.repository = repository;
		
	}

}
