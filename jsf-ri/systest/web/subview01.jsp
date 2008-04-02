<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
<c:import url="foo01.jsp"/>
<h:outputText value="subview01"/>
<c:import url="bar01.jsp"/>
<h:outputText value="End test <c:import> with subview tag in imported page"/>
</body>
</html>
</f:view>
