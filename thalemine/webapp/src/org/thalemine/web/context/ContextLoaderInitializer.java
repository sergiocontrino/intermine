package org.thalemine.web.context;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

public class ContextLoaderInitializer implements javax.servlet.Filter {

	protected static final Logger log = Logger.getLogger(ContextLoaderInitializer.class);
	private final static StopWatch timer = new StopWatch();

	public FilterConfig filterConfig;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		log.debug("Thalemine WebContext Filter/Initializer");

		ServletContext servletContext = filterConfig.getServletContext();

		synchronized (this) {
			try {
				if (!isContextInitialized(servletContext)) {

					initialize(request);

				} else {
					log.debug("Context has been already initialized. Skipping Initialization.");
				}
			} catch (Exception e) {
				log.error("Error ocurred while initilalizing web application context." + e.getMessage());
				e.printStackTrace();
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	private void initialize(ServletRequest request) {

		Exception exception = null;

		log.info("WebApplication Initialization has started...");

		try {
			timer.reset();
			timer.start();

			WebApplicationLauncher launcher = WebApplicationLauncher.getInstance();
			ServletContext servletContext = filterConfig.getServletContext();

			launcher.initialize(servletContext, request);
		} catch (Exception e) {
			exception = e;
		} finally {

			timer.stop();

			if (exception != null) {
				log.error("Error occurred during Web Application Initialization:" + exception.getMessage());
				exception.printStackTrace();
			} else {

				log.info("Web Application Initialization has completed." + "; Total time taken. " + timer.toString());
			}

		}

	}

	private boolean isContextInitialized(ServletContext servletContext) {

		boolean result = false;

		Object objectContext = servletContext
				.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

		try {
			if (objectContext != null) {

				WebApplicationContext context = (WebApplicationContext) objectContext;
				result = context.isContextInitialized();

			}
		} catch (Exception e) {

			log.error("Cannot identify state of WebApplication Context!");
			e.printStackTrace();
		}

		log.debug(WebApplicationContext.CONTEXT_INITIALIZED + ":" + result);

		return result;
	}
}
