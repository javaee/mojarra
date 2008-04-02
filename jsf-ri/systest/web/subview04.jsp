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
        <title>subview04</title>
    </head>

    <body>
    <p>
        <h:outputText value="[A]"/></p>

    <f:subview id="foo02">
        <p><h:outputText value="Begin test < c:include> with subview tag in
            including page"/></p>

        <p>
            <jsp:include page="bar01.jsp"/>
        </p>
    </f:subview>

    <p><h:outputText value="subview04"/></p>

    <f:subview id="bar02">
        <p>
            <jsp:include page="bar02.jsp"/>
        </p>
    </f:subview>

    <p><h:outputText value="End test < c:include> with subview tag in including
        page"/></p>
    </body>
    </html>
</f:view>
