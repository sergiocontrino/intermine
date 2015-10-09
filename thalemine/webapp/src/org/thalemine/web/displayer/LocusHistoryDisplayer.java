package org.thalemine.web.displayer;

//package org.intermine.bio.web.displayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.intermine.api.InterMineAPI;
import org.intermine.api.profile.Profile;
import org.intermine.api.query.PathQueryExecutor;
import org.intermine.api.results.ExportResultsIterator;
import org.intermine.api.results.ResultElement;
import org.intermine.model.bio.Gene;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.pathquery.PathQuery;
import org.intermine.web.displayer.ReportDisplayer;
import org.intermine.web.logic.config.ReportDisplayerConfig;
import org.intermine.web.logic.results.ReportObject;
import org.intermine.web.logic.session.SessionMethods;


public class LocusHistoryDisplayer extends ReportDisplayer {

  protected static final Logger LOG = Logger.getLogger(LocusHistoryDisplayer.class);
  PathQueryExecutor exec;

  /**
   * Construct with config and the InterMineAPI.
   * @param config to describe the report displayer
   * @param im the InterMine API
   */
  public LocusHistoryDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
      super(config, im);
  }

  @Override
  public void display(HttpServletRequest request, ReportObject reportObject) {
      HttpSession session = request.getSession();
      final InterMineAPI im = SessionMethods.getInterMineAPI(session);

      Gene geneObj = (Gene)reportObject.getObject();

      // query
      PathQuery query = getProteinTable(geneObj.getId());
      Profile profile = SessionMethods.getProfile(session);
      exec = im.getPathQueryExecutor(profile);
      ExportResultsIterator result;
      try {
        result = exec.execute(query);
      } catch (ObjectStoreException e) {
        // silently return
        LOG.warn("Had an ObjectStoreException in LocusHistoryDisplayer.java: " +e.getMessage());
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

    query.addViews("Gene.locusHistory.locusOperation",
            "Gene.locusHistory.datestamp",
            "Gene.locusHistory.source.name",
            "Gene.locusHistory.lociInvolved.primaryIdentifier",
            "Gene.locusHistory.lociInvolved.id");

    query.setOuterJoinStatus("Gene.locusHistory.lociInvolved", OuterJoinStatus.OUTER);

    // Add orderby
    query.addOrderBy("Gene.locusHistory.datestamp", OrderDirection.DESC);

    query.addConstraint(Constraints.eq("Gene.id",id.toString()));
    return query;
  }

  public class ProteinRecord {
    private String operation;
    private String date;
    private String source;
    private String locus;
    private String id;

    public ProteinRecord(List<ResultElement> resElement) {
      // the fields are a copy of the query results
      operation = ((resElement.get(0)!=null) && (resElement.get(0).getField()!= null))?
                                 resElement.get(0).getField().toString():"&nbsp;";
      date = ((resElement.get(1)!=null) && (resElement.get(1).getField()!= null))?
              formatDate(resElement.get(1).getField().toString()):"&nbsp;";
      source = ((resElement.get(2)!=null) && (resElement.get(2).getField()!= null))?
                                 resElement.get(2).getField().toString():"&nbsp;";
      locus = ((resElement.get(3)!=null) && (resElement.get(3).getField()!= null))?
                                 resElement.get(3).getField().toString():"&nbsp;";
      id = ((resElement.get(4)!=null) && (resElement.get(4).getField()!= null))?
                                 resElement.get(4).getField().toString():"&nbsp;";
    }

    public String getId() { return id; }
    public String getOperation() { return operation; }
    public String getDate() { return date; }
    public String getSource() { return source; }
    public String getLocus() { return locus; }
  }

  private String formatDate (String yyyymmdd) {
      //  String dateString1 = "16-04-2011";
      try {
          Date date = new SimpleDateFormat("yyyymmdd").parse(yyyymmdd);
          String dateString2 = new SimpleDateFormat("d MMM yyyy").format(date);
          return dateString2;

        } catch (ParseException e) {
          // silently return
          LOG.warn("Error in parsing date " + yyyymmdd + " -- " + e.getMessage());
          return null;
        }
  }

}
