<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>SelectMany with invalid Value</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
</head>

<body>
<h1>SelectMany with invalid Value</h1>

<f:view>

    <h:form>

        <h:panelGrid columns="1">

            <h:selectManyListbox
                  required="true"
                  valueChangeListener="#{test3.valueChanged}"
                  value="#{test3.multiSelection}">
                <f:selectItems
                      value="#{test3.nondeterministicSelectList}"/>
            </h:selectManyListbox>

            <h:messages/>

            <h:outputText value="#{test3.valueChangeMessage}"/>

        </h:panelGrid>

        <h:commandButton value="submit"/>

    </h:form>

</f:view>

<hr>
</body>
</html>
