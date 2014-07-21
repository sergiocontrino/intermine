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
    <c:set var="chr" value="${reportObject.object.chromosomeLocation.locatedOn.primaryIdentifier}"/>

    <c:set var="padding" value="${10}"/>
    <c:set var="offset" value="${fn:substringBefore((reportObject.object.length * 0.1), '.')}"/>
    <c:set var="start" value="${reportObject.object.chromosomeLocation.start - offset}"/>
    <c:set var="end" value="${reportObject.object.chromosomeLocation.end + offset}"/>

    <c:set var="tracks" value="TAIR10_loci,TAIR10_genes"/>
    <c:set var="extraParams" value="tracklist=1&nav=0&overview=0"/>

    <c:set var="jbLink" value="${baseUrl}/?data=${datasource}&loc=${chr}:${start}..${end}&tracks=${tracks}&${extraParams}"/>

    <div>
        <p>Click and drag the browser to move the view. Check to turn on/off the tracks from left menu to see the data in the main panel.</p>
        <iframe id="jbrowse" name="jbrowse" height="300px" width="98%" style="border: 1px solid #dfdfdf; padding: 1%" src="${jbLink}"></iframe>
        <p>
            <a href="${jbLink}" target="jbrowse">Center on ${reportObject.object.symbol}</a>&nbsp;|&nbsp;
            <a href="javascript:;" onclick="jQuery('iframe').css({height: '600px'});">Expand viewer</a>&nbsp;|&nbsp;
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
