<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <BODY>
        <H3> JSF Basic Components Test Page </H3>


       <f:view>
        <h:form id="/basicForm">

            <h:textentry_input id="userName" />

            <h:commandButton id="login" />

	    <!-- <h:commandLink id="/basicForm/login" /> -->

            <h:outputText id="userLabel" />

            <h:selectManyCheckbox id="validUser" />

            <h:selectOneListbox id="appleQuantity" />

            <h:selectOneRadio id="shipType" />

            <h:textentry_secret id="password" />

            <h:textentry_textarea id="address" />


        </h:form>
       </f:view>

    </BODY>
</HTML>
