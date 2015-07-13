package org.thalemine.web.query.repository.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.pathquery.PathQuery;
import org.thalemine.web.query.repository.QueryResult;
import org.thalemine.web.service.builder.BusinessServiceBuilder;
import org.thalemine.web.service.core.IRepositoryManager;

public class QueryResultImpl implements QueryResult {

protected static final Logger log = Logger.getLogger(QueryResultImpl.class);
private final PathQuery query;
private final IRepositoryManager repository;

	public QueryResultImpl(IRepositoryManager repository, PathQuery query) {
		this.repository = repository;
		this.query = query;
	}

	@Override
	public Iterator<List<Object>> getResultItems() throws Exception {

		Iterator<List<Object>> resultItems = null;
		
		validateState();
		resultItems = repository.getFactory().getQueryService().getRowListIterator(this.query);
			
		return resultItems;
	}

	@Override
	public void validateState() throws Exception {
		
		if (this.repository == null){
			
			throw new IllegalStateException("Repository must not be null!");
		}
		
		if (this.repository.getFactory() == null){
			
			throw new IllegalStateException("Repository Service Factory must not be null!");
		}
		
		if (StringUtils.isBlank(this.repository.getSystemService().getServiceEndPoint())) {
			throw new IllegalStateException("Repository Service Endpoint must not be null!");
		}
		
	}

}
