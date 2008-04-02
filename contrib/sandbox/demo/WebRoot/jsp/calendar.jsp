<?xml version='1.0' encoding='UTF-8'?>
<%@ page contentType="text/html" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/ri/sandbox" prefix="risb" %>
<html>
<f:view> 
    <h:form>
        <risb:calendar value="#{testBean.date}"></risb:calendar>
        <h:commandButton value="Submit"></h:commandButton>
    </h:form>
    <h:outputText value="#{testBean.date}" rendered="#{!empty testBean.date}"/>
</f:view>
</html>