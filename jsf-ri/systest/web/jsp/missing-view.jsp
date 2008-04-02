<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>JSP page missing a view</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>JSP page missing a view</h1>

  <h:form>

    <h:panelGrid columns="1">

      <h:outputText value="Hello" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

    <hr>
  </body>
</html>
