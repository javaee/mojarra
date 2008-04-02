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
        <title>immediate01</title>
    </head>

    <body>

    <h:form>

        <p>PENDING: write an HTMLUNIT testcase for this.</p>

        Output: <h:outputText value="#{immediateBean.stringProperty}"/> Input:
        <h:inputText value="#{immediateBean.stringProperty}"/> <br/>
        <h:commandLink action="null">Submit</h:commandLink> <p/>
        <h:commandLink action="null" immediate="true">Cancel</h:commandLink>
        <p/>

        <h:commandButton value="Submit" action="null"/><p/>
        <h:commandButton value="Cancel" action="null" immediate="true"/><p/>

        <hr/>

        <h:commandButton value="Clear Bean Property">
            <f:setPropertyActionListener
                  target="#{immediateBean.stringProperty}" value=""/>
        </h:commandButton>

    </h:form>

    </body>
    </html>
</f:view>
