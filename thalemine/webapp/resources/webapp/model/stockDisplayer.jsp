<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- alelleDisplayer.jsp -->


<div id="allele-displayer" class="collection-table">

	<c:set var="rowCount" value="${fn:length(list)}" />

	<div class="header">
		<h3>
			<c:out value="${rowCount}" />
			Stocks
		</h3>
		<p>
			Data Source: <a target="_blank" href="">ABRC Germplasms/Seed Stocks</a>
		</p>
		
		<p>
			Data Source: <a target="_blank" href="">NASC Germplasms/Seed Stocks</a>
		</p>
		
	</div>

	<c:choose>
		<c:when test="${!empty list}">

			<div>
				<c:set var="geneList" value="${geneName}" />

				<table>
					<thead>
						<tr>
							<th>Germplasm Name</th>
							<th>Genotype</th>
							<th>Background Accession</th>
							<th>Stock Name</th>
						</tr>
					</thead>
					<tbody>
     				 	<c:forEach var="item" items="${list}">	
     				 	<tr>
     				 		<td>
     				 		 <a href="report.do?id=${item.stockObjectId}">${item.germplasmName}</a>
     				 		</td>
     				 		<td>
     				 			${item.genotypeName}
     				 		</td>
     				 		<td>
     				 			
     				 		</td>
     				 		<td>
     				 			${item.stockName}
     				 		</td>
     				 		<td>
     				 			
     				 		</td>
     				 	</tr>
     				  	</c:forEach>
   					 </tbody>
				<table>
			</div>

		</c:when>
		<c:otherwise>
    			No Allele Data Available
  		</c:otherwise>
	</c:choose>
</div>

<!-- /alelleDisplayer.jsp -->
