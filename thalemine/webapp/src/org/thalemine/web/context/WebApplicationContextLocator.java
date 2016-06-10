package org.thalemine.web.context;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


public class WebApplicationContextLocator {

	protected static final Logger log = Logger.getLogger(WebApplicationContextLocator.class);

	private static String SERVICE_URL;
	private static String BASE_URL;
	private static final String SERVICE_SUFFIX = "/service/";
	
	public static String getServiceUrl(HttpServletRequest request) {

		if (SERVICE_URL != null) {

			log.info("Retrieved Portal Service URL:" + SERVICE_URL);
			
			return SERVICE_URL;
		}
		
		SERVICE_URL = getPortalBaseUrl(request) + SERVICE_SUFFIX;
		log.info("Obtained Portal Service URL:" + SERVICE_URL);
				
		return SERVICE_URL;

	}

	public static String getPortalBaseUrl(HttpServletRequest request) {

		if (BASE_URL != null) {

			log.info("Retrieved Portal Base URL:" + BASE_URL);

			return BASE_URL;
		}

		Exception exception = null;
		URI contextUrl = URI.create(request.getRequestURL().toString()).resolve(request.getContextPath());
		URL baseUrl = null;

		log.info("Webapp Context URI:" + contextUrl);

		try {

			baseUrl = contextUrl.toURL();
			log.info("Base URL:" + contextUrl);
			String externalBaseUrl = baseUrl.toExternalForm();
			log.info("External Base URL:" + externalBaseUrl);

		} catch (MalformedURLException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Severe error occurred in obtaining web-application base url. Web-based Query Services will fail!"
						+ ":Cause" + exception.getCause() + ";Message" + exception.getMessage());

				exception.printStackTrace();
			} else {
				BASE_URL = baseUrl.toExternalForm();
			}
		}

		log.info("Obtained Portal Base URL:" + BASE_URL);

		return BASE_URL;
	}

}
