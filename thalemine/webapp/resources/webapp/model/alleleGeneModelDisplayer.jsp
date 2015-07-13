<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- allele-gene-model-displayer.jsp -->


<div id="allele-gene-model-displayer" class="collection-table">

<div class="header">
		<h3>
			<c:out value="${rowCount}" />
			Associated Genes
		</h3>

</div>

<c:choose>
		<c:when test="${!empty list}">

			<div>

				<table>
					<thead>
						<tr>
							<th>Gene Model</th>
							<th>Locus</th>
							<th>Description</th>
						</tr>
					</thead>
					<tbody>
     				 	<c:forEach var="item" items="${list}">	
     				 	<tr>
     				 		<td>
     				 		
     				 		<c:if test="${not empty item.geneModelName}">
   									 <a href="report.do?id=${item.geneModelObjectId}">${item.geneModelName}</a>
							</c:if>
							
							<c:if test="${empty item.geneModelName}">
   									 &nbsp;
							</c:if>
     		
     				 		</td>
     				 		<td>
     				 			 <a href="report.do?id=${item.geneObjectId}">${item.geneName}</a>
     	
     				 		</td>
     				 		
     				 		<td>
     				 		
     				 		
     				 			<c:if test="${not empty item.description}">
   									 ${item.description}
								</c:if>
     				 		
     				 			<c:if test="${empty item.description}">
   									 &nbsp;
								</c:if>
								
								
						
     				 		</td>
     				
     				 	</tr>
     				  	</c:forEach>
   					 </tbody>
				<table>
			</div>

		</c:when>
		<c:otherwise>
    			No Gene Model Data Available
  		</c:otherwise>
	</c:choose>

</div>

<!-- /allele-gene-model-displayer.jsp -->
