<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- stockAccessionDisplayer.jsp -->


<div id="stock-accession_displayer" class="collection-table">

	<c:choose>
		<c:when test="${!empty list}">
		
		<div class="header">
		<h3>			
			Accession
		</h3>
		
		</div>

			<div>

				<table>
					<thead>
						<tr>
							<th>Abbreviated Name</th>
							<th>Location</th>
							<th>Habitat</th>
						</tr>
					</thead>
					<tbody>
     				 	<c:forEach var="item" items="${list}">	
     				 	<tr>
     				 		<td>
     				 		<a href="report.do?id=${item.objectId}">${item.abbreviationName}</a>    				 		
     				 		</td>
     				 		<td>
     				 			${item.geoLocation}
     				 		</td>
     				 		<td>
     				 			${item.habitat}
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
