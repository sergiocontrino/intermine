<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>


<!-- dataCategories -->


<html:xhtml/>

<div class="body">

<%--
<im:boxarea title="Data" stylename="plainbox">
<p>ThaleMine integrates data from a large number of sources into a single data warehouse.  This page lists the data that are included in the current release.  Many more data sets will be added in future releases, please contact us if there are any particular data you would like to see included.</p>
</im:boxarea>
--%>

<im:boxarea title="Data" stylename="yellbox">
<p>ThaleMine integrates data from a large number of sources into a single data warehouse.</p>
<p>This page lists the data that are included in the current release and it is manually curated; its contents are not indexed in our keyword search.</p>
<p>More data sets will be added in future releases, please <a href="mailto:araport@jcvi.org?Subject=Data request"> contact us </a> if there are any particular data you would like to see included.</p>
</im:boxarea>

<br/>
<table cellpadding="0" cellspacing="0" border="0" class="dbsources">
  <tr>
    <th>Data Category</th>
    <th>Data</th>
    <th>Source</th>
    <th>PubMed</th>
  </tr>

  <tr>
    <td rowspan="2" class="leftcol"><h2><p>Genes</p></h2></td>
    <td>TAIR10 Genome release builds upon gene structures of previous TAIR9 release using RNA-seq and proteomics datasets as well as manual updates, informed by cross species alignments, peptides and community input regarding missing and incorrectly annotated genes.</td>
    <td><a href="http://www.arabidopsis.org/portals/genAnnotation/gene_structural_annotation/genome_annotation.jsp" target="_blank" class="extlink">TAIR</a>- Release TAIR10 (03/31/14)</td>
    <td>Arabidopsis Genome Initiative - <a href="http://www.ncbi.nlm.nih.gov/pubmed/11130711" target="_blank" class="extlink">PubMed: 11130711</a>
  </tr>

  <tr>
    <td>GeneRIF - Functional annotation added by scientists to genes described in NCBI Gene</td>
    <td><a href="http://www.ncbi.nlm.nih.gov/gene/about-generif" target="_blank" class="extlink">GeneRIF</a>(04/20/2015)</td>
    <td>Maglott et al - <a href="http://www.ncbi.nlm.nih.gov/pubmed/17148475" target="_blank" class="extlink">PubMed: 17148475</a></td>
  </tr>

  <tr>
    <td rowspan="2" class="leftcol"><h2><p>Proteins</p></h2></td>
    <td>Proteins from UniProt (trEMBL and SwissProt)</td>
    <td><a href="http://www.ebi.uniprot.org/index.shtml" target="_blank" class="extlink">UniProt</a>- Release 2015_05</td>
    <td>UniProt Consortium - <a href="http://www.ncbi.nlm.nih.gov/pubmed/17142230" target="_blank" class="extlink">PubMed: 17142230</a></td>
  </tr>
  <tr>
    <td>Protein family and domain assignments to proteins</td>
    <td><a href="http://www.ebi.ac.uk/interpro" target="_blank" class="extlink">InterPro</a>(v51.0)</td>
    <td>Mulder et al - <a href="http://www.ncbi.nlm.nih.gov/pubmed/17202162" target="_blank" class="extlink">PubMed: 17202162</a></td>
  </tr>

  <tr>
    <td rowspan="2" class="leftcol"><h2><p>Homology</p></h2></td>
    <td>Orthologue and paralogue relationships based on the inferred speciation and gene duplication events in the phylogenetic tree.</td>
    <td><a href="http://www.pantherdb.org" target="_blank" class="extlink">Panther</a>- Release 10.0, 15 May 2015</td>
    <td>Mi et al - <a href="http://www.ncbi.nlm.nih.gov/pubmed/23193289 " target="_blank" class="extlink">PubMed: 23193289</a></td>
  </tr>
  <tr>
    <td>Phytozome Homologs Generated with InParanoid</td>
    <td><a href="http://phytozome.jgi.doe.gov/phytomine" target="_blank" class="extlink">Phytozome</a>- v10.2.1</td>
    <td>Goodstein et al - <a href="http://www.ncbi.nlm.nih.gov/pubmed/22110026" target="_blank" class="extlink">PubMed: 22110026</a></td>
  </tr>

  <tr>
    <td rowspan="3" class="leftcol"><h2><p>Gene Ontology</p></h2></td>
    <td>GO annotations made by TAIR curators, TIGR Arabidopsis annotation effort</td>
    <td><a href="http://www.geneontology.org" target="_blank" class="extlink">Gene Ontology Site</a>- 31 Mar 2015</td>
    <td>Berardini et al., 2004 - <a href="http://www.ncbi.nlm.nih.gov" target="_blank" class="extlink">PubMed: 15173566</a><br />
        Gene Ontology Consortium - <a href="http://www.ncbi.nlm.nih.gov/pubmed/10802651" target="_blank" class="extlink">PubMed:10802651</a></td>
  </tr>
  <tr>
    <td>Several electronic and manual GO annotation methods utilized by UniProt</td>
    <td><a href="http://www.ebi.ac.uk/GOA/arabidopsis_release" target="_blank" class="extlink">UniProt GOA</a>- Release 2015_05</td>
    <td>Camon et al - <a href="http://www.ncbi.nlm.nih.gov/pubmed/14681408" target="_blank" class="extlink">PubMed: 14681408</a></td>
  </tr>
  <tr>
    <td>InterPro domains to GO terms</td>
    <td><a href="http://www.ebi.ac.uk/interpro" target="_blank" class="extlink">InterPro</a>(from <a href="http://www.geneontology.org" target="_blank" class="extlink">Gene Ontology Site</a>)</td>
    <td>Mulder et al - <a href="http://www.ncbi.nlm.nih.gov/pubmed/17202162" target="_blank" class="extlink">PubMed: 17202162</a></td>
  </tr>

  <tr>
    <td class="leftcol"><p><h2>Interactions</p><h2></td>
    <td>Confirmed Arabidopsis interacting proteins come from <a href="http://www.bind.ca/">BIND</a>, the Biomolecular Interaction Network Database, from high-density Arabidopsis protein microarrays (<a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&cmd=Retrieve&dopt=AbstractPlus&list_uids=17360592&query_hl=6&itool=pubmed_ExternalLink">Popescu et al., 2007</a>; <a href="http://dx.doi.org/10.1101/gad.1740009">Popescu et al., 2009</a>), from Braun et al.'s Arabidopsis Interactome <a href="http://dx.doi.org/10.1126/science.1203877">2011</a>, from Wolf Frommer's Membrane protein INteractome Database <a href="http://associomics.org">MIND</a>, and over 1190 other literature sources.</td>
    <td><a href="http://bar.utoronto.ca/" target="_blank" class="extlink">Bio-Analytic Resource</a> for Plant Biology</td>
    <td>Geisler-Lee et al., 2007 - <a href="http://www.ncbi.nlm.nih.gov/pubmed/17675552">PubMed: 17675552</a></td>
  </tr>

  <tr>
    <td rowspan="2" class="leftcol"><p><h2>Expression</h2></p></td>
    <td>Electronic Fluorescent Pictograph (eFP) Visualization paints gene expression information from one of the AtGenExpress data sets or other compendia for a desired gene onto a diagrammatic representation of <em>Arabidopsis thaliana</em> plants.</td>
    <td>BAR Arabidopsis <a href="http://bar.utoronto.ca/webservices/efp_service/efp_service.php" target="_blank" class="extlink">eFP Webservices</a></td>
    <td>Winter et al., 2007 - <a href="http://www.ncbi.nlm.nih.gov/pubmed/17684564" target="_blank" class="extlink">PubMed: 17684564</a><br />
        Brady et al., 2009 - <a href="http://www.ncbi.nlm.nih.gov/pubmed/19401381" target="_blank" class="extlink">PubMed: 19401381</a></td>
  </tr>
  <tr>
    <td>AtGenExpress data summarizing global gene expression in Arabidopsis in response to seven basic phytohormones (auxin, cytokinin, gibberellin, brassinosteroid, abscisic acid, jasmonate and ethylene) and their inhibitors (and in related experiments), as part of the AtGenExpress project.</td>
    <td><a href="http://www.weigelworld.org/resources/microarray/AtGenExpress/" target="_blank" class="extlink">AtGenExpress</a>- Weigel World</td>
    <td>Goda et al., 2008 - <a href="http://www.ncbi.nlm.nih.gov/pubmed/18419781" target="_blank" class="extlink">PubMed: 18419781</a></td>
  </tr>

  <tr>
    <td class="leftcol"><h2><p>Publications</p></h2></td>
    <td>Relevant publications are identified by searching databases such as PubMed (full-text is read, relevant information extracted and added to the entry)</td>
    <td><a href="http://www.uniprot.org/help/publications" target="_blank" class="extlink">Publications</a>from UniProt (Release 2015_05) and NCBI gene2pubmed (04/20/15)</td>
    <td>Magrane et al., 2011 - <a href="http://www.ncbi.nlm.nih.gov/pubmed/21447597" target="_blank" class="extlink">PubMed: 21447597</a></td>
  </tr>

  <tr>
    <td rowspan="2" class="leftcol"><h2><p>Pathways</p></h2></td>
    <td>KEGG pathways - Wiring diagrams of molecular interactions, reactions, and relations</td>
    <td><a href="http://www.kegg.jp/kegg/pathway.html" target="_blank" class="extlink">KEGG</a>Release 72.0</td>
    <td>Kanehisa et al - <a href="http://www.ncbi.nlm.nih.gov/pubmed/24214961" target="_blank" class="extlink">PubMed: 24214961</a></td>
  </tr>
  <tr>
    <td>Non-curated Reactome Linked Dataset that captures the Reactome pathways in BioPAX format</td>
    <td><a href="http://www.ebi.ac.uk/rdf/services/reactome" target="_blank" class="extlink">Reactome</a>(v49)</td>
    <td>Croft et al - <a href="http://www.ncbi.nlm.nih.gov/pubmed/24243840" target="_blank" class="extlink">PubMed: 24243840</a></td>
  </tr>

</table>

</div>
</div>
<!-- /dataCategories -->
