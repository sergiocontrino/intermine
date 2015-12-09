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
import org.intermine.model.bio.Stock;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.PathQuery;
import org.intermine.util.DynamicUtil;
import org.intermine.web.displayer.ReportDisplayer;
import org.intermine.web.logic.config.ReportDisplayerConfig;
import org.intermine.web.logic.results.ReportObject;
import org.intermine.web.logic.session.SessionMethods;
import org.thalemine.web.constants.AppConstants;
import org.thalemine.web.context.WebApplicationContextLocator;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.GeneModelVO;
import org.thalemine.web.domain.StockAvailabilityVO;
import org.thalemine.web.domain.StrainVO;
import org.intermine.pathquery.OuterJoinStatus;
import org.thalemine.web.domain.AlleleSummaryVO;
import org.thalemine.web.query.AlleleQueryService;
import org.thalemine.web.query.StockQueryService;
import org.thalemine.web.service.StockService;
import org.thalemine.web.service.core.ServiceConfig;
import org.thalemine.web.service.core.ServiceManager;
import org.thalemine.web.utils.QueryServiceLocator;

public class StockAvailabilityDisplayer extends ReportDisplayer {

	private static final String STOCK_SERVICE = "StockQueryService";
	protected static final Logger log = Logger.getLogger(StockAvailabilityDisplayer.class);

	/**
	 * Construct with config and the InterMineAPI.
	 * 
	 * @param config
	 *            to describe the report displayer
	 * @param im
	 *            the InterMine API
	 */
	public StockAvailabilityDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
		super(config, im);
	}

	@Override
	public void display(HttpServletRequest request, ReportObject reportObject) {

		Exception exception = null;
		List<StockAvailabilityVO> result = new ArrayList<StockAvailabilityVO>();
		InterMineObject object = null;
		String externalURL = null;
		String objectName = null;

		try {

			object = reportObject.getObject();

			log.info("Stock/Availability Displayer:" + "Stock:" + object);

			StockService businesservice = (StockService) ServiceManager.getInstance().getService(
					ServiceConfig.STOCK_SERVICE);

			if (businesservice != null) {
				log.info("Calling Stock Service:" + businesservice);

				result = businesservice.getStockAvailability(object.getId().toString());

				log.info("Stock/Availability VO:" + result);

			}

			if (object!=null){
				Stock stock = (Stock) object;
				
				String objectAccession = stock.getSecondaryIdentifier();
				externalURL = getExternalURL(objectAccession);
				
				objectName = stock.getGermplasmName();
				
			}
			
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Stock/Availability." + ";Message:" + exception.getMessage() + ";Cause:"
						+ exception.getCause());
				return;
			} else {

				// Set Request Attributes
				request.setAttribute("result", result);
				request.setAttribute("externalURL", externalURL);
				request.setAttribute("objectName", objectName);
								
				log.info("Germplasm/Stock." + "ObjectName:" + objectName + ";External Url:" +externalURL);
			}
		}
	}

	private String getExternalURL(final String objectAccession) {
		String url = null;
		int index = -1;
		Exception exception = null;

		log.info("Object Unique Accession:" + objectAccession);

		try {
			if (StringUtils.isNotEmpty(objectAccession)) {
				if (objectAccession.startsWith("Germplasm:")) {
					index = objectAccession.indexOf(":");

					if (index > 1) {
						url = AppConstants.TAIR_GERMPLASM_URL_PREFIX + objectAccession.substring(index + 1);
					}
				}

			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error obtaining object external url. Object:" + objectAccession + ";Error:" + exception.getMessage());
			} 
		}

		log.info("Object External Url:" + url);

		return url;
	}
}
