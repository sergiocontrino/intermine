package org.thalemine.web.query.repository;

import org.intermine.pathquery.PathQuery;
import org.thalemine.web.injection.RepositorySetter;
import org.thalemine.web.service.Verifiable;

public interface GenotypeDAO extends GeneralDAO{

	public QueryResult getGenotype(Object item) throws Exception;
	
}
