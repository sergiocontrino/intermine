<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/string.tld" prefix="s"%>

<!-- accessionStocksDisplayer.jsp -->

<c:set var="object" value="${intermineobject}" />
<c:set var="stocks" value="${object.backgroundStocks}" />

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


<div id="accessions_stocks_displayer" class="collection-table">
	<c:set var="rowCount" value="${fn:length(stocks)}" />

	<div class="header">
		<h3>
			<c:out value="${rowCount}" />
			Stocks
		</h3>
	</div>

	<c:choose>
		<c:when test="${!empty stocks}">

			<table>
				<thead>
					<tr>
						<th>Germplasm/Stock</th>
						<th>Description</th>
						<th>Stock Center</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach var="item" items="${stocks}">
						<tr>

							<td><a href="report.do?id=${item.id}">${item.displayName}</a></td>
							
							<td>
								${item.description}
							</td>
							
							<td><a class="button"
								href="report.do?id=${item.id}#StockAvailabilityDisplayer">Order</a>
							</td>

						</tr>
					</c:forEach>
				</tbody>
			</table>

		</c:when>
	</c:choose>
</div>



<!-- /accessionStocksDisplayer.jsp -->
