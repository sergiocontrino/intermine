<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- stockDisplayer.jsp -->


<div id="stock-displayer" class="collection-table">

	<c:set var="rowCount" value="${fn:length(list)}" />

    <c:out value="${rowCount}" />
			<p> Context URL
			<c:out value="${contextURL}" />
			</p>
			<p> Stock Service URL
	<c:out value="${stockServiceUrl}" />
	</p>

	<div class="header">
		<h3>
			<c:out value="${rowCount}" />
			Stocks
		</h3>
		<p>
			Data Source: <a target="_blank" href="${WEB_PROPERTIES['ABRC_SeedStockSearch_url']}">ABRC Germplasms/Seed
				Stocks</a> <span>&nbsp; &nbsp; &nbsp;</span> <span> Data Source:
				<a target="_blank" href="${WEB_PROPERTIES['NASC_StockSearch_url']}">NASC Germplasms/Seed Stocks</a>
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
							<th>Stock Name</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${list}">
							<tr>
								<td><a href="report.do?id=${item.stockObjectId}">${item.germplasmName}</a>
								</td>
								<td>${item.genotypeName}</td>
								<td><c:forEach var="bgitem" items="${item.backgrounds}">
										<a href="report.do?id=${bgitem.objectId}">${bgitem.abbreviationName}</a>
									</c:forEach></td>
								<td>${item.stockName}</td>

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
														<td>${phenotypeItem.description}
														<c:choose>
															<c:when test="${!empty phenotypeItem.publications}">
																<c:forEach var="publicationItem" items="${phenotypeItem.publications}">
																<span>&nbsp;</span>
																	<a href="report.do?id=${publicationItem.objectId}">${publicationItem.displayTitleStockContext}</a>
																</c:forEach>
															</c:when>
														</c:choose>
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
					<table>
						</div>

						</c:when>
						<c:otherwise>
    			No stock information available at this time
  		</c:otherwise>
						</c:choose>
						</div>

						<!-- /stockDisplayer.jsp -->