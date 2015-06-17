<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- stockAccessionDisplayer.jsp -->


<div id="stock-accession_displayer" class="collection-table">

	<c:choose>
		<c:when test="${!empty result}">
		
		<div class="header">
		<h3>			
			Stock Availability
		</h3>
		
		</div>

			<div>

				<table>
					<thead>
						<tr>
							<th>Stock Number</th>
							<th>Available At ?</th>
						</tr>
					</thead>
					<tbody>
     				 	<c:forEach var="item" items="${result}">	
     				 	<tr>
     				 		<td>			
     				 		
     				 		<c:if test = "${item.stockCenter eq 'ABRC'}">
  								<a href="${item.externalStockObjectUrlPrefix}${item.stockNumber}&type=germplasm">${item.stockName}</a>
							</c:if>
							<c:if test = "${item.stockCenter eq 'NASC'}">
  								<a href="${item.externalStockObjectUrlPrefix}${item.stockNumber}">${item.stockName}</a>
							</c:if>
	
     				 		</td>
     				 		<td>
     				 		
     				 			${item.stockCenter} 
     				 			
     				 		</td>
     				 		
     				 	</tr>
     				  	</c:forEach>
   					 </tbody>
				<table>
			</div>

		</c:when>

	</c:choose>
</div>

<!-- /stockAccessionDisplayer.jsp -->
