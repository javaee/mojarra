<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test EL Performance</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Test EL Performance</h1>

<f:view>

  <h:form id="form">
  
    <p>Number of views in session is 
       <h:outputText value="#{sessionPeek.numberOfViewsInSession}" />.</p>

    <h:commandButton value="back" action="back"/>

  </h:form>

</f:view>

  </body>
</html>
