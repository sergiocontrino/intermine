package org.thalemine.web.query.repository;

import org.apache.log4j.Logger;
import org.intermine.webservice.client.core.ServiceFactory;
import org.thalemine.web.injection.SystemServiceSetter;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.Verifiable;
import org.thalemine.web.service.core.IRepositoryManager;

public class AbstractRepository implements IRepositoryManager, SystemServiceSetter, Verifiable {

	protected static final Logger log = Logger.getLogger(AbstractRepository.class);
	
	protected ServiceFactory factory;
	protected InfrastructureService service;

	@Override
	public void validateState() throws Exception {
		
		log.debug("Validating Repository has started...");
		
		if (this.service == null) {
			log.error("Service must not be null!");
			throw new Exception("Service must not be null!");
		}

		this.service.validateState();

		if (this.factory == null) {
			log.error("Service Factory must not be null!");
			throw new Exception("Service Factory must not be null!");
		}

		log.debug("Validating Repository has completed.");
	}

	@Override
	public void setSystemService(InfrastructureService service) throws Exception {
		
		log.debug("Setting System Service of Repository...");
		this.service = service;
		log.debug("Validating System Service of Repository...");
		this.service.validateState();
		log.debug("Validation of System Service Completed.");
		
		log.debug("Setting Service factory has started...");
		
		log.debug("Service endpoint:" + this.service.getServiceEndPoint());
		this.factory = new ServiceFactory(this.service.getServiceEndPoint());
		
		log.debug("System Repository has been set.");
	}

	@Override
	public InfrastructureService getSystemService() {
		return this.service;
	}

	@Override
	public ServiceFactory getFactory() {
		return this.factory;
	}

}
