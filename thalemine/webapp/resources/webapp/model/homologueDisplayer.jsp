<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- homologueDisplayer.jsp -->
<div id="homologue-displayer" class="collection-table">

<div class="header">
<h3>Panther Homologs</h3>
<p id="homologue_dataSource"></p>
</div>

<c:choose>
<c:when test="${homologues != null && !empty homologues}">
<table class="tiny-font">
  <thead>
  <tr>
    <c:forEach items="${homologues}" var="entry">
      <th><c:out value="${entry.key}"/></th>
    </c:forEach>
  </tr>
  </thead>
  <tbody>
    <tr>
    <c:forEach items="${homologues}" var="entry">
      <c:set var="genes" value="${entry.value}"/>
      <c:choose>
        <c:when test="${empty genes}">
          <td></td>
        </c:when>
        <c:otherwise>
          <td class="one-line">
            <c:forEach items="${genes}" var="resultElement">
              <a href="report.do?id=${resultElement.id}">${resultElement.field}</a>
            </c:forEach>
          </td>
        </c:otherwise>
      </c:choose>
    </c:forEach>
    </tr>
  </tbody>
</table>
<script>
var root = window.location.protocol + "//" + window.location.host + "/thalemine";
var homologue_Service = new intermine.Service({ root: root});
var query = {
    from: 'DataSource',
    select: ["name", "url"],
    where: {
        name: 'Panther'
    }};

    homologue_Service.rows(query).then(function (rows) {
        rows.forEach(function printRow(row) {
	    link = 'Data Source: <a target="_blank" href="'+row[1]+'">'+row[0]+'</a>';
	    jQuery('#homologue_dataSource').html(link);
        });
    });
    </script>
</c:when>

<c:when test="${homologues != null && empty homologues}">
  <p style="font-style:italic;">No data found</p>
</c:when>
<c:otherwise>
  <p>There was a problem rendering the displayer.</p>
  <script type="text/javascript">
    jQuery('#homologue-displayer').addClass('warning');
  </script>  
</c:otherwise>
</c:choose>
</div>

<!-- /homologueDisplayer.jsp -->
