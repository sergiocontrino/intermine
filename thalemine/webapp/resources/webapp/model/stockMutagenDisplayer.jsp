<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- stockMutagenDisplayer.jsp -->


<div id="stock_mutagen_displayer" class="collection-table">


	<c:choose>
		<c:when test="${!empty result}">


			<div class="header">

				<h3>Chromosomal Constitution</h3>
			</div>
			<table>
				<thead>
					<tr>
						<th>Ploidy</th>
						<th>Aneuploid Chromosome ?</th>
					</tr>
				</thead>
				<tbody>

					<tr>
						<td>${result.ploidy}</td>
						<td>${result.aneploid}</td>
					</tr>

				</tbody>
			</table>

			<c:choose>
				<c:when test="${!empty result.mutagen}">
					<div class="header">

						<h3>Mutagen</h3>
					</div>
					<table>
						<thead>
							<tr>
								<th>Mutagen</th>
							</tr>
						</thead>
						<tbody>

							<tr>

								<td>${result.mutagen}</td>

							</tr>

						</tbody>
					</table>
				</c:when>

			</c:choose>

		</c:when>

	</c:choose>
</div>

<!-- /stockAnnotationDisplayer.jsp -->