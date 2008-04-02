<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Command Link Back Button</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Command Link Back Button</h1>

<f:view>

  <h:form id="form">

    <h:commandLink id="link" action="#{bean.linkPressed}">
      <h:outputText value="reload" />
    </h:commandLink>

    <p />

    <h:commandButton id="button" style="color: red" value="reload" 
                     action="#{bean.buttonPressed}" />

    <p />

    <h:outputText value="#{bean.result}" />

    <h:messages />


  </h:form>

</f:view>

  </body>
</html>
