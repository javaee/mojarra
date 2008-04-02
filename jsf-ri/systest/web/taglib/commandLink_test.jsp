<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%--
   Copyright 2004 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: commandLink_test.jsp,v 1.6 2004/05/12 18:31:18 ofung Exp $ --%>

<html>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <title>commandLink_test.jsp</title>
</head>
<body>
    <f:loadBundle basename="com.sun.faces.systest.resources.Resources" 
         var="messageResources"/>
    <f:view>
      <h:form id="form01">
        <h:commandLink id="hyperlink01"><f:verbatim>My Link</f:verbatim></h:commandLink>
        <h:commandLink id="hyperlink02"><h:outputText value="#{test1.stringProperty}"/></h:commandLink>
        <h:commandLink id="hyperlink03"><h:outputText value="#{messageResources.hyperlink_key}"/></h:commandLink>
        <h:commandLink id="hyperlink04"><f:verbatim escape="false"><img src="duke.gif" /></f:verbatim></h:commandLink>
        <h:commandLink id="hyperlink05"><h:graphicImage value="#{messageResources.image_key}"/></h:commandLink>
        <h:commandLink id="hyperlink06"><f:verbatim>Paramter Link</f:verbatim>
            <f:param name="param1" value="value1"/>
        </h:commandLink>
      </h:form>
    </f:view>
</body>
</html>

