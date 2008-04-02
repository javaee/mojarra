<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <title>Hello</title> </HEAD>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <body bgcolor="white">
    <h2>Hi. My name is Duke.  I'm thinking of a number from 0 to 10.
    Can you guess it?</h2>
    <f:view>
    <h:form id="helloForm" >
  	<h:inputText id="userNo"  value="NUMBER" /> <BR>

	 <h:commandButton id="submit" value="Submit" />
    </h:form>
    </f:view>
</HTML>  
