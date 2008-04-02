<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Test view tag with Locale</title>
</head>

<body>
<h1>Test view tag with Locale</h1>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view locale="fr">
    <h:form id="form">

        <hr>

        <p>Press a button, see some text.</p>
        <h:inputText id="field" required="true"/>

        <h:commandButton id="button" value="submit"/>

        <h:message for="field"/>
    </h:form>
</f:view>


<hr>
<address><a href="mailto:Ed Burns <ed.burns@sun.com>"></a></address>
</body>
</html>
