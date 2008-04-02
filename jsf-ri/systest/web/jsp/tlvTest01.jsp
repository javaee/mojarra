<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>TLV Test 01</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>TLV Test 01</h1>

<f:view>

  <h:form>

    <p>This is a bound button</p>

    <h:commandButton binding="#{test3.boundButton}" />

  </h:form>

</f:view>

    <hr>
  </body>
</html>
