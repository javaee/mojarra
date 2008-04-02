<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<html>
<title>Validator Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
</head>
<body>

<%
  pageContext.setAttribute("first", new String("Harrison"));
  pageContext.setAttribute("last", new String("Ford"));
%>

<h1>TLV commandButton, valid 'id' expression</h1>
This page should Succeed.
<br>
<br>

<f:view>
  <c:set var="temp" scope="request" value="${pageScope.first}${pageScope.last}"/>
  <h:commandButton id="temp" value="indiana" />

</f:view>

</body>
</head>
</html>
