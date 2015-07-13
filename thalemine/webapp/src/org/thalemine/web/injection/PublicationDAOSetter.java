package org.thalemine.web.injection;

import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.query.repository.PublicationDAO;
import org.thalemine.web.query.repository.StockDAO;

public interface PublicationDAOSetter {

	public void setDAO(PublicationDAO dao);
	
}
