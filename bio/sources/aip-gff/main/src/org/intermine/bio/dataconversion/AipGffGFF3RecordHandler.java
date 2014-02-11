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

import org.intermine.bio.io.gff3.GFF3Record;
import org.intermine.metadata.Model;
import org.intermine.xml.full.Item;
import java.util.List;

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
            if("Gene".equals(clsName) || "MRNA".equals(clsName) || "TransposableElementGene".equals(clsName)|| "Pseudogene".equals(clsName) || "PseudogenicTranscript".equals(clsName)){
                if(record.getAttributes().get("Note") != null){
                    String note = record.getAttributes().get("Note").iterator().next();
                    if(note != null){
                        feature.setAttribute("Note", note);
                    }
                }
            }
            if("TransposableElement".equals(clsName)|| "Gene".equals(clsName) || "MRNA".equals(clsName)){
                List<String> aliases = record.getAliases();
                if(aliases != null){
                    StringBuilder sb = new StringBuilder(aliases.get(0));
                    for (int i=1; i < aliases.size(); i++){
                        sb.append(" ").append(aliases.get(i));
                    }
                    feature.setAttribute("Alias",sb.toString());
                }
            }
            if("MRNA".equals(clsName) || "Gene".equals(clsName)){
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



        }
}
