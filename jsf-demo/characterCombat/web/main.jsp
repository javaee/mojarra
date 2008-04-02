<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>

<f:view>
<html>
<head>
  <title>
  </title>
</head>

<body>

  <h:dataTable value="#{modelBean.dataTable}"
               var="character" >

    <f:facet		 name="header">
      <h:outputText    value="Table Header"/>
    </f:facet>

    <f:facet		 name="footer">
      <h:outputText    value="Table Footer"/>
    </f:facet>

    <h:column>
      <f:facet name="header">
          <h:outputText value="Name"/>
      </f:facet>
      <f:facet name="footer">
          <h:outputText value="Name Footer"/>
      </f:facet>

        <h:outputText value="#{character.name}"/>

    </h:column>

    <h:column>
      <f:facet name="header">
          <h:outputText value="Species"/>
      </f:facet>
      <f:facet name="footer">
          <h:outputText value="Species Footer"/>
      </f:facet>

        <h:outputText value="#{character.species}"/>

    </h:column>

    <h:column>
      <f:facet name="header">
          <h:outputText value="Language"/>
      </f:facet>
      <f:facet name="footer">
          <h:outputText value="Language Footer"/>
      </f:facet>

        <h:outputText value="#{character.language}"/>

    </h:column>

    <h:column>
      <f:facet name="header">
          <h:outputText value="Immortal"/>
      </f:facet>
      <f:facet name="footer">
          <h:outputText value="Immortal Footer"/>
      </f:facet>

        <h:outputText value="#{character.immortal}"/>

    </h:column>

  </h:dataTable>

</body>

</html>
</f:view>
