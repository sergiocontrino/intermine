<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- footer.jsp -->
<br/>
<br/>
<br/>

<div class="body" align="center" style="clear:both">
    <!-- powered -->
    <p>Powered by</p>
    <a target="new" href="http://intermine.org" title="InterMine">
        <img src="images/icons/intermine-footer-logo.png" alt="InterMine logo" />
    </a>

    <!-- contact -->
    <c:if test="${pageName != 'contact'}">
        <div id="contactFormDivButton">
            <im:vspacer height="11" />
            <div class="contactButton">
                <a href="#" onclick="showContactForm();return false">
                    <b><fmt:message key="feedback.title"/></b>
                </a>
            </div>
        </div>
        <div id="contactFormDiv" style="display:none;">
            <im:vspacer height="11" />
            <tiles:get name="contactForm" />
        </div>
    </c:if>
    <br/>
</div>

<div class="body bottom-footer">
    <!-- funding -->
    <div align="center" class="body funding-footer">
        <fmt:message key="funding" />
    </div>

    <ul class="footer-links">
        <!-- contact us form link -->
        <li><a href="#" onclick="showContactForm();return false;">Contact Us</a></li>
        <c:set value="${WEB_PROPERTIES['header.links']}" var="headerLinks"/>
        <!-- web properties -->
        <c:forEach var="entry" items="${headerLinks}" varStatus="status">
            <c:set value="header.links.${entry}" var="linkProp"/>
            <c:choose>
                <c:when test="${!empty WEB_PROPERTIES[linkProp]}">
                    <li><a href="${WEB_PROPERTIES[linkProp]}">${entry}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${WEB_PROPERTIES['project.sitePrefix']}/${entry}.shtml">${entry}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>

    <div style="clear:both"></div>
</div>
<!-- /footer.jsp -->
