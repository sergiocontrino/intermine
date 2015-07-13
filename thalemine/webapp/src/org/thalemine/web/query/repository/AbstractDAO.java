package org.thalemine.web.query.repository;

import org.apache.log4j.Logger;
import org.thalemine.web.query.repository.AbstractRepository;
import org.thalemine.web.service.core.IRepositoryManager;

public class AbstractDAO{
	
	protected static final Logger log = Logger.getLogger(AbstractDAO.class);
	
	private IRepositoryManager repository;
	
	public IRepositoryManager getRepository() {
		return repository;
	}

	public void setRepository(IRepositoryManager repository){
		this.repository = repository;
	}

}
