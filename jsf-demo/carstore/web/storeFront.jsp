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
    <title>Welcome to CarStore</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/stylesheet.css">
</head>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<body bgcolor="white">

<f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>

<f:view>

    <h:form>

        <h:graphicImage url="/images/cardemo.jpg"/>

        <h:panelGrid columns="2"
                     footerClass="form-footer"
                     headerClass="form-header"
                     styleClass="top-table"
                     columnClasses="single-column"
                     summary="#{bundle.chooseCar}"
                     title="#{bundle.chooseCar}">

            <h:panelGrid columns="2"
                         styleClass="storeFrontCar">

                <!-- Jalopy -->
                <h:graphicImage
                      binding="#{carstore.models.Jalopy.components.imageSmall}"/>
                <h:outputText styleClass="subtitlebig"
                              value="#{carstore.models.Jalopy.attributes.title}"/>
                <h:outputText
                      value="#{carstore.models.Jalopy.attributes.description}"/>
                <h:commandButton
                      action="#{carstore.storeFrontJalopyPressed}"
                      value="#{bundle.moreButton}">
                </h:commandButton>

                <!-- Roadster -->
                <h:graphicImage
                      binding="#{carstore.models.Roadster.components.imageSmall}"/>
                <h:outputText styleClass="subtitlebig"
                              value="#{carstore.models.Roadster.attributes.title}"/>
                <h:outputText
                      value="#{carstore.models.Roadster.attributes.description}"/>
                <h:commandButton
                      action="#{carstore.storeFrontRoadsterPressed}"
                      value="#{bundle.moreButton}">
                </h:commandButton>

            </h:panelGrid>

            <h:panelGrid columns="2"
                         styleClass="storeFrontCar">

                <!-- Luxury -->
                <h:graphicImage
                      binding="#{carstore.models.Luxury.components.imageSmall}"/>
                <h:outputText styleClass="subtitlebig"
                              value="#{carstore.models.Luxury.attributes.title}"/>
                <h:outputText
                      value="#{carstore.models.Luxury.attributes.description}"/>
                <h:commandButton
                      action="#{carstore.storeFrontLuxuryPressed}"
                      value="#{bundle.moreButton}">
                </h:commandButton>

                <!-- SUV -->
                <h:graphicImage
                      binding="#{carstore.models.SUV.components.imageSmall}"/>
                <h:outputText styleClass="subtitlebig"
                              value="#{carstore.models.SUV.attributes.title}"/>
                <h:outputText
                      value="#{carstore.models.SUV.attributes.description}"/>
                <h:commandButton action="#{carstore.storeFrontSUVPressed}"
                                 value="#{bundle.moreButton}">
                </h:commandButton>

            </h:panelGrid>

        </h:panelGrid>

    </h:form>

    <jsp:include page="bottomMatter.jsp"/>

</f:view>

</body>

</html>
