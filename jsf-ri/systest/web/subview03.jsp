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
<title>subview03</title>
</head>
<body>
<p>
<h:outputText value="Begin test <c:include> with subview tag in included page"/></p>

<p><jsp:include page="foo01.jsp"/></p>

<p><h:outputText value="subview03"/></p>

<p><jsp:include page="bar01.jsp"/></p>

<p><h:outputText value="End test <c:include> with subview tag in included page"/></p>
</body>
</html>
</f:view>
