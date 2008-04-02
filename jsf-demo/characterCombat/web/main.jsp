<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>

<f:view>
<html>
<head>
  <title>
    CharacterCombat Main Page
  </title>
  <link rel="stylesheet" type="text/css"
    href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<body>

  <h2>Welcome to the Character Combat</h2>
  <p>This sample application illustrates how you can easily display data
     from a backing bean, process user input, handle navigation, and
     display the results all using the JavaServer Faces Framework</p>

  <p>This initial page displays a list of available characters in a table
     format. You can choose to add your own character to the list using
     the input text field or simply go on to the next page</p>

  <h:dataTable columnClasses="list-column-center,
                              list-column-center,
                              list-column-center,
                              list-column-center"
               headerClass="list-header"
               styleClass="list-background"
               value="#{modelBean.dataList}"
               var="character" >

    <f:facet name="header">
      <h:outputText value="List of Available Characters"/>
    </f:facet>

    <h:column>
      <f:facet name="header">
        <h:outputText value="Name"/>
      </f:facet>

        <h:outputText value="#{character.name}"/>

    </h:column>

    <h:column>
      <f:facet name="header">
          <h:outputText value="Species"/>
      </f:facet>

        <h:outputText value="#{character.species.type}"/>

    </h:column>

    <h:column>
      <f:facet name="header">
        <h:outputText value="Language"/>
      </f:facet>

        <h:outputText value="#{character.species.language}"/>

    </h:column>

    <h:column>
      <f:facet name="header">
        <h:outputText value="Immortal"/>
      </f:facet>

        <h:outputText value="#{character.species.immortal}"/>

    </h:column>

  </h:dataTable>

<br>

  <h:form>
    <h:panelGrid columnClasses="list-column-center,
                                list-column-center"
                 headerClass="list-header"
                 styleClass="inputList-background"
                 columns="2">
      <f:facet name="header">
        <h:outputText value="Customize Character:"/>
      </f:facet>
      <h:inputText value="#{modelBean.customName}" />
      <h:selectOneListbox value="#{modelBean.customSpecies}"
        required="true" size="1" >
        <f:selectItems value="#{modelBean.speciesOptions}"/>
      </h:selectOneListbox>
    </h:panelGrid>
<br>
    <h:panelGrid columns="1">
      <h:commandButton action="#{modelBean.addCustomName}" value="Add Name"/>
    </h:panelGrid>

    <jsp:include page="wizard-buttons.jsp"/>


  </h:form>

</body>

</html>
</f:view>
