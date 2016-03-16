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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.intermine.bio.io.gff3.GFF3Record;
import org.intermine.metadata.Model;
import org.intermine.metadata.StringUtil;
import org.intermine.xml.full.Item;

/**
 * A converter/retriever for the AipGff dataset via GFF files.
 */

public class AipGffGFF3RecordHandler extends GFF3RecordHandler
{

    private final Map<String, Item> pubmedIdMap = new HashMap<String, Item>();
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

            // For the Protein feature class, check if the Dbxref(s) to the UniProt
            // are defined. If true, assign their values to the InterMine attribute
            // `primaryAccession`
            if(clsName.equals("Protein")) {
                String primaryIdentifier = feature.getAttribute("primaryIdentifier").getValue();
                primaryIdentifier = primaryIdentifier.replace("-Protein", "");  // strip "-Protein" suffix
                feature.setAttribute("primaryIdentifier", primaryIdentifier);

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
                                String attrName1 = "primaryAccession";
                                String attrName2 = "secondaryIdentifier";
                                String dbxrefValue = ref.substring(colonIndex + 1);
                                if(dbxrefValue.contains("_ARATH")) {
                                    attrName1 = "primaryIdentifier";
                                    attrName2 = "symbol";
                                }
                                feature.setAttribute(attrName1, dbxrefValue);
                                feature.setAttribute(attrName2, dbxrefValue);
                            } else {
                                throw new RuntimeException("unknown external reference type: " + ref);
                            }
                        }
                    }
                }
            }
        }
}
