<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<html>
<title>Validator Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
</head>
<body>

<%
  pageContext.setAttribute("ford", new String("harrison"), PageContext.REQUEST_SCOPE);
%>

<h1>TLV commandButton, valid 'binding' expression</h1>
This page should Succeed.
<br>
<br>

<f:view>

  <h:commandButton value="hello" binding="#{requestScope.ford}" />

</f:view>

</body>
</head>
</html>
