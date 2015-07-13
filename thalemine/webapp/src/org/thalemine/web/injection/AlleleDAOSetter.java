package org.thalemine.web.injection;

import org.thalemine.web.query.repository.AlleleDAO;
import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.query.repository.StockDAO;

public interface AlleleDAOSetter {

	public void setDAO(AlleleDAO dao);
	
}
