<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!-- phytomineOrthologDisplayer.jsp -->
<div class="basic-table">
<h3>Phytozome InParanoid Homologs</h3>

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
 <div id="phytomine-container">
 <p class="apology">
  Please be patient while the results of your query are retrieved.
 </p>
</div>

  <script type="text/javascript">
  
  var geneId = "${name}";
  var webapp_root_url = "${WEB_PROPERTIES['phytomine.homolog.prefix']}";

  var options = {
    type: 'table',
    url: webapp_root_url,
    token: 'd6506952eec1ebcb6305000dbc7c02df',
    query: {"model":{"name":"genomic"},"select":["Homolog.groupName","Homolog.gene1.name","Homolog.organism1.shortName","Homolog.gene2.name","Homolog.organism2.shortName","Homolog.relationship"],"constraintLogic":"A and B","orderBy":[{"Homolog.relationship":"ASC"}],"where":[{"path":"Homolog.organism1.taxonId","op":"=","code":"A","value":"3702"},{"path":"Homolog.gene1.name","op":"=","code":"B","value":geneId}]}
  };
	jQuery('#phytomine-container').imWidget(options);

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
