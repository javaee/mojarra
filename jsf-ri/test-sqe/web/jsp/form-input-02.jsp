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
