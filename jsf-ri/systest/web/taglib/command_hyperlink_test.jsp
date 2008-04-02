<%--
   Copyright 2003 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: command_hyperlink_test.jsp,v 1.1 2003/07/29 16:31:11 rlubke Exp $ --%>

<html>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <title>command_hyperlink_test.jsp</title>
</head>
<body>
    <fmt:setBundle basename="com.sun.faces.systest.resources.Resources" scope="request" var="messageResources"/>
    <f:use_faces>
        <h:command_hyperlink id="hyperlink01" label="My Link"/>
        <h:command_hyperlink id="hyperlink02" valueRef="test1.stringProperty"/>
        <h:command_hyperlink id="hyperlink03" key="hyperlink_key" bundle="messageResources"/>
        <h:command_hyperlink id="hyperlink04" image="duke.gif" label="FAIL"/>
        <h:command_hyperlink id="hyperlink05" imageKey="image_key" bundle="messageResources"/>
        <h:command_hyperlink id="hyperlink06" label="Paramter Link">
            <f:parameter name="param1" value="value1"/>
        </h:command_hyperlink>
    </f:use_faces>
</body>
</html>

