<%@ tag body-content="scriptless" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="loginMessage" required="false" %>

<%
String returnToPath = "/" + (String) request.getAttribute("pageName") + ".do";
if (returnToPath != null) {
    if (request.getQueryString() != null) {
        returnToPath += "?" + request.getQueryString();
    }
    String encodedReturnToPath = java.net.URLEncoder.encode(returnToPath);
    request.setAttribute("returnToPath", encodedReturnToPath);
}
%>

<c:set var="loginController" value="login.do"/>
<c:set var="paramSep" value="?"/>
<c:set var="providerString" value=""/>
<c:set var="returnToString" value=""/>
<c:if test="${loginController != 'login'}">
  <c:if test="${!empty returnToPath}">
    <c:set var="returnToString" value="returnto=${returnToPath}"/>
  </c:if>
  <c:if test="${!empty OAUTH2_PROVIDERS && WEB_PROPERTIES['oauth2.allowed'] != 'false'}">
    <c:set var="loginController" value="oauth2authenticator.do"/>
    <c:set var="paramSep" value="&"/>
    <c:forEach var="provider" varStatus="status" items="${OAUTH2_PROVIDERS}">
      <c:if test="${status.first}">
        <c:set var="providerString" value="?provider=${provider}"/>
      </c:if>
    </c:forEach>
  </c:if>
</c:if>

<c:choose>
  <c:when test="${!PROFILE.loggedIn}">
    <a href="/${WEB_PROPERTIES['webapp.path']}/${loginController}${providerString}${paramSep}${returnToString}" rel="NOFOLLOW">
      <c:if test="${empty loginMessage}">
        <fmt:message var="loginMessage" key="menu.login"/>
      </c:if>
      ${loginMessage}
    </a>
  </c:when>
  <c:otherwise>
  <a href="<html:rewrite page="/logout.do"/>" rel="NOFOLLOW">
      <fmt:message key="menu.logout"/>
    </a>
  </c:otherwise>
</c:choose>
