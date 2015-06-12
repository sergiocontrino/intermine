package org.thalemine.web.utils;

import org.apache.log4j.Logger;
import org.thalemine.web.query.PublicationQueryService;
import org.thalemine.web.query.StockQueryService;

import javax.servlet.http.HttpServletRequest;


public class InitialServiceContext {

	protected static final Logger log = Logger.getLogger(InitialServiceContext.class);
	
	public Object lookup(String serviceName, HttpServletRequest request) {

		if (serviceName.equalsIgnoreCase("StockQueryService")) {
			
			 log.info("Looking up and creating a new StockQueryService object");
			 
			return StockQueryService.getInstance(request);
			
		} else if (serviceName.equalsIgnoreCase("PublicationQueryService")){
			
			return PublicationQueryService.getInstance(request);
		}
	
		return null;
	}

}
