package org.thalemine.web.query;

import org.apache.log4j.Logger;
import org.intermine.web.logic.session.SessionMethods;
import org.intermine.web.util.URLGenerator;
import org.thalemine.web.context.WebApplicationContextLocator;
import org.thalemine.web.domain.AuthorVO;
import org.thalemine.web.domain.PhenotypeVO;
import org.thalemine.web.domain.PublicationVO;
import org.thalemine.web.domain.StockVO;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class PublicationQueryService implements Service {
	
	protected static final Logger log = Logger.getLogger( PublicationQueryService .class);
	
	private static final String className = "Publication";
	private static String SERVICE_URL;
	private static ServiceFactory factory;
	
	private PublicationQueryService() {

	}

	private static class PublicationQueryServiceHolder {

		public static final PublicationQueryService INSTANCE = new PublicationQueryService();

	}

	public static PublicationQueryService getInstance(HttpServletRequest request) {
		
		SERVICE_URL = WebApplicationContextLocator.getServiceUrl(request);
		factory = new ServiceFactory(SERVICE_URL);
		
		return PublicationQueryServiceHolder.INSTANCE;
	}

	public void init(HttpServletRequest request){
		
		SERVICE_URL = WebApplicationContextLocator.getServiceUrl(request);
		factory = new ServiceFactory(SERVICE_URL);

		log.info("Service URL:" + SERVICE_URL);

	}
	
	@Override
	public String getClassName() {
		return className;
	}
	
	
	public List<PublicationVO> getPhenotypePublications(PhenotypeVO item){
		
		 ArrayList<PublicationVO> resultList = new ArrayList<PublicationVO>();
		 
		 PathQuery query = getPhenotypePublicationsQuery(item);
		 QueryService service = factory.getQueryService();
		 
		 Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);
		 
		 while (resultSetIterator.hasNext()) {
			 
			 List<Object> currentItem = resultSetIterator.next();
			 
			 PublicationVO resultItem = new PublicationVO(currentItem);
			 
			 List<AuthorVO> authors = getPublicationsAuthors(resultItem);
			 
			 if (authors.size() > 0){
				 resultItem.setAuthors(authors);
			 }
			 
			 resultList.add(resultItem);
			 
			 log.info("Publication Result Item:" + resultItem + ";Authors Size:" + authors.size());
			 
		 }
		 
		 return resultList;
	}
	
	
	private PathQuery getPhenotypePublicationsQuery(PhenotypeVO item){
		
		Model model = factory.getModel();
        PathQuery query = new PathQuery(model);
        
        query.addViews(
    		    "Phenotype.publications.id",
                "Phenotype.publications.pubMedId",
                "Phenotype.publications.title",
                "Phenotype.publications.year",
                "Phenotype.publications.firstAuthor"
                );
       
       
       // Add orderby
   	    query.addOrderBy("Phenotype.publications.title", OrderDirection.ASC);
   	         
	    query.addConstraint(Constraints.eq("Phenotype.id",item.getObjectId()));
	    return query;
}

		private List<AuthorVO> getPublicationsAuthors(PublicationVO item){
			
			ArrayList<AuthorVO> resultList = new ArrayList<AuthorVO>();
			
			PathQuery query = getPublicationsAuthorsQuery(item);
			QueryService service = factory.getQueryService();
			 
			Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);
			 
			 while (resultSetIterator.hasNext()) {
				 
				 List<Object> currentItem = resultSetIterator.next();
				 
				 AuthorVO resultItem = new AuthorVO(currentItem);
				 
				 resultList.add(resultItem);
				 
				 log.info("Author Result Item:" + resultItem);
				 
			 }
					
			return resultList;
		
		}
		
private PathQuery getPublicationsAuthorsQuery(PublicationVO item){
		
		Model model = factory.getModel();
        PathQuery query = new PathQuery(model);
        
        // Select the output columns:
        query.addViews("Publication.authors.id",
                "Publication.authors.name");

        // Add orderby
        query.addOrderBy("Publication.authors.name", OrderDirection.ASC);
         	         
	    query.addConstraint(Constraints.eq("Publication.id",item.getObjectId()));
	    return query;
}
	
	@Override
	public Iterator<List<Object>> getResultSet(PathQuery query) {
		QueryService service = factory.getQueryService();
		 return service.getRowListIterator(query);
	}
	
}
