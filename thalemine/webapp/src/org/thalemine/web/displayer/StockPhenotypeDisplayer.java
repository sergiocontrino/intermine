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
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.StockVO;
import org.intermine.pathquery.OuterJoinStatus;


public class StockPhenotypeDisplayer extends ReportDisplayer {
	
	
	protected static final Logger LOG = Logger.getLogger(StockPhenotypeDisplayer.class);
	PathQueryExecutor exec;
	
	public  StockPhenotypeDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
	      super(config, im);
	}

	@Override
	  @SuppressWarnings("unchecked")
	  public void display(HttpServletRequest request, ReportObject reportObject) {
		
		HttpSession session = request.getSession();
	    final InterMineAPI im = SessionMethods.getInterMineAPI(session);
	      
		String className = reportObject.getClassDescriptor().getUnqualifiedName();
        request.setAttribute("className", className);

	     LOG.info("Gene StockPhenotype Displayer:" + "Class Name:"  + className);
	     
	     Gene gene = (Gene)reportObject.getObject();
	     
	     LOG.info("Generating StockPhenotype Report. Gene Id:" + gene.getPrimaryIdentifier());
	     
	     PathQuery query = geStockResultSet(gene.getId());
	     Profile profile = SessionMethods.getProfile(session);
	     
	     exec = im.getPathQueryExecutor(profile);
	     ExportResultsIterator result;
	     
	      try {
	        result = exec.execute(query);
	      } catch (ObjectStoreException e) {
	        
	        LOG.error("Had an ObjectStoreException in StockPhenotype Displayer java: "+e.getMessage());
	        return;
	      }

	      ArrayList<StockVO> stockList = new ArrayList<StockVO>();
	      
	      while (result.hasNext()) {
	        List<ResultElement> resElement = result.next();
	        StockVO item = new StockVO(resElement);
	        stockList.add(item);
	        LOG.info("Stock:" + item);
	      }
	      
	   // for accessing this within the jsp
	      request.setAttribute("geneName",gene.getPrimaryIdentifier());
	      request.setAttribute("list",stockList);
	      request.setAttribute("id",gene.getId());
	}

	private PathQuery geStockResultSet(Integer id){
		
		
		 PathQuery query = new PathQuery(im.getModel());
		 
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
	        
		    query.addConstraint(Constraints.eq("Gene.id",id.toString()));
		    return query;
	   	    
	}
	
		
	private PathQuery getTAIRDataSet(){
		
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
	        query.addConstraint(Constraints.eq("DataSet.name", "TAIR Stock"));
	        
	        return query;
		
	}
}
