<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>

<f:view>
<html>
<head>
  <title>
    Second Selection Page
  </title>
  <link rel="stylesheet" type="text/css"
    href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<body>
  <h2>Second Selection Page</h2>

  <p>This page displays the same data as the previous page except it
     does not include the user's first combat choice so that character
     will not be picked twice.</p>

  <p>You may now choose your second character that will be waging a
  magical combat with the first.</p>


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
      action="#{modelBean.addSecondSelection}"
      value="Submit Second Character Selection"/>

  </h:form>

</body>
</html>
</f:view>
