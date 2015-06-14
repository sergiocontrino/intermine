<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- alelleSummaryDisplayer.jsp -->


<div id="allele-summary_displayer" class="collection-table">

	<div class="header">
		<h3>			
			Summary
		</h3>
		
	</div>

	<c:choose>
		<c:when test="${!empty list}">

			<div>
				

				<table>
					<thead>
						<tr>
							<th>Sequence AlterationType</th>
							<th>Mutagen</th>
							<th>Mutation Site</th>
							<th>Allele Class</th>
							<th>Inheritance Type</th>
						</tr>
					</thead>
					<tbody>
     				 	<c:forEach var="item" items="${list}">	
     				 	<tr>
     				 		<td>
     				 		${item.sequenceAlterationType}
     				 		</td>
     				 		<td>
     				 			${item.mutagen}
     				 		</td>
     				 		<td>
     				 			${item.mutationSite}
     				 		</td>
     				 		<td>
     				 			${item.alleleClass}
     				 		</td>
     				 		<td>
     				 			${item.inheritanceType}
     				 		</td>
     				 	</tr>
     				  	</c:forEach>
   					 </tbody>
				<table>
			</div>

		</c:when>

	</c:choose>
</div>

<!-- /alelleSummaryDisplayer.jsp -->
