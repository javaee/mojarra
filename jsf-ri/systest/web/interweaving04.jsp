<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<%
    pageContext.setAttribute("cond", Boolean.TRUE,
                             PageContext.REQUEST_SCOPE);
 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<f:view>
<html>
<head>
<title>jstl-if-without-id</title>
</head>
<body>
<h:outputText value="[First]"/>
<c:if test="${requestScope.cond}">
  <h:outputText value="[Second]"/>
</c:if>
<h:outputText value="[Third]"/>
</body>
</html>
</f:view>
