package org.thalemine.web.utils;

import org.thalemine.web.query.Service;
import javax.servlet.http.HttpServletRequest;

public class QueryServiceLocator {

	private static CacheService cache;

	   static {
	      cache = new CacheService();		
	   }

	   public static Service getService(String serviceName, HttpServletRequest request){

		   
		  Service service = cache.getService(serviceName);

	      if(service != null){
	         return service;
	      }
	      

	      InitialServiceContext context = new InitialServiceContext();
	      
	      Service service1 = (Service) context.lookup(serviceName, request);
	      
	      cache.addService(service1);
	      return service1;
	   }
	
}
