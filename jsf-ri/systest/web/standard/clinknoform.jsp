<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>clinknoform</title>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
  </head>
  <body>
  <f:view>

      <h:commandLink id="clink1" value="Link1"/>

      <h:commandLink id="clink2">
          <h:outputText value="Link2"/>
      </h:commandLink>

      <h:commandLink id="clink3" value="Click me once">
          <h:outputText value=" and click me twice"/>
      </h:commandLink>

  </f:view>
  </body>
</html>