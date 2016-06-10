package org.thalemine.web.service.builder;

import org.apache.log4j.Logger;
import org.thalemine.web.query.repository.AbstractRepository;
import org.thalemine.web.service.InfrastructureService;

public class RepositoryBuilder {
	
	protected static final Logger log = Logger.getLogger(RepositoryBuilder.class);
	
	private final AbstractRepository repository;
	
	public RepositoryBuilder(){
		this.repository = new AbstractRepository();
	}
	
		
	public AbstractRepository build(InfrastructureService service) throws Exception {
		
		log.info("Building Repository has started...");
		
		repository.setSystemService(service);
		log.info("Building Repository. Validation stage has started.");
		repository.validateState();
		log.info("Building Repository. Validation stage has completed.");
		
		log.info("Building Repository has completed.");
		return repository;
				
	}
	
	
}
