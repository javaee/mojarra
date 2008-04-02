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
<title>subview01</title>
</head>
<body>
<h:outputText value="Begin test <c:import> with subview tag in imported page"/>

<p>
<c:import url="foo01.jsp"/>
</p>

<p><h:outputText value="subview01"/></p>

<p><c:import url="bar01.jsp"/></p>

<p><h:outputText value="End test <c:import> with subview tag in imported page"/></p>
</body>
</html>
</f:view>
