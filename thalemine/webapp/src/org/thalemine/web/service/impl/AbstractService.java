package org.thalemine.web.service.impl;

import org.apache.log4j.Logger;
import org.thalemine.web.injection.CompositeDAOSetter;
import org.thalemine.web.query.repository.AbstractRepository;
import org.thalemine.web.query.repository.AlleleDAO;
import org.thalemine.web.query.repository.GenotypeDAO;
import org.thalemine.web.query.repository.PhenotypeDAO;
import org.thalemine.web.query.repository.PublicationDAO;
import org.thalemine.web.query.repository.StockDAO;
import org.thalemine.web.service.BusinessService;
import org.thalemine.web.service.core.IRepositoryManager;

public class AbstractService implements BusinessService {
	
	protected static final Logger log = Logger.getLogger(AbstractService.class);
	
	protected StockDAO stockDao;
	protected AlleleDAO alleleDao;
	protected PublicationDAO publicationDao;
	protected GenotypeDAO genotypeDao;
	protected PhenotypeDAO phenotypeDao;
	
	private IRepositoryManager repository;
	
	public IRepositoryManager getRepository() {
		return repository;
	}

	public void setRepository(IRepositoryManager repository){
		this.repository = repository;
	}

	@Override
	public void setDAO(StockDAO dao) {
		this.stockDao = dao;
	}

	@Override
	public void setDAO(PublicationDAO dao) {
		this.publicationDao = dao;
	}

	@Override
	public void setDAO(GenotypeDAO dao) {
		this.genotypeDao = dao;
	}

	@Override
	public void setDAO(AlleleDAO dao) {
		this.alleleDao = dao;
	}

	@Override
	public void setDAO(PhenotypeDAO dao) {
		this.phenotypeDao = dao;
	}

	@Override
	public void validateState() throws Exception {
				
	}

}
