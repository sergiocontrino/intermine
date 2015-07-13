<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- stock-genetic_context-displayer.jsp -->


<div id="stock-genetic_context-displayer" class="collection-table">

	<div class="header">
		<h3>			
			Associated Polymorphisms
		</h3>
		
	</div>

	<c:choose>
		<c:when test="${!empty list}">

			<div>
				

				<table>
					<thead>
						<tr>
							<th>Name</th>
							<th>Locus</th>
							<th>Allele Mutagen</th>
							<th>Zygosity</th>
						</tr>
					</thead>
					<tbody>
     				 	<c:forEach var="item" items="${list}">	
     				 		
     				 			   				 					
     				 		<c:forEach var="alleleitem" items="${item.alleles}">
     				 	<tr>
     				 		<td>
     				 		    <p>
     				 				<a href="report.do?id=${alleleitem.objectId}"><i>${alleleitem.name}</i></a>
     				 			</p>
     				 		</td>
     				 		<td>
     				 				<c:forEach var="geneitem" items="${alleleitem.geneList}">
     				 					<p>
     				 						<a href="report.do?id=${geneitem.geneObjectId}">${geneitem.geneName}</a>
     				 					</p>
     				 				</c:forEach>
     				 		</td>
     				 		
     				 		<td>
     				 			${alleleitem.mutagen}
     				 		</td>
     				 		<td>
     				 			${alleleitem.zygosity}
     				 		</td>
     				     				 		
     				 	 </tr>
     				 	 </c:forEach>
     				  	</c:forEach>
   					 </tbody>
				</table>
			</div>

		</c:when>

	</c:choose>
</div>

<!-- /stock-genetic_context-displayer.jsp -->
