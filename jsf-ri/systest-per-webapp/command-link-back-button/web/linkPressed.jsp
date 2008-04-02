<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Link Pressed</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Link Pressed</h1>

<f:view>

  <h:form id="form">

    <h:outputText value="Link Pressed" />

    <h:messages />

    <h:commandButton action="back" value="back" />

  </h:form>

</f:view>

  </body>
</html>
