<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- esynDisplayer.jsp -->

<div id="esyn_displayer" class="collection-table">
<c:if test="${!empty reportObject.object.symbol && !empty reportObject.object.organism.taxonId && !empty reportObject.object.interactions}">

<div class="header">
    <h3>esyN Network Diagram </h3>
    <p>Data Source: <a target="_blank" href="${WEB_PROPERTIES['esyN_url']}">esyN</a></p>
</div>

    <c:set var="symbol" value="${reportObject.object.symbol}"/>
    <c:set var="taxon" value="${reportObject.object.organism.taxonId}"/>

<iframe name="esyn" class="seamless" scrolling="no" id="iframe"
src="${WEB_PROPERTIES['esyN_url']}/app.php?embedded=true&type=Graph&query=${symbol}&organism=${taxon}&interactionType=any&includeInteractors=true&source=biogrid"
width="500" height="500"></iframe>

    <p>These are physical (yellow lines) and genetic (green lines) interactions from BioGRID. See <a href="http://esyn.org/">esyn</a> for details.
    
</c:if>

</div>

<!-- /esynDisplayer.jsp -->
