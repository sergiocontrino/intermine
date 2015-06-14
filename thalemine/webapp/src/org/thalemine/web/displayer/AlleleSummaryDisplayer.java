package org.thalemine.web.displayer;
/*
 * Copyright (C) 2002-2014 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import javax.servlet.http.HttpServletRequest;

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
import org.intermine.model.bio.Allele;
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
import org.thalemine.web.domain.GeneModelVO;
import org.intermine.pathquery.OuterJoinStatus;
import org.thalemine.web.domain.AlleleSummaryVO;
import org.thalemine.web.query.AlleleQueryService;
import org.thalemine.web.utils.QueryServiceLocator;
import org.thalemine.web.utils.WebApplicationContextLocator;

 
public class AlleleSummaryDisplayer extends ReportDisplayer
{
	
	private static final String ALLELE_SERVICE = "AlleleQueryService";
	protected static final Logger log = Logger.getLogger(GeneModelDisplayer.class);

    /**
     * Construct with config and the InterMineAPI.
     * @param config to describe the report displayer
     * @param im the InterMine API
     */
    public AlleleSummaryDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
        super(config, im);
    }

    @Override
    public void display(HttpServletRequest request, ReportObject reportObject) {
       
    	Exception exception = null;
		Allele allele = null;
		
		String objectClassName = reportObject.getClassDescriptor().getUnqualifiedName();	
		List<AlleleSummaryVO> resultList = new ArrayList<AlleleSummaryVO>();
		
		try{
			
			String contextURL = WebApplicationContextLocator.getServiceUrl(request);
			log.info("Service Context URL:" + contextURL);

			AlleleQueryService alleleService = (AlleleQueryService) QueryServiceLocator.getService(ALLELE_SERVICE, request);
			String alleleServiceUrl = alleleService.getServiceUrl();
			log.info("Allele Service Context URL:" + contextURL);
			
			request.setAttribute("className", objectClassName);
			log.info("Allele/Summary Displayer:" + "Class Name:" + objectClassName);
			
			allele = (Allele) reportObject.getObject();		

			log.info("Allele/Summary Displayer:" + "Allele:" + allele.getPrimaryIdentifier());
			
			resultList = alleleService.getAlleleSummary(allele);
			
		}catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Allele/Summary Displayer." + ";Message:" + exception.getMessage()
						+ ";Cause:" + exception.getCause());
				return;
			} else {
				
				// Set Request Attributes		
				request.setAttribute("list", resultList);
			}
		}
    }
}
