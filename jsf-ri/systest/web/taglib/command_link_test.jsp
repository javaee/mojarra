<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: command_link_test.jsp,v 1.10 2004/02/05 05:05:29 horwat Exp $ --%>

<html>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <title>command_link_test.jsp</title>
</head>
<body>
    <f:loadBundle basename="com.sun.faces.systest.resources.Resources" 
         var="messageResources"/>
    <f:view>
      <h:form id="form01">
        <h:command_link id="hyperlink01"><f:verbatim>My Link</f:verbatim></h:command_link>
        <h:command_link id="hyperlink02"><h:output_text value="#{test1.stringProperty}"/></h:command_link>
        <h:command_link id="hyperlink03"><h:output_text value="#{messageResources.hyperlink_key}"/></h:command_link>
        <h:command_link id="hyperlink04"><f:verbatim escape="false"><img src="duke.gif" /></f:verbatim></h:command_link>
        <h:command_link id="hyperlink05"><h:graphic_image value="#{messageResources.image_key}"/></h:command_link>
        <h:command_link id="hyperlink06"><f:verbatim>Paramter Link</f:verbatim>
            <f:param name="param1" value="value1"/>
        </h:command_link>
      </h:form>
    </f:view>
</body>
</html>

