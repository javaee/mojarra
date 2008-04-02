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
  pageContext.setAttribute("ford", new String("harrison"));
%>

<h1>TLV commandButton, invalid 'id' expression</h1>
This page should Fail.
<br>
<br>

<f:view>

  <p>This command button has an invalid id expression</p>
  <h:commandButton id="#{#{ford}}" value="hello" />

</f:view>

</body>
</head>
</html>
