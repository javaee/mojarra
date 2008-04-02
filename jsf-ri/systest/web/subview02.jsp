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
<title>subview02</title>
</head>
<body>
<h:outputText value="Begin test <c:import> with subview tag in importing page"/>
<f:subview id="foo02">
<c:import url="foo02.jsp"/>
</f:subview>
<h:outputText value="subview02"/>
<f:subview id="bar02">
<c:import url="bar02.jsp"/>
</f:subview>
<h:outputText value="End test <c:import> with subview tag in importing page"/>
</body>
</html>
</f:view>
