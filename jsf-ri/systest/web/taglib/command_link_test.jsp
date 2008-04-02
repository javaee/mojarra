<%--
   Copyright 2003 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: command_link_test.jsp,v 1.2 2003/10/30 16:14:34 eburns Exp $ --%>

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
        <h:command_link id="hyperlink01"><f:verbatim>My Link</f:verbatim></h:command_link>
        <h:command_link id="hyperlink02"><h:output_text valueRef="test1.stringProperty"/></h:command_link>
        <h:command_link id="hyperlink03"><h:output_text key="hyperlink_key" bundle="messageResources"/></h:command_link>
        <h:command_link id="hyperlink04"><f:verbatim><img src="duke.gif" /></f:verbatim></h:command_link>
        <h:command_link id="hyperlink05"><h:graphic_image key="image_key" bundle="messageResources"/></h:command_link>
        <h:command_link id="hyperlink06"><f:verbatim>Paramter Link</f:verbatim>
            <f:parameter name="param1" value="value1"/>
        </h:command_link>
    </f:view>
</body>
</html>

