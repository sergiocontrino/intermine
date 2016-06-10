package org.thalemine.web.query.repository;

import org.intermine.pathquery.PathQuery;

public interface QueryRepository {
	
	public void setConstraint(PathQuery query, Object item) throws Exception;

}
