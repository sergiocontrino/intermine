package org.thalemine.web.injection;

import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.query.repository.StockDAO;

public interface StockDAOSetter {

	public void setDAO(StockDAO dao);
	
}
