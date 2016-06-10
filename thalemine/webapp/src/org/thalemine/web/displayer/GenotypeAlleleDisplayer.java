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

import org.apache.log4j.Logger;
import org.intermine.api.InterMineAPI;
import org.intermine.model.InterMineObject;
import org.intermine.web.displayer.ReportDisplayer;
import org.intermine.web.logic.config.ReportDisplayerConfig;
import org.intermine.web.logic.results.ReportObject;


public class GenotypeAlleleDisplayer extends ReportDisplayer
{

	protected static final Logger log = Logger.getLogger(GenotypeAlleleDisplayer.class);
	
    /**
     * Construct with config and the InterMineAPI.
     * @param config to describe the report displayer
     * @param im the InterMine API
     */
    public GenotypeAlleleDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
        super(config, im);
    }

    @Override
    public void display(HttpServletRequest request, ReportObject reportObject) {
    	
        String className = reportObject.getClassDescriptor().getUnqualifiedName();
        InterMineObject object = reportObject.getObject();
        
        log.info("Report Object:" + object);
        log.info("Class Name:" + className);
        
        request.setAttribute("className", className);
        request.setAttribute("intermineobject", object);
       
    }
}
