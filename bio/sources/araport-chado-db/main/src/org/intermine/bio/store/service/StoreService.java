package org.intermine.bio.store.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.intermine.bio.chado.CVService;
import org.intermine.bio.dataconversion.ChadoDBConverter;
import org.intermine.bio.dataflow.config.AppLauncher;
import org.intermine.bio.dataflow.config.DataFlowConfig;
import org.intermine.bio.item.util.ItemHolder;
import org.intermine.bio.reader.CVTermReader;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.xml.full.Item;
import org.intermine.xml.full.ReferenceList;

public class StoreService {

	protected static final Logger log = Logger.getLogger(StoreService.class);

	private static ChadoDBConverter service;

	private static class StoreServiceHolder {

		public static final StoreService INSTANCE = new StoreService();

	}

	public static StoreService getInstance(ChadoDBConverter chadoDBConverter) {
		service = chadoDBConverter;
		return StoreServiceHolder.INSTANCE;
	}

	public static ChadoDBConverter getService() {
		return service;
	}

	public static void storeCollection(Collection<Item> collection, ItemHolder itemHolder, final String collectionName) throws ObjectStoreException {

		Integer itemId = itemHolder.getItemId();

		ReferenceList refs = new ReferenceList();

		refs.setName(collectionName);
		List<Item> terms = new ArrayList<Item>(collection);

		for (Item term : terms) {

			Item item = (Item) term;
			refs.addRefId(item.getIdentifier());
		}

		service.store(refs, itemId);
	}

	public static void initialize(ChadoDBConverter chadoDBConverter) {

		log.info("Initializing Store Service has started...");

		service = chadoDBConverter;

		log.info("Initialization of Store Service has completed.");

	}

	public static boolean storeItem(Item item) {

		Exception exception = null;

		boolean result = true;

		try {
			StoreService.getService().store(item);
		} catch (ObjectStoreException e) {
			exception = e;
		} finally {
			if (exception != null) {
				result = false;
				log.error("Error occured during Item Store:" + exception.getCause());
				exception.printStackTrace();
			} 
		}

		return result;
	}
}
