package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2011 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.intermine.bio.io.gff3.GFF3Record;
import org.intermine.metadata.Model;
import org.intermine.metadata.StringUtil;
import org.intermine.xml.full.Item;

/**
 * A converter/retriever for the AipGff dataset via GFF files.
 */

public class AipGffGFF3RecordHandler extends GFF3RecordHandler
{

    private static final Logger LOG = Logger.getLogger(AipGffGFF3RecordHandler.class);
    private final Map<String, Item> pubmedIdMap = new HashMap<String, Item>();
    private final Map<String, String> mrnaIdMap = new HashMap<String, String>();
    private final HashMap<String, ArrayList<String>> mrnaIdsMap = new HashMap<String, ArrayList<String>>();
    private final HashMap<String, ArrayList<String>> protMrnaMap = new HashMap<String, ArrayList<String>>();
    /**
     * Create a new AipGffGFF3RecordHandler for the given data model.
     * @param model the model for which items will be created
     */
    public AipGffGFF3RecordHandler (Model model) {
        super(model);
        refsAndCollections.put("MRNA", "gene");
        refsAndCollections.put("Exon", "transcripts");
        refsAndCollections.put("FivePrimeUTR", "mRNAs");
        refsAndCollections.put("ThreePrimeUTR", "mRNAs");
        refsAndCollections.put("TransposonFragment", "transposableelements");
        refsAndCollections.put("PseudogenicExon","pseudogenictranscripts");
        refsAndCollections.put("PseudogenicTranscript","pseudogene");
    }

