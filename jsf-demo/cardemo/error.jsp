<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ page extends="com.sun.faces.Page" %>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core/" prefix="f" %>
    <H3> JSF Basic Components Test Page </H3>
    <hr>
    <f:usefaces>
    <h:form id="errorForm" formName="errorForm" >
	<faces:output_text id="hello_label" value="An Error Happened!"/>
             <P></P>
    </h:Form>
    </f:usefaces>
</HTML>
