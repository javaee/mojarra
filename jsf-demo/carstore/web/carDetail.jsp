<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">

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

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1">
    <title>CarStore</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/stylesheet.css">
</head>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>

<body bgcolor="white">

<f:view>

    <h:form>


        <!-- non-option details -->

        <h:panelGrid columns="1"
                     summary="#{bundle.carDetails}"
                     title="#{bundle.carDetails}">

            <h:graphicImage url="/images/cardemo.jpg"/>

            <h:graphicImage
                  binding="#{carstore.currentModel.components.image}"/>

            <h:outputText styleClass="subtitlebig"
                          binding="#{carstore.currentModel.components.title}"/>

            <h:outputText
                  binding="#{carstore.currentModel.components.description}"/>

            <h:panelGrid columns="2">

                <h:outputText styleClass="subtitle"
                              value="#{bundle.basePriceLabel}"/>

                <h:outputText
                      binding="#{carstore.currentModel.components.basePrice}"/>

                <h:outputText styleClass="subtitle"
                              value="#{bundle.yourPriceLabel}"/>

                <h:outputText value="#{carstore.currentModel.currentPrice}"/>

            </h:panelGrid>

            <h:commandButton action="#{carstore.buyCurrentCar}"
                             value="#{bundle.buy}"/>

        </h:panelGrid>

        <jsp:include page="optionsPanel.jsp"/>

        <h:commandButton value="#{bundle.recalculate}"
                         action="#{carstore.currentModel.updatePricing}"/>

        <h:commandButton action="#{carstore.buyCurrentCar}"
                         value="#{bundle.buy}"/>

    </h:form>

    <jsp:include page="bottomMatter.jsp"/>

</f:view>
</body>

</html>
