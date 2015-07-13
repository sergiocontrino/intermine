package org.thalemine.web.displayer;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
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
import org.intermine.web.util.URLGenerator;
import org.thalemine.web.context.WebApplicationContextLocator;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.PhenotypeVO;
import org.thalemine.web.domain.StockGenotypeVO;
import org.thalemine.web.domain.StockGrowthRequirementsVO;
import org.thalemine.web.domain.StockVO;
import org.thalemine.web.domain.StrainVO;
import org.thalemine.web.query.StockQueryService;
import org.thalemine.web.service.StockService;
import org.thalemine.web.service.core.ServiceConfig;
import org.thalemine.web.service.core.ServiceManager;
import org.thalemine.web.utils.QueryServiceLocator;
import org.intermine.pathquery.OuterJoinStatus;

public class PhenotypesDisplayer extends ReportDisplayer {
	
	protected static final Logger log = Logger.getLogger(PhenotypesDisplayer.class);

	public PhenotypesDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
		super(config, im);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void display(HttpServletRequest request, ReportObject reportObject) {

		Exception exception = null;
		InterMineObject object = null;

		List<PhenotypeVO> resultList = new ArrayList<PhenotypeVO>();
		StockGrowthRequirementsVO growthConditions = null;


		try {

			object = reportObject.getObject();
			
			StockService businesservice = (StockService) ServiceManager.getInstance().getService(ServiceConfig.STOCK_SERVICE);
			
			if (businesservice!=null){
				log.info("Calling Stock Service:" + businesservice);
				
				growthConditions = businesservice.getGrowthRequirements(object);
				
				log.info("Stock Grow VO:" + growthConditions);
				
				resultList = businesservice.getStockPhenotypes(object);
				
				log.info("Phenotype VO:" + resultList);
				
			}

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error occurred Phenotypes Displayer." + ";Message:" + exception.getMessage() + ";Cause:"
						+ exception.getCause());
				return;
			} else {

				// Set Request Attributes
				request.setAttribute("list", resultList);
				request.setAttribute("id", object.getId());
				request.setAttribute("growthConditions", growthConditions);

			}
		}

	}

}
