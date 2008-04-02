<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>SelectMany with primitive int array value</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
</head>

<body>
<h1>SelectMany with primitive int array value</h1>

<f:view>

    <h:form>

        <h:panelGrid columns="1">
            <h:selectManyListbox id="intListbox" value="#{test3.intsProperty}">
                <f:selectItem itemLabel="first" itemValue="1"/>
                <f:selectItem itemLabel="second" itemValue="2"/>
                <f:selectItem itemLabel="three" itemValue="3"/>
            </h:selectManyListbox>

            <h:messages/>

        </h:panelGrid>

        <h:commandButton value="submit"/>

    </h:form>

</f:view>

<hr>
</body>
</html>
