<?xml version='1.0' encoding='UTF-8'?>
<%@ page contentType="text/html" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/ri/sandbox" prefix="risb" %>
<html>
<f:view> 
            <h1>You uploaded some files!</h1>
            <h:dataTable value="#{testBean.fileNames}" var="fileName">
                <h:column><h:outputText value="#{fileName}"/></h:column>
            </h:dataTable>
</f:view>
</html>