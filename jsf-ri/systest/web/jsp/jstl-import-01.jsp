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
        <title>jstl-import-01</title>
    </head>

    <body>
    <h:outputText value="[A]"/>
    <c:import url="jstl-import-01a.jsp"/>
    <h:outputText value="[C]"/>
    <c:import url="jstl-import-01b.jsp"/>
    <h:outputText value="[E]"/>
    </body>
    </html>
</f:view>
