<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">

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
<div id="stock-container">
 
 </div>
 
<style>

.link{
color: blue;
}

</style>

<script type="text/javascript">

var geneId = "${name}";
var serviceURL = "${serviceURL}";

var formatLink = function(url, text, target, cls){
    target = target || "_self";
    text = text || url;
   if (cls == 'extlink') {
         return '<a class="'+cls+'" href="'+url+'" target="'+target+'">' + text + '</a>';
      } else {
         return '<a href="'+url+'" target="'+target+'">' + text + '</a>';
      }
    };

var wrapSpan = function(text){
        return '<span  class="link">'+text+'</span>';
     };
    
     var formatDataLink = function(name, id, text, dataClass, value){
	 var url = '/${WEB_PROPERTIES['webapp.path']}' + '/report.do?id=' + id + '#StockAvailabilityDisplayer';
	 var target = "_blank";
		 
	 console.log("In formatDataLink!");
	 
	 return name + ' &nbsp;&nbsp;' + '<a href="'+url+'" target="'+target+'">' + wrapSpan(' Order Stock ' + '<i class="fa fa-external-link"></i>')  + '</a>';
		
 }
 
var selector = '#stock-container';
var service  = {root: serviceURL};
var query    = {
		  "from": "Stock",
		  "select": [
		    "germplasmName",
		    "genotypes.primaryIdentifier"
		  ],
		  "where": [
		    {
		      "path": "genotypes.alleles.affectedGenes.primaryIdentifier",
		      "op": "=",
		      "value": geneId
		    }
		  ]
		};

var stockFormatter = function(o) {
	
	console.log("In Stock Formatter!");
    return formatDataLink(o.get('germplasmName'), o.get('id'), "Stock", undefined);
};


imtables.formatting.registerFormatter(
		stockFormatter,
	    'genomic',
	    'Stock',
	    ['germplasmName', 'id']
	);

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
