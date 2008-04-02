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
        <title>interweaving01</title>
    </head>

    <body>
    <h:outputText value="Begin"/>
    test
    <h:outputText value="jstl import without verbatim"/>

    <c:import url="test01.jsp"/>

    <h:outputText value="End"/>
    test
    <h:outputText value="jstl import without verbatim"/>
    </body>
    </html>
</f:view>


