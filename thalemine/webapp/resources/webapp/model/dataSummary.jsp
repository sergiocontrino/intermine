<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>


<!-- dataSummary -->

<html:xhtml />

<div class="body">

	<%--
<im:boxarea title="Data Computed From Database" stylename="plainbox">
<p>ThaleMine integrates data from a large number of sources into a single data warehouse.  This page lists the data that are included in the current release.  Many more data sets will be added in future releases, please contact us if there are any particular data you would like to see included.</p>
</im:boxarea>
--%>

	<div class="datasummarytitle">
		<p class="pagetitle">Data</p>
		<p>ThaleMine integrates biological data from a wide array of public sources into a data warehouse.
		 This page lists the datasets that are included in the current release.</p>
		<p>
			Please <a
				href="mailto:araport@jcvi.org?Subject=Data request"> contact us
			</a> if there are any particular data you would like to suggest.</p>
	</div>

	<p class="datasummarytabletitle"><i>Arabidopsis thaliana</i> Col-0 genome</p>
	
	<table cellpadding="0" cellspacing="0" border="0" class="dbsources">
	<thead>
		<tr>
			<th>Data Category</th>
			<th>Feature Count</th>
			<th>Data Source</th>
			<th>Data Set</th>
			<th>Data Set Description</th>
			<th class="rightcol">PubMed</th>
		</tr>
	<thead>
		<c:choose>
		<c:when test="${!empty generesults}">
			<tbody>
				<c:forEach var="item" items="${generesults}">
					<tr>
						<td class="leftcol">
							<b>${item.categoryName}</b>
						</td>
						<td>
							<b>${item.featureCount}
							</b>
						</td>
						<td>
							<a href="${item.dataSourceUrl}" target="_blank" class="extlink">${item.dataSourceName}</a>
							- 
							${item.dataSourceDescription}
						</td>
						<td>
						<c:choose>
							<c:when test="${not empty item.dataSetUrl}">
							<a href="${item.dataSetUrl}" target="_blank" class="extlink">${item.dataSetName}</a>
							<c:if test="${not empty item.dataSetVersion}">
  									- ${item.dataSetVersion}
							</c:if>
							</c:when>
							<c:otherwise>
							
								${item.dataSetName}
								<c:if test="${not empty item.dataSetVersion}">
  									- ${item.dataSetVersion}
								</c:if>
								
							</c:otherwise>
						</c:choose>
										
						</td>
						<td>
							${item.dataSetDescription}
						</td>
						<td>
							<c:if test="${not empty item.author}">
  											 ${item.author}
  												 <c:if test="${not empty item.year}">
  												 ${item.year}
												</c:if>
							</c:if>
							<c:if test="${not empty item.pubMedId}">
								<br>
								<a href="http://www.ncbi.nlm.nih.gov/pubmed/${item.pubMedId}" target="_blank" class="extlink">PubMed: ${item.pubMedId}</a>
								</br>
							</c:if>
							
							<c:if test="${not empty item.pubTitle}">
  									${item.pubTitle}
  							</c:if>
							</td>
					</tr>
				</c:forEach>
			</tbody>
		</c:when>

	</c:choose>

	</table>
	
	<p class="datasummarytabletitle">Public datasets</p>
	
	<table cellpadding="0" cellspacing="0" border="0" class="dbsources">
	<thead>
		<tr>
			<th>Data Category</th>
			<th>Gene Count</th>
			<th>Feature Count</th>
			<th>Data Source</th>
			<th>Data Set</th>
			<th>Data Set Description</th>
			<th class="rightcol">PubMed</th>
		</tr>
	<thead>
		<c:choose>
		<c:when test="${!empty results}">
			<tbody>
				<c:forEach var="item" items="${results}">
					<tr>
						<td class="leftcol">
							<b>${item.categoryName}</b>
						</td>
						<td>
							<b>${item.geneCount}</b>
						</td>
						<td>
							<b>
							<span>
							${item.featureCount}
							<c:if test="${!empty item.units}">
							&nbsp;${item.units}
							</c:if>
							</span>
							</b>
						</td>
						<td>
							<a href="${item.dataSourceUrl}" target="_blank" class="extlink">${item.dataSourceName}</a>
																										
							<c:forEach var="itemdetail" items="${item.categoryDetails}">
								<br/>
								<a href="${itemdetail.dataSourceUrl}" target="_blank" class="extlink">${itemdetail.dataSourceName}</a>
														
							</c:forEach>
							
						</td>
						<td>
						<c:choose>
							<c:when test="${not empty item.dataSetUrl}">
							<a href="${item.dataSetUrl}" target="_blank" class="extlink">${item.dataSetName}</a>
							<c:if test="${not empty item.dataSetVersion}">
  									- ${item.dataSetVersion}
							</c:if>
							</c:when>
							<c:otherwise>
							
								${item.dataSetName}
								<c:if test="${not empty item.dataSetVersion}">
  									- ${item.dataSetVersion}
								</c:if>
								
							</c:otherwise>
						</c:choose>
						
						<c:forEach var="itemdetail" items="${item.categoryDetails}">
							<br/>
							<c:choose>
							<c:when test="${not empty itemdetail.dataSetUrl}">
							<a href="${itemdetail.dataSetUrl}" target="_blank" class="extlink">${itemdetail.dataSetName}</a>
							<c:if test="${not empty itemdetail.dataSetVersion}">
  									- ${itemdetail.dataSetVersion}
							</c:if>
							</c:when>
							<c:otherwise>
							
								${itemdetail.dataSetName}
								<c:if test="${not empty itemdetail.dataSetVersion}">
  									- ${itemdetail.dataSetVersion}
								</c:if>
								
							</c:otherwise>
						</c:choose>
						
								
						</c:forEach>
										
						</td>
						<td>
							${item.dataSetDescription}
						</td>
						<td>
							<c:if test="${not empty item.author}">
  											 ${item.author}
  												 <c:if test="${not empty item.year}">
  												 ${item.year}
												</c:if>
							</c:if>
							<c:if test="${not empty item.pubMedId}">
								<br>
								<a href="http://www.ncbi.nlm.nih.gov/pubmed/${item.pubMedId}" target="_blank" class="extlink">PubMed: ${item.pubMedId}</a>
								</br>
							</c:if>
							</td>
					</tr>
				</c:forEach>
			</tbody>
		</c:when>

	</c:choose>

	</table>

</div>
</div>
<!-- /dataSummary -->
