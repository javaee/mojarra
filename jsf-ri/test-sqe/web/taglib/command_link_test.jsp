<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<%--
   Copyright 2004 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: command_link_test.jsp,v 1.2 2005/08/22 22:12:09 ofung Exp $ --%>

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

