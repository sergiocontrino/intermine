package org.thalemine.web.service.builder;

import org.apache.log4j.Logger;
import org.thalemine.web.query.repository.impl.StockDAOImpl;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.core.ComponentManager;
import org.thalemine.web.service.core.ComponentRegistry;
import org.thalemine.web.service.core.IComponentManager;
import org.thalemine.web.service.core.IServiceManager;
import org.thalemine.web.service.core.ServiceManager;
import org.thalemine.web.service.core.ServiceRegistry;

public class ServiceManagerBuilder {
	
protected static final Logger log = Logger.getLogger(ServiceManagerBuilder.class);
	
private IServiceManager manager;

	public ServiceManagerBuilder(){
		
		manager = ServiceManager.getInstance();
	}
	
	public IServiceManager build(IComponentManager componentManager) throws Exception{
	
		log.info("ServiceManagerBuilder: Building Services has started.");
		ServiceRegistry registry = new ServiceRegistry();	
		registry.initialize();
		
		manager.setRegistry(registry);
		manager.setComponentManager(componentManager);
		
		manager.initialize();
		manager.validateState();
		
		log.info("ServiceManagerBuilder:Service Manager has completed.");
		
		return manager;
		
	}
	
}
