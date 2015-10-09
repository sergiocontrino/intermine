package org.thalemine.web.displayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private static final Map<String, String> OPERATIONS = new HashMap<String, String>();

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

  static {

      OPERATIONS.put("merge", "the gene model has been merged with another one and retained its name");
      OPERATIONS.put("delete", "the gene model has been eliminated");
      OPERATIONS.put("mergeobsolete", "the gene model has been merged and its name has not been retained");
      OPERATIONS.put("insert", "the gene model has been inserted from scratch");
      OPERATIONS.put("split", "the gene model has been split and retained its name");
      OPERATIONS.put("splitinsert", "the gene model has been split and has a new name");
      OPERATIONS.put("new", "the gene model has been generated");
      OPERATIONS.put("obsolete", "the gene model has disappeared");

  }


  @Override
  public void display(HttpServletRequest request, ReportObject reportObject) {
      HttpSession session = request.getSession();
      final InterMineAPI im = SessionMethods.getInterMineAPI(session);

      Gene geneObj = (Gene)reportObject.getObject();

      // query
      PathQuery query = getHistoryTable(geneObj.getId());
      Profile profile = SessionMethods.getProfile(session);
      exec = im.getPathQueryExecutor(profile);
      ExportResultsIterator result;
      try {
        result = exec.execute(query);
      } catch (ObjectStoreException e) {
        // silently return
        LOG.warn("ObjectStoreException in LocusHistoryDisplayer.java: " + e.getMessage());
        return;
      }

      ArrayList<HistoryRecord> historyList = new ArrayList<HistoryRecord>();

      while (result.hasNext()) {
        List<ResultElement> resElement = result.next();
        HistoryRecord r = new HistoryRecord(resElement);
        historyList.add(r);
      }

      // for accessing this within the jsp
      request.setAttribute("list", historyList);
      request.setAttribute("ops", OPERATIONS);
  }

  private PathQuery getHistoryTable(Integer id) {
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

  public class HistoryRecord {
    private String operation;
    private String date;
    private String source;
    private String locus;
    private String id;

    public HistoryRecord(List<ResultElement> resElement) {
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
      //
      try {
          Date date = new SimpleDateFormat("yyyyMMdd").parse(yyyymmdd);
          return new SimpleDateFormat("dd MMM yyyy").format(date);
        } catch (ParseException e) {
          // silently return
          LOG.warn("Error in parsing date " + yyyymmdd + " -- " + e.getMessage());
          return null;
        }
  }

}
