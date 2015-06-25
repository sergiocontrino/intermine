<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- alelleDisplayer.jsp -->

<style>
/**
 * Tooltip Styles
 */

/* Base styles for the element that has a tooltip */
[data-tooltip], .tooltip {
	position: relative;
	cursor: pointer;
}

/* Base styles for the entire tooltip */
[data-tooltip]:before, [data-tooltip]:after, .tooltip:before, .tooltip:after
	{
	position: absolute;
	visibility: hidden;
	-ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";
	filter: progid:DXImageTransform.Microsoft.Alpha(Opacity=0);
	opacity: 0;
	-webkit-transition: opacity 0.2s ease-in-out, visibility 0.2s
		ease-in-out, -webkit-transform 0.2s
		cubic-bezier(0.71, 1.7, 0.77, 1.24);
	-moz-transition: opacity 0.2s ease-in-out, visibility 0.2s ease-in-out,
		-moz-transform 0.2s cubic-bezier(0.71, 1.7, 0.77, 1.24);
	transition: opacity 0.2s ease-in-out, visibility 0.2s ease-in-out,
		transform 0.2s cubic-bezier(0.71, 1.7, 0.77, 1.24);
	-webkit-transform: translate3d(0, 0, 0);
	-moz-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0);
	pointer-events: none;
}

/* Show the entire tooltip on hover and focus */
[data-tooltip]:hover:before, [data-tooltip]:hover:after, [data-tooltip]:focus:before,
	[data-tooltip]:focus:after, .tooltip:hover:before, .tooltip:hover:after,
	.tooltip:focus:before, .tooltip:focus:after {
	visibility: visible;
	-ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=100)";
	filter: progid:DXImageTransform.Microsoft.Alpha(Opacity=100);
	opacity: 1;
}

/* Base styles for the tooltip's directional arrow */
.tooltip:before, [data-tooltip]:before {
	z-index: 1001;
	border: 6px solid transparent;
	background: transparent;
	content: "";
}

/* Base styles for the tooltip's content area */
.tooltip:after, [data-tooltip]:after {
	z-index: 1000;
	padding: 8px;
	width: 280px;
	background-color: #FBFDFE;
	color: #0A0A0A;
	border: 1px solid #172478;
	content: attr(data-tooltip);
	font-size: 11px;
	line-height: 1.2;
	white-space: pre-wrap;
}

/* Directions */

/* Top (default) */
[data-tooltip]:before, [data-tooltip]:after, .tooltip:before, .tooltip:after,
	.tooltip-top:before, .tooltip-top:after {
	bottom: 100%;
	left: 50%;
}

[data-tooltip]:before, .tooltip:before, .tooltip-top:before {
	margin-left: -6px;
	margin-bottom: -12px;
	border-top-color: #172478;
}

/* Horizontally align top/bottom tooltips */
[data-tooltip]:after, .tooltip:after, .tooltip-top:after {
	margin-left: -80px;
}

[data-tooltip]:hover:before, [data-tooltip]:hover:after, [data-tooltip]:focus:before,
	[data-tooltip]:focus:after, .tooltip:hover:before, .tooltip:hover:after,
	.tooltip:focus:before, .tooltip:focus:after, .tooltip-top:hover:before,
	.tooltip-top:hover:after, .tooltip-top:focus:before, .tooltip-top:focus:after
	{
	-webkit-transform: translateY(-12px);
	-moz-transform: translateY(-12px);
	transform: translateY(-12px);
}

/* Left */
.tooltip-left:before, .tooltip-left:after {
	right: 100%;
	bottom: 50%;
	left: auto;
}

.tooltip-left:before {
	margin-left: 0;
	margin-right: -12px;
	margin-bottom: 0;
	border-top-color: transparent;
	border-left-color: #172478;
}

.tooltip-left:hover:before, .tooltip-left:hover:after, .tooltip-left:focus:before,
	.tooltip-left:focus:after {
	-webkit-transform: translateX(-12px);
	-moz-transform: translateX(-12px);
	transform: translateX(-12px);
}

/* Bottom */
.tooltip-bottom:before, .tooltip-bottom:after {
	top: 100%;
	bottom: auto;
	left: 50%;
}

