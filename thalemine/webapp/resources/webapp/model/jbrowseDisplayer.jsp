<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!-- jbrowseDisplayer.jsp -->
<div class="basic-table">
<h3>JBrowse</h3>

<c:set var="object" value="${reportObject.object}"/>

<c:choose>
<c:when test="${((!empty object.chromosomeLocation && !empty object.chromosome)
                || className == 'Chromosome') && className != 'ChromosomeBand'}">
<br />

<div id="jbrowse" class="feature basic-table">
  <c:set var="name" value="${object.primaryIdentifier}"/>

  <c:if test="${className == 'CDS' || fn:containsIgnoreCase(className, 'exon') || fn:containsIgnoreCase(className, 'UTR')}">
    <c:set var="name" value="${object.gene.primaryIdentifier}"/>
  </c:if>

  <c:choose>
  <c:when test="${WEB_PROPERTIES['jbrowse.database.source'] != null}">

    <c:set var="baseUrl" value="${WEB_PROPERTIES['jbrowse.prefix']}"/>
    <c:set var="datasource" value="${WEB_PROPERTIES['jbrowse.database.source']}"/>
    <c:set var="chr" value="${object.chromosomeLocation.locatedOn.primaryIdentifier}"/>

    <c:set var="offset" value="${fn:substringBefore((object.length * 0.1), '.')}"/>
    <c:set var="start" value="${object.chromosomeLocation.start}"/>
    <c:set var="end" value="${object.chromosomeLocation.end}"/>
    <c:set var="upstream" value="500"/>
    <c:set var="offsetstart" value="${start - offset - upstream}"/>
    <c:if test="${offsetstart < 1}"><c:set var="offsetstart" value="1"/></c:if>
    <c:set var="offsetend" value="${end + offset}"/>

    <c:set var="tracks" value="Araport11_Loci,Araport11_gene_models"/>
    <c:set var="extraParams" value="tracklist=0&nav=0&overview=0&menu=0"/>
    <c:set var="loc" value="${chr}:${offsetstart}..${offsetend}"/>

    <c:set var="jbLink" value="${baseUrl}/?data=${datasource}&loc=${loc}&tracks=${tracks}"/>
    <c:set var="jbLinkEmbed" value="${jbLink}&${extraParams}"/>

    <style text="text/css">
    .embed-responsive-item {
        top: 0;
        left: 0;
        bottom: 0;
        height: 300px;
        width: 100%;
        border: 0;
    }
    </style>

    <div>
        <iframe name="jbrowse-embed" class="embed-responsive-item" src="${jbLinkEmbed}"></iframe>
        <p>
            <a href="${jbLinkEmbed}" target="jbrowse">Center on ${object.primaryIdentifier}</a>&nbsp;|&nbsp;
            <a href="${jbLink}" target="_blank" class="extlink">Full Screen View</a>&nbsp;|&nbsp;
            Powered by <a href="http://jbrowse.org" target="_blank" class="extlink">JBrowse</a>
        </p>
    </div>
  </c:when>
  <c:otherwise>
  <p>There was a problem rendering the displayer, check: <code>${WEB_PROPERTIES['jbrowse.database.source']}</code>.</p>
	<script type="text/javascript">
		jQuery('#jbrowse').addClass('warning');
	</script>
  </c:otherwise>
  </c:choose>
</c:when>
<c:otherwise>
  <p style="font-style:italic;">No JBrowse visualization available</p>
</c:otherwise>
</c:choose>
</div>
<!-- /jbrowseDisplayer.jsp -->
