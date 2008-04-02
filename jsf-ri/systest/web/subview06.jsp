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
<title>subview06</title>
</head>
<body>
<p><h:outputText value="Begin test <c:import> with iterator tag in imported page"/></p>
<f:subview id="subviewOuter">
<br />
<p><c:import url="subviewIterator01.jsp"/></p>
</f:subview>

<p><h:outputText value="Text from subview06.jsp"/></p>

<p><h:outputText value="End test <c:import> with iterator tag in imported page"/></p>
</body>
</html>
</f:view>
