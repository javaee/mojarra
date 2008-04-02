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
        Value is <h:outputText value="#{test3.setPropertyTarget}"/>. <p/>
        <h:commandButton id="literalButton" value="submit with literal value">
            <f:setPropertyActionListener target="#{test3.setPropertyTarget}"
                                         value="literal value"/>
        </h:commandButton>
        <p/>
        <h:commandButton id="expressionButton1"
                         value="submit with expression value">
            <f:setPropertyActionListener target="#{test3.setPropertyTarget}"
                                         value="#{test3.stringProperty}"/>
        </h:commandButton>
        <p/>
        <h:commandButton id="expressionButton2" value="increment counter">
            <f:setPropertyActionListener target="#{test3.setPropertyTarget}"
                                         value="#{test3.counter}"/>
        </h:commandButton>
    </h:form>
    </body>
    </html>
</f:view>
