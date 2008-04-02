<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
    <html>
    <head>
        <title>jsp-dynamic-00</title>
    </head>

    <body>
    Dummy page to force resetting the component tree.
    <h:outputText id="dummy" value="DUMMY"/>
    <h:outputText value="#{ResetUniqueRequestIdBean.reset}"/>
    </body>
    </html>
</f:view>
