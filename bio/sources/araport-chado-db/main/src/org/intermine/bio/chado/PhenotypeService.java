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

public class PhenotypeService {

	private static Map<String, ItemHolder> phenotypeMap = new HashMap<String, ItemHolder>();

	private PhenotypeService() {

	}

	private static class GenotypeServiceHolder {

		public static final PhenotypeService INSTANCE = new PhenotypeService();

	}

	public static PhenotypeService getInstance() {

		return GenotypeServiceHolder.INSTANCE;
	}

	public static void addPhenotypeItem(String name, ItemHolder item) {

		phenotypeMap.put(name, item);

	}

	public static ItemHolder getPhenotypeItem(String name) {

		ItemHolder itemHolder = null;

		if (phenotypeMap.containsKey(name)) {
			itemHolder = phenotypeMap.get(name);
		}

		return itemHolder;

	}

	public static Map<String, ItemHolder> getPhenotypeMap() {

		return phenotypeMap;

	}

}
