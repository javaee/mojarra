<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>test</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>test</h1>

<f:view>

  <h:form>

    <p>Page Two</p>

    <h:commandLink id="one" action="one">
      <h:outputText value="one" />
    </h:commandLink>

<br />

    <h:commandLink id="three" action="three">
      <h:outputText value="three" />
    </h:commandLink>


  </h:form>

</f:view>

    <hr>
  </body>
</html>
