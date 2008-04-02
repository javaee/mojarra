\<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

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

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head><title>Test SelectItem with escape true and false</title></head>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<body bgcolor="white">
<f:view>

    <h:form prependId="false">

        <h:panelGrid columns="2">

            SelectOneMenu with no escape value

            <h:selectOneMenu id="menu1">
                <f:selectItem itemValue="10" itemLabel="menu1_Guy <Lafleur>"/>
                <f:selectItem itemValue="99" itemLabel="menu1_Wayne <Gretzky>"/>
                <f:selectItem itemValue="4" itemLabel="menu1_Bobby +Orr+"/>
                <f:selectItem itemValue="2" itemLabel="menu1_Brad &{Park}"/>
                <f:selectItem itemValue="9" itemLabel="menu1_Gordie &Howe&"/>
            </h:selectOneMenu>
            SelectOneMenu with true escape value

            <h:selectOneMenu id="menu2">
                <f:selectItem escape="true" itemValue="10"
                              itemLabel="menu2_Guy <Lafleur>"/>
                <f:selectItem escape="true" itemValue="99"
                              itemLabel="menu2_Wayne <Gretzky>"/>
                <f:selectItem escape="true" itemValue="4"
                              itemLabel="menu2_Bobby +Orr+"/>
                <f:selectItem escape="true" itemValue="2"
                              itemLabel="menu2_Brad &{Park}"/>
                <f:selectItem escape="true" itemValue="9"
                              itemLabel="menu2_Gordie &Howe&"/>
            </h:selectOneMenu>

            SelectOneMenu with false escape value

            <h:selectOneMenu id="menu3">
                <f:selectItem escape="false" itemValue="10"
                              itemLabel="menu3_Guy <Lafleur>"/>
                <f:selectItem escape="false" itemValue="99"
                              itemLabel="menu3_Wayne <Gretzky>"/>
                <f:selectItem escape="false" itemValue="4"
                              itemLabel="menu3_Bobby +Orr+"/>
                <f:selectItem escape="false" itemValue="2"
                              itemLabel="menu3_Brad &{Park}"/>
                <f:selectItem escape="false" itemValue="9"
                              itemLabel="menu3_Gordie &Howe&"/>
            </h:selectOneMenu>


        </h:panelGrid>


    </h:form>

</f:view>
</body>
</html>  
