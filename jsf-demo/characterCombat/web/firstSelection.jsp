<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>

<f:view>
<html>
<head>
  <title>
    First Selection Page
  </title>
  <link rel="stylesheet" type="text/css"
    href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<body>
  <h2>First Selection Page</h2>

  <p>This page illustrates how the same data from the model can be
     displayed in a different format using the built-in JavaServer
     Faces components</p>

  <p>You may now choose your first character that will be waging a
  magical combat</p>

  <h:form>

    <h:panelGrid columns="1">

      <h:selectOneRadio 
        layout="pageDirection" 
        value="#{modelBean.currentSelection}">
        <f:selectItems 
          value="#{modelBean.charactersToSelect}" />
      </h:selectOneRadio>

    </h:panelGrid>

    <h:commandButton
      action="#{modelBean.addFirstSelection}"
      value="Submit First Character Selection"/>

  </h:form>

</body>
</html>
</f:view>
