package org.thalemine.web.query;

import org.apache.log4j.Logger;
import org.intermine.web.logic.session.SessionMethods;
import org.intermine.web.util.URLGenerator;
import org.thalemine.web.domain.PhenotypeVO;
import org.thalemine.web.domain.PublicationVO;
import org.thalemine.web.domain.StockVO;
import org.thalemine.web.domain.StrainVO;
import org.thalemine.web.domain.GeneModelVO;
import org.thalemine.web.domain.GeneVO;
import org.thalemine.web.utils.QueryServiceLocator;
import org.thalemine.web.utils.WebApplicationContextLocator;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.repeat;

import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.PathQuery;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.metadata.Model;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.core.ServiceFactory;
import org.intermine.webservice.client.services.QueryService;
import org.intermine.model.bio.Gene;
import org.intermine.model.bio.Allele;
import org.intermine.model.InterMineObject;

public class AlleleQueryService implements Service {

	protected static final Logger log = Logger.getLogger(AlleleQueryService.class);
	
	private static String className = "Allele";
	private static String SERVICE_URL;
	private static ServiceFactory factory;
	
	private AlleleQueryService() {

	}

	private static class AlleleQueryServiceHolder {

		public static final AlleleQueryService INSTANCE = new AlleleQueryService();

	}

	public static AlleleQueryService getInstance(HttpServletRequest request) {

		SERVICE_URL = WebApplicationContextLocator.getServiceUrl(request);
		factory = new ServiceFactory(SERVICE_URL);

		return AlleleQueryServiceHolder.INSTANCE;
	}
	
	@Override
	public String getClassName() {
		return className;
	}

	public String getServiceUrl() {
		return SERVICE_URL;
	}

	@Override
	public Iterator<List<Object>> getResultSet(PathQuery query) {

		QueryService service = factory.getQueryService();
		return service.getRowListIterator(query);

	}
	
	public List<GeneModelVO> getGeneModels(Allele item, String objectClassName) throws Exception{
		
		Map<String,GeneVO> geneList = new HashMap<String, GeneVO>();
		
		ArrayList<GeneModelVO> resultList = new ArrayList<GeneModelVO>();

		PathQuery query = null;
		
		log.info("Allele Source Item:" + item);
		
		query = getGeneModelsByAlleleQuery(item);
		
		QueryService service = factory.getQueryService();
		Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);
		
		while (resultSetIterator.hasNext()) {

			List<Object> currentItem = resultSetIterator.next();
			
			log.info("Gene Model Item:" + currentItem);	
			
			GeneModelVO resultItem = new GeneModelVO(currentItem);
			
			resultList.add(resultItem);
			
			log.info("Gene Model Result Item:" + resultItem);		
			
			
		}
		
		return resultList;
		
	}
	
	private PathQuery getGeneModelsByAlleleQuery(Allele item) {

		Model model = factory.getModel();
		PathQuery query = new PathQuery(model);

		query.addViews(
				"Allele.affectedGenes.id",
				"Allele.affectedGenes.transcripts.id",
				"Allele.primaryIdentifier",
				"Allele.affectedGenes.primaryIdentifier",
				"Allele.affectedGenes.transcripts.primaryIdentifier",
				 "Allele.affectedGenes.transcripts.curatorSummary"
				);
		
		// Outer Joins
				// Show all information about these relationships if they exist, but do
				// not require that they exist.
		query.setOuterJoinStatus("Allele.affectedGenes.transcripts", OuterJoinStatus.OUTER);

		
		query.addConstraint(Constraints.eq("Allele.id", item.getId().toString()));
		return query;

	}

}
