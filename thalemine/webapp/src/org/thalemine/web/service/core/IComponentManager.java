package org.thalemine.web.service.core;

import org.thalemine.web.exception.ComponentInitializationException;
import org.thalemine.web.injection.RepositorySetter;
import org.thalemine.web.injection.SystemServiceSetter;
import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.service.Initializable;
import org.thalemine.web.service.Verifiable;


public interface IComponentManager extends Verifiable, SystemServiceSetter, RepositorySetter, Initializable {

	 public GeneralDAO getComponent(String name) throws ComponentInitializationException, Exception;
	 
	 public void addComponent(String name) throws Exception, ComponentInitializationException;
	 
	 public void validateComponent(String name) throws ComponentInitializationException, Exception;
	 
	 public void initComponents() throws ComponentInitializationException, Exception;
	 
	 public String[] listComponents() throws ComponentInitializationException;
	 
	 public GeneralDAO lookup(String name) throws Exception;
	 
	 public boolean hasComponent(String name);
	 
	 public void setRegistry(ComponentRegistry registry);
	 

 	
}
