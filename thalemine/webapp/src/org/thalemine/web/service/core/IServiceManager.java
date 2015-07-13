package org.thalemine.web.service.core;

import org.thalemine.web.exception.ServiceException;
import org.thalemine.web.exception.ServiceInitializationException;
import org.thalemine.web.injection.SystemServiceSetter;
import org.thalemine.web.service.BusinessService;
import org.thalemine.web.service.Initializable;
import org.thalemine.web.service.Verifiable;

public interface IServiceManager extends Verifiable, Initializable{

	 public BusinessService getService(String name) throws ServiceException, ServiceInitializationException;
	 
	 public void addService(String name) throws ServiceException, ServiceInitializationException;
	 
	 public void validateService(String name) throws ServiceException;
	 
	 public void initServices() throws ServiceInitializationException;
	 
	 public String[] listServices() throws ServiceInitializationException;
	 
	 public BusinessService lookup(String name) throws Exception;
	 
	 public boolean hasService(String name);
	 
	 public void setRegistry(ServiceRegistry registry);
	 
	 public void setComponentManager(IComponentManager manager);
 	
}
