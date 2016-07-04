package org.intermine.bio.postprocess;

/*
 * Copyright (C) 2002-2016 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.Model;
import org.intermine.metadata.TypeUtil;
import org.intermine.model.InterMineObject;
import org.intermine.model.bio.OntologyTerm;
import org.intermine.model.bio.SOTerm;
import org.intermine.objectstore.ObjectStore;
import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.ObjectStoreWriter;
import org.intermine.objectstore.query.ConstraintSet;
import org.intermine.objectstore.query.ContainsConstraint;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.QueryObjectReference;
import org.intermine.objectstore.query.Results;
import org.intermine.objectstore.query.ResultsRow;

/**
 * Populate the SequenceFeature.childFeatures() collection for: Gene, Transcript, Exon
 * Only used for JBrowse
 *
 * @author Julie Sullivan
 */
public class PopulateChildFeatures
{
    private static final Logger LOG = Logger.getLogger(PopulateChildFeatures.class);
    protected ObjectStoreWriter osw;
    private Model model;
    private static final String TARGET_COLLECTION = "childFeatures";
    private Map<String, Set<CollectionHolder>> parentToChildren
        = new HashMap<String, Set<CollectionHolder>>();

    private Map<String,String> parNochild = new HashMap<String, String>();

    /**
     * Construct with an ObjectStoreWriter, read and write from same ObjectStore
     * @param osw an ObjectStore to write to
     */
    public PopulateChildFeatures(ObjectStoreWriter osw) {
        this.osw = osw;
        this.model = Model.getInstanceByName("genomic");
    }

    /**
     * Populate the SequenceFeature.locatedFeatures() collection for: Gene, Transcript, Exon
     * and CDS
     * @throws Exception if anything goes wrong
     */
    @SuppressWarnings("unchecked")
    public void populateCollection() throws Exception {
        LOG.info("B4  populateSOT..-------------------");
        Map<String, SOTerm> soTerms = populateSOTermMap(osw);
        LOG.info("B4  getAllPar..-------------------");
        Query q = getAllParents();
        Results res = osw.getObjectStore().execute(q);
        LOG.info("PP parent size " + res.size());
        Iterator<Object> resIter = res.iterator();
        osw.beginTransaction();
        int parentCount = 0;
        int childCount = 0;
        int skipCountSO = 0;
        int skipCountPA = 0;

        while (resIter.hasNext()) {
            ResultsRow<InterMineObject> rr = (ResultsRow<InterMineObject>) resIter.next();
            InterMineObject parent = rr.get(0);
            SOTerm soTerm = (SOTerm) rr.get(1);
            if (soTerm.getClass().getSimpleName().toLowerCase().equals("probe")) {
                skipCountSO++;
                continue;
            }
            if (soTerm.getClass().getSimpleName().toLowerCase().equals("genotype")) {
                skipCountSO++;
                continue;
            }
            if (soTerm.getClass().getSimpleName().toLowerCase().equals("allele")) {
                skipCountSO++;
                continue;
            }
            if (soTerm.getClass().getSimpleName().toLowerCase().equals("intron")) {
                skipCountSO++;
                continue;
            }

            if (parent.getClass().getSimpleName().toLowerCase().equals("probe")) {
                skipCountPA++;
                continue;
            }
            if (soTerm.getClass().getSimpleName().toLowerCase().equals("genotype")) {
                skipCountPA++;
                continue;
            }
            if (soTerm.getClass().getSimpleName().toLowerCase().equals("allele")) {
                skipCountPA++;
                continue;
            }
            if (soTerm.getClass().getSimpleName().toLowerCase().equals("intron")) {
                skipCountPA++;
                continue;
            }

            InterMineObject o = PostProcessUtil.cloneInterMineObject(parent);
            Set<InterMineObject> newCollection = getChildFeatures(soTerms, soTerm, o);
            if (newCollection != null && !newCollection.isEmpty()) {
                o.setFieldValue(TARGET_COLLECTION, newCollection);
                osw.store(o);
                parentCount++;
                childCount += newCollection.size();
            }
        }
        osw.commitTransaction();
        LOG.info("Stored " + childCount + " child features for " + parentCount
                + " parent features. ");
        //+ skipCountSO + " records skipped + " + skipCountPA);
        LOG.info("lonely parents map: " + parNochild);
    }

    // for each collection in this class (e.g. Gene), test if it's a child feature
    @SuppressWarnings("unchecked")
    private Set<InterMineObject> getChildFeatures(Map<String, SOTerm> soTerms, SOTerm soTerm,
            InterMineObject o) {
        //LOG.info("IN getChildFeatures ================");
        // e.g. gene
        String parentSOTerm = soTerm.getName();

        // if we have not seen this class before, set relationships
        if (parentToChildren.get(parentSOTerm) == null) {
            populateParentChildMap(soTerms, parentSOTerm);
        }

        Set<InterMineObject> newCollection = new HashSet<InterMineObject>();

        Set<CollectionHolder> childHolders = parentToChildren.get(parentSOTerm);
        if (childHolders == null) {
            return null;
        }
        for (CollectionHolder h : childHolders) {
            String childCollectionName = h.getCollectionName();
            String childClassName = h.getClassName();
            try {
                Set<InterMineObject> childObjects
                    = (Set<InterMineObject>) o.getFieldValue(childCollectionName);
                newCollection.addAll(childObjects);
            } catch (IllegalAccessException e) {
                LOG.error("couldn't set relationship between " + parentSOTerm + " and "
                        + childClassName);
                return null;
            }
        }
        return newCollection;
    }

