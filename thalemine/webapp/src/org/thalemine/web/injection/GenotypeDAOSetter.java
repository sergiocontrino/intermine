package org.thalemine.web.injection;

import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.query.repository.GenotypeDAO;
import org.thalemine.web.query.repository.StockDAO;

public interface GenotypeDAOSetter {

	public void setDAO(GenotypeDAO dao);
	
}
