<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/string.tld" prefix="s"%>

<!-- alleleName.jsp -->

<c:set var="allele" value="${interMineObject}" />
<c:set var="name" value="${allele.primaryIdentifier}" />

<jsp:useBean id="formatter" class="org.thalemine.web.formatter.AlleleFormatter"/>
<c:set var="formattedName" value="${formatter.getFormattedName(name)}" />

<c:choose>

	<c:when test="${!empty formattedName}">
		<a href="report.do?id=${allele.id}"><i>${formattedName}</i></a>
	</c:when>
	<c:otherwise>
		${name}
	</c:otherwise>
	
</c:choose>

<!-- /genotypeName.jsp -->
