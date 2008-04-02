<?xml version='1.0' encoding='UTF-8'?>
<%@ page contentType="text/html" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/ri/sandbox" prefix="risb" %>
<html>
<f:view> 
    <h:outputText id="text" value="This should have a context menu!">
        <risb:contextMenu width="225px">
            <risb:menuItem value="Test!" url="http://www.yahoo.com"/>
            <risb:menuItem>
                <h:outputLink value="https://javaserverfaces.dev.java.net/">JSF RI Homepage</h:outputLink>
            </risb:menuItem>
        </risb:contextMenu>
    </h:outputText>
</f:view>
</html>