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
import org.thalemine.web.context.WebApplicationContextLocator;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.builder.AlleleVOBuilder;
import org.thalemine.web.query.StockQueryService;
import org.thalemine.web.service.AlleleService;
import org.thalemine.web.service.StockService;
import org.thalemine.web.service.core.ServiceConfig;
import org.thalemine.web.service.core.ServiceManager;
import org.thalemine.web.utils.QueryServiceLocator;
import org.intermine.pathquery.OuterJoinStatus;

public class GeneAlleleDisplayer extends ReportDisplayer {

	protected static final Logger log = Logger.getLogger(GeneAlleleDisplayer.class);
	PathQueryExecutor exec;

	public GeneAlleleDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
		super(config, im);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void display(HttpServletRequest request, ReportObject reportObject) {

		InterMineObject object = reportObject.getObject();
		Exception exception = null;
		Gene gene = (Gene) object;
		List<AlleleVO> alleleList = new ArrayList<AlleleVO>();

		try {

			AlleleService businesservice = (AlleleService) ServiceManager.getInstance().getService(
					ServiceConfig.ALLELE_SERVICE);

			if (businesservice != null) {
				log.info("Calling Allele Service:" + businesservice);

				alleleList = businesservice.getAllelesByGene(object.getId().toString());

				log.info("Allele VO Result:" + alleleList);

			}
		} catch (Exception e) {
			exception = e;
		}finally {

			if (exception != null) {
				log.error("Error Gene/Allele Displayer" + ";Message:" + exception.getMessage()
						+ ";Cause:" + exception.getCause());
				return;
			} else {
				
				// for accessing this within the jsp
				request.setAttribute("geneName", gene.getPrimaryIdentifier());
				request.setAttribute("list", alleleList);
				request.setAttribute("id", object.getId().toString());
			}
		}

	}

}
