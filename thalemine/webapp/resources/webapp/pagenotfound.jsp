<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<tiles:importAttribute/>

<!-- pagenotfound.jsp -->
<html:xhtml/>

<div class="body pagenotfound">
  <h1>Oops, Page Not Found</h1>

  <p>The page you attempted to access does not exist. Try...</p>
  <ol>
    <li>going to the home page</li>
    <li>using the quicksearch</li>
    <li>or <a href="#" onclick="showContactForm()">Contact us</a> at araport@jcvi.org </li>
  </ol>
</div>
<a href="${WEB_PROPERTIES['project.sitePrefix']}" alt="Home" rel="NOFOLLOW"><img id="logo" src="${WEB_PROPERTIES['project.siteLogo']}" width="45px" height="43px" alt="Logo" /></a>
<!-- <img border="0" src="model/images/logo.png" title="Sorry..."/> -->

<!-- /pagenotfound.jsp -->
