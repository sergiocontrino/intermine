package org.thalemine.web.displayer;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.api.InterMineAPI;
import org.intermine.api.profile.Profile;
import org.intermine.api.query.PathQueryExecutor;
import org.intermine.api.results.ExportResultsIterator;
import org.intermine.api.results.ResultElement;
import org.intermine.bio.web.displayer.GeneSNPDisplayer.GenoSample;
import org.intermine.bio.web.displayer.GeneSNPDisplayer.SNPList;
import org.intermine.model.InterMineObject;
import org.intermine.model.bio.Gene;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.PathQuery;
import org.intermine.util.DynamicUtil;
import org.intermine.web.displayer.ReportDisplayer;
import org.intermine.web.logic.config.ReportDisplayerConfig;
import org.intermine.web.logic.results.ReportObject;
import org.intermine.web.logic.session.SessionMethods;
import org.thalemine.web.builder.AlleleVOBuilder;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.query.StockQueryService;
import org.thalemine.web.utils.QueryServiceLocator;
import org.thalemine.web.utils.WebApplicationContextLocator;
import org.intermine.pathquery.OuterJoinStatus;


public class GeneAlleleDisplayer extends ReportDisplayer {
	
	private static final String STOCK_SERVICE = "StockQueryService";
	protected static final Logger LOG = Logger.getLogger(GeneAlleleDisplayer.class);
	PathQueryExecutor exec;
	
	public GeneAlleleDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
	      super(config, im);
	}

	@Override
	  @SuppressWarnings("unchecked")
	  public void display(HttpServletRequest request, ReportObject reportObject) {
		
		HttpSession session = request.getSession();
	    final InterMineAPI im = SessionMethods.getInterMineAPI(session);
	      
		String className = reportObject.getClassDescriptor().getUnqualifiedName();
        request.setAttribute("className", className);
        
        String contextURL = WebApplicationContextLocator.getServiceUrl(request);
		LOG.info("Service Context URL:" + contextURL);

		StockQueryService stockService = (StockQueryService) QueryServiceLocator.getService(STOCK_SERVICE, request);
		String stockServiceUrl = stockService.getServiceUrl();
		LOG.info("Stock Service Context URL:" + contextURL);
		

	    LOG.info("Gene Allele Displayer:" + "Class Name:"  + className);
	     
	     Gene gene = (Gene)reportObject.getObject();
	     
	     LOG.info("Generating Gene/Alleles Report. Gene Id:" + gene.getPrimaryIdentifier());
	     
	     PathQuery query = getAllelesResultSet(gene.getId());
	     Profile profile = SessionMethods.getProfile(session);
	     
	     exec = im.getPathQueryExecutor(profile);
	     ExportResultsIterator result;
	     
	      try {
	        result = exec.execute(query);
	      } catch (ObjectStoreException e) {
	        
	        LOG.error("Had an ObjectStoreException in Gene/Alleles Displayer java: "+e.getMessage());
	        return;
	      }

	      ArrayList<AlleleVO> alleleList = new ArrayList<AlleleVO>();
	      
	      while (result.hasNext()) {
	       List<ResultElement> resElement = result.next();
	       //AlleleVO item = new AlleleVO(resElement);
	        
	      AlleleVOBuilder builder = new AlleleVOBuilder();
	      AlleleVO item1 = builder.build(resElement);
	        
	        alleleList.add(item1);
	        LOG.info("Allele:" + item1);
	      }
	      
	   // for accessing this within the jsp
	      request.setAttribute("geneName",gene.getPrimaryIdentifier());
	      request.setAttribute("list",alleleList);
	      request.setAttribute("id",gene.getId());
	      request.setAttribute("contextURL",contextURL);
	      request.setAttribute("stockServiceUrl",stockServiceUrl);
	}

	
	private PathQuery getAllelesResultSet(Integer id){
		
		 PathQuery query = new PathQuery(im.getModel());
		 
		// Select the output columns:
		 
	        query.addViews("Gene.affectedAlleles.id",
	        			   "Gene.affectedAlleles.primaryIdentifier",
	        			   "Gene.affectedAlleles.name",
	                       "Gene.affectedAlleles.mutagen.name",
	                       "Gene.affectedAlleles.sequenceAlterationType.name", 
	                       "Gene.affectedAlleles.mutationSite.name",
	                       "Gene.affectedAlleles.inheritanceMode.name",
	                       "Gene.affectedAlleles.alleleClass.name"
	                       
	        		);
	        
	     // Add orderby
	   	    query.addOrderBy("Gene.affectedAlleles.name", OrderDirection.ASC);
		    	        
	        
		 // Outer Joins
	        // Show all information about these relationships if they exist, but do not require that they exist.
	        query.setOuterJoinStatus("Gene.affectedAlleles.mutagen", OuterJoinStatus.OUTER);
	        query.setOuterJoinStatus("Gene.affectedAlleles.sequenceAlterationType", OuterJoinStatus.OUTER);
	        query.setOuterJoinStatus("Gene.affectedAlleles.mutationSite", OuterJoinStatus.OUTER);
	        query.setOuterJoinStatus("Gene.affectedAlleles.inheritanceMode", OuterJoinStatus.OUTER);
	        query.setOuterJoinStatus("Gene.affectedAlleles.alleleClass", OuterJoinStatus.OUTER);
	        
		    query.addConstraint(Constraints.eq("Gene.id",id.toString()));
		    return query;
		
	}
	
	
	private PathQuery getDataSet(){
		
		 PathQuery query = new PathQuery(im.getModel());
		 
		// Select the output columns:
		 
		// Add orderby
	        query.addOrderBy("Allele.affectedGenes.affectedAlleles.name", OrderDirection.ASC);
	        
	        query.addViews(
	        		"DataSet.id",
	        		"DataSet.name",
	                "DataSet.dataSource.name",
	                "DataSet.url"
	        		);

	        // Filter the results with the following constraints:
	        query.addConstraint(Constraints.eq("DataSet.name", "TAIR Polymorphism"));
	        
	        return query;
		
	}
}
