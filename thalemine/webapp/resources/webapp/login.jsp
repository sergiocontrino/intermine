<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="im"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- login.jsp -->
<html:xhtml/>
<div class="body">

<h2>Account</h2>

<div id="login">
  <div class="plainbox">

    <div class="column">
      <c:if test="${!empty OPENID_PROVIDERS && WEB_PROPERTIES['openid.allowed'] != 'false' && isExternallyAccessible}">
        <div class="column second">
          <im:debug message="${OPENID_PROVIDERS}"/>
          <h3 class="openid"><fmt:message key="login.openid"/></h3>
          <c:forEach var="provider" items="${OPENID_PROVIDERS}">
            <a class="<c:out value="${fn:toLowerCase(provider)}"/>"
            href="/${WEB_PROPERTIES['webapp.path']}/openid?provider=${provider}"></a>
          </c:forEach>
        </div>
      </c:if>

      <c:if test="${!empty OAUTH2_PROVIDERS && WEB_PROPERTIES['oauth2.allowed'] != 'false'}">
        <div class="column second oauth2"><im:debug message="${OAUTH2_PROVIDERS}"/>
          <h3 class="oauth"><fmt:message key="login.oauth2"/></h3>
          <c:forEach var="provider" items="${OAUTH2_PROVIDERS}">
            <a class="oauth2-button"
               href="/${WEB_PROPERTIES['webapp.path']}/oauth2authenticator.do?provider=${provider}">
               <i class="fa fa-fw fa-<c:out value="${fn:toLowerCase(provider)}"/>"></i>
               ${provider}
            </a>
          </c:forEach>
        </div>
      </c:if>

      <div class="clear"></div>

      <div id="passwordDiv" style="padding-top:20px">
          <html:link styleClass="extlink" target="_blank" href="https://www.araport.org/user/password">Forgot password?</html:link>
        <br/>
      </div>
    </div>

    <div class="clear"></div>
  </div>
</div>

<div id="create">
  <div class="plainbox">
    <h3>Create account &hellip;</h3>

    <ul>
      <li>your <span class="lists">lists</span> and <span class="queries">queries</span> will be privately saved</li>
      <li>you can <span class="favorite">favorite</span> items</li>
      <li>you can share <span class="lists">lists</span> with other users</li>
    </ul>

    <br />

    <html:link styleClass="extlink" target="_blank" href="https://www.araport.org/user/register">Create <strong>Araport</strong> account now</html:link>
  </div>
</div>

<div class="clear"></div>
<!-- /login.jsp -->
