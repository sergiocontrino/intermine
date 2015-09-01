package org.intermine.bio.postprocess;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.intermine.api.InterMineAPI;
import org.intermine.bio.util.Constants;
import org.intermine.metadata.ConstraintOp;
import org.intermine.model.InterMineObject;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.ObjectStoreWriter;
import org.intermine.objectstore.intermine.ObjectStoreInterMineImpl;
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
import org.intermine.objectstore.query.SimpleConstraint;
import org.intermine.objectstore.query.SubqueryConstraint;
import org.intermine.postprocess.PostProcessor;
import org.thalemine.web.displayer.Gene;
import org.thalemine.web.displayer.Protein;
import org.thalemine.web.displayer.Publication;
import org.thalemine.web.displayer.Transcript;

public class AraportGFFPostProcess extends PostProcessor {

	private static final Logger log = Logger.getLogger(AraportGFFPostProcess.class);
	protected ObjectStore os;

	public AraportGFFPostProcess(ObjectStoreWriter osw) {

		super(osw);

	}

	@Override
	public void postProcess() throws ObjectStoreException {

		processGenesTranscriptsPublications();

	}

	private Query getGeneQuerySourceRecordsbyTranscripts() throws ObjectStoreException {

		log.info("Building of Query Gene Source Records To Validate/Transfer Publications from Transcripts has started.");

		Query outerQuery = new Query();

		// Create Outer Query Constraint
		ConstraintSet outerQueryCS = new ConstraintSet(ConstraintOp.AND);

		outerQuery.setDistinct(true);
		QueryClass qcPub = new QueryClass(Publication.class);
		QueryClass qcGenes = new QueryClass(Gene.class);
		QueryClass qcTranscript = new QueryClass(Transcript.class);

		// outer query from clause
		outerQuery.addFrom(qcPub);
		outerQuery.addFrom(qcTranscript);
		outerQuery.addFrom(qcGenes);

		// outer query select clause
		outerQuery.addToSelect(qcGenes);

		// join to collection
		QueryCollectionReference transcriptsGenePubCollection = new QueryCollectionReference(qcGenes, "transcripts");
		outerQueryCS.addConstraint(new ContainsConstraint(transcriptsGenePubCollection, ConstraintOp.CONTAINS,
				qcTranscript));

		QueryCollectionReference transcriptsPublCollection = new QueryCollectionReference(qcTranscript, "publications");
		outerQueryCS.addConstraint(new ContainsConstraint(transcriptsPublCollection, ConstraintOp.CONTAINS, qcPub));

		outerQuery.setConstraint(outerQueryCS);

		return outerQuery;

	}

	private Iterator<?> getGeneSourceIterator(final Query query) {

		((ObjectStoreInterMineImpl) os).precompute(q, Constants.PRECOMPUTE_CATEGORY);
		Results res = os.execute(query, 5000, true, false, true);
		return res.iterator();
	}

	private Iterator<?> getPublicationIterator(final Query query) {

		((ObjectStoreInterMineImpl) os).precompute(q, Constants.PRECOMPUTE_CATEGORY);
		Results res = os.execute(query, 5000, true, false, true);
		return res.iterator();
	}

	private void processGenesTranscriptsPublications() {

		Set<Gene> set = new HashSet<Gene>();
		Set<Publication> notExistingTranscriptsPublications = new HashSet<Publication>();
		Set<Publication> existingGenePublications = new HashSet<Publication>();
	
		long startTime = System.currentTimeMillis();

		osw.beginTransaction();

		Query query = getGeneQuerySourceRecordsbyTranscripts();
		Iterator<?> iterator = getGeneSourceIterator(query);

		int count = 0;
		Gene lastGene = null;

		while (iterator.hasNext()) {

			ResultsRow item = (ResultsRow) iterator.next();
			Gene gene = (Gene) item.get(0);

			InterMineObject object = (InterMineObject) gene;

			log.info("Processing Current Gene: = " + gene.getPrimaryIdentifier());

			notExistingTranscriptsPublications.clear();
			existingGenePublications.clear();

			existingGenePublications = gene.getPublications();

			log.info("Current Gene # Publication Count: = " + existingGenePublications.size());

			notExistingTranscriptsPublications = getPublications(object);

			log.info("Current Gene # Not Existing Publication Count: = " + notExistingTranscriptsPublications.size());

			if (notExistingTranscriptsPublications.siaze() > 0 && !notExistingTranscriptsPublications.isEmpty()) {
				log.info("Adding not existing pub to a gene publication collection.");
				existingGenePublications.addAll(notExistingTranscriptsPublications);
				log.info("Current Gene # Existing Publication Count After Merge: = " + existingGenePublications.size());

				// Attempt to store Gene Publication Collection

				try {

					Gene tempGene;
					tempGene = PostProcessUtil.cloneInterMineObject(gene);

					tempGene.setFieldValue("publications", existingGenePublications);

					log.info("Storing gene " + gene.getPrimaryIdentifier() + " with updated publications.");

					osw.store(tempGene);

				} catch (IllegalAccessException e) {
					throw new RuntimeException("Failed to clone InterMineObject: " + gene, e);
				}

			} else {
				log.info("Count of # Not Existing Transcripts Publication Size = 0. Nothing to merge!");
			}

		}

		osw.commitTransaction();

	}

