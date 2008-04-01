<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
    <BODY>
        <H3> JSF Basic Components Test Page </H3>

        <faces:form id="/basicForm">

            <faces:textentry_input id="userName" />

            <faces:command_button id="login" />

	    <!-- <faces:command_hyperlink id="/basicForm/login" /> -->

            <faces:output_text id="userLabel" />

            <faces:selectboolean_checkbox id="validUser" />

            <faces:textentry_secret id="password" />

            <faces:textentry_textarea id="address" />

        </faces:form>

    </BODY>
</HTML>
