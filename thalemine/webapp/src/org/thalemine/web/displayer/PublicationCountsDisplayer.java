package org.thalemine.web.displayer;

/*
 * Copyright (C) 2002-2015 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.intermine.api.InterMineAPI;
import org.intermine.model.bio.Gene;
import org.intermine.model.bio.Transcript;
import org.intermine.model.bio.Protein;
import org.intermine.metadata.Model;
import org.intermine.model.InterMineObject;
import org.intermine.model.bio.Publication;
import org.intermine.objectstore.ObjectStore;
import org.intermine.metadata.ConstraintOp;
import org.intermine.objectstore.query.ConstraintSet;
import org.intermine.objectstore.query.ContainsConstraint;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.QueryCollectionReference;
import org.intermine.objectstore.query.QueryField;
import org.intermine.objectstore.query.QueryFunction;
import org.intermine.objectstore.query.QueryValue;
import org.intermine.objectstore.query.Results;
import org.intermine.objectstore.query.ResultsRow;
import org.intermine.objectstore.query.SubqueryExistsConstraint;
import org.intermine.util.DynamicUtil;
import org.intermine.web.displayer.ReportDisplayer;
import org.intermine.web.logic.config.ReportDisplayerConfig;
import org.intermine.web.logic.results.ReportObject;
import org.intermine.web.logic.session.SessionMethods;

/**
 * Display publication and number of genes annotated by that publication. order
 * ASC
 *
 * @author Julie Sullivan
 */
public class PublicationCountsDisplayer extends ReportDisplayer {
	protected static final Logger log = Logger.getLogger(PublicationCountsDisplayer.class);

	/**
	 * Construct with config and the InterMineAPI.
	 *
	 * @param config
	 *            to describe the report displayer
	 * @param im
	 *            the InterMine API
	 */
	public PublicationCountsDisplayer(ReportDisplayerConfig config, InterMineAPI im) {
		super(config, im);
	}

	@Override
	public void display(HttpServletRequest request, ReportObject reportObject) {

		log.info("Thalemine Publication Displayer has started.");
		Map<Publication, String> bioEntityPublications = new LinkedHashMap<Publication, String>();
		

		InterMineObject object = reportObject.getObject();
		String type = DynamicUtil.getSimpleClass(object).getSimpleName();
		HttpSession session = request.getSession();
		final InterMineAPI im = SessionMethods.getInterMineAPI(session);

		Exception exception = null;

		try {

			Query query = queryManager(im, object, type);
			bioEntityPublications = getPublications(query, im);

		} catch (Exception e) {
			exception = e;
		}
		if (exception != null) {
			log.error("Error occurred while retrieving publications for object: " + " ; Message: " + exception.getMessage()
					+ "; Cause: " + exception.getCause() + "; Object: " + object);
			exception.printStackTrace();
		} else {
			log.info("Retrieval of the Object Publication Collection has successfully completed. " + "; Publication Collection Size: " +bioEntityPublications.size());
		}
		
		request.setAttribute("totalNumberOfPubs", bioEntityPublications.size());
		request.setAttribute("results", bioEntityPublications);

		request.setAttribute("type", type);

		if (bioEntityPublications.size() == 0) {
			request.setAttribute("noResults", "No publications found");
		}

		log.info("Thalemine Publication Displayer has completed.");
	}

