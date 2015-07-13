package org.thalemine.web.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ClassUtils;
import org.apache.log4j.Logger;
import org.thalemine.web.utils.ClassInstanceUtils;

public class ContextLoader {

	protected static final Logger log = Logger.getLogger(ContextLoader.class);

	/**
	 * The root WebApplicationContext instance that this loader manages.
	 */
	private WebApplicationContext context;
	
	private ContextLoader(){

	}

	private static class ContextLoaderContextHolder {

		public static final ContextLoader INSTANCE = new ContextLoader();

	}

	public static ContextLoader getInstance() {

		return ContextLoaderContextHolder.INSTANCE;
		
	}

	public WebApplicationContext initWebApplicationContext(ServletContext servletContext, ServletRequest request) {

		log.info("Initializing Root WebApplicationContext");

		if (log.isInfoEnabled()) {
			log.info("Root WebApplicationContext: initialization started...");
		}

		long startTime = System.currentTimeMillis();

		try {

			// Store context in local instance variable, to guarantee that
			// it is available on ServletContext shutdown.

			if (this.context == null) {
				this.context = createWebApplicationContext(servletContext, "StaticWebApplicationContext");
			}

			this.context.initialize(servletContext, request);

			servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);

			log.info("Published root WebApplicationContext as ServletContext attribute with name ["
					+ WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");

			long elapsedTime = System.currentTimeMillis() - startTime;
			log.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");

			return this.context;

		} catch (Exception e) {
			log.error("Webapplication Context Initialization Failed!" + " ;Message:" + e.getMessage());
		}

		return null;
	}

	protected WebApplicationContext createWebApplicationContext(ServletContext sc, String contextClassName)
			throws ClassNotFoundException {

		log.info("CreateWebApplicationContext has started...");

		WebApplicationContext webappContext = StaticWebApplicationContext.getInstance();
		
		webappContext.setServletContext(sc);
		
		log.info("CreateWebApplicationContext has completed.");

		return webappContext;
	}

	public WebApplicationContext getWebApplicationContext() {
		return this.context;
	}
}
