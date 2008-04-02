<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Nested Tables 2</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Nested Tables 2</h1>

<f:view>

  <h:form id="form">

    <h:dataTable id="outerData" value="#{outer}" var="outerVar">

      <h:column id="outerColumn0">

       <h:commandButton id="outerButton" action="#{bean.outerAction}" 
                        value="outerButton" />

      </h:column>

      <h:column id="outerColumn1">

        <h:dataTable id="innerData" value="#{inner.listDataModel}" 
                     var="innerVar">


          <h:column id="innerColumn">
            <h:commandButton id="innerButton" action="#{bean.innerAction}" 
                       value="innerButton" />
          </h:column>

        </h:dataTable>

      </h:column>

    </h:dataTable>

    <h:commandButton style="color: red" value="reload" />

    <p />

    <p>Outer Action called 
       <h:outputText value="#{bean.outerActionCallCount}" /> times</p>

    <p>Inner Action called 
       <h:outputText value="#{bean.innerActionCallCount}" /> times</p>

    <h:messages />


  </h:form>

</f:view>

  </body>
</html>
