package org.thalemine.web.context;

import java.util.Date;

public interface ApplicationContext {
	
	/**
	 * Return the timestamp when this context was first loaded.
	 * @return the timestamp (ms) when this context was first loaded
	 */
	Date getStartupDate();

}
