package org.thalemine.web.controller;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.intermine.api.InterMineAPI;
import org.intermine.objectstore.ObjectStore;
import org.intermine.web.logic.session.SessionMethods;
import org.thalemine.web.database.DBConnectionManager;
import org.thalemine.web.database.core.NonTransientResourceException;
import org.thalemine.web.database.core.reader.DatabaseItemReader;
import org.thalemine.web.database.core.reader.ParseException;
import org.thalemine.web.database.core.reader.UnexpectedInputException;
import org.thalemine.web.database.domain.reader.DataSetReader;
import org.thalemine.web.domain.datacategory.Category;
import org.thalemine.web.domain.datacategory.DataSetTO;
import org.thalemine.web.domain.datacategory.DataSummaryVO;
import org.thalemine.web.metadata.DataSummaryService;
import org.thalemine.web.metadata.MetaDataCacheImpl;
import org.thalemine.web.utils.FileUtils;

public class DataCategoriesController extends TilesAction {

	protected static final Logger log = Logger.getLogger(DataCategoriesController.class);

	public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Exception exception = null;
		List<DataSummaryVO> results = new ArrayList<DataSummaryVO>();

		log.info("Data Summary Retrieval has started.");
		try {
			results = DataSummaryService.getSummary();
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error occurred while retriiveing Data Summaries." + "; Message: " + exception.getMessage()
						+ "; Cause: " + exception.getCause());
				exception.printStackTrace();

			} else {
				request.setAttribute("results", results);
				log.info("Summary Successfully Retrieved." + "ResultSet size:" + results.size());
			}
		}

		return null;
	}
}