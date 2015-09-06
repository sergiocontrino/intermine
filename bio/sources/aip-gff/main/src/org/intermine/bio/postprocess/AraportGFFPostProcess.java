package org.intermine.bio.postprocess;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.intermine.bio.util.Constants;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
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
import org.intermine.model.bio.Gene;
import org.intermine.model.bio.Protein;
import org.intermine.model.bio.Publication;
import org.intermine.model.bio.Transcript;

public class AraportGFFPostProcess extends PostProcessor {

	private static final Logger log = Logger.getLogger(AraportGFFPostProcess.class);
	protected ObjectStore os;

	public AraportGFFPostProcess(ObjectStoreWriter osw) {

		super(osw);
		this.os = osw.getObjectStore();

	}

	@Override
	public void postProcess() throws ObjectStoreException {

		log.info("Araport Gff Postprocessor has started.");

		try {
			processGenesTranscriptsPublications();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occrured during araport gff postrprocessing." + ";Message:" + e.getMessage());
		}

		log.info("Araport Gff Postprocessor has completed.");

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

		log.info("Building of Query Gene Source Records To Validate/Transfer Publications from Transcripts has completed.");

		return outerQuery;

	}

	private Iterator<?> getGeneSourceIterator(final Query query) throws ObjectStoreException {

		log.info("In getGeneSourceIterator start");

		if (osw == null) {
			log.info("OSW Is Null");
		} else {
			log.info("OSW Is NOT Null");
		}

		if (query == null) {
			log.info("Query Is Null");
		} else {
			log.info("Query Is NOT Null");
		}

		/*
		 * if (os == null) { os = osw.getObjectStore(); }
		 */

		ObjectStore os1 = osw.getObjectStore();

		((ObjectStoreInterMineImpl) os1).precompute(query, Constants.PRECOMPUTE_CATEGORY);
		Results res = os1.execute(query, 5000, true, false, true);


		if (res != null) {
			log.info("Gene Source Result Set Size:" + res.size());
		}

		log.info("In getGeneSourceIterator end");
		return res.iterator();
	}

	private Iterator<?> getPublicationIterator(final Query query) throws ObjectStoreException {

		log.info("In getPublicationIterator start");

		if (osw == null) {
			log.info("OSW Is Null");
		} else {
			log.info("OSW Is NOT Null");
		}

		ObjectStore os1 = osw.getObjectStore();

		((ObjectStoreInterMineImpl) os1).precompute(query, Constants.PRECOMPUTE_CATEGORY);
		Results res = os1.execute(query, 5000, true, false, true);

		if (res != null) {
			log.info("Publications Result Set Size:" + res.size());
		}

		log.info("In getPublicationIterator end");
		return res.iterator();
	}

	private void processGenesTranscriptsPublications() throws Exception, ObjectStoreException {

		log.info("ProcessGenesTranscriptsPublications has started.");

		Exception exception = null;

		Set<Gene> set = new HashSet<Gene>();

		long startTime = System.currentTimeMillis();

		log.info("ProcessGenesTranscriptsPublications before query");

		Query query = getGeneQuerySourceRecordsbyTranscripts();

		log.info("ProcessGenesTranscriptsPublications after query");

		log.info("Gene Source Query:" + query.toString());

		log.info("Before entering iterator");

		Iterator<?> iterator = getGeneSourceIterator(query);

		log.info("After entering iterator");

		int count = 0;
		int pubAddedCount = 0;
		
		osw.beginTransaction();

		while (iterator.hasNext()) {

			log.info("Iterator First Line");

			ResultsRow item = (ResultsRow) iterator.next();

			log.info("Iterator 2 Line");

			Gene gene = (Gene) item.get(0);

			log.info("Iterator 3 Line");

			InterMineObject object = (InterMineObject) gene;

			log.info("Iterator 4 Line");

			log.info("Processing Current Gene: = " + gene.getPrimaryIdentifier());
			
			count++;

			log.info("Iterator 5 Line");

			Set<Publication> notExistingTranscriptsPublications = new HashSet<Publication>();

			log.info("Iterator 6 Line");

			Set<Publication> existingGenePublications = new HashSet<Publication>();

			log.info("Iterator 7 Line");

			existingGenePublications = gene.getPublications();

			log.info("Iterator 8 Line");

			log.info("Current Gene # Publication Count: = " + existingGenePublications.size());

			notExistingTranscriptsPublications = getPublications(object);

			log.info("Iterator 8 Line");

			log.info("Current Gene # Not Existing Publication Count: = " + notExistingTranscriptsPublications.size()
					+ "; Gene:" + gene.getPrimaryIdentifier());

			String destClassName = "Gene";
			String collectionName = "publications";

			if (notExistingTranscriptsPublications.size() > 0 && !notExistingTranscriptsPublications.isEmpty()) {
				log.info("Adding not existing pub to a gene publication collection.");
				// existingGenePublications.addAll(notExistingTranscriptsPublications);
				// log.info("Current Gene Expected of # Existing Publication Count After the Merge: = "
				// + existingGenePublications.size());

				// Attempt to store Gene Publication Collection

				try {

					for (Publication pubItem : notExistingTranscriptsPublications) {

						log.info("Attempt to Store Not Existing Pub: Processing Publication for a gene. " + pubItem);

						InterMineObject pubObject = (InterMineObject) pubItem;
						insertPublicationCollectionField(object, pubObject, destClassName, collectionName);

						log.info("Iterator 9 Line");
					}

				} catch (Exception e) {
					exception = e;
				} finally {
					if (exception != null) {
						log.error("Error occurred while processing gene publication collection." + "; Gene:" + gene
								+ "; Message: " + exception.getMessage() + "; Cause: " + exception.getCause());
						exception.printStackTrace();

					} else {
						log.info("Publication successfully added to the gene collection.");
						pubAddedCount++;
					}
				}

			} else {
				log.info("Count of # Not Existing Transcripts Publication Size = 0. Nothing to merge!");
			}

			log.info("Processed Gene Count:" + count);
		}
		osw.commitTransaction();
		
		log.info("Total Processed Gene Count:" + count);
		log.info("Total Publications Added Processed:" + pubAddedCount);
		
	}

