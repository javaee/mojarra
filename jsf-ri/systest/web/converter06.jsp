
<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Converters</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
    <%@ taglib uri="/WEB-INF/taglib.tld"           prefix="s" %>
  </head>

  <body>
    <h1>Converters</h1>

<f:loadBundle basename="com.sun.faces.CustomMessages" var="customBundle"/>

<f:view>

  <h:form id="form">

    <h:panelGrid id="panelGrid" columns="3">


<%--
      Exercises javax.faces.webapp.ConverterELTag when ConverterException
      Expected result: FacesMessage queued;  Log message;
--%>
      <h:outputText value="Number4:" />
      <h:inputText id="number4" label="Number4" size="10" maxlength="20" value="aaa">
         <f:convertNumber type="number" />
      </h:inputText>
      <h:message for="number4" showSummary="true" />

<%--
      Exercises javax.faces.webapp.ConverterELTag when ConverterException
      Expected result: Log message;
--%>
      <h:outputText value="Number5:" />
      <h:outputText id="number5" value="aaa">
         <f:convertNumber type="number" />
      </h:outputText>
      <h:message for="number5" showSummary="true" />

<%--
      Exercises javax.faces.webapp.ConverterELTag when ConverterException
      Expected result: FacesMessage queued;  Log message; 
--%>
      <h:outputText value="Number6:" />
      <h:inputText id="number6" label="Number6" size="10" maxlength="20" value="aaa" converterMessage="My own message">
         <f:convertNumber type="number" />
      </h:inputText>
      <h:message for="number6" showSummary="false" />

<%--
      Exercises javax.faces.webapp.ConverterTag when ConverterException
      Expected result: FacesMessage queued;  Log message; 
--%>
      <h:outputText value="Number6:" />
      <h:outputText value="Number7:" />
      <h:inputText id="number7" label="Number7" size="10" maxlength="20" value="aaa">
         <s:converter converterId="javax.faces.Number" />
      </h:inputText>
      <h:message for="number7" showSummary="true" />

<%--
      Exercises javax.faces.webapp.ConverterTag when ConverterException
      Expected result: Log message;
--%>
      <h:outputText value="Number8:" />
      <h:outputText id="number8" value="aaa">
         <s:converter converterId="javax.faces.Number" />
      </h:outputText>
      <h:message for="number8" showSummary="true" />

<%--
      Exercises javax.faces.webapp.ConverterTag when ConverterException
      Expected result: FacesMessage queued;  Log message; 
--%>
      <h:outputText value="Number9:" />
      <h:inputText id="number9" label="Number6" size="10" maxlength="20" value="aaa" converterMessage="My own message">
         <s:converter converterId="javax.faces.Number" />
      </h:inputText>
      <h:message for="number9" showSummary="false" />
      <h:commandButton value="submit" /> 
    </h:panelGrid>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
