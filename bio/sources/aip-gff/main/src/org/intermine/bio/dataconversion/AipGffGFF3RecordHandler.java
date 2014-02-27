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

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

import org.intermine.bio.io.gff3.GFF3Record;
import org.intermine.metadata.Model;
import org.intermine.xml.full.Item;
import org.intermine.util.StringUtil;

/**
 * A converter/retriever for the AipGff dataset via GFF files.
 */

public class AipGffGFF3RecordHandler extends GFF3RecordHandler
{

    /**
     * Create a new AipGffGFF3RecordHandler for the given data model.
     * @param model the model for which items will be created
     */
    public AipGffGFF3RecordHandler (Model model) {
        super(model);
        refsAndCollections.put("Exon", "transcripts");
        refsAndCollections.put("MRNA", "gene");
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
            // the key.   For example:
            //
            Item feature = getFeature();
            //     String symbol = record.getAttributes().get("symbol");
            //     feature.setAttrinte("symbol", symbol);
            //
            // Any new Items created can be stored by calling addItem().  For example:
            //
            //     String geneIdentifier = record.getAttributes().get("gene");
            //     gene = createItem("Gene");
            //     gene.setAttribute("primaryIdentifier", geneIdentifier);
            //     addItem(gene);
            //
            // You should make sure that new Items you create are unique, i.e. by storing in a map by
            // some identifier.
            String clsName = feature.getClassName();

            String regexp = "Gene|MRNA|MiRNA|NcRNA|RRNA|SnRNA|SnoRNA|TRNA|Pseudogene|PseudogenicTranscript|TransposableElementGene";
            Pattern p = Pattern.compile(regexp);
            Matcher m = p.matcher(clsName);
            if(m.find()) {
                if(record.getNote() != null){
                    feature.setAttribute("briefDescription", record.getNote());
                }
            }

            regexp = "Gene|MRNA|TransposableElement|TransposableElementGene";
            p = Pattern.compile(regexp);
            m = p.matcher(clsName);
            if(m.find()) {
                if(record.getAttributes().get("Name") != null){
                    String name = record.getAttributes().get("Name").iterator().next();
                    if(name != null){
                        regexp = "^AT[A-z0-9]{1}[A-z]+[0-9]+";
                        p = Pattern.compile(regexp);
                        m = p.matcher(name);
                        if(!m.find()){
                            feature.setAttribute("name", name);
                        }
                    }
                }
                if(record.getAttributes().get("symbol") != null){
                    String symbol = record.getAttributes().get("symbol").iterator().next();
                    if(symbol != null){
                        feature.setAttribute("symbol", symbol);
                    }
                }
                List<String> aliases = record.getAliases();
                if(aliases != null){
                    StringBuilder sb = new StringBuilder(aliases.get(0));
                    for (int i = 1; i < aliases.size(); i++){
                        sb.append(" ").append(aliases.get(i));
                    }
                    feature.setAttribute("alias", sb.toString());
                }
            }

            regexp = "Gene|MRNA|MiRNA|NcRNA|RRNA|SnRNA|SnoRNA|TRNA|PseudogenicTranscript";
            p = Pattern.compile(regexp);
            m = p.matcher(clsName);
            if(m.find()) {
                if(record.getAttributes().get("computational_description") != null){
                    String comp_descr = record.getAttributes().get("computational_description").iterator().next();
                    if(comp_descr != null){
                        feature.setAttribute("computationalDescription", comp_descr);
                    }
                }
                if(record.getAttributes().get("curator_summary") != null){
                    String cur_summary = record.getAttributes().get("curator_summary").iterator().next();
                    if(cur_summary != null){
                        feature.setAttribute("curatorSummary", cur_summary);
                    }
                }
            }

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
                            } else {
                                throw new RuntimeException("unknown external reference type: " + ref);
                            }
                        }
                    }
                }
            }

            if("MRNA".equals(clsName)) {
                if(record.getAttributes().get("conf_class") != null){
                    String conf_class = record.getAttributes().get("conf_class").iterator().next();
                    if(conf_class != null){
                        feature.setAttribute("confidenceClass", conf_class);
                    }
                }
                if(record.getAttributes().get("conf_rating") != null){
                    String conf_rating = record.getAttributes().get("conf_rating").iterator().next();
                    if(conf_rating != null){
                        feature.setAttribute("confidenceRating", conf_rating);
                    }
                }
            }
        }
}
