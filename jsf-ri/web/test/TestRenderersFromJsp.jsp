<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="h" %>

    <%@ taglib uri="http://java.sun.com/jsf/core/" prefix="f" %>

    <BODY>
        <H3> JSF Basic Components Test Page </H3>


       <f:usefaces>
        <h:form id="/basicForm">

            <h:textentry_input id="userName" />

            <h:command_button id="login" />

	    <!-- <h:command_hyperlink id="/basicForm/login" /> -->

            <h:output_text id="userLabel" />

            <h:selectboolean_checkbox id="validUser" />

            <h:selectone_listbox id="appleQuantity" />

            <h:selectone_radio id="shipType" />

            <h:textentry_secret id="password" />

            <h:textentry_textarea id="address" />


        </h:form>
       </f:usefaces>

    </BODY>
</HTML>
