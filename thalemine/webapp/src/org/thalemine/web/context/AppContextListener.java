package org.thalemine.web.context;

import java.net.URI;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class AppContextListener implements ServletContextListener {

	protected static final Logger log = Logger.getLogger(AppContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {

		Exception exception = null;
		log.info("Closing Thalemine WebApplicationContext");
		try {
			ServletContext servletContext = contextEvent.getServletContext();
			servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error to close Thalemine WebApplicationContext");
			} else {
				log.info("Thalemine WebApplicationContext has been shutdown successfully.");
			}
		}

	}

	@Override
	public void contextInitialized(ServletContextEvent servletContext) {

		log.info("Starting Thalemine WebApplication has started.");

	}

}
