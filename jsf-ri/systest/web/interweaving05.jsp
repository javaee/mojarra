<%@ page import="javax.servlet.jsp.PageContext"%>
<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<%
    pageContext.setAttribute("choose", "a",
                             PageContext.REQUEST_SCOPE);

%>

<f:view>
    <html>
    <head>
        <title>jstl-choose test without "id"</title>
    </head>

    <body>
    <h:outputText value="Begin jstl-choose test without id"/>
    <c:choose>
        <c:when test="${requestScope.choose == 'a'}">
            <h:outputText value="[FIRST]"/>
            <h:outputText value="[SECOND]"/>
        </c:when>
        <c:when test="${requestScope.choose == 'b'}">
            <h:outputText value="[THIRD]"/>
            <h:outputText value="[FOURTH]"/>
        </c:when>
        <c:otherwise>
            <h:outputText value="[FIFTH]"/>
            <h:outputText value="[SIXTH]"/>
        </c:otherwise>
    </c:choose>
    <h:outputText value="End jstl-choose test without id"/>
    </body>
    </html>
</f:view>
