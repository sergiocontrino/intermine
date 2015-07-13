package org.thalemine.web.service.core;

import java.util.HashMap;
import org.thalemine.web.service.Initializable;

public class ServiceRegistry implements Initializable {

	private final HashMap<String, String> serviceRegistry = new HashMap<String, String>();
	
	public ServiceRegistry(){
		
	}
	
	@Override
	public void initialize() throws Exception {
		
		serviceRegistry.clear();
		serviceRegistry.put(ServiceConfig.STOCK_SERVICE, ServiceConfig.STOCK_SERVICE);
		serviceRegistry.put(ServiceConfig.ALLELE_SERVICE, ServiceConfig.ALLELE_SERVICE);
		serviceRegistry.put(ServiceConfig.PUBLICATION_SERVICE, ServiceConfig.PUBLICATION_SERVICE);
		serviceRegistry.put(ServiceConfig.PHENOTYPE_SERVICE, ServiceConfig.PHENOTYPE_SERVICE);
		serviceRegistry.put(ServiceConfig.GENOTYPE_SERVICE, ServiceConfig.GENOTYPE_SERVICE);
		
	}
	
	public HashMap<String, String> getServiceRegistry(){
		
		return serviceRegistry;
		
	}
	
	public String getEntry(String key){
		
		String result = null;
				
		if (serviceRegistry!=null && serviceRegistry.size() > 0){
			if (serviceRegistry.containsKey(key)){
				result = serviceRegistry.get(key);
			}
		}
		
		return result;
		
	}

}
