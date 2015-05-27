package org.intermine.bio.chado;


import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.intermine.xml.full.Item;
import org.intermine.bio.item.util.ItemHolder;

public class AlleleService {

	private static Map<String, ItemHolder> alleleMap = new HashMap<String, ItemHolder>();
	private static MultiMap alleleItemSet = new MultiValueMap();
	private static MultiKeyMap genotypeAlleleItemMap = new MultiKeyMap();

		
	private AlleleService() {

	}

	private static class AlleleServiceHolder {

	public static final AlleleService INSTANCE = new AlleleService();
		
	}

	public static AlleleService getInstance() {

		return AlleleServiceHolder.INSTANCE;
	}

	public static void addAleleItem(String name, ItemHolder item) {

		alleleMap.put(name, item);
			
	}

	public static void getAleleItem(String name) {

		alleleMap.get(name);

	}
		
	public static Map<String, ItemHolder> getAlleleMap() {

		return alleleMap;

	}
	
	public static void addGenotypeItem(String stockName, String genotypeName, Item item){
		
		genotypeAlleleItemMap.put(stockName, genotypeName, item);
		alleleItemSet.put(stockName,item);
	}
	
	 
	 public static MultiMap getAlleleItemSet(){
		 return alleleItemSet;
	 }
	

}