    /**
     * {@inheritDoc}
     */
    @Override
        public void process(GFF3Record record) {
            // This method is called for every line of GFF3 file(s) being read.  Features and their
            // locations are already created but not stored so you can make changes here.  Attributes
            // are from the last column of the file are available in a map with the attribute name as
            // the key.
            Item feature = getFeature();
            String clsName = feature.getClassName();

            // For the following feature classes, check if the GFF3 attribute `full_name`, is defined
            // If true, assign its value to the InterMine attribute `name`
            String regexp = "Gene|MRNA|TransposableElement|TransposableElementGene";
            Pattern p = Pattern.compile(regexp);
            Matcher m = p.matcher(clsName);
            if(m.find()) {
                if(record.getAttributes().get("full_name") != null){
                    String full_name = record.getAttributes().get("full_name").iterator().next();
                    feature.setAttribute("name", full_name);
                }
            }

            // For the following feature classes, check if the Dbxref(s) to the TAIR
            // `locus` and `gene` are defined. If true, assign their values to the
            // InterMine attribute `secondaryIdentifier`
            //
            // Also,  check if there are Dbxref(s) to PubMed identifiers. If true,
            // assign their values to the `Publication` entity of the data model
            regexp = "Gene|MRNA|Pseudogene|TransposableElementGene";
            p = Pattern.compile(regexp);
            m = p.matcher(clsName);
            if(m.find()) {
                String primaryIdentifier = feature.getAttribute("primaryIdentifier").getValue();

                List<String> dbxrefs = record.getDbxrefs();
                if (dbxrefs != null) {
                    Iterator<String> dbxrefsIter = dbxrefs.iterator();

                    while (dbxrefsIter.hasNext()) {
                        String dbxref = dbxrefsIter.next();

                        List<String> refList = new ArrayList<String>(
                                Arrays.asList(StringUtil.split(dbxref, ",")));
                        for (String ref : refList) {
                            ref = ref.trim();
                            int colonIndex = ref.indexOf(":");
                            if (colonIndex == -1) {
                                throw new RuntimeException("external reference not understood: " + ref);
                            }

                            if (ref.startsWith("gene:") || ref.startsWith("locus:")) {
                                feature.setAttribute("secondaryIdentifier", ref);
                            } else if(ref.startsWith("PMID:")) {
                                String pmid = ref.substring(colonIndex + 1);
                                Item pubmedItem;
                                if (pubmedIdMap.containsKey(pmid)) {
                                    pubmedItem = pubmedIdMap.get(pmid);
                                } else {
                                    pubmedItem = converter.createItem("Publication");
                                    pubmedIdMap.put(pmid, pubmedItem);
                                    pubmedItem.setAttribute("pubMedId", pmid);
                                    addItem(pubmedItem);
                                }
                                addPublication(pubmedItem);
                            } else if(ref.startsWith("UniProt:")) {
                                String uniprotAcc = ref.substring(colonIndex + 1);

                                if (protMrnaMap.get(uniprotAcc) == null) protMrnaMap.put(uniprotAcc, new ArrayList<String>());
                                protMrnaMap.get(uniprotAcc).add(primaryIdentifier);
                            } else {
                                throw new RuntimeException("unknown external reference type: " + ref);
                            }
                        }
                    }
                }
            }

            // For the following feature classes, check if the GFF3 attribute `Name` is defined.
            // If true, assign its value to the InterMine `symbol` attribute
            regexp = "Exon|CDS|UTR|Fragment";
            p = Pattern.compile(regexp);
            m = p.matcher(clsName);
            if(m.find()) {
                if(record.getAttributes().get("Name") != null){
                    String name = record.getAttributes().get("Name").iterator().next();
                    feature.setAttribute("symbol", name);
                }
            }

            // For the MRNA feature class, store the InterMineObject ID in a map for
            // use with the Protein feature loading
            if(clsName.equals("MRNA")) {
                String primaryIdentifier = feature.getAttribute("primaryIdentifier").getValue();
                mrnaIdMap.put(primaryIdentifier, feature.getIdentifier());
            }

            // For the Protein feature class, check if the Dbxref(s) to the UniProt
            // are defined. If true, assign their values to the InterMine attribute
            // `primaryAccession`
            if(clsName.equals("Protein")) {
                String primaryIdentifier = feature.getAttribute("primaryIdentifier").getValue();
                primaryIdentifier = primaryIdentifier.replace("-Protein", "");  // strip "-Protein" suffix
                feature.setAttribute("primaryIdentifier", primaryIdentifier);

                String mrnaId = getMRNA(primaryIdentifier);

                if(mrnaId == null) {
                    throw new RuntimeException("Protein does not have corresponding mRNA entity: " + primaryIdentifier);
                }

                List<String> dbxrefs = record.getDbxrefs();
                if (dbxrefs != null) {
                    Iterator<String> dbxrefsIter = dbxrefs.iterator();

                    while (dbxrefsIter.hasNext()) {
                        String dbxref = dbxrefsIter.next();

                        List<String> refList = new ArrayList<String>(
                                Arrays.asList(StringUtil.split(dbxref, ",")));
                        for (String ref : refList) {
                            ref = ref.trim();
                            int colonIndex = ref.indexOf(":");
                            if (colonIndex == -1) {
                                throw new RuntimeException("external reference not understood: " + ref);
                            }

                            if(ref.startsWith("UniProt:")) {
                                String dbxrefValue = ref.substring(colonIndex + 1);
                                //LOG.info("Processing UniProt dbxref: " + dbxrefValue + " for Transcript: " + primaryIdentifier);
                                String attrName1 = "primaryAccession";

                                // store mapping of uniprot to Araport InterMine IDs in an ArrayList
                                if (mrnaIdsMap.get(dbxrefValue) == null) mrnaIdsMap.put(dbxrefValue, new ArrayList<String>());
                                mrnaIdsMap.get(dbxrefValue).add(mrnaId);

                                // skip loading duplicate protein entities
                                if (protMrnaMap.get(dbxrefValue).size() != mrnaIdsMap.get(dbxrefValue).size()) {
                                    //clear();
                                    removeFeature();
                                    return;
                                }

                                // set the Transcripts and mRNAs collections with above instantiated ArrayList
                                feature.setCollection("transcripts", mrnaIdsMap.get(dbxrefValue));
                                feature.setCollection("mRNA", mrnaIdsMap.get(dbxrefValue));

                                for (String mrnaPrimaryId : protMrnaMap.get(dbxrefValue)) {
                                    if (mrnaPrimaryId.equals(primaryIdentifier)) {
                                        continue;   // skip loading current feature ID as synonym
                                    }
                                    // add Araport IDs as synonyms of Protein
                                    Item synonym = converter.createItem("Synonym");
                                    synonym.setAttribute("value", mrnaPrimaryId);
                                    synonym.setReference("subject", feature.getIdentifier());
                                    items.put(synonym.getIdentifier(), synonym);
                                }
                                feature.setAttribute(attrName1, dbxrefValue);
                            } else {
                                throw new RuntimeException("unknown external reference type: " + ref);
                            }
                        }
                    }
                }
            }
        }

    /*
     * retrieve InterMine ID of mRNA from HashMap using Araport Transcript ID as key
     */
    private String getMRNA(String primaryIdentifier) {
        String mrnaId = mrnaIdMap.get(primaryIdentifier);
        if (mrnaId != null) {
            return mrnaId;
        }
        return null;
    }

    /**
     * Return false - skip loading Locations for Protein features
     * Return true  - load Locations for all other types of features
     * {@inheritDoc}
     */
    @Override
    protected boolean createLocations(@SuppressWarnings("unused") GFF3Record record) {
        String type = record.getType();
        if (type.equals("protein")) {
            return false;
        }
        return true;
    }
}
