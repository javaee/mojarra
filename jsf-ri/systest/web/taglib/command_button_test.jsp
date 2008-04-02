<%--
   Copyright 2004 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: command_button_test.jsp,v 1.9 2004/02/26 20:33:55 eburns Exp $ --%>
<html>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <title>command_button_test.jsp</title>
</head>
<body>
    
    <f:loadBundle basename="com.sun.faces.systest.resources.Resources" 
        var="messageBundle"/>
    <f:view locale="en_US">
      <h:form id="form01">
        <h:command_button id="button01" type="submit" value="My Label"/>
        <h:command_button id="button02" type="reset" value="#{test1.stringProperty}"/>
        <h:command_button id="button03" type="submit" value="#{messageBundle.button_key}"/>
        <h:command_button id="button04" type="reset" image="duke.gif" value="FAIL"/>
        <h:command_button id="button05" type="submit" image="#{messageBundle.image_key}"/>
      </h:form>
    </f:view>
</body>
</html>

