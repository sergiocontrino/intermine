package org.thalemine.web.injection;

import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.query.repository.PhenotypeDAO;
import org.thalemine.web.query.repository.StockDAO;

public interface PhenotypeDAOSetter {

	public void setDAO(PhenotypeDAO dao);
	
}
