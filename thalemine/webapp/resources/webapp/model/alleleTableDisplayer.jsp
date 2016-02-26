<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!-- aleleleTableDisplayer.jsp -->
<div id="alleleTableDisplayer" class="collection-table">

<div class="header">
	<h3>Alleles</h3>
	<p>
			Data Source: <a target="_blank"
				href="/${WEB_PROPERTIES['webapp.path']}/portal.do?class=DataSet&externalids=TAIR+Polymorphism">TAIR/ABRC
				Polymorphisms</a>
    </p>
</div>

<c:set var="object" value="${reportObject.object}"/>
<c:set var="name" value="${object.primaryIdentifier}"/>

<div id="allele-table-container" class="collection-table imtables-dashboard container-fluid imtables">
<link rel="stylesheet" type="text/css" href="${WEB_PROPERTIES['head.cdn.location']}/js/intermine/im-tables/latest/imtables.css">
 <div id="allele-container">
 
 </div>

<script type="text/javascript">

var geneId = "${name}";
var serviceURL = "${serviceURL}";

var selector = '#allele-container';
var service  = {root: serviceURL};
var query    = {
  "from": "Allele",
  "select": [
    "primaryIdentifier",
    "sequenceAlterationType.name",
    "alleleClass.name",
    "mutagen.name",
    "inheritanceMode.name"
  ],
  "orderBy": [
    {
      "path": "primaryIdentifier",
      "direction": "ASC"
    }
  ],
  "joins": [
    "alleleClass",
    "inheritanceMode",
    "mutagen",
    "sequenceAlterationType"
  ],
  "where": [
    {
      "path": "affectedGenes.primaryIdentifier",
      "op": "=",
      "value": geneId
    }
  ]
};

imtables.configure('DefaultPageSize', 5);
imtables.configure('TableCell.IndicateOffHostLinks', false);
imtables.configure('Tables.CacheFactor', 5);

imtables.loadTable(
  selector,
  {"start":0,"size":5},  
  {service: service, query: query}
).then(
  function (table) { console.log('Table loaded', table); },
  function (error) { console.error('Could not load table', error); }
);
</script>
	</div>
</div>
<!-- /alleleTableDisplayer.jsp -->
