<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Programmatic Component Addition</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
</head>

<body>
<h1>Programmatic Component Addition</h1>

<f:view>

    <h:form id="form">

        <h:commandButton value="submit" id="button"
                         actionListener="#{test3.addComponentToTree}"/>

        <h:panelGroup id="addHere"/>

    </h:form>

</f:view>

<hr>
</body>
</html>
