package org.thalemine.web.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.thalemine.web.service.Verifiable;

public interface WebApplicationContext extends ApplicationContext, Verifiable{
	
	/**
	 * Context attribute to bind root WebApplicationContext to on successful first invocation of a
	 * webservice matched url pattern /service/*.
	 * <p>Note: If the startup of the root context fails, this attribute can contain
	 * an exception or error as value. Use WebApplicationContextUtils for convenient
	 * lookup of the root WebApplicationContext.
	 */
	
	String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";
	String CONTEXT_INITIALIZED = "CONTEXT_INITIALIZED";
	String SERVICE_ENDPOINT_CONTEXT_ATTRIBUTE = "SERVICE_ENDPOINT";
	
	/**
	 * Scope identifier for the global web application scope: "application".
	 * Supported in addition to the standard scopes "singleton" and "prototype".
	 */
	String SCOPE_APPLICATION = "application";
	
	/**
	 * Return the standard Servlet API ServletContext for this application.
	 * <p>Also available for a Portlet application, in addition to the PortletContext.
	 */
	ServletContext getServletContext();
	
	/**
	 * Set the ServletContext for this web application context.
	 * <p>Does not cause an initialization of the context: refresh needs to be
	 * called after the setting of all configuration properties.
	 * @see #refresh()
	 */
	void setServletContext(ServletContext servletContext);
	
	void initialize(ServletContext servletContext, ServletRequest request) throws Exception;
	
	boolean isContextInitialized() throws Exception;
	
	boolean isValidServiceEndPoint();
	
	boolean isValidRootWebAppContext();
	
	String getRootWebApplicationContext();
	
	String getServiceEndPoint();

}
