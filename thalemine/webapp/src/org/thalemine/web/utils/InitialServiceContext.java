package org.thalemine.web.utils;

import org.apache.log4j.Logger;
import org.thalemine.web.query.AlleleQueryService;
import org.thalemine.web.query.PublicationQueryService;
import org.thalemine.web.query.StockQueryService;

import javax.servlet.http.HttpServletRequest;


public class InitialServiceContext {

	protected static final Logger log = Logger.getLogger(InitialServiceContext.class);
	
	public Object lookup(String serviceName, HttpServletRequest request) {

		if (serviceName.equalsIgnoreCase("StockQueryService")) {
			
			 log.info("Looking up and creating a new StockQueryService object");
			 
			 StockQueryService service = StockQueryService.getInstance(request);
			 service.init(request);
			 
			 return service;
			
		} else if (serviceName.equalsIgnoreCase("PublicationQueryService")){
			
			PublicationQueryService service = PublicationQueryService.getInstance(request);
			service.init(request);
			
			 return service;
			
		}else if (serviceName.equalsIgnoreCase("AlleleQueryService")){
			
			AlleleQueryService service = AlleleQueryService.getInstance(request);
			service.init(request);
			
			 return service;
		}
		
	
		return null;
	}

}
