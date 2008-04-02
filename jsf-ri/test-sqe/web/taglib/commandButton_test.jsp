<%--
   Copyright 2004 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: commandButton_test.jsp,v 1.1 2005/07/25 18:35:36 rajprem Exp $ --%>
<html>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <title>commandButton_test.jsp</title>
</head>
<body>
    
    <f:loadBundle basename="com.sun.faces.systest.resources.Resources" 
        var="messageBundle"/>
    <f:view locale="en_US">
      <h:form id="form01">
        <h:commandButton id="button01" type="submit" value="My Label"/>
        <h:commandButton id="button02" type="reset" value="#{test1.stringProperty}"/>
        <h:commandButton id="button03" type="submit" value="#{messageBundle.button_key}"/>
        <h:commandButton id="button04" type="reset" image="duke.gif" value="FAIL"/>
        <h:commandButton id="button05" type="submit" image="#{messageBundle.image_key}"/>
      </h:form>
    </f:view>
</body>
</html>

