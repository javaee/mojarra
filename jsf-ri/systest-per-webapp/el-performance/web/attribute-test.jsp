<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test Attribute/Property Transparency Performance</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Test Attribute/Property Transparency Performance</h1>

<f:view>

  <h:form id="form">

    <h:panelGrid columns="2">

      <h:outputText value="Repetitions:" />

      <h:inputText id="reps" value="#{evaluator.reps}" />

      <h:outputText value="Number of Threads:" />

      <h:inputText id="numThreads" value="#{evaluator.numThreads}" />

      <h:outputText value="Show results:" />

      <h:selectBooleanCheckbox value="#{evaluator.showResults}" />

      <!-- expression evaluator widgets -->

      <h:commandButton id="c0" value="evaluate" 
                       actionListener="#{evaluator.doAttributeMapGet}"/>

    </h:panelGrid>

    <hr />

    <p>Elapsed time: <h:outputText value="#{evaluator.elapsedTime}" /></p>

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
