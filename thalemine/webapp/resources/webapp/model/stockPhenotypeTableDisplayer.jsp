<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!-- stockPhenotypeTableDisplayer.jsp -->
<div id="stockPhenotypeTableDisplayer" class="collection-table">

<div class="header">
	<h3>Stocks</h3>
	<p>
			Data Source: <a target="_blank" href="/${WEB_PROPERTIES['webapp.path']}/portal.do?class=DataSet&externalids=TAIR+Germplasm">TAIR/ABRC Germplasms/Seed
				Stocks</a>
			</span>
	</p>
</div>

<c:set var="object" value="${reportObject.object}"/>
<c:set var="name" value="${object.primaryIdentifier}"/>

<div id="stock-table-container" class="collection-table imtables-dashboard container-fluid imtables">
<link rel="stylesheet" type="text/css" href="${WEB_PROPERTIES['head.cdn.location']}/js/intermine/im-tables/latest/imtables.css">
 <div id="stock-container">
 
 </div>

<script type="text/javascript">

var geneId = "${name}";
var serviceURL = "${serviceURL}";

var selector = '#stock-container';
var service  = {root: serviceURL};
var query    = {
		  "from": "Stock",
		  "select": [
		    "germplasmName",
		    "genotypes.primaryIdentifier",
		    "backgroundAccessions.abbreviationName",
		    "genotypes.phenotypesObserved.description",
		    "genotypes.genotypephenotypeAnnotations.publication.firstAuthor",
		    "genotypes.genotypephenotypeAnnotations.publication.year",
		    "stockAvailabilities.stockCenter.name"
		  ],
		  "joins": [
		    "backgroundAccessions",
		    "genotypes.genotypephenotypeAnnotations",
		    "genotypes.genotypephenotypeAnnotations.publication",
		    "genotypes.phenotypesObserved",
		    "stockAvailabilities",
		    "stockAvailabilities.stockCenter"
		  ],
		  "where": [
		    {
		      "path": "genotypes.alleles.affectedGenes.primaryIdentifier",
		      "op": "=",
		      "value": geneId
		    }
		  ]
		};
var properties = { SubtableInitialState: 'open' };

imtables.configure('DefaultPageSize', 5);
imtables.configure('TableCell.IndicateOffHostLinks', false);
imtables.configure('Tables.CacheFactor', 5);

imtables.loadDash(
  selector,
  {"start":0,"size":5},  
  {service: service, query: query, properties: properties}
).then(
  function (table) { console.log('Gene Report: Stocks Table loaded', table); },
  function (error) { console.error('Gene Report: Could not load Stocks Table', error); }
);
</script>
	</div>
</div>
<!-- /stockPhenotypeTableDisplayer.jsp -->
