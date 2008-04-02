<%--
   Copyright 2003 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: command_button_test.jsp,v 1.3 2003/09/09 01:01:50 horwat Exp $ --%>
<html>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <title>command_button_test.jsp</title>
</head>
<body>
    <fmt:setLocale value="en_US"/>
    <fmt:setBundle basename="com.sun.faces.systest.resources.Resources" scope="request" var="messageBundle"/>
    <f:view>
        <h:command_button id="button01" type="submit" value="My Label"/>
        <h:command_button id="button02" type="reset" valueRef="test1.stringProperty"/>
        <h:command_button id="button03" type="submit" key="button_key" bundle="messageBundle"/>
        <h:command_button id="button04" type="reset" image="duke.gif" value="FAIL"/>
        <h:command_button id="button05" type="submit" imageKey="image_key" bundle="messageBundle"/>
    </f:view>
</body>
</html>

