package org.thalemine.web.service;

import org.intermine.webservice.client.core.ServiceFactory;
import org.thalemine.web.injection.ContextSetter;

public interface InfrastructureService extends Verifiable, ContextSetter{

	String getRootWebApplicationContext();
	String getServiceEndPoint();
	
	
}
