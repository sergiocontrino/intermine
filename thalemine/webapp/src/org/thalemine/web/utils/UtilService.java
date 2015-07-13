package org.thalemine.web.utils;

import org.apache.log4j.Logger;
import org.intermine.model.InterMineObject;
import org.intermine.pathquery.Constraints;

public final class UtilService {

	protected static final Logger log = Logger.getLogger(UtilService.class);

	public static boolean isInterMineObject(Object item) {
		boolean result = false;

		if (item instanceof InterMineObject) {
			result = true;
		}

		return result;
	}
	
	public static String getObjectIdentifier(Object item) throws Exception {
		
		String primaryIdentifier = null;
		
		if (item == null) {
			throw new Exception("Object cannot be null!");
		}
		
		if ((item!=null) && UtilService.isInterMineObject(item)){
			primaryIdentifier = ((InterMineObject) item).getId().toString();
			
		}else{
			primaryIdentifier = item.toString();
			
		}
		
		if (primaryIdentifier == null) {
			throw new Exception("Primary Identifier cannot be null!");
		}
		
		return primaryIdentifier;
		
	}

}
