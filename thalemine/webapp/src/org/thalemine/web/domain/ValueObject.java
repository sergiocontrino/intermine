package org.thalemine.web.domain;

import java.util.List;

import org.apache.log4j.Logger;
import org.intermine.api.results.ResultElement;

public class ValueObject<E> extends DomainVO{
	
	protected static final Logger log = Logger.getLogger(ValueObject.class);
	
	protected String createElement(List<ResultElement> list, int index) {

		String element = ((list.get(index) != null) && (list.get(index) != null)) ? list.get(index).toString()
				: "&nbsp;";

		if (element!=null && ((element.equalsIgnoreCase("unknown") || (element.equalsIgnoreCase("null"))))){
			element = "&nbsp;";
		}
		
		if (element==null){
			element = "&nbsp;";
		}
		
		log.info("Create element:" + element);
		
		return element;

	}

}
