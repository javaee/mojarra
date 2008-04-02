<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
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

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>

<head>
    <title>Welcome to CarStore</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/stylesheet.css">
</head>

<body bgcolor="white">
<f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>
<f:view>
    <h:form>
        <h:graphicImage url="/images/cardemo.jpg"/>

        <h:panelGrid id="thanksPanel" columns="1" footerClass="subtitle"
                     headerClass="subtitlebig" styleClass="medium"
                     columnClasses="subtitle,medium">
            <f:facet name="header">
                <h:outputFormat title="thanks" value="#{bundle.thanksLabel}">
                    <f:param value="#{sessionScope.firstName}"/>
                </h:outputFormat>
            </f:facet>
        </h:panelGrid>

    </h:form>
    <jsp:include page="bottomMatter.jsp"/>
</f:view>
</body>
</html>
