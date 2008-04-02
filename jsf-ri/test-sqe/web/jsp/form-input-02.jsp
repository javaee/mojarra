<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>form-input-02</title>
</head>
<body>

<h:form id="formInput02_form">

  <h:panelGrid columns="3">

    <h:outputText value="booleanProperty"/>
    <h:inputText id="booleanProperty" value="#{formInput02.booleanProperty}"/>
    <h:message for="booleanProperty"/>

    <h:outputText value="byteProperty"/>
    <h:inputText id="byteProperty" value="#{formInput02.byteProperty}"/>
    <h:message for="byteProperty"/>

    <h:outputText value="doubleProperty"/>
    <h:inputText id="doubleProperty" value="#{formInput02.doubleProperty}"/>
    <h:message for="doubleProperty"/>

    <h:outputText value="floatProperty"/>
    <h:inputText id="floatProperty" value="#{formInput02.floatProperty}"/>
    <h:message for="floatProperty"/>

    <h:outputText value="intProperty"/>
    <h:inputText id="intProperty" value="#{formInput02.intProperty}"/>
    <h:message for="intProperty"/>

    <h:outputText value="longProperty"/>
    <h:inputText id="longProperty" value="#{formInput02.longProperty}"/>
    <h:message for="longProperty"/>

    <h:outputText value="shortProperty"/>
    <h:inputText id="shortProperty" value="#{formInput02.shortProperty}"/>
    <h:message for="shortProperty"/>

    <h:outputText value="stringProperty"/>
    <h:inputText id="stringProperty" value="#{formInput02.stringProperty}"/>
    <h:message for="stringProperty"/>

    <h:commandButton id="submit" type="submit" value="Submit"/>
    <h:commandButton id="reset"  type="reset"  value="Reset"/>
    <h:outputText value=""/>

  </h:panelGrid>

</h:form>

</body>
</html>
</f:view>