	private Query getNotExistingTranscriptsPubbyGeneQuery(InterMineObject object) {

		log.info("Looking up non-existing transcripst publications for a gene: " + object);

		QueryClass qcPub = new QueryClass(Publication.class);
		QueryClass qcOtherGenes = null;
		QueryClass qcTranscript = new QueryClass(Transcript.class);
		QueryClass qcProtein = new QueryClass(Protein.class);

		Query outerQuery = new Query();

		// outer query constraints
		ConstraintSet outerQueryCS = new ConstraintSet(ConstraintOp.OR);
		outerQuery.setDistinct(true);

		// outer query from clause
		outerQuery.addFrom(qcPub);
		outerQuery.addFrom(qcTranscript);
		outerQuery.addFrom(qcOtherGenes);

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

		log.info("Retrieving Not Existing Gene Publications has started - Transcripts. Classes includes: Gene vs Transcripts .");

		// Gene subquery

		Query geneSubQuery = new Query();
		QueryClass qcPubGeneSQ = new QueryClass(Publication.class);
		geneSubQuery.alias(qcPubGeneSQ, "geneSQ");
		geneSubQuery.setDistinct(false);
		geneSubQuery.addFrom(qcPubGeneSQ);
		geneSubQuery.addFrom(qcOtherGenes);
		geneSubQuery.addToSelect(qcPubGeneSQ);

		ConstraintSet geneSubSetCS = new ConstraintSet(ConstraintOp.AND);

		// works for a single gene object
		QueryCollectionReference genePubCollection = new QueryCollectionReference(object, "publications");
		geneSubSetCS.addConstraint(new ContainsConstraint(genePubCollection, ConstraintOp.CONTAINS, qcPubGeneSQ));

		QueryCollectionReference geneClassPubCollection = new QueryCollectionReference(qcOtherGenes, "publications");
		geneSubSetCS.addConstraint(new ContainsConstraint(geneClassPubCollection, ConstraintOp.CONTAINS, qcPubGeneSQ));

		geneSubQuery.setConstraint(geneSubSetCS);

		outerQueryCS.addConstraint(new SubqueryConstraint(qcPub, ConstraintOp.NOT_IN, geneSubQuery));

		ConstraintSet outerQueryMainCS = new ConstraintSet(ConstraintOp.AND);

		QueryField geneIdField = new QueryField(qcOtherGenes, "id");
		QueryValue geneIdValue = new QueryValue(object.getId());
		SimpleConstraint geneIdCS = new SimpleConstraint(geneIdField, ConstraintOp.EQUALS, geneIdValue);

		QueryCollectionReference transcriptsGenePubCollection = new QueryCollectionReference(qcOtherGenes,
				"transcripts");
		outerQueryMainCS.addConstraint(new ContainsConstraint(transcriptsGenePubCollection, ConstraintOp.CONTAINS,
				qcTranscript));

		QueryCollectionReference transcriptsPublCollection = new QueryCollectionReference(qcTranscript, "publications");
		outerQueryMainCS.addConstraint(new ContainsConstraint(transcriptsPublCollection, ConstraintOp.CONTAINS, qcPub));

		outerQueryMainCS.addConstraint(geneIdCS);
		outerQueryMainCS.addConstraint(outerQueryCS);

		outerQuery.setConstraint(outerQueryMainCS);

		return outerQuery;

	}

	private Set<Publication> getPublications(InterMineObject object) throws Exception {

		Set<Publication> publications = new HashSet<Publication();
		Exception exception = null;

		long startTime = System.currentTimeMillis();

		Query query = getNotExistingTranscriptsPubbyGeneQuery(object);
		Iterator<?> iterator = getPublicationIterator(query);

		int itemCount = 0;
		
		try {

			if (query == null) {
				exception = new Exception("Publication Query cannot be null.");
				throw exception;
			}

			log.info("Input Publication Query: " + query.toString() + "Idl Query: " + query.getIqlQuery());

			while (iterator.hasNext()) {

				ResultsRow item = (ResultsRow) iterator.next();
				Publication pub = (Publication) item.get(0);

				log.info("Current Publication: = " + pub);
				
				itemCount++;
				
				log.info("Object:" + object + "; Current Item Count:" + itemCount);
				
				Object countObject = (Object) item.get(1);
				log.info("Count Object: = " + countObject);

				Long publicationCount = (Long) countObject;
				publications.add(pub);

			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error occurred while executing Publication Query." + " ; Message: " + exception.getMessage()
						+ "; Cause: " + exception.getCause());
				throw exception;
			} else {
				log.info("Publication Query has successfully completed. " + "; Result Set Size: " + publications.size()
						+ "; Object: " + object);
			}
		}

		return publications;

	}
}
