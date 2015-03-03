package org.intermine.bio.web.displayer;

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
import org.intermine.model.bio.Protein;
import org.intermine.model.bio.MRNA;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.PathQuery;
import org.intermine.util.DynamicUtil;
import org.intermine.web.displayer.ReportDisplayer;
import org.intermine.web.logic.config.ReportDisplayerConfig;
import org.intermine.web.logic.results.ReportObject;
import org.intermine.web.logic.session.SessionMethods;


public class ProteinDisplayer extends ReportDisplayer {

  protected static final Logger LOG = Logger.getLogger(ProteinDisplayer.class);
  PathQueryExecutor exec;
  private HashMap<Integer,String> organismMap = new HashMap<Integer,String>();

  /**
   * Construct with config and the InterMineAPI.
   * @param config to describe the report displayer
   * @param im the InterMine API
   */
  public ProteinDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
      super(config, im);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void display(HttpServletRequest request, ReportObject reportObject) {
      HttpSession session = request.getSession();
      final InterMineAPI im = SessionMethods.getInterMineAPI(session);

      Gene geneObj = (Gene)reportObject.getObject();
      
      LOG.info("Entering ProteinDisplayer.display for "+geneObj.getPrimaryIdentifier());
      LOG.info("Id is "+geneObj.getId());

      // query the proteins
      PathQuery query = getProteinTable(geneObj.getId());
      Profile profile = SessionMethods.getProfile(session);
      exec = im.getPathQueryExecutor(profile);
      ExportResultsIterator result;
      try {
        result = exec.execute(query);
      } catch (ObjectStoreException e) {
        // silently return
        LOG.warn("Had an ObjectStoreException in ProteinDisplayer.java: "+e.getMessage());
        return;
      }

      ArrayList<ProteinRecord> proteinList = new ArrayList<ProteinRecord>();
      
      while (result.hasNext()) {
        List<ResultElement> resElement = result.next();
        ProteinRecord r = new ProteinRecord(resElement);
        proteinList.add(r);
      }
        
      // for accessing this within the jsp
      request.setAttribute("geneName",geneObj.getPrimaryIdentifier());
      request.setAttribute("list",proteinList);
      request.setAttribute("id",geneObj.getId());
      
  }

  private PathQuery getProteinTable(Integer id) {
    PathQuery query = new PathQuery(im.getModel());
    query.addViews( "Gene.proteins.id",
		    "Gene.proteins.primaryIdentifier",
		    "Gene.proteins.primaryAccession",
		    "Gene.proteins.uniprotAccession",
		    "Gene.proteins.length",
		    "Gene.proteins.mRNA.primaryIdentifier"
		    );

    query.addOrderBy("Gene.proteins.primaryIdentifier", OrderDirection.ASC);
    query.addConstraint(Constraints.eq("Gene.id",id.toString()));
    return query;
  }
  
  public class ProteinRecord {
    private String id;
    private String primaryIdentifier;
    private String primaryAccession;
    private String uniprotAccession;
    private String length;
    private String geneName;

    public ProteinRecord(List<ResultElement> resElement) {
      // the fields are a copy of the query results
      id = ((resElement.get(0)!=null) && (resElement.get(0).getField()!= null))?
                                 resElement.get(0).getField().toString():"&nbsp;";
      primaryIdentifier = ((resElement.get(1)!=null) && (resElement.get(1).getField()!= null))?
                                 resElement.get(1).getField().toString():"&nbsp;";
      primaryAccession = ((resElement.get(2)!=null) && (resElement.get(2).getField()!= null))?
                                 resElement.get(2).getField().toString():"&nbsp;";
      uniprotAccession = ((resElement.get(3)!=null) && (resElement.get(3).getField()!= null))?
                                 resElement.get(3).getField().toString():"&nbsp;";
      length = ((resElement.get(4)!=null) && (resElement.get(4).getField()!= null))?
                                 resElement.get(4).getField().toString():"&nbsp;";
      geneName = ((resElement.get(5)!=null) && (resElement.get(5).getField()!= null))?
                                 resElement.get(5).getField().toString():"&nbsp;";
    }

    public String getId() { return id; }
    public String getPrimaryIdentifier() { return primaryIdentifier; }
    public String getPrimaryAccession() { return primaryAccession; }
    public String getUniprotAccession() { return uniprotAccession; }
    public String getLength() { return length; }
    public String getGeneName() { return geneName; }
  }
  
}
