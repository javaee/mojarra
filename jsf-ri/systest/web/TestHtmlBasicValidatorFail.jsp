<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
  pageContext.setAttribute("hello", new String("hello"), PageContext.REQUEST_SCOPE);
%>

<h1>TLV commandButton, no lable or key</h1>
This page should FAIL.
<br>
<br>

<f:view>

  <p>This must have either a label or key attribute.</p>
  <h:commandButton>
    <h:outputText value="#{hello}"/>
  </h:commandButton>

</f:view>

</body>
</head>
</html>
