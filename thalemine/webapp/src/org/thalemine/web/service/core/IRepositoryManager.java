package org.thalemine.web.service.core;

import org.intermine.webservice.client.core.ServiceFactory;
import org.thalemine.web.service.InfrastructureService;


public interface IRepositoryManager {

	InfrastructureService getSystemService();
	ServiceFactory getFactory();
	
}