	private Query getNotExistingTranscriptsPubbyGeneQuery(InterMineObject object) {

		log.info("Looking up non-existing transcripst publications for a gene: " + object);

		QueryClass qcPub = new QueryClass(Publication.class);
		QueryClass qcOtherGenes = new QueryClass(Gene.class);
		QueryClass qcTranscript = new QueryClass(Transcript.class);

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

		Set<Publication> publications = new HashSet<Publication>();
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

	private CollectionDescriptor getCollectionDescriptor(final String className, final String collectionName) {

		ClassDescriptor classDesc;
		CollectionDescriptor colDesc = null;
		classDesc = osw.getModel().getClassDescriptorByName(className);
		String classDescAsStr = null;
		Map<String, CollectionDescriptor> collectionDescMap = new LinkedHashMap<String, CollectionDescriptor>();

		if (classDesc != null) {
			classDescAsStr = classDesc.getName();

			//log.info("Class Descriptor:" + classDescAsStr);

			Set<CollectionDescriptor> collectionDescGene = classDesc.getAllCollectionDescriptors();

			// Adding Class Collection Descriptors to the Map
			//log.info("Adding Class Collection Descriptors to the Map:");

			for (CollectionDescriptor item : collectionDescGene) {

			//	log.info("Collection Decscriptor Name: " + item.toString());

				boolean manyToManyC = false;

				if (item.relationType() == CollectionDescriptor.M_N_RELATION) {
					manyToManyC = true;
				}

				collectionDescMap.put(item.getName(), item);

				//log.info("Collection Type Many To Many ?: " + manyToManyC);
			}

			if (!collectionDescMap.isEmpty() && collectionDescMap.size() > 0) {
				if (collectionDescMap.containsKey(collectionName)) {
					colDesc = collectionDescMap.get(collectionName);
				}
			}
		}

		if (colDesc != null) {
			log.info("Class Collection Desc: " + "; Class: " + "; Collection Desc: " + colDesc.getName());
		}
		return colDesc;
	}

	private void insertPublicationCollectionField(InterMineObject destObject, InterMineObject sourceObject,
			final String destClassName, final String collectionName) throws Exception {

		Exception exception = null;
		String errorMessage = null;

		try {

			//osw.beginTransaction();

			CollectionDescriptor collectionDesc = getCollectionDescriptor(destClassName, collectionName);
			ClassDescriptor classDesc = osw.getModel().getClassDescriptorByName(destClassName);

			// if this is a many to many collection we can use
			// ObjectStore.addToCollection which will
			// write directly to the database.
			boolean manyToMany = false;

			if (collectionDesc == null) {

				errorMessage = "Cannot find collection " + collectionName + " for the class " + destClassName;
				exception = new Exception(errorMessage);
				log.error(errorMessage);
				throw exception;

			}

			log.info("Found Collection Descriptor: " + "; Class: " + "; Collection Desc: " + collectionDesc.getName());

			if (collectionDesc.relationType() == CollectionDescriptor.M_N_RELATION) {
				manyToMany = true;
			}

			log.info("Collection Type Many To Many ?: " + manyToMany);

			if (manyToMany) {
				log.info("Adding Pub to Gene/Pub Collection before");
				osw.addToCollection(destObject.getId(), classDesc.getType(), collectionName, sourceObject.getId());
				log.info("Adding Pub to Gene/Pub Collection after");
			} else {

				// InterMineObject tempObject =
				// PostProcessUtil.cloneInterMineObject(destObject);
				// tempObject.setFieldValue(collectionName, collection);
				// osw.store(tempObject);

			}

			//osw.commitTransaction();

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				exception.printStackTrace();
				log.error("Error occurred during persistence of collection for object: " + destObject.toString()
						+ "; Collection Name: " + collectionName + ";Message:" + exception.getMessage() + "; Cause:" + exception.getCause());
				throw exception;
			} else {
				log.info("Element of Collection " + collectionName + " successfully stored in the database."
						+ "; Dest Object:" + destObject.toString() + "; Source Object:" + sourceObject.toString());
			}
		}

	}
}
