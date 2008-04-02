<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>f:attribute</title>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
  </head>

  <body>
    <h1>f:attribute</h1>

<f:view>
    <h:outputText value="This Should Be Red">
      <f:attribute name="style" value="color: red" />
    </h:outputText>
</f:view>

  </body>
</html>
