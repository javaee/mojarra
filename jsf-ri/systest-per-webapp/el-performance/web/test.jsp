<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test EL Performance</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Test EL Performance</h1>

<f:view>

  <h:form id="form">

    <h:panelGrid columns="2">

      <h:outputText value="Repetitions:" />

      <h:inputText id="reps" value="#{evaluator.reps}" />

      <h:outputText value="Show results:" />

      <h:selectBooleanCheckbox value="#{evaluator.showResults}" />

      <!-- expression evaluator widgets -->

      <h:inputText id="i0" value="#{evaluator.expressions[0]}" />

      <h:commandButton id="c0" value="evaluate" 
                       actionListener="#{evaluator.doGet}"/>

    </h:panelGrid>

    <hr />

    <p>

    <h:outputText rendered="#{evaluator.showResults}" value="Results:" />

    <pre>

<h:outputText rendered="#{evaluator.showResults}" 
              value="#{evaluator.results}" />

    </pre>

  </h:form>

</f:view>

  </body>
</html>
