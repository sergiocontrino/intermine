<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- stockGrowthRequirementsDisplayer.jsp -->


<div id="stock_growth_displayer" class="collection-table">


	<c:choose>
		<c:when test="${!empty result}">


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
						<td>${result.growthConditions}</td>

					</tr>

				</tbody>
			</table>
		</c:when>

	</c:choose>

</div>

<!-- /stockGrowthRequirementsDisplayer.jsp -->