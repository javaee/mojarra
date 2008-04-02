<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Nested Tables</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Nested Tables</h1>

<f:view>

  <h:form id="form">

    <h:dataTable id="outerData" value="#{outer}" var="outerVar">

      <h:column id="outerColumn">

        <h:dataTable id="innerData" value="#{inner.listDataModel}" 
                     var="innerVar">

          <f:facet name="header">
            <h:outputText id="header" value="#{outerVar}" />
          </f:facet>

          <h:column id="innerColumn">
            <h:inputText id="inputText" value="#{innerVar.stringProperty}" />
          </h:column>

        </h:dataTable>

      </h:column>

    </h:dataTable>

    <h:commandButton style="color: red" value="reload" />

    <p />

    <h:messages />


  </h:form>

</f:view>

  </body>
</html>
