package org.thalemine.web.metadata;

/*
 * Copyright (C) 2002-2014 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.intermine.api.InterMineAPI;
import org.intermine.objectstore.ObjectStore;
import org.intermine.web.logic.session.SessionMethods;
import org.json.JSONObject;



public class MetadataCacheQueryService
{
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(MetadataCacheQueryService.class);

    private static final Map<String, String> RESOURCE_METHOD_MAP;
    
    static {
        Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("catexp", "getCatExpJsonString");
        tempMap.put("webapp_path", "getWebappPath");
        tempMap.put("gbrowse_base_url", "getGBrowseBaseURL");
        tempMap.put("test", "testWebservice");
        tempMap.put("catexp.test", "testWebservice");
        RESOURCE_METHOD_MAP = Collections.unmodifiableMap(tempMap);
    }

   
    private ObjectStore os;
    private HttpServletRequest request;

    private static final String NO_DATA_RETURN = "No data return";

    /**
     * Generate JSON string
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param resourcePath resource path such as "/catexp/fly"
     * @throws Exception Exception
     */
    protected void service(HttpServletRequest request,
            HttpServletResponse response, String resourcePath) throws Exception {

        HttpSession session = request.getSession();
        final InterMineAPI im = SessionMethods.getInterMineAPI(session);
        this.os = im.getObjectStore();
        this.request = request;
       
    }

    private void doWrite(HttpServletResponse response, String jsonString) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(jsonString);
        out.flush();
        out.close();
    }

    @SuppressWarnings("unused")
    private String getCatExpJsonString() {
        
        return null;
    }


    @SuppressWarnings("unused")
    private String testWebservice() {
        return "Resource available";
    }

    @SuppressWarnings("unused")
    /**
     * return webapp path, e.g. modminepreview
     */
    private String getWebappPath() {
        return SessionMethods.getWebProperties(
                request.getSession().getServletContext()).getProperty("webapp.path");
    }

  

}
