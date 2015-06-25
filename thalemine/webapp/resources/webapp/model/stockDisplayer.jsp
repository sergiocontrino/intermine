<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- stockDisplayer.jsp -->


<div id="stock-displayer" class="collection-table">


<style>
	.button {
	-moz-box-shadow: 3px 4px 0px 0px #899599;
	-webkit-box-shadow: 3px 4px 0px 0px #899599;
	box-shadow: 3px 4px 0px 0px #899599;
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ededed), color-stop(1, #bab1ba));
	background:-moz-linear-gradient(top, #ededed 5%, #bab1ba 100%);
	background:-webkit-linear-gradient(top, #ededed 5%, #bab1ba 100%);
	background:-o-linear-gradient(top, #ededed 5%, #bab1ba 100%);
	background:-ms-linear-gradient(top, #ededed 5%, #bab1ba 100%);
	background:linear-gradient(to bottom, #ededed 5%, #bab1ba 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ededed', endColorstr='#bab1ba',GradientType=0);
	background-color:#ededed;
	-moz-border-radius:15px;
	-webkit-border-radius:15px;
	border-radius:15px;
	border:1px solid #d6bcd6;
	display:inline-block;
	cursor:pointer;
	color:#3a8a9e;
	font-family:Arial;
	font-size:14px;
	padding:7px 25px;
	text-decoration:none;
	text-shadow:0px 1px 0px #e1e2ed;
}
.button:hover {
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #bab1ba), color-stop(1, #ededed));
	background:-moz-linear-gradient(top, #bab1ba 5%, #ededed 100%);
	background:-webkit-linear-gradient(top, #bab1ba 5%, #ededed 100%);
	background:-o-linear-gradient(top, #bab1ba 5%, #ededed 100%);
	background:-ms-linear-gradient(top, #bab1ba 5%, #ededed 100%);
	background:linear-gradient(to bottom, #bab1ba 5%, #ededed 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#bab1ba', endColorstr='#ededed',GradientType=0);
	background-color:#bab1ba;
}

.button:active {
	position:relative;
	top:1px;
}

a.button:link {
    color:#3a8a9e;
    text-decoration:none;
}

/* visited link */
a.button:visited {
    color:#3a8a9e;
    text-decoration:none;
}

/* mouse over link */
a.button:hover {
    color:#3a8a9e;
    text-decoration:none;
}

/* selected link */
a.button:active {
  color:#3a8a9e;
  text-decoration:none;
}

</style>


	<c:set var="rowCount" value="${fn:length(list)}" />

	<div class="header">
		<h3>
			<c:out value="${rowCount}" />
			Stocks
		</h3>
		<p>
			Data Source: <a target="_blank" href="/${WEB_PROPERTIES['webapp.path']}/portal.do?class=DataSet&externalids=TAIR+Germplasm">TAIR/ABRC Germplasms/Seed
				Stocks</a>
			</span>
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
							<th>Stock Center</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${list}">
							<tr>
								<td><a href="report.do?id=${item.stockObjectId}">${item.germplasmName}</a>
								</td>
								<td>
								<!-- 
									<a href="report.do?id=${item.genotypeObjectId}">${item.genotypeName}</a>
									
									-->
									
									<a href="report.do?id=${item.genotypeObjectId}"><i>${item.allelesNames}</i>&nbsp;${item.genesNames}</a>
								</td>
								<td><c:forEach var="bgitem" items="${item.backgrounds}">
										<a href="report.do?id=${bgitem.objectId}">${bgitem.abbreviationName}</a>
									</c:forEach></td>
								<td><a class="button" href="report.do?id=${item.stockObjectId}#StockAvailabilityDisplayer">Order</a></td>

								<c:choose>
									<c:when test="${!empty item.phenotypes}">

										<table>

											<thead>
												<tr>
													
													<th>Phenotypes</th>

												</tr>
											</thead>
											<tbody>
												<c:forEach var="phenotypeItem" items="${item.phenotypes}">
													<tr>
														<td>														
														<a href="report.do?id=${phenotypeItem.objectId}">${phenotypeItem.description}</a>
														<!--  
														<c:choose>
															<c:when test="${!empty phenotypeItem.publications}">
																<c:forEach var="publicationItem" items="${phenotypeItem.publications}">
																<span>&nbsp;</span>
																	<a href="report.do?id=${publicationItem.objectId}">${publicationItem.displayTitleStockContext}</a>
																</c:forEach>
															</c:when>
														</c:choose> -->
														
														</td>
													</tr>
									
												</c:forEach>

											</tbody>
										</table>
									</c:when>
									<c:otherwise>
									<tr>
									<td>
											No phenotype information available at this time
									</td>
									</tr>
  									</c:otherwise>
								</c:choose>

							</tr>

						</c:forEach>

					</tbody>
					</table>
						</div>

						</c:when>
						<c:otherwise>
    			No stock information available at this time
  		</c:otherwise>
						</c:choose>
						</div>

						<!-- /stockDisplayer.jsp -->