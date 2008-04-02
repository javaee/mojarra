<%--
   Copyright 2003 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: command_link_test.jsp,v 1.1 2003/10/23 05:17:50 eburns Exp $ --%>

<html>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <title>command_link_test.jsp</title>
</head>
<body>
    <fmt:setBundle basename="com.sun.faces.systest.resources.Resources" scope="request" var="messageResources"/>
    <f:view>
        <h:command_link id="hyperlink01" value="My Link"/>
        <h:command_link id="hyperlink02" valueRef="test1.stringProperty"/>
        <h:command_link id="hyperlink03" key="hyperlink_key" bundle="messageResources"/>
        <h:command_link id="hyperlink04" image="duke.gif" value="FAIL"/>
        <h:command_link id="hyperlink05" imageKey="image_key" bundle="messageResources"/>
        <h:command_link id="hyperlink06" value="Paramter Link">
            <f:parameter name="param1" value="value1"/>
        </h:command_link>
    </f:view>
</body>
</html>

