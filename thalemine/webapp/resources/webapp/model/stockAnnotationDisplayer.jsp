<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- stockAnnotationDisplayer.jsp -->


<div id="stock-accession_displayer" class="collection-table">

	<c:choose>
		<c:when test="${!empty result}">

			<div class="header">
				<h3>Summary</h3>

			</div>

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
				<table>

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
								<table>
									</c:when>

									</c:choose>

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
										<table>



											</c:when>

											</c:choose>
									</div>

									<!-- /stockAnnotationDisplayer.jsp -->