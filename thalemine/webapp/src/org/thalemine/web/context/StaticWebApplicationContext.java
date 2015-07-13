package org.thalemine.web.context;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class StaticWebApplicationContext implements WebApplicationContext {

	protected static final Logger log = Logger.getLogger(StaticWebApplicationContext.class);

	private static String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE_VALUE;
	private static String SERVICE_ENDPOINT_CONTEXT_ATTRIBUTE_VALUE;

	private ServletContext servletContext;

	private StaticWebApplicationContext() {

	}

	private static class StaticWebApplicationContextHolder {

		public static final StaticWebApplicationContext INSTANCE = new StaticWebApplicationContext();

	}

	public static StaticWebApplicationContext getInstance() {

		return StaticWebApplicationContextHolder.INSTANCE;
		
	}

	/**
	 * Set the ServletContext that this WebApplicationContext runs in.
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public Date getStartupDate() {
		
		return new Date();
	}

	@Override
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	@Override
	public void initialize(ServletContext servletContext, ServletRequest request) throws Exception {

		log.info("Static WebApplication Context Initialization has started...");
		
		if (servletContext == null) {
			log.error("Servlet Context cannot be null.");
			throw new Exception("Servlet Context cannot be null.");
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE_VALUE = WebApplicationContextLocator.getPortalBaseUrl(httpRequest);
		SERVICE_ENDPOINT_CONTEXT_ATTRIBUTE_VALUE = WebApplicationContextLocator.getServiceUrl(httpRequest);
		
		this.servletContext.setAttribute(WebApplicationContext.CONTEXT_INITIALIZED, "true");

		log.info("ROOT_WEB_APPLICATION_CONTEXT:" + getRootWebApplicationContext());
		log.info("SERVICE_ENDPOINT_CONTEXT_ATTRIBUTE_VALUE:" + getServiceEndPoint());
		
		log.info("Static WebApplication Context Initialization has completed.");
	}

	public boolean isContextInitialized() throws Exception {

		boolean result = false;
		boolean contextInitialized = false;

		if (isValidServletContext()) {
			Object attribute = this.servletContext.getAttribute(WebApplicationContext.CONTEXT_INITIALIZED);
			
			log.debug("Raw Attribute:" + WebApplicationContext.CONTEXT_INITIALIZED + ":" + attribute);
			
			contextInitialized = getBooleanAttribute(WebApplicationContext.CONTEXT_INITIALIZED, attribute);
			
			log.debug(WebApplicationContext.CONTEXT_INITIALIZED + ":" + contextInitialized);
		}

		if (contextInitialized && isValidRootWebAppContext() && isValidServiceEndPoint()) {
			result = true;
		}

		log.debug(WebApplicationContext.CONTEXT_INITIALIZED + "; Result:" + result);
		
		return result;
	}

	private boolean isValidServletContext() {

		boolean result = true;

		if (this.servletContext == null) {
			result = false;
			log.error("Not Valid Servlet Context!");
		}

		return result;
	}

	public boolean isValidContext() {
		boolean result = true;

		return result;
	}

	private boolean getBooleanAttribute(String attributeName, Object attributeValue) throws Exception {

		boolean result = true;

		if (StringUtils.isBlank(attributeName)) {
			result = false;
			log.error("Cannot parse context attribute. Attribute Name is Null.");
			throw new Exception("Cannot parse context attribute. Attribute Name is Null.");
		}

		if (!isValidAttribute(attributeValue)) {
			result = false;
			log.error("Cannot parse context attribute." + "Attribute:" + attributeName + "Attribute is Null.");
			throw new Exception("Cannot parse context attribute." + "Attribute:" + attributeName + "Attribute is Null.");
		}

		String parsedAttribute = (String) attributeValue;

		if (StringUtils.isBlank(parsedAttribute)) {

			result = false;
			log.error("Cannot parse context attribute." + "Attribute:" + attributeName + "Attribute is Null.");
			throw new Exception("Cannot parse context attribute." + "Attribute:" + attributeName + "Attribute is Null.");
		}

		result = BooleanUtils.toBooleanObject(parsedAttribute);
		
		return result;

	}

	private boolean isValidAttribute(Object attribute) {
		boolean result = true;

		if (attribute == null) {
			result = false;
		}

		return result;

	}

	public boolean isValidRootWebAppContext() {

		boolean result = true;

		if (StringUtils.isBlank(ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE_VALUE)) {
			result = false;
			log.error("Not Valid ROOT Webapp Context!");
		}

		return result;
	}

	public boolean isValidServiceEndPoint() {

		boolean result = true;

		if (StringUtils.isBlank(SERVICE_ENDPOINT_CONTEXT_ATTRIBUTE_VALUE)) {
			result = false;
			log.error("Not Valid Web Service Query End Point!");
		}

		return result;
	}

	@Override
	public String getRootWebApplicationContext() {

		return ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE_VALUE;
	}

	@Override
	public String getServiceEndPoint() {
		
		return SERVICE_ENDPOINT_CONTEXT_ATTRIBUTE_VALUE;
	
	}

	@Override
	public void validateState() throws Exception {

		Exception exception = null;
		boolean result = false;
		try {
			
			result = isContextInitialized();

		} catch (Exception e) {
			exception = e;
		} finally {
			
			if (!result) {
				log.error("Not Valid WebApplication Context." + "; Message: Context has not been initialized correctly.");
			}
			
			if (exception != null) {
				log.error("Not Valid WebApplication Context." + "; Message:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
			
	

		}

	}
}
