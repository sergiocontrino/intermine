package org.thalemine.web.domain;

import org.apache.log4j.Logger;import java.util.List;


public abstract class DomainVO {
	
	protected static final Logger log = Logger.getLogger(DomainVO.class);

	protected String getElement(List<Object> list, int index) {

		String element = ((list.get(index) != null) && (list.get(index) != null)) ? list.get(index).toString()
				: "&nbsp;";

		if (element!=null && ((element.equalsIgnoreCase("unknown") || (element.equalsIgnoreCase("null"))))){
			element = "&nbsp;";
		}
		
		if (element==null){
			element = "&nbsp;";
		}
		return element;

	}
	
	protected String formatValue(String type, String value){
		String result = value;
	
		log.info("Value:" + value);
		
		if (value!=null && !value.isEmpty()){
			
			log.info("Working on Value:" + type + ";" + value);
			
			if (type.equals("sequenceAlterationType") && value.equalsIgnoreCase("T-dna Insertion")){
				result = "insertion (T-DNA Insertion)";  
			}
			
			if (type.equals("mutagen") && value.equalsIgnoreCase("T Dna Insertion")){
				result = "T-DNA Insertion";  
			}
			
			if (value.equalsIgnoreCase("UNKNOWN")){
				result = "&nbsp;";
			}
		
			
			if (value.equalsIgnoreCase("null")){
				result = "&nbsp;";
			}
			
			
		}
		
		if (value.isEmpty()){
			result = "&nbsp;";
		}
		
		
		log.info("Formatted Value:" +result);
		
		return result;
	}
	
}
