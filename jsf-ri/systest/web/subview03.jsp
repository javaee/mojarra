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
<h:outputText value="Begin test <c:include> with subview tag in included page"/>
<jsp:include page="foo01.jsp"/>
<h:outputText value="subview03"/>
<jsp:include page="bar01.jsp"/>
<h:outputText value="End test <c:include> with subview tag in included page"/>
</body>
</html>
</f:view>
