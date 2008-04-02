<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>SelectMany with mismatched value type</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>SelectMany with mismatched value type</h1>

<f:view>

  <h:form>

    <h:panelGrid columns="1">

      <h:selectManyListbox
        required="true" 
        value="#{test3.selection}">
        <f:selectItems 
          value="#{test3.selectList}" />
      </h:selectManyListbox>

      <h:messages />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

</f:view>

    <hr>
  </body>
</html>
