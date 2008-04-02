<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Property that violates scope rules</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Property that violates scope rules</h1>

<f:view>

  <h:form>
 
    <h:outputText value="#{test4.stringProperty}" />
   
  </h:form>

</f:view>

    <hr>
  </body>
</html>
