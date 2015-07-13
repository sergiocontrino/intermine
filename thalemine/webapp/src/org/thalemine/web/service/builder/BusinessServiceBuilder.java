package org.thalemine.web.service.builder;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.intermine.api.results.ResultElement;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.ValueObject;
import org.thalemine.web.query.repository.AbstractRepository;
import org.thalemine.web.query.repository.AlleleDAO;
import org.thalemine.web.query.repository.GeneralDAO;
import org.thalemine.web.query.repository.GenotypeDAO;
import org.thalemine.web.query.repository.PhenotypeDAO;
import org.thalemine.web.query.repository.PublicationDAO;
import org.thalemine.web.query.repository.QueryRepository;
import org.thalemine.web.query.repository.StockDAO;
import org.thalemine.web.service.BusinessService;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.core.IComponentManager;
import org.thalemine.web.service.core.IRepositoryManager;
import org.thalemine.web.service.impl.AbstractService;
import org.thalemine.web.service.impl.StockServiceImpl;

public class BusinessServiceBuilder{
	
	protected static final Logger log = Logger.getLogger(BusinessServiceBuilder.class);

	public BusinessServiceBuilder() {
	
	}
			
	public BusinessService build(BusinessService businessService, StockDAO stockDAO, PublicationDAO publicationDAO, GenotypeDAO genotypeDAO, PhenotypeDAO phenotypeDAO, AlleleDAO alleleDAO) throws Exception {
	
		if (stockDAO!=null){
			businessService.setDAO(stockDAO);
		}
		
		if (publicationDAO!=null){
			businessService.setDAO(publicationDAO);
		}
		
		if (genotypeDAO!=null){
			businessService.setDAO(genotypeDAO);
		}
		
		if (phenotypeDAO!=null){
			businessService.setDAO(phenotypeDAO);
		}
		
		if (alleleDAO!=null){
			businessService.setDAO(alleleDAO);
		}
		
		return businessService;
		
		
	}
	
}
