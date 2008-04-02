<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>ValueChangeListeners and Validators</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
</head>

<body>
<h1>ValueChangeListeners and Validators</h1>

<f:view>

    <h:form>

        <p>Enter numbers from 1 to 10.</p>

        <h:panelGrid columns="2">

            <h:inputText id="textA"
                         valueChangeListener="#{valueChangeListenerBean.textAChanged}">
                <f:validateLongRange minimum="1" maximum="10"/>
            </h:inputText>

            <h:inputText id="textB"
                         valueChangeListener="#{valueChangeListenerBean.textBChanged}">
                <f:validateLongRange minimum="1" maximum="10"/>
            </h:inputText>

            <h:outputText value="#{valueChangeListenerBean.textAResult}"/>

            <h:outputText value="#{valueChangeListenerBean.textBResult}"/>

            <h:commandButton value="submit"/> <p>

                <h:messages dir="LTR" lang="en"/>

            <hr/>

            <h:message for="textB" dir="RTL" lang="de"/>


        </h:panelGrid>

    </h:form>

</f:view>

<hr>
</body>
</html>
