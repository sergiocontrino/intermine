<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- phenotypesDisplayer.jsp -->


<div id="phenotypesDisplayer_displayer" class="collection-table">

	<div class="header">
		<h3>			
			Phenotypes
		</h3>
		
	</div>

	<c:choose>
		<c:when test="${!empty list}">

			<div>
				

				<table>
					<thead>
						<tr>
							<th>Description</th>
						</tr>
					</thead>
					<tbody>
     				 	<c:forEach var="item" items="${list}">	
     				 	<tr>
     				 		<td>
     				 		<a href="report.do?id=${item.objectId}">${item.description}</a>
     				 		</td>
     				 	</tr>
     				  	</c:forEach>
   					 </tbody>
				<table>
			</div>

		</c:when>

	</c:choose>
</div>

<!-- /phenotypesDisplayer.jsp -->
