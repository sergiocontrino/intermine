package org.thalemine.web.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.intermine.web.logic.PortalHelper;
import org.intermine.web.logic.session.SessionMethods;
import org.intermine.web.util.URLGenerator;

public class WebApplicationContextLocator {
	
	protected static final Logger log = Logger.getLogger(WebApplicationContextLocator.class);
	
	//private static String SERVICE_URL;
	private static final String SERVICE_PREFIX = "/service";
	
	private static final String SERVICE_URL = "https://apps.araport.org/demo-thalemine/service/";
	//private static final String SERVICE_URL = "http://localhost:8081/thalemine/service/";
	
	//private static final String SERVICE_URL = "http://ibelyaev-lx.jcvi.org:8081/thalemine/service/";
	
	/*
	static {
		SERVICE_URL= null;		
	   } */
	
	public static String getServiceUrl(HttpServletRequest request){
	
		/*
		 if(SERVICE_URL != null){
	         return SERVICE_URL;
	      }
		
		*/
		
	//SERVICE_URL = new URLGenerator(request).getPermanentBaseURL()+SERVICE_PREFIX;
	
	//SERVICE_URL = PortalHelper.getBaseUrl(request)+SERVICE_PREFIX;
		
	URI contextUrl = URI.create(request.getRequestURL().toString()).resolve(request.getContextPath());
	
	log.info("URI=" + contextUrl);
	
	
	try {
		URL baseUrl = contextUrl.toURL();
		log.info("Base URL=" + contextUrl);
		String externalBaseUrl = baseUrl.toExternalForm();
		log.info("External Base URL=" + externalBaseUrl);
		
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			      
	return SERVICE_URL;
		
	}

}
