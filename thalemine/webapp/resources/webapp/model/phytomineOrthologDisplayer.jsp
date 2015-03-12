<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!-- phytomineOrthologDisplayer.jsp -->
<div class="basic-table">
<h3>InParanoid Homologs</h3>
<p>Data Source: <a href="${WEB_PROPERTIES['phytomine_url']}">Phytomine</a></p>
<c:set var="object" value="${reportObject.object}"/>

<c:choose>
<c:when test="${((!empty object.chromosomeLocation && !empty object.chromosome)
                || className == 'Chromosome') && className != 'ChromosomeBand'}">
<br />

<div id="InParanoidDisplayer" class="feature basic-table">
  <c:set var="name" value="${object.primaryIdentifier}"/>

  <c:choose>
  <c:when test="${WEB_PROPERTIES['phytomine.homolog.prefix'] != null}">
<link rel="stylesheet" type="text/css" href="https://cdn.araport.org/js/intermine/im-tables/latest/imtables.css">
 <div id="phytomine-paralog-container">
 <p class="apology">
  Please be patient while the results of your query are retrieved.
 </p>
</div>
 <div id="phytomine-ortholog-container">
 <p class="apology">
  Please be patient while the results of your query are retrieved.
 </p>
</div>

  <script type="text/javascript">
  
  var geneId = "${name}";
  var webapp_root_url = "${WEB_PROPERTIES['phytomine.homolog.prefix']}";

  var optionsP = {
    type: 'table',
    url: webapp_root_url,
    query: {"model":{"name":"genomic"},"select":["Homolog.groupName","Homolog.gene2.name","Homolog.organism2.shortName","Homolog.relationship"],"constraintLogic":"A and B","orderBy":[{"Homolog.relationship":"ASC"}],"where":[{"path":"Homolog.organism1.taxonId","op":"=","code":"A","value":"3702"},{"path":"Homolog.organism2.taxonId","op":"=","code":"C","value":"3702"},{"path":"Homolog.gene1.name","op":"=","code":"B","value":geneId}]}
  };

  var optionsO = {
    type: 'table',
    url: webapp_root_url,
    query: {"model":{"name":"genomic"},"select":["Homolog.groupName","Homolog.gene2.name","Homolog.organism2.shortName","Homolog.relationship"],"constraintLogic":"A and B","orderBy":[{"Homolog.relationship":"ASC"}],"where":[{"path":"Homolog.organism1.taxonId","op":"=","code":"A","value":"3702"},{"path":"Homolog.organism2.taxonId","op":"!=","code":"C","value":"3702"},{"path":"Homolog.gene1.name","op":"=","code":"B","value":geneId}]}
  };

	jQuery('#phytomine-paralog-container').imWidget(optionsP);
	jQuery('#phytomine-ortholog-container').imWidget(optionsO);

  </script>

  </c:when>
  <c:otherwise>
   	<p>There was a problem rendering the homolog data</code>.</p>
	<script type="text/javascript">
		jQuery('#InParanoidDisplayer').addClass('warning');
	</script>
  </c:otherwise>
  </c:choose>
</c:when>
<c:otherwise>
  <p style="font-style:italic;">No homolog data available</p>
</c:otherwise>
</c:choose>
</div>
<!-- /phytomineOrthologDisplayer.jsp -->
