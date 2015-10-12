package org.thalemine.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.thalemine.web.domain.datacategory.DataSummaryVO;
import org.thalemine.web.metadata.DataSummaryService;

public class DataCategoriesController extends TilesAction {

    protected static final Logger log = Logger.getLogger(DataCategoriesController.class);

    public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Exception exception = null;
        List<DataSummaryVO> results = new ArrayList<DataSummaryVO>();
        List<DataSummaryVO> geneResults = new ArrayList<DataSummaryVO>();

        log.info("Data Summary Retrieval has started.");
        try {
            results = DataSummaryService.getSummary();
            geneResults = DataSummaryService.getGeneSummary();
        } catch (Exception e) {
            exception = e;
        } finally {
            if (exception != null) {
                log.error("Error occurred while retriiveing Data Summaries." + "; Message: " + exception.getMessage()
                        + "; Cause: " + exception.getCause());
                exception.printStackTrace();

            } else {
                request.setAttribute("results", results);
                request.setAttribute("generesults", geneResults);
                log.info("Summary Successfully Retrieved." + "ResultSet size:" + results.size());
            }
        }

        return null;
    }
}
