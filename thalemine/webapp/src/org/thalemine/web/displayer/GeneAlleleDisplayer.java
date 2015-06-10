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


public class GeneAlleleDisplayer extends ReportDisplayer {
	
	
	protected static final Logger LOG = Logger.getLogger(GeneAlleleDisplayer.class);
	
	public GeneAlleleDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
	      super(config, im);
	}

	@Override
	  @SuppressWarnings("unchecked")
	  public void display(HttpServletRequest request, ReportObject reportObject) {
		String className = reportObject.getClassDescriptor().getUnqualifiedName();
        request.setAttribute("className", className);

	     LOG.info("Gene Allele Displayer:" + "Class Name:"  + className);
	     
	     Gene gene = (Gene)reportObject.getObject();
	     
	     LOG.info("Generating Gene/Alleles Report. Gene Id:" + gene.getPrimaryIdentifier());
	}
	
}
