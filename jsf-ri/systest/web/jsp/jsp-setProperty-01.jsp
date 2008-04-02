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
<title>f:setPropertyActionListener</title>
</head>
<body>
<h:form>

Integer Property is: <h:outputText value="#{test3.intProperty}" />. </p>
String Property is: <h:outputText value="#{test3.stringProperty}" />. </p>

<h:commandButton id="expressionButton1" value="Convert from String To Integer">
  <f:setPropertyActionListener target="#{test3.intProperty}" value="100" />
</h:commandButton>
<h:commandButton id="expressionButton2" value="Convert from Integer to String">
  <f:setPropertyActionListener target="#{test3.stringProperty}" value="#{test3.intProperty}" />
</h:commandButton>   
<h:commandButton id="expressionButton3" value="String to String">
  <f:setPropertyActionListener target="#{test3.stringProperty}" value="String" />
</h:commandButton>
<h:commandButton id="expressionButton4" value="FacesContext to String">
  <f:setPropertyActionListener target="#{test3.stringProperty}" value="#{facesContext}" />
</h:commandButton>

</h:form>
</body>
</html>
</f:view>
