package org.thalemine.web.query;

import java.util.Iterator;
import java.util.List;
import org.intermine.pathquery.PathQuery;

public interface Service {

	String getClassName();
	Iterator<List<Object>> getResultSet(PathQuery query);
}
