<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
    <html>
    <head>
        <title>jstl-choose-01</title>
    </head>

    <body>
    <h:outputText value="[1]"/>
    <c:choose>
        <c:when test="${param.choose == 'a'}">
            <h:outputText value="[2a]"/>
            <h:outputText value="[2z]"/>
        </c:when>
        <c:when test="${param.choose == 'b'}">
            <h:outputText value="[2b]"/>
            <h:outputText value="[2y]"/>
        </c:when>
        <c:otherwise>
            <h:outputText value="[2c]"/>
            <h:outputText value="[2x]"/>
        </c:otherwise>
    </c:choose>
    <h:outputText value="[3]"/>
    </body>
    </html>
</f:view>
