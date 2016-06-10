package org.thalemine.web.service.builder;

import org.apache.log4j.Logger;
import org.thalemine.web.query.repository.AbstractDAO;
import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.core.IRepositoryManager;

public class DAOBuilder {
	
	protected static final Logger log = Logger.getLogger(DAOBuilder.class);

	public DAOBuilder() {
	}
			
	public GeneralDAO build(GeneralDAO dao, IRepositoryManager repository) throws Exception {

		dao.setRepository(repository);
		
		return dao;
		
		
	}
	
}
