<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!-- eFPBrowserDisplayer.jsp -->
<div class="basic-table">
<h3>eFP Visualization</h3>
<br />

<c:set var="object" value="${reportObject.object}"/>

<c:choose>
<c:when test="${((!empty object.chromosomeLocation && !empty object.chromosome)
                || className == 'Chromosome') && className != 'ChromosomeBand'}">


<div id="eFPBrowser" class="feature basic-table">
  <c:set var="name" value="${object.primaryIdentifier}"/>
  <c:set var="datasource" value="Developmental_Map"/>

  <c:choose>
  <c:when test="${WEB_PROPERTIES['bar.eFPBrowser.prefix'] != null}">
  <style type="text/css">
      #powerby { padding: 5px; text-align: center; }
      #powerby a { color: rgb(136, 136, 136); text-decoration: none; background-color: white; }
      #powerby img { vertical-align: middle; }
  </style>
  <div>
      <input type="hidden" id="agi" value="${name}" />
      <label for="datasource"><strong>Data Source: </strong></label>
      <select id="datasource" name="datasource">
          <option value="Abiotic_Stress">Abiotic Stress</option>
          <option value="Abiotic_Stress_At-TAX">Abiotic Stress At-TAX</option>
          <option value="Abiotic_Stress_II">Abiotic Stress II</option>
          <option value="Biotic_Stress">Biotic Stress</option>
          <option value="Biotic_Stress_II">Biotic Stress II</option>
          <option value="Chemical">Chemical</option>
          <option value="Development_RMA">Development RMA</option>
          <option value="Developmental_Map" selected="selected">Developmental Map</option>
          <option value="Developmental_Map_At-TAX">Developmental Map At-TAX</option>
          <option value="Guard_Cell">Guard Cell</option>
          <option value="Hormone">Hormone</option>
          <option value="Lateral_Root_Initiation">Lateral Root Initiation</option>
          <option value="Light_Series">Light Series</option>
          <option value="Natural_Variation">Natural Variation</option>
          <option value="Regeneration">Regeneration</option>
          <option value="Root">Root</option>
          <option value="Root_II">Root II</option>
          <option value="Seed">Seed</option>
          <option value="Tissue_Specific">Tissue Specific</option>
      </select>
      <br /><br />
      <img id="${name}_eFP" src="${WEB_PROPERTIES['bar.eFPBrowser.prefix']}?request={%22agi%22:%22${name}%22,%22datasource%22:%22${datasource}%22}" />
  </div>
  <div id="powerby">
      <a onmouseout="this.style.backgroundColor='white';" onmouseover="this.style.backgroundColor='#f1f1d1';" title="BAR eFP Webservices" target="_blank" href="http://bar.utoronto.ca/webservices/efp_service/efp_service.php">
          Powered by <img border="0/" src="http://bar.utoronto.ca/ntools/bbc_logo_small.gif" height="15" width="15"> BAR eFP Webservices
      </a>
  </div>
  <script type="text/javascript">
      jQuery('#datasource').change(function() {
          var bar_eFPBrowser_prefix = "${WEB_PROPERTIES['bar.eFPBrowser.prefix']}";
          var agi = jQuery('#agi').val();
          var datasource = jQuery('#datasource').val();

          jQuery('#'+agi+'_eFP').attr('src', bar_eFPBrowser_prefix+'?request={"agi":"'+agi+'","datasource":"'+datasource+'"}');
      });
  </script>
  </c:when>
  <c:otherwise>
   	<p>There was a problem rendering the BAR eFP Browser</code>.</p>
	<script type="text/javascript">
		jQuery('#eFPBrowser').addClass('warning');
	</script>
  </c:otherwise>
  </c:choose>
</div>
</c:when>
<c:otherwise>
<div id="eFPBrowser" class="feature basic-table warning">
  <h3><fmt:message key="sequenceFeature.eFPBrowser.message"/></h3>
  <p>There was a problem rendering the BAR eFP Browser.</p>
</div>
</c:otherwise>
</c:choose>
<!-- /eFPBrowserDisplayer.jsp -->
