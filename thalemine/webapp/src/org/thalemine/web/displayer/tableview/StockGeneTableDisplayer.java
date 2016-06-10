package org.thalemine.web.displayer.tableview;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.intermine.api.InterMineAPI;
import org.intermine.web.displayer.ReportDisplayer;
import org.intermine.web.logic.config.ReportDisplayerConfig;
import org.intermine.web.logic.results.ReportObject;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import org.intermine.metadata.Model;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.core.ServiceFactory;
import org.intermine.webservice.client.services.QueryService;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.impl.InfrastructureServiceImpl;

public class StockGeneTableDisplayer extends ReportDisplayer{

	protected static final Logger log = Logger.getLogger(StockGeneTableDisplayer.class);
		
	  /**
     * Construct with config and the InterMineAPI.
     * @param config to describe the report displayer
     * @param im the InterMine API
     */
    public StockGeneTableDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
        super(config, im);
    }
    
    @Override
    public void display(HttpServletRequest request, ReportObject reportObject) {
    	 String className = reportObject.getClassDescriptor().getUnqualifiedName();
         request.setAttribute("className", className);
         
         log.info("Stock/Gene Table Displayer has started.");
         InfrastructureService systemService = InfrastructureServiceImpl.getInstance();
         String serviceURL = systemService.getServiceEndPoint();
         log.info("Service URL:" + serviceURL);
         request.setAttribute("serviceURL", serviceURL);
         log.info("Stock/Gene Table Displayer has completed.");
    }
    

}
