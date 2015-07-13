package org.thalemine.web.service.core;

import java.util.HashMap;
import org.thalemine.web.service.Initializable;

public class ComponentRegistry implements Initializable {

	private final HashMap<String, String> componentRegistry = new HashMap<String, String>();
	
	public ComponentRegistry(){
		
	}
	
	@Override
	public void initialize() throws Exception {
		
		componentRegistry.clear();
		componentRegistry.put(ComponentConfig.STOCK_DAO, ComponentConfig.STOCK_DAO);
		componentRegistry.put(ComponentConfig.PUBLICATION_DAO, ComponentConfig.PUBLICATION_DAO);
		componentRegistry.put(ComponentConfig.GENOTYPE_DAO, ComponentConfig.GENOTYPE_DAO);
		componentRegistry.put(ComponentConfig.ALLELE_DAO, ComponentConfig.ALLELE_DAO);
		componentRegistry.put(ComponentConfig.PHENOTYPE_DAO, ComponentConfig.PHENOTYPE_DAO);
		
	}
	
	public HashMap<String, String> getComponentRegistry(){
		
		return componentRegistry;
		
	}
	
	public String getEntry(String key){
		
		String result = null;
				
		if (componentRegistry!=null && componentRegistry.size() > 0){
			if (componentRegistry.containsKey(key)){
				result = componentRegistry.get(key);
			}
		}
		
		return result;
		
	}

}
