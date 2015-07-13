package org.thalemine.web.service.impl;

import org.apache.log4j.Logger;
import org.thalemine.web.context.WebApplicationContext;
import org.thalemine.web.injection.ContextSetter;
import org.thalemine.web.query.repository.AbstractRepository;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.Verifiable;

public class InfrastructureServiceImpl implements InfrastructureService {

	protected static final Logger log = Logger.getLogger(InfrastructureServiceImpl.class);
	
	// Internal reference to the context used by this client.
	private WebApplicationContext context;

	private InfrastructureServiceImpl() {

	}

	private static class InfrastructureServiceImplServiceHolder {

		public static final InfrastructureServiceImpl INSTANCE = new InfrastructureServiceImpl();

	}

	public static InfrastructureServiceImpl getInstance() {

		return InfrastructureServiceImplServiceHolder.INSTANCE;
	}
	
	// Set the context that this client is to use.
	@Override
	public void setContext(WebApplicationContext context) {

		this.context = context;

	}

	@Override
	public String getRootWebApplicationContext() {
		return this.context.getRootWebApplicationContext();
	}

	@Override
	public String getServiceEndPoint() {

		return this.context.getServiceEndPoint();

	}

	@Override
	public void validateState() throws Exception {

		log.info("Validating Infrastructure Service has started...");
		
		if (this.context == null) {
			throw new Exception("Context must not be null!");
		}

		if (!this.context.isContextInitialized() && !this.context.isValidRootWebAppContext()
				&& this.context.isValidServiceEndPoint()) {
			
			log.error("Invalid Webapplication Context or Web Service endpoint!");
			throw new Exception("Invalid Webapplication Context or Web Service endpoint!");

		}

		log.info("Validating Infrastructure Service has completed.");
	}

}
