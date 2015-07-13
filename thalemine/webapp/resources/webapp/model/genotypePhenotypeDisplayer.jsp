<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/string.tld" prefix="s"%>

<!-- genotypePhenotypeDisplayer.jsp -->

<c:set var="object" value="${intermineobject}" />
<c:set var="phenotypes" value="${object.phenotypesObserved}" />

<div id="genotype_phenotype_displayer" class="collection-table">
	<c:set var="rowCount" value="${fn:length(phenotypes)}" />

	<div class="header">
		<h3>
			<c:out value="${rowCount}" />
			Phenotypes
		</h3>
	</div>

	<c:choose>
		<c:when test="${!empty phenotypes}">

			<table>
				<thead>
					<tr>
						<th>Description</th>
					</tr>
				</thead>
				<tbody>
					
					<c:forEach var="item" items="${phenotypes}">
						<tr>
						
						<td><a href="report.do?id=${item.id}">${item.description}</a></td>
						
						
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</c:when>
	</c:choose>
</div>



<!-- /genotypePhenotypeDisplayer.jsp -->
