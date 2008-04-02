<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ page extends="com.sun.faces.Page" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <H3> JSF Basic Components Test Page </H3>
    <hr>
    <f:use_faces>
    <h:form  formName="errorForm" >
	<faces:output_text  value="An Error Happened!"/>
             <P></P>
    </h:Form>
    </f:use_faces>
</HTML>
