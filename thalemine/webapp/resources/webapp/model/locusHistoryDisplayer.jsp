<!doctype html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>


<!-- curatedProteinsDisplayer.jsp -->
<div id="curated-proteins-displayer" class="collection-table">

  <div class="header">
    <h3>
      Locus History
    </h3>
  </div>

  <table>
    <thead>
      <tr>
        <th>Operation</th>
        <th>Date</th>
        <th>Source</th>
        <th>Loci Involved</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="protein" items="${list}">
          <td><c:out value="${protein.operation}"/>
              <c:if test="${!empty ops[protein.operation] }">
               &nbsp;<im:helplink text="${ops[protein.operation]}"/>
              </c:if>
          </td>
          <td>${protein.date}</td>
          <td>${protein.source}</td>
          <td><html:link action="/report?id=${protein.id}">${protein.locus}</html:link></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>



  <div class="show-in-table">
    <html:link action="/collectionDetails?id=${reportObject.object.id}&amp;field=locusHistory">
      Show all in a table &raquo;
    </html:link>
  </div>

  <script type="text/javascript">
  (function() {
    var t = jQuery('#curated-proteins-displayer');
    t.find('h3 div.right a').click(function(e) {
      t.find('table tbody tr').toggle();
      e.preventDefault();
    });
  })();
  </script>
</div>
<!-- /curatedProteinsDisplayer.jsp -->