    private void populateParentChildMap(Map<String, SOTerm> soTerms, String parentSOTermName) {
        String parentClsName = TypeUtil.javaiseClassName(parentSOTermName);
        LOG.info("PARENT CLASS " + parentClsName + " (" + parentSOTermName + ")");
        ClassDescriptor cd = model.getClassDescriptorByName(parentClsName);
        if (cd == null) {
            LOG.error("couldn't find class in model:" + parentClsName);
            return;
        }
        Class<?> parentClass = cd.getType();

        // all intermine collections for gene
        Map<String, Class<?>> childCollections = model.getCollectionsForClass(parentClass);
        Set<CollectionHolder> children = new HashSet<CollectionHolder>();

        // for each collection, see if this is a child class
        for (Map.Entry<String, Class<?>> entry : childCollections.entrySet()) {

            String childCollectionName = entry.getKey();
            String childClassName = entry.getValue().getSimpleName();

            // TODO use same method as in the oboparser
            // is this a child collection? e.g. transcript
//            LOG.info("CHILD CLASS pre " + childClassName + " toLower -> "
//                    + childClassName.toLowerCase());

            SOTerm childSOTerm = null;

            if (childClassName.contains("CDS")) {
                childSOTerm = soTerms.get(childClassName);
            } else {
            //SOTerm childSOTerm = soTerms.get(childClassName.toLowerCase());
                childSOTerm = soTerms.get(childClassName.toLowerCase());
            }
            if (childSOTerm == null) {
//                parNochild.put(parentClsName, childClassName);
                //LOG.warn("NULL child! " + childClassName);
                // for testing
                continue;
            }

            LOG.info("CHILD CLASS " + childClassName + " (" + childSOTerm.getName() + ") :"
                    + childCollectionName);

            // is gene in transcript parents collection
            // exon.parents() contains transcript, but we need to match on mRNA which is a
            // subclass of transcript

            // loop through all parents
            for (OntologyTerm parent : childSOTerm.getParents()) {
                if (parent.getName().equals(parentSOTermName)) {
                    CollectionHolder h = new CollectionHolder(childClassName, childCollectionName);
                    children.add(h);
                }
            }
            // check for superclasses too
            ClassDescriptor parentClassDescr = model.getClassDescriptorByName(parentClsName);
            Set<String> parentInterMineClassNames = parentClassDescr.getSuperclassNames();

            for (String superParent : parentInterMineClassNames) {
                if (!superParent.equalsIgnoreCase("SequenceFeature")) {
                    CollectionHolder h = new CollectionHolder(childClassName, childCollectionName);
                    children.add(h);
                }
            }
        }
        if (children.size() > 0) {
            LOG.info("Adding " + children.size() + " children to parent class "
                    + parentSOTermName);
            parentToChildren.put(parentSOTermName, children);
        }
    }

    /**
     * @param os object store
     * @return map of name to so term
     * @throws ObjectStoreException if something goes wrong
     */
    protected Map<String, SOTerm> populateSOTermMap(ObjectStore os) throws ObjectStoreException {
        Map<String, SOTerm> soTerms = new HashMap<String, SOTerm>();
        Query q = new Query();
        q.setDistinct(false);

        QueryClass qcSOTerm = new QueryClass(SOTerm.class);
        q.addToSelect(qcSOTerm);
        q.addFrom(qcSOTerm);
        q.addToOrderBy(qcSOTerm);

        Results res = os.execute(q);

        Iterator it = res.iterator();

        while (it.hasNext()) {
            ResultsRow<InterMineObject> rr = (ResultsRow<InterMineObject>) it.next();
            SOTerm soTerm = (SOTerm) rr.get(0);
            soTerms.put(soTerm.getName(), soTerm);
            LOG.info("PPthis: " + soTerm.getName());
        }
        LOG.info("PP populateSOTermMap: " + soTerms.keySet() + " size " + soTerms.keySet().size());
        return soTerms;
    }

    /**
     * @return query to get all parent so terms
     */
    protected Query getAllParents() {
        Query q = new Query();
        q.setDistinct(false);

        QueryClass qcFeature =
                new QueryClass(model.getClassDescriptorByName("SequenceFeature").getType());

        q.addToSelect(qcFeature);
        q.addFrom(qcFeature);

        QueryClass qcSOTerm = new QueryClass(OntologyTerm.class);
        q.addToSelect(qcSOTerm);
        q.addFrom(qcSOTerm);
        q.addToOrderBy(qcSOTerm);

        ConstraintSet cs = new ConstraintSet(ConstraintOp.AND);

        QueryObjectReference ref1 = new QueryObjectReference(qcFeature, "sequenceOntologyTerm");
        cs.addConstraint(new ContainsConstraint(ref1, ConstraintOp.CONTAINS, qcSOTerm));

        // Set the constraint of the query
        q.setConstraint(cs);

//        forCheck.put(qcFeature.getClass().getSimpleName(), qcSOTerm.getClass().getSimpleName());

        return q;
    }

    // holds the class name, e.g. transcript and the collection name, e.g. transcripts.
    // might not be necessary for most collections but matters for MRNAs, etc.
    private class CollectionHolder
    {
        private String className;
        private String collectionName;

        protected CollectionHolder(String className, String collectionName) {
            this.className = className;
            this.collectionName = collectionName;
        }

        protected String getClassName() {
            return className;
        }

        protected String getCollectionName() {
            return collectionName;
        }
    }
}
