<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- phenotypesDisplayer.jsp -->

<div class="collection-of-collections">

	<div class="collection-table">

		<div class="header">

			<h3>Phenotypes</h3>

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
									<td><a href="report.do?id=${item.objectId}">${item.description}</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

			</c:when>

		</c:choose>

	</div>

	<div class="collection-table">
		<div class="header">

			<h3>Growth Requirements</h3>

		</div>
		<table>
			<thead>
				<tr>
					<th>Special Growth Conditions</th>
				</tr>
			</thead>
			<tbody>

				<tr>
					<c:choose>
						<c:when test="${!empty growthConditions.growthRequirements}">
							<td>${growthConditions.growthRequirements}</td>
						</c:when>
						<c:otherwise>
										<td> None </td>
					</c:otherwise>
					</c:choose>
				</tr>

			</tbody>
		</table>
	</div>

</div>
<!-- /phenotypesDisplayer.jsp -->