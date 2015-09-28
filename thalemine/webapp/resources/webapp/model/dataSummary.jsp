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

	<im:boxarea title="Data Computed From Database" stylename="yellbox">
		<p>ThaleMine integrates data from a large number of sources into a
			single data warehouse.</p>
		<p>This page lists the data that are included in the current
			release and it is manually curated; its contents are not indexed in
			our keyword search.</p>
		<p>
			More data sets will be added in future releases, please <a
				href="mailto:araport@jcvi.org?Subject=Data request"> contact us
			</a> if there are any particular data you would like to see included.
		</p>
	</im:boxarea>

	<br />
	<table cellpadding="0" cellspacing="0" border="0" class="dbsources">
	<thead>
		<tr>
			<th>Data Category</th>
			<th>Data Source</th>
			<th>Data Source Description</th>
			<th>Data Set</th>
			<th>Data Set Description</th>
			<th>PubMed</th>
		</tr>
	<thead>
		<c:choose>
		<c:when test="${!empty results}">
			<tbody>
				<c:forEach var="item" items="${results}">
					<tr>
						<td>
							<b>${item.categoryName}</b>
						</td>
						<td><a href="${item.dataSourceUrl}" target="_blank" class="extlink">${item.dataSourceName}</a>
						<p>
							<b><span>Gene Count: ${item.geneCount}</span></b>
							</p>
							<p>
							<b><span>Feature Count: ${item.featureCount}</span></b>
							</p>
						</td>
						<td>
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
  												 ., &nbsp; ${item.year}
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
