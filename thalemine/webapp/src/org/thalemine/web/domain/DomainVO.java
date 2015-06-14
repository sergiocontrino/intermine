package org.thalemine.web.domain;

import java.util.List;

public abstract class DomainVO {

	protected String getElement(List<Object> list, int index) {

		String element = ((list.get(index) != null) && (list.get(index) != null)) ? list.get(index).toString()
				: "&nbsp;";

		if (element==null){
			element = "&nbsp;";
		}
		return element;

	}
	
}
