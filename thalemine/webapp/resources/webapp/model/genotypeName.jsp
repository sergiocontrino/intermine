<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/string.tld" prefix="s"%>

<!-- genotypeName.jsp -->

<c:set var="genotype" value="${interMineObject}" />
<c:set var="name" value="${genotype.primaryIdentifier}" />

<jsp:useBean id="formatter" class="org.thalemine.web.formatter.GenotypeFormatter"/>

<c:set var="allelesNames" value="${formatter.getAllelesNames(name)}" />
<c:set var="geneNames" value="${formatter.getGenesNames(name)}" />


<c:choose>

	<c:when test="${!empty allelesNames || !empty geneNames}">
		<span><i>${allelesNames}</i>&nbsp;${geneNames}</span>
	</c:when>
	<c:otherwise>
		${name}
	</c:otherwise>
	
</c:choose>

<!-- /genotypeName.jsp -->
