<%--
   Copyright 2003 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: command_hyperlink_test.jsp,v 1.3 2003/09/09 01:01:50 horwat Exp $ --%>

<html>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <title>command_hyperlink_test.jsp</title>
</head>
<body>
    <fmt:setBundle basename="com.sun.faces.systest.resources.Resources" scope="request" var="messageResources"/>
    <f:view>
        <h:command_hyperlink id="hyperlink01" value="My Link"/>
        <h:command_hyperlink id="hyperlink02" valueRef="test1.stringProperty"/>
        <h:command_hyperlink id="hyperlink03" key="hyperlink_key" bundle="messageResources"/>
        <h:command_hyperlink id="hyperlink04" image="duke.gif" value="FAIL"/>
        <h:command_hyperlink id="hyperlink05" imageKey="image_key" bundle="messageResources"/>
        <h:command_hyperlink id="hyperlink06" value="Paramter Link">
            <f:parameter name="param1" value="value1"/>
        </h:command_hyperlink>
    </f:view>
</body>
</html>

