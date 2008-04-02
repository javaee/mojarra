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
<title>Test case for bug5030555</title>
</head>
<body>
<h:form id="form">
<h:inputText id="toChange" binding="#{methodRef.inputField}"/>
<h:commandButton id="changeValue" value="changeValue"/>
</h:form>
</body>
</html>
</f:view>
