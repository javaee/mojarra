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
        <title>interweaving07</title>
    </head>

    <body>
    <p><h:outputText value="Begin test < c:import> with iterator tag in imported
        page"/></p>
    <br/>

    <p><c:import url="subviewIterator02.jsp"/></p>

    <p><h:outputText value="Text from interweaving07.jsp"/></p>

    <p><h:outputText value="End test < c:import> with iterator tag in imported
        page"/></p>
    </body>
    </html>
</f:view>
