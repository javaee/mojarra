<?xml version='1.0' encoding='UTF-8'?>
<%@ page contentType="text/html" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/ri/sandbox" prefix="risb" %>
<html>
<f:view> 
     <risb:tree id="foo" model="#{testBean.tree}" >
        <risb:treeNode>
            <h:outputText value="Test 1"/>
            <risb:treeNode>
                <h:outputText value="Test 1-1"/>
            </risb:treeNode>
        </risb:treeNode>
        <risb:treeNode>
            <h:outputText id="test2" value="Test 2"/>
            <risb:treeNode>
                <h:outputLink value="http://blogs.steeplesoft.com">
                    <h:graphicImage id="image" url="/download.jpg" />
                </h:outputLink>
            </risb:treeNode>
        </risb:treeNode>
     </risb:tree>
</f:view>
</html>