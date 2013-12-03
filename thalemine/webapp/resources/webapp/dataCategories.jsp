<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>


<!-- dataCategories -->


<html:xhtml/>

<div class="body">
<im:boxarea title="Data" stylename="plainbox"><p>ThaleMine integrates data from a large number of sources into a single data warehouse.  This page lists the data that are included in the current release.  Many more data sets will be added in future releases, please contact us if there are any particular data you would like to see included.</p></im:boxarea>


<div style="padding: 10px 40px">
<h3>The following data are loaded in the ThaleMine alpha:</h3>

<br/>

<table>
       <tr>
               <th>Type</th>
               <th>Source</th>
               <th>Version</th>
       </tr>
       <tr>
               <td>Genome sequence and annotation</td>
               <td></td>
               <td></td>
               <td></td>
       </tr>
       <tr>
               <td>Protein sequence and annotation</a></td>
               <td><a href="http://www.uniprot.org/">UniProt</a></td>
               <td>November 2013</td>
       </tr>
       <tr>
               <td>Protein domains</td>
               <td><a href="http://www.ebi.ac.uk/interpro/">InterPro</a></td>
               <td>November 2013</td>
       </tr>
       <tr>
               <td>Gene Ontology</td>
               <td><a href="http://www.uniprot.org/">UniProt</a>, <a href="http://www.geneontology.org/">GO Consortium</a></td>
               <td>November 2013</td>
       </tr>
       <tr>
               <td>Interactions</td>
               <td><a href="http://www.ebi.ac.uk/intact/">IntAct</a></td>
                <td>November 2013</td>
       </tr>
       <tr>
               <td>Interactions</td>
               <td><a href="http://thebiogrid.org/">BioGRID</a></td>
               <td>November 2013</td>
       </tr>
       <tr>
               <td>Publications</td>
               <td><a href="ftp://ftp.ncbi.nih.gov/gene/DATA/">NCBI PubMed (gene to PubMed id mappings)</a></td>
               <td>November 2013</td>
       </tr>       
</table>

</div>
</div>
<!-- /dataCategories -->
