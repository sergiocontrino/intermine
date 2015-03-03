<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!-- phytomineOrthologDisplayer.jsp -->
<div class="basic-table">
<h3>Phytomine Orthologs and Paralogs</h3>

<c:set var="object" value="${reportObject.object}"/>

<c:choose>
<c:when test="${((!empty object.chromosomeLocation && !empty object.chromosome)
                || className == 'Chromosome') && className != 'ChromosomeBand'}">
<br />
       <p>${object.primaryIdentifier}</p>
<div id="phytomineOrthologs" class="feature basic-table">
       <p>${object.primaryIdentifier}</p>
        <link rel="stylesheet" type="text/css" href="http://phytozome.jgi.doe.gov/intermine/cdn/js/intermine/im-tables/latest/imtables.css" />
       <p>${object.primaryIdentifier}</p>
  <c:set var="name" value="${object.primaryIdentifier}"/>

  <c:choose>
  <div id="query-container">
       <p class="apology">
       Please be patient while the results of your query are retrieved.
       </p>
  </div>
       <script type="text/javascript" src="http://phytozome.jgi.doe.gov/intermine/cdn/js/intermine/im-tables/latest/imtables-bundled.js" ></script>

  </script>

  <c:otherwise>
   	<p>There was a problem retrieving the ortholog/paralog data.</code>.</p>
	<script type="text/javascript">
		jQuery('#phytomineOrtholgs').addClass('warning');
	</script>
  </c:otherwise>
  </c:choose>
</c:when>
<c:otherwise>
  <p style="font-style:italic;">No Orthologs or Paralogs available</p>
</c:otherwise>
</c:choose>
</div>
<!-- /phytomineOrthologDisplayer.jsp -->
