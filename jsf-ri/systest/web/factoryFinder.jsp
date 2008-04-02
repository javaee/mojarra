<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Print out the names of the current Factories</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Print out the names of the current Factories</h1>

<f:view>

  <h:form>

      <h:outputText value="#{test3.factoryPrintout}" />

  </h:form>

</f:view>

    <hr>
  </body>
</html>
