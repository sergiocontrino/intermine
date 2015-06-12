package org.thalemine.web.query;

import org.apache.log4j.Logger;
import org.intermine.web.logic.session.SessionMethods;
import org.intermine.web.util.URLGenerator;
import org.thalemine.web.domain.PhenotypeVO;
import org.thalemine.web.domain.PublicationVO;
import org.thalemine.web.domain.StockVO;
import org.thalemine.web.domain.StrainVO;
import org.thalemine.web.utils.QueryServiceLocator;
import org.thalemine.web.utils.WebApplicationContextLocator;

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

public class StockQueryService implements Service {
	
	protected static final Logger log = Logger.getLogger(StockQueryService.class);
	
	private static String className = "Stock";
	private static String SERVICE_URL;
	private static final String PUBLICATION_SERVICE = "PublicationQueryService";
	private static ServiceFactory factory;
	
	private static PublicationQueryService publicationService;
	
	public PublicationQueryService getPublicationService() {
		return publicationService;
	}

	public void setPublicationService(PublicationQueryService publicationService) {
		this.publicationService = publicationService;
	}

	private StockQueryService() {

	}

	private static class StockQueryServiceHolder {

		public static final StockQueryService INSTANCE = new StockQueryService();

	}

	public static StockQueryService getInstance(HttpServletRequest request) {
		
		SERVICE_URL = WebApplicationContextLocator.getServiceUrl(request);
		factory = new ServiceFactory(SERVICE_URL);
		
		publicationService = (PublicationQueryService) QueryServiceLocator.getService(PUBLICATION_SERVICE, request);
		
		return StockQueryServiceHolder.INSTANCE;
	}

	@Override
	public String getClassName() {
		return className;
	}
	
	private PathQuery getStocksByGeneQuery(Gene item){
		
		Model model = factory.getModel();
        PathQuery query = new PathQuery(model);
		 
	       query.addViews(
	    		   	"Gene.affectedAlleles.genotypes.id",
	                "Gene.affectedAlleles.genotypes.displayName",
	                "Gene.affectedAlleles.genotypes.name",
	                "Gene.affectedAlleles.genotypes.primaryIdentifier",
	                "Gene.affectedAlleles.genotypes.stocks.id",
	                "Gene.affectedAlleles.genotypes.stocks.germplasmName",
	                "Gene.affectedAlleles.genotypes.stocks.primaryIdentifier",
	                "Gene.affectedAlleles.genotypes.stocks.stockName",
	                "Gene.affectedAlleles.genotypes.stocks.primaryAccession",
	                "Gene.affectedAlleles.genotypes.stocks.stockAccession",
	                "Gene.affectedAlleles.genotypes.stocks.accession.id",
	                "Gene.affectedAlleles.genotypes.stocks.accession.abbreviationName"
	                );
	       
	       
	       // Add orderby
	   	    query.addOrderBy("Gene.affectedAlleles.genotypes.stocks.germplasmName", OrderDirection.ASC);
	   	    
	   	// Outer Joins
	        // Show all information about these relationships if they exist, but do not require that they exist.
	        query.setOuterJoinStatus("Gene.affectedAlleles.genotypes.stocks.accession", OuterJoinStatus.OUTER);
	        
		    query.addConstraint(Constraints.eq("Gene.id",item.getId().toString()));
		    return query;
	   	    
	}
	
	public String getServiceUrl(){
		return SERVICE_URL;
	}
	
	 public Iterator<List<Object>> getResultSet(PathQuery query){
		 
		 QueryService service = factory.getQueryService();
		 return service.getRowListIterator(query);
		 
	 }
	 
	 public List<StockVO> getStocks(Gene item){
		 
		 ArrayList<StockVO> resultList = new ArrayList<StockVO>();
		 PathQuery query = getStocksByGeneQuery(item);
		 
		 QueryService service = factory.getQueryService();
		 
		 Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);
		 
