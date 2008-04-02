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
  pageContext.setAttribute("ford", new String("harrison"), PageContext.REQUEST_SCOPE);
%>

<h1>TLV command_button, invalid 'componentRef' expression</h1>
This page should Fail.
<br>
<br>

<f:view>

  <p>This command button has an invalid componentRef expression</p>
  <h:command_button value="hello" componentRef=".ford"/>

</f:view>

</body>
</head>
</html>
