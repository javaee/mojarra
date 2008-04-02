<?xml version='1.0' encoding='UTF-8'?>
<%@ page contentType="text/html" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/ri/sandbox" prefix="risb" %>
<html>
<f:view> 
    <h:outputText id="text" value="This should have a context menu!">
        <risb:contextMenu value="#{testBean.menu}" width="225px"/>
    </h:outputText>
</f:view>
</html>