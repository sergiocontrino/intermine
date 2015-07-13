package org.thalemine.web.injection;

import org.intermine.webservice.client.core.ServiceFactory;

public interface ServiceFactorySetter {

	void setServiceFactory(ServiceFactory factory) throws Exception;

}
