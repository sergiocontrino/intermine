package org.thalemine.web.service.builder;

import org.apache.log4j.Logger;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.core.ComponentManager;
import org.thalemine.web.service.core.ComponentRegistry;
import org.thalemine.web.service.core.IComponentManager;
import org.thalemine.web.service.core.IRepositoryManager;

public class ComponentManagerBuilder {
	
	private final IComponentManager manager;
	protected static final Logger log = Logger.getLogger(ComponentManagerBuilder .class);
	
	public ComponentManagerBuilder(){
		
		manager = ComponentManager.getInstance();
	}
	
	public IComponentManager build(InfrastructureService service) throws Exception{
		
		log.info("ComponentManagerBuilder: Component Builder Manager has started...");
		
		manager.setSystemService(service);
		log.info("System Service has been set.");
		IRepositoryManager repository = new RepositoryBuilder().build(service);
		manager.setRepository(repository);
		log.info("Repository has been set.");
		
		ComponentRegistry registry = new ComponentRegistry();
		registry.initialize();	
		log.info("Component Registry has been initialized.");
		
		manager.setRegistry(registry);
		log.info("Component Registry has been set.");
		
		manager.initialize();
		
		log.info("ComponentManagerBuilder: Component Manager Builder has completed.");
		
		return manager;
		
	}

}