		 while (resultSetIterator.hasNext()) {
			 
			 List<Object> currentItem = resultSetIterator.next();
			 
			 StockVO resultItem = new StockVO(currentItem );
			 
			 //Stock Background Accessions
			 List<StrainVO> bgAccessions = new ArrayList<StrainVO>();
			 bgAccessions = getBackgroundAccessions(resultItem);
			 resultItem.setBackgrounds(bgAccessions);
			 
			 //Phenotypes
			 List<PhenotypeVO> phenotypes = new ArrayList<PhenotypeVO>();
			 phenotypes = getPhenotypes(item, resultItem);
			 resultItem.setPhenotypes(phenotypes);
				
			 resultList.add(resultItem);
				 
			 log.info("Stock Result Item:" + resultItem);
	           
	     }
		 
		 return resultList;
			 
	 }
		 
	 private QueryService getQueryService(){
		 return factory.getQueryService();
	 }
	
	 private PathQuery getBackGroundAccessionsQuery(String itemId){
			
		 Model model = factory.getModel();
	     PathQuery query = new PathQuery(model);	
			 
	       query.addViews(
	    		    "Stock.backgroundAccessions.id",
	    		    "Stock.backgroundAccessions.abbreviationName"
	                );
	       
	        
		    query.addConstraint(Constraints.eq("Stock.id",itemId));
	
		    return query;
	   	    
	}
	 
	 
	 public List<StrainVO> getBackgroundAccessions(StockVO item){
			List<StrainVO> result = new ArrayList<StrainVO>();
			
			PathQuery query = getBackGroundAccessionsQuery(item.getStockObjectId());
		
			QueryService service = factory.getQueryService();
			 
			 Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);
			 
			 while (resultSetIterator.hasNext()) {
				 
				 List<Object> currentItem = resultSetIterator.next();
				 
				 StrainVO resultItem = new StrainVO(currentItem );
				 
				 result.add(resultItem);
					 
				 log.info("Background Accession Result Item:" + resultItem);
		           
		     }
			 
			 return result;
		}
	 
	 private PathQuery getPhenotypesByGeneStockQuery(Gene geneItem, StockVO stockItem){
		 
		 Model model = factory.getModel();
		 PathQuery query = new PathQuery(model);
		 
		 query.addViews(
				 	"Gene.affectedAlleles.genotypes.phenotypesObserved.id",
	                "Gene.affectedAlleles.genotypes.phenotypesObserved.description",
				 	"Gene.affectedAlleles.primaryIdentifier",
	                "Gene.affectedAlleles.genotypes.name",
	                "Gene.affectedAlleles.genotypes.phenotypesObserved.primaryIdentifier",
	                "Gene.affectedAlleles.genotypes.stocks.primaryIdentifier",
	                "Gene.primaryIdentifier");

		   // Add orderby
	        query.addOrderBy("Gene.affectedAlleles.genotypes.phenotypesObserved.primaryIdentifier", OrderDirection.ASC);
	        
	        // Filter the results with the following constraints:
	        query.addConstraint(Constraints.eq("Gene.id", geneItem.getId().toString()), "A");
	        query.addConstraint(Constraints.eq("Gene.affectedAlleles.genotypes.stocks.id", stockItem.getStockObjectId()), "B");
	        // Specify how these constraints should be combined.
	        query.setConstraintLogic("A and B");
	        
		    return query;
		 
	 }
	 
	 public List<PhenotypeVO> getPhenotypes(Gene geneItem, StockVO stockItem){
		 
		 List<PhenotypeVO> result = new ArrayList<PhenotypeVO>();
		 
		 PathQuery query = getPhenotypesByGeneStockQuery(geneItem, stockItem);
			
		 QueryService service = factory.getQueryService();
			 
		 Iterator<List<Object>> resultSetIterator = service.getRowListIterator(query);
		 
		 while (resultSetIterator.hasNext()) {
			 
			 List<Object> currentItem = resultSetIterator.next();
			 
			 PhenotypeVO resultItem = new PhenotypeVO(currentItem );
			 
			 if (resultItem.getObjectId()!=null && publicationService!=null){
				 
				 List<PublicationVO> publications = new ArrayList<PublicationVO>();
				 
				 publications = publicationService.getPhenotypePublications(resultItem);
				 resultItem.setPublications(publications);
				 log.info("Phenotype:" + resultItem + ";Publication Size:" + publications.size());		 
								 
			 }
			 
			 result.add(resultItem);
				 
			 log.info("Phenotype Result Item:" + resultItem);
	           
	     }
			 
		 
		 return result;
		 
	 }
}


