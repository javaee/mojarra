<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
    <html>
    <head>
        <title>jstl-import-03</title>
    </head>

    <body>
    <h:outputText value="[1]"/>
    <c:choose>
        <c:when test="${param.choose == 'a'}">
            <c:import url="jstl-import-03a.jsp"/>
        </c:when>
        <c:when test="${param.choose == 'b'}">
            <c:import url="jstl-import-03b.jsp"/>
        </c:when>
        <c:otherwise>
            <c:import url="jstl-import-03c.jsp"/>
        </c:otherwise>
    </c:choose>
    <h:outputText value="[3]"/>
    </body>
    </html>
</f:view>
