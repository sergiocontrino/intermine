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

public class StockService {

	private static Map<String, ItemHolder> stockMap = new HashMap<String, ItemHolder>();
	private static MultiMap bgAccessionItemSet = new MultiValueMap();
	private static MultiKeyMap bgAccessionStockItemMap = new MultiKeyMap();

		
	private StockService() {

	}

	private static class StockServiceHolder {

	public static final StockService INSTANCE = new StockService();
		
	}

	public static StockService getInstance() {

		return StockServiceHolder.INSTANCE;
	}

	public static void addStockItem(String name, ItemHolder item) {

		stockMap.put(name, item);
			
	}

	public static void getStockItem(String name) {

		stockMap.get(name);

	}
		
	public static Map<String, ItemHolder> getStockMap() {

		return stockMap;

	}
	
	public static void addBgAccessionItem(String stockName, String strainName, Item item){
		
		bgAccessionStockItemMap.put(stockName, strainName, item);
		bgAccessionItemSet.put(stockName,item);
	}
	
	 
	 public static MultiMap getBgAccessionItemSet(){
		 return bgAccessionItemSet;
	 }
	

}
