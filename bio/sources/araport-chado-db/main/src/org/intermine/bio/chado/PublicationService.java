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

public class PublicationService {

	private static Map<String, ItemHolder> publicationMap = new HashMap<String, ItemHolder>();

	private static MultiMap publicationStockItemSet = new MultiValueMap();
	private static MultiMap publicationPhenotypeItemSet = new MultiValueMap();

	private PublicationService() {

	}

	private static class PublicationServiceHolder {

		public static final PublicationService INSTANCE = new PublicationService();

	}

	public static PublicationService getInstance() {

		return PublicationServiceHolder.INSTANCE;
	}

	public static void addPublicationItem(String name, ItemHolder item) {

		publicationMap.put(name, item);

	}

	public static ItemHolder getPublicationItem(String name) {

		ItemHolder itemHolder = null;

		if (publicationMap.containsKey(name)) {
			itemHolder = publicationMap.get(name);
		}

		return itemHolder;

	}

	public static Map<String, ItemHolder> getPublicationMap() {

		return publicationMap;

	}

	public static void addPublicationStockItem(String stockName, Item item) {

		publicationStockItemSet.put(stockName, item);
	}

	public static MultiMap getPublicationStockItemSet() {
		return publicationStockItemSet;
	}

	public static void addPublicationPhenotypeItem(String phenotypeName, Item item) {

		publicationPhenotypeItemSet.put(phenotypeName, item);
	}

	public static MultiMap getPublicationPhenotypeItemSet() {
		return publicationPhenotypeItemSet;
	}

}
