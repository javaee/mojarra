<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Verify property ordering</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Verify property ordering</h1>

<f:view>

  <h:form>
 
    <h:outputText value="#{propertyOrder.order}" /> 
    <h:outputText value="#{propertyOrder.listProperty[0]}" />
    <h:outputText value="#{propertyOrder.listProperty[1]}" />
    <h:outputText value="#{propertyOrder.listProperty[2]}" />
    <h:outputText value="#{propertyOrder.listProperty[3]}" />
   
  </h:form>

</f:view>

    <hr>
  </body>
</html>