	private Query queryManager(InterMineAPI im, InterMineObject object, String type) throws Exception {

		Model model = im.getModel();
		QueryClass queryClass = null;
		Query query = null;

		Exception exception = null;

		try {
			log.info("Determination of the target parent class has started.");
			queryClass = new QueryClass(Class.forName(model.getPackageName() + "." + type));
			log.info("Determination of the target parent class has completed. " + "; Target Parent Class: "
					+ queryClass);

			if (type.equals("Gene")) {
				query = getAllPublicationsQuery(im, object, type, queryClass);
			} else {
				query = getQuery(im, object, type, queryClass);
			}

			if (query == null) {
				exception = new Exception("Publication Query cannot be null.");
				throw exception;
			}
		} catch (ClassNotFoundException e) {
			exception = e;
		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error occurred while executing Publication Query." + " ; Message: " + exception.getMessage()
						+ "; Cause: " + exception.getCause());
				throw exception;
			} else {
				log.debug("Publication Query has been created. Query: " + query.toString() + "; Idl Query: "
						+ query.getIqlQuery());
			}
		}

		return query;
	}

	private Query getQuery(InterMineAPI im, InterMineObject object, String type, final QueryClass queryClass) {
		Model model = im.getModel();
		QueryClass qcPub = new QueryClass(Publication.class);
		QueryClass qcOtherGenes = null;

		try {
			qcOtherGenes = new QueryClass(Class.forName(model.getPackageName() + "." + type));
		} catch (ClassNotFoundException e) {
			log.error("Error rendering publication count displayer", e);
			return null;
		}

		QueryField qfDate = new QueryField(qcPub, "year");

		// constraints
		ConstraintSet cs = new ConstraintSet(ConstraintOp.AND);

		// This lets us join to the gene/publication indirection table without
		// joining gene as well
		QueryCollectionReference qcr1 = new QueryCollectionReference(object, "publications");
		cs.addConstraint(new ContainsConstraint(qcr1, ConstraintOp.CONTAINS, qcPub));

		QueryCollectionReference qcr2 = new QueryCollectionReference(qcOtherGenes, "publications");
		cs.addConstraint(new ContainsConstraint(qcr2, ConstraintOp.CONTAINS, qcPub));

		Query q = new Query();
		q.setDistinct(true);

		// from statement
		q.addFrom(qcPub);
		q.addFrom(qcOtherGenes);

		// add constraints to query
		q.setConstraint(cs);

		q.addToSelect(qcPub);

		QueryFunction qf = new QueryFunction();
		q.addToSelect(qf);

		q.addToGroupBy(new QueryField(qcPub, "id"));
		q.addToGroupBy(qfDate);

		q.addToSelect(qfDate);
		// removed to order by date instead
		// q.addToOrderBy(qf);
		q.addToOrderBy(qfDate, "desc");
		return q;
	}

	private Query getTranscriptsQuery(InterMineAPI im, InterMineObject object, String type) {

		Model model = im.getModel();

		QueryClass qcPub = new QueryClass(Publication.class);
		QueryClass qcOtherGenes = null;
		QueryClass qcTranscript = new QueryClass(Transcript.class);

		Query q = null;

		if (type.equals("Gene")) {
			log.info("Retrieving Gene mRNA Publications has started." + type);
			try {
				qcOtherGenes = new QueryClass(Class.forName(model.getPackageName() + "." + type));
			} catch (ClassNotFoundException e) {
				log.error("Error rendering publication count displayer", e);
				return null;
			}

			log.info("qcOtherGenes= " + qcOtherGenes);
			log.info("Type= " + type);

			q = new Query();
			q.setDistinct(true);

			QueryField qfDate = new QueryField(qcPub, "year");

			// constraints
			ConstraintSet csPrimary = new ConstraintSet(ConstraintOp.AND);

			// This lets us join to the gene/publication indirection table
			// without
			// joining gene as well
			// QueryClass qcTranscript = new QueryClass(Transcript.class);
			QueryCollectionReference qcr3 = new QueryCollectionReference(object, "transcripts");
			csPrimary.addConstraint(new ContainsConstraint(qcr3, ConstraintOp.EXISTS, qcTranscript));

			QueryCollectionReference qcr4 = new QueryCollectionReference(qcOtherGenes, "transcripts");
			csPrimary.addConstraint(new ContainsConstraint(qcr4, ConstraintOp.CONTAINS, qcTranscript));

			QueryCollectionReference qcr5 = new QueryCollectionReference(qcTranscript, "publications");
			csPrimary.addConstraint(new ContainsConstraint(qcr5, ConstraintOp.CONTAINS, qcPub));

			// from statement
			q.addFrom(qcPub);
			q.addFrom(qcOtherGenes);
			q.addFrom(qcTranscript);

			q.setConstraint(csPrimary);

			q.addToSelect(qcPub);

			QueryFunction qf = new QueryFunction();
			q.addToSelect(qf);

			q.addToGroupBy(new QueryField(qcPub, "id"));
			q.addToGroupBy(qfDate);

			q.addToSelect(qfDate);

			q.addToOrderBy(qfDate, "desc");

		} else {
			log.info("Skipping mRNA Publications Retrieval. The parent class is not a Gene. Parent Class: " + type);
		}

		return q;
	}

	private Query getProteinsQuery(InterMineAPI im, InterMineObject object, String type) {

		Model model = im.getModel();

		QueryClass qcPub = new QueryClass(Publication.class);
		QueryClass qcOtherGenes = null;
		QueryClass qcProtein = new QueryClass(Protein.class);

		Query q = null;

		if (type.equals("Gene")) {
			log.info("Retrieving Gene Protein Publications has started." + type);
			try {
				qcOtherGenes = new QueryClass(Class.forName(model.getPackageName() + "." + type));
			} catch (ClassNotFoundException e) {
				log.error("Error rendering publication count displayer", e);
				return null;
			}

			q = new Query();
			q.setDistinct(true);

			log.info("qcOtherGenes= " + qcOtherGenes);
			log.info("Type= " + type);

			QueryField qfDate = new QueryField(qcPub, "year");

			// constraints
			ConstraintSet csPrimary = new ConstraintSet(ConstraintOp.AND);

			// This lets us join to the gene/publication indirection table
			// without
			// joining gene as well
			// QueryCollectionReference qcr1 = new
			// QueryCollectionReference(object, "publications");
			// csPrimary.addConstraint(new ContainsConstraint(qcr1,
			// ConstraintOp.CONTAINS, qcPub));

			// QueryCollectionReference qcr2 = new
			// QueryCollectionReference(qcOtherGenes, "publications");
			// csPrimary.addConstraint(new ContainsConstraint(qcr2,
			// ConstraintOp.CONTAINS, qcPub));

			QueryCollectionReference qcr3 = new QueryCollectionReference(object, "proteins");
			csPrimary.addConstraint(new ContainsConstraint(qcr3, ConstraintOp.CONTAINS, qcProtein));

			QueryCollectionReference qcr4 = new QueryCollectionReference(qcOtherGenes, "proteins");
			csPrimary.addConstraint(new ContainsConstraint(qcr4, ConstraintOp.CONTAINS, qcProtein));

			QueryCollectionReference qcr5 = new QueryCollectionReference(qcProtein, "publications");
			csPrimary.addConstraint(new ContainsConstraint(qcr5, ConstraintOp.CONTAINS, qcPub));

			// from statement
			q.addFrom(qcPub);
			q.addFrom(qcOtherGenes);
			q.addFrom(qcProtein);

			// add constraints to query
			q.setConstraint(csPrimary);

			q.addToSelect(qcPub);

			QueryFunction qf = new QueryFunction();
			q.addToSelect(qf);

			q.addToGroupBy(new QueryField(qcPub, "id"));
			q.addToGroupBy(qfDate);

			q.addToSelect(qfDate);
			// removed to order by date instead
			// q.addToOrderBy(qf);
			q.addToOrderBy(qfDate, "desc");

		} else {
			log.info("Skipping protein Publications Retrieval. The parent class is not a Gene. Parent Class: " + type);
		}

		return q;

	}

	private Query getAllPublicationsQuery(InterMineAPI im, InterMineObject object, String type,
			final QueryClass queryClass) {

		Model model = im.getModel();

		QueryClass qcPub = new QueryClass(Publication.class);
		QueryClass qcOtherGenes = null;
		QueryClass qcTranscript = new QueryClass(Transcript.class);
		QueryClass qcProtein = new QueryClass(Protein.class);

		Query outerQuery = null;

		if (type.equals("Gene")) {
			// outer query constraints

			outerQuery = new Query();

			ConstraintSet outerQueryCS = new ConstraintSet(ConstraintOp.OR);
			outerQuery.setDistinct(true);

			// outer query from clause
			outerQuery.addFrom(qcPub);

			// outer query group by clause
			outerQuery.addToGroupBy(new QueryField(qcPub, "id"));

			QueryField qfDate = new QueryField(qcPub, "year");
			outerQuery.addToGroupBy(qfDate);

			// outer query select clause
			outerQuery.addToSelect(qcPub);

			// publication count
			QueryFunction qf = new QueryFunction();
			outerQuery.addToSelect(qf);

			outerQuery.addToSelect(qfDate);

			log.info("Retrieving All Gene Publications has started. Classes includes: Gene, transcripts, proteins ."
					+ type);
			try {
				qcOtherGenes = new QueryClass(Class.forName(model.getPackageName() + "." + type));
			} catch (ClassNotFoundException e) {
				log.error(
						"Error rendering publication count displayer. Error occurred in Get All Gene Publications method. ",
						e);
				return null;
			}

			// Gene subquery

			Query geneSubQuery = new Query();
			geneSubQuery.alias(qcPub, outerQuery.getAliases().get(qcPub));
			geneSubQuery.setDistinct(false);
			geneSubQuery.addFrom(qcOtherGenes);
			geneSubQuery.addToSelect(new QueryValue(1));
			ConstraintSet geneSubSetCS = new ConstraintSet(ConstraintOp.AND);
	
			QueryCollectionReference genePubCollection = new QueryCollectionReference(object, "publications");
			geneSubSetCS.addConstraint(new ContainsConstraint(genePubCollection, ConstraintOp.CONTAINS, qcPub));

			QueryCollectionReference geneClassPubCollection = new QueryCollectionReference(qcOtherGenes, "publications");
			geneSubSetCS.addConstraint(new ContainsConstraint(geneClassPubCollection, ConstraintOp.CONTAINS, qcPub));

			geneSubQuery.setConstraint(geneSubSetCS);

			outerQueryCS.addConstraint(new SubqueryExistsConstraint(ConstraintOp.EXISTS, geneSubQuery));

			// Transcripts SubQuery
			Query transcriptsSubQuery = new Query();
			transcriptsSubQuery.alias(qcPub, outerQuery.getAliases().get(qcPub));
			transcriptsSubQuery.setDistinct(false);
			transcriptsSubQuery.addFrom(qcTranscript);
			transcriptsSubQuery.addFrom(qcOtherGenes);
			transcriptsSubQuery.addToSelect(new QueryValue(1));
			ConstraintSet transcriptsSetCS = new ConstraintSet(ConstraintOp.AND);
					
			QueryCollectionReference geneTranscriptsCollection = new QueryCollectionReference(object, "transcripts");
			transcriptsSetCS.addConstraint(new ContainsConstraint(geneTranscriptsCollection, ConstraintOp.CONTAINS,
					qcTranscript));

			QueryCollectionReference transcriptsClassPubCollection = new QueryCollectionReference(qcOtherGenes,
					"transcripts");
			transcriptsSetCS.addConstraint(new ContainsConstraint(transcriptsClassPubCollection, ConstraintOp.CONTAINS,
					qcTranscript));

			QueryCollectionReference transcriptsPubCollection = new QueryCollectionReference(qcTranscript,
					"publications");
			transcriptsSetCS.addConstraint(new ContainsConstraint(transcriptsPubCollection, ConstraintOp.CONTAINS,
					qcPub));

			transcriptsSubQuery.setConstraint(transcriptsSetCS);
			outerQueryCS.addConstraint(new SubqueryExistsConstraint(ConstraintOp.EXISTS, transcriptsSubQuery));

			// Proteins SubQuery
			Query proteinsSubQuery = new Query();
			proteinsSubQuery.alias(qcPub, outerQuery.getAliases().get(qcPub));
			proteinsSubQuery.setDistinct(false);
			proteinsSubQuery.addFrom(qcProtein);
			proteinsSubQuery.addFrom(qcOtherGenes);
			proteinsSubQuery.addToSelect(new QueryValue(1));
			ConstraintSet proteinsSetCS = new ConstraintSet(ConstraintOp.AND);
					
			QueryCollectionReference geneProteinsCollection = new QueryCollectionReference(object, "proteins");
			proteinsSetCS
					.addConstraint(new ContainsConstraint(geneProteinsCollection, ConstraintOp.CONTAINS, qcProtein));

			QueryCollectionReference proteinsClassPubCollection = new QueryCollectionReference(qcOtherGenes, "proteins");
			proteinsSetCS.addConstraint(new ContainsConstraint(proteinsClassPubCollection, ConstraintOp.CONTAINS,
					qcProtein));

			QueryCollectionReference proteinsPubCollection = new QueryCollectionReference(qcProtein, "publications");
			proteinsSetCS.addConstraint(new ContainsConstraint(proteinsPubCollection, ConstraintOp.CONTAINS, qcPub));

			proteinsSubQuery.setConstraint(proteinsSetCS);
			outerQueryCS.addConstraint(new SubqueryExistsConstraint(ConstraintOp.EXISTS, proteinsSubQuery));

			outerQuery.setConstraint(outerQueryCS);
			
			outerQuery.addToOrderBy(qfDate, "desc");

		}

		return outerQuery;

	}

	private Map<Publication, String> getPublications(final Query query, final InterMineAPI im) throws Exception{

		Map<Publication, String> publications = new LinkedHashMap<Publication, String>();
		Exception exception = null;

		try {

			if (query == null) {
				exception = new Exception("Publication Query cannot be null.");
				throw exception;
			}

			log.debug("Input Publication Query: " + query.toString() + "Idl Query: " + query.getIqlQuery());

			ObjectStore os = im.getObjectStore();
			Results results = os.execute(query, 2000, true, false, true);
			Iterator<Object> it = results.iterator();

			while (it.hasNext()) {
				ResultsRow rr = (ResultsRow) it.next();
				Publication pub = (Publication) rr.get(0);

				log.debug("Current Publication: = " + pub);
				Object countObject = (Object) rr.get(1);
				log.debug("Count Object: = " + countObject);

				Long publicationCount = (Long) countObject;
				publications.put(pub, publicationCount.toString());

			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error occurred while executing Publication Query." + " ; Message: " + exception.getMessage()
						+ "; Cause: " + exception.getCause());
				throw exception;
			} else {
				log.info("Publication Query has successfully completed. " + "; Result Set Size: " + publications.size());
			}
		}

		return publications;

	}

	private Map<Publication, String> mergePublications(Map<Publication, String> bioEntityPublications,
			Map<Publication, String> transcriptsPublications, Map<Publication, String> proteinPublications) {

		Map<Publication, String> mergedPublications = new LinkedHashMap<Publication, String>();
		Map<Publication, String> unionPublications = new LinkedHashMap<Publication, String>();

		Map<Publication, String> bioEntityTranscriptsUnion = new LinkedHashMap<Publication, String>();
		Map<Publication, String> bioEntityTranscriptsProteinUnion = new LinkedHashMap<Publication, String>();

		bioEntityTranscriptsUnion = unionPublications(bioEntityPublications, transcriptsPublications);
		bioEntityTranscriptsProteinUnion = unionPublications(bioEntityTranscriptsUnion, proteinPublications);
		mergedPublications = bioEntityTranscriptsProteinUnion;

		for (Map.Entry<Publication, String> entry : mergedPublications.entrySet()) {

			Publication publicationKey = entry.getKey();
			String publicationCountStr = entry.getValue();
			int publicationCount = getPublicationCount(publicationKey, publicationCountStr);

			log.info("Merged Publication:" + publicationKey + ";String Count:" + publicationCountStr
					+ "; Integer Count:" + publicationCount);
		}

		log.info("Merged Publication Count: " + mergedPublications.size());

		return mergedPublications;

	}

	private Map<Publication, String> union(final Map<Publication, String> publicationMap1,
			final Map<Publication, String> publicationMap2) {

		Map<Publication, String> unionPublications = new LinkedHashMap<Publication, String>();

		if (publicationMap1.size() > 0 && publicationMap2.size() > 0) {

			for (Map.Entry<Publication, String> entry : publicationMap1.entrySet()) {

				int totalPublicationCount = 0;
				Publication publication1Key = entry.getKey();
				String publication1CountStr = entry.getValue();
				int publication1Count = getPublicationCount(publication1Key, publication1CountStr);

				String publication2CountStr = null;
				int publication2Count = 0;

				try {
					if (publicationMap2.containsKey(publication1Key)) {
						log.info("Found publication from Map1 in the publication Map2.");
						publication2CountStr = publicationMap1.get(publication1Key);
						publication2Count = getPublicationCount(publication1Key, publication2CountStr);
					}
				} catch (Exception e) {
					log.error("Error parsing publication map entry." + publication1Key + "; Message: " + e.getMessage());
				}

				log.info("Publication Map1:" + publication1Key + ";String Count:" + publication1CountStr
						+ "; Integer Count:" + publication1Count);

				log.info("Publication Map2:" + publication1Key + ";String Count:" + publication2CountStr
						+ "; Integer Count:" + publication2Count);

				totalPublicationCount = publication1Count + publication2Count;
				String totalPublicationCountStr = String.valueOf(totalPublicationCount);

				log.info("Total Publication Count: " + publication1Key + totalPublicationCount);

				unionPublications.put(publication1Key, totalPublicationCountStr);
			}

		} else if (publicationMap1.size() > 0 && publicationMap2.size() == 0) {
			unionPublications = publicationMap1;
		} else if (publicationMap1.size() == 0 && publicationMap2.size() > 0) {
			unionPublications = publicationMap2;
		}

		return unionPublications;

	}

	private Map<Publication, String> unionPublications(final Map<Publication, String> unionMap,
			final Map<Publication, String> sourceMap) {

		Map<Publication, String> unionPublications = new LinkedHashMap<Publication, String>();

		unionPublications = unionMap;

		for (Map.Entry<Publication, String> entry : sourceMap.entrySet()) {

			int totalPublicationCount = 0;
			Publication publication1Key = entry.getKey();
			String publication1CountStr = entry.getValue();

			String publication2CountStr = null;
			int publication2Count = 0;

			int publication1Count = getPublicationCount(publication1Key, publication1CountStr);

			if (!unionPublications.containsKey(publication1Key)) {
				unionPublications.put(publication1Key, publication1CountStr);

				log.info("Adding publication to the publication union: " + publication1Key + ";String Count:"
						+ publication1CountStr + "; Integer Count:" + publication1Count);

			} else { // update Publication Count
				publication2CountStr = unionPublications.get(publication1Key);
				publication2Count = getPublicationCount(publication1Key, publication2CountStr);
			}

			log.info("Publication Map1:" + publication1Key + ";String Count:" + publication1CountStr
					+ "; Integer Count:" + publication1Count);

			log.info("Publication Map2:" + publication1Key + ";String Count:" + publication2CountStr
					+ "; Integer Count:" + publication2Count);

			totalPublicationCount = publication1Count + publication2Count;
			String totalPublicationCountStr = String.valueOf(totalPublicationCount);

			log.info("Total Publication Count: " + publication1Key + totalPublicationCount);

			// update total count
			unionPublications.put(publication1Key, totalPublicationCountStr);
		}

		return unionPublications;

	}

	private int getPublicationCount(Publication publication, String stringCount) {
		int count = 0;

		if (StringUtils.isNotBlank(stringCount)) {
			try {
				count = Integer.parseInt(stringCount);
			} catch (Exception e) {
				log.error("Error parsing publication map entry." + publication + "; Message: " + e.getMessage());
			}

		} else {
			log.error("Error parsing publication map entry." + publication + "; Message: "
					+ "Publication String count is empty or null." + stringCount);
		}

		return count;
	}
}
