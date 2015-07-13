package org.thalemine.web.query.repository;

import java.util.Iterator;
import java.util.List;

import org.thalemine.web.service.Verifiable;

public interface QueryResult extends Verifiable{
		
	Iterator<List<Object>> getResultItems() throws Exception;

}
