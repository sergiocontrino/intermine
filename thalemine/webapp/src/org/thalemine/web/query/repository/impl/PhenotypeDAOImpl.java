package org.thalemine.web.query.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.metadata.Model;
import org.intermine.model.InterMineObject;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.PathQuery;
import org.thalemine.web.domain.StockGenotypeVO;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.webservice.client.core.ServiceFactory;
import org.intermine.webservice.client.services.QueryService;
import org.thalemine.web.query.StockQueryService;
import org.thalemine.web.query.repository.AbstractDAO;
import org.thalemine.web.query.repository.AbstractRepository;
import org.thalemine.web.query.repository.PhenotypeDAO;
import org.thalemine.web.query.repository.PublicationDAO;
import org.thalemine.web.query.repository.QueryRepository;
import org.thalemine.web.query.repository.QueryResult;
import org.thalemine.web.query.repository.StockDAO;
import org.thalemine.web.service.Verifiable;
import org.thalemine.web.service.core.IRepositoryManager;
import org.thalemine.web.utils.UtilService;

public class PhenotypeDAOImpl implements QueryRepository, PhenotypeDAO, Verifiable {

	private static final String PRIMARY_IDENTIFIER_CONSTRAINT = "Stock.primaryIdentifier";
	private static final String OBJECT_IDENTIFIER_CONSTRAINT = "Stock.id";

	private IRepositoryManager repository;

	protected static final Logger log = Logger.getLogger(PhenotypeDAOImpl.class);
	
	public PhenotypeDAOImpl(){
		super();
	}
	

	@Override
	public QueryResult getGenotype(Object item) throws Exception {

		Exception exception = null;
		PathQuery query = null;
		QueryResult queryResult = null;

		validateState();

		try {
			query = getGenotypeQuery(item);
			queryResult = new QueryResultImpl(this.repository, query);
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {

				log.error("Error occured while retrieving result set for query:" + "; Query:" + query);
				log.error("Error:" + exception.getMessage() + ";Cause:" + exception.getCause());
				exception.printStackTrace();

			} else {
				log.info("Successfully retrieved resultset for query." + "; Query:" + query);
			}
		}

		return queryResult;
	}

	private PathQuery getGenotypeQuery(Object item) throws Exception {

		PathQuery query = new PathQuery(getModel());
		query.addViews("Stock.id", "Stock.primaryIdentifier", "Stock.genotypes.id", "Stock.genotypes.name");

		setConstraint(query, item);

		return query;
	}

	public void setConstraint(PathQuery query, Object item) throws Exception {

		String primaryIdentifier = UtilService.getObjectIdentifier(item);

		if ((item != null) && UtilService.isInterMineObject(item)) {
			query.addConstraint(Constraints.eq(OBJECT_IDENTIFIER_CONSTRAINT, primaryIdentifier));
		} else {
			query.addConstraint(Constraints.eq(PRIMARY_IDENTIFIER_CONSTRAINT, primaryIdentifier));
		}

	}

	@Override
	public void setRepository(IRepositoryManager repository) {
		this.repository = repository;
	}

	private Model getModel() {
		return repository.getFactory().getModel();
	}

	@Override
	public void validateState() throws Exception {

		if (this.repository == null) {

			throw new IllegalStateException("Repository must not be null!");
		}

		if (this.repository.getFactory() == null) {

			throw new IllegalStateException("Repository Service Factory must not be null!");
		}

		if (StringUtils.isBlank(this.repository.getSystemService().getServiceEndPoint())) {
			throw new IllegalStateException("Repository Service Endpoint must not be null!");
		}

	}

}
