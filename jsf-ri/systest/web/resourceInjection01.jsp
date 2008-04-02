<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Test Resource Injection in Managed Beans</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
</head>

<body>
<h1>Test Resource Injection in Managed Beans</h1>

<f:view>

    <h:outputText value="#{resourceInjectionBean.injectedData1}"/>
    <h:outputText value="#{resourceInjectionBean.injectedData2}"/>
    <h:outputText value="#{resourceInjectionBean.injectedData3}"/>
    <h:outputText value="#{resourceInjectionBean.injectedData4}"/>
    <h:outputText value="#{resourceInjectionBean.injectedData5}"/>
    <h:outputText value="#{resourceInjectionBean.injectedData6}"/>

</f:view>

<hr>
</body>
</html>