.tooltip-bottom:before {
	margin-top: -12px;
	margin-bottom: 0;
	border-top-color: transparent;
	border-bottom-color: #172478;
}

.tooltip-bottom:hover:before, .tooltip-bottom:hover:after,
	.tooltip-bottom:focus:before, .tooltip-bottom:focus:after {
	-webkit-transform: translateY(12px);
	-moz-transform: translateY(12px);
	transform: translateY(12px);
}

/* Right */
.tooltip-right:before, .tooltip-right:after {
	bottom: 50%;
	left: 100%;
}

.tooltip-right:before {
	margin-bottom: 0;
	margin-left: -12px;
	border-top-color: transparent;
	border-right-color: #172478;
}

.tooltip-right:hover:before, .tooltip-right:hover:after, .tooltip-right:focus:before,
	.tooltip-right:focus:after {
	-webkit-transform: translateX(12px);
	-moz-transform: translateX(12px);
	transform: translateX(12px);
}

/* Move directional arrows down a bit for left/right tooltips */
.tooltip-left:before, .tooltip-right:before {
	top: 3px;
}

/* Vertically center tooltip content for left/right tooltips */
.tooltip-left:after, .tooltip-right:after {
	margin-left: 0;
	margin-bottom: -16px;
}
</style>

<c:set var="alleleClassTooltip"
	value="Allele Type: A classification of mutant alleles based upon the molecular function of the altered gene product.

Antimorphic: A type of mutation in which the altered gene product possesses an altered molecular function that acts antagonistically to the wild-type allele. Antimorphic mutations are always dominant or semidominant. 

Gain of function: A type of mutation in which the altered gene product possesses a new molecular function or a new pattern of gene expression. Gain-of-function mutations are almost always Dominant or Semidominant. 

Hypermorphic: A type of mutation in which the altered gene product possesses an increased level of activity, or in which the wild-type gene product is expressed at a increased level. 

Hypomorphic: A type of mutation in which the altered gene product possesses a reduced level of activity, or in which the wild-type gene product is expressed at a reduced level.
">

</c:set>

<c:set var="inheritanceTypeTooltip"
	value="Inheritance Type: The phenotype effect of a given allele in reference to another allele with respect to a particular trait.

Co-Dominant: Two different alleles for a phenotypic trait are both present.

Dominant: An allele that causes the same phenotype regardless of the homozygous or heterozygous state.

Incompletely dominant: Also known as semi-dominant. An allele causes intermediate phenotype in the heterozygous state that is distinct from homozygous mutant and homozyogous wild type.

Recessive: An allele that lacks phenotype when in the heterozygous state.

Unknown: Used when the inheritance type is not known or the information is not yet available.
">

</c:set>

<div id="allele-displayer" class="collection-table">

	<c:set var="rowCount" value="${fn:length(list)}" />

	<div class="header">
		<h3>

			<c:out value="${rowCount}" />
			Alleles
		</h3>
		<p>
			Data Source: <a target="_blank"
				href="/${WEB_PROPERTIES['webapp.path']}/portal.do?class=DataSet&externalids=TAIR+Polymorphism">TAIR/ABRC
				Polymorphisms</a>
		</p>
	</div>

	<c:choose>
		<c:when test="${!empty list}">

			<div>
				<c:set var="geneList" value="${geneName}" />

				<table>
					<thead>
						<tr>
							<th>Allele Name</th>
							<th>Sequence Alteration Type</th>
							<th class="tooltip-top" data-tooltip="${alleleClassTooltip}">Allele
								Type</th>
							<th>Mutagen</th>
							<th class="tooltip-top" data-tooltip="${inheritanceTypeTooltip}">Inheritance
								Type</th>
						</tr>

					</thead>
					<tbody>
						<c:forEach var="item" items="${list}">
							<tr>
								<td><a href="report.do?id=${item.objectId}"><i>${item.name}</i></a>
								</td>
								<td>${item.sequenceAlterationType}</td>
								<td>${item.alleleClass}</td>
								<td>${item.mutagen}</td>
								<td>${item.inheritanceType}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

		</c:when>
		<c:otherwise>
    			No Allele Data Available
  		</c:otherwise>
	</c:choose>

</div>

<!-- /alelleDisplayer.jsp -->
