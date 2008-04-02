<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
