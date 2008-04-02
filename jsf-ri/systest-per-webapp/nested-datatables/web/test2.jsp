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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Nested Tables 2</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
</head>

<body>
<h1>Nested Tables 2</h1>

<f:view>

    <h:form id="form">

        <h:dataTable id="outerData" value="#{outer}" var="outerVar">

            <h:column id="outerColumn0">

                <h:commandButton id="outerButton" action="#{bean.outerAction}"
                                 value="outerButton"/>

            </h:column>

            <h:column id="outerColumn1">

                <h:dataTable id="innerData" value="#{inner.listDataModel}"
                             var="innerVar">


                    <h:column id="innerColumn">
                        <h:commandButton id="innerButton"
                                         action="#{bean.innerAction}"
                                         value="innerButton"/>
                    </h:column>

                </h:dataTable>

            </h:column>

        </h:dataTable>

        <h:commandButton style="color: red" value="reload"/>

        <p/>

        <p>Outer Action called
            <h:outputText value="#{bean.outerActionCallCount}"/> times</p>

        <p>Inner Action called
            <h:outputText value="#{bean.innerActionCallCount}"/> times</p>

        <h:messages/>


    </h:form>

</f:view>

</body>
</html>
