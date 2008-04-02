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

        <h:panelGrid id="mainPanel" columns="1" footerClass="subtitle"
                     styleClass="medium" columnClasses="medium">

            <h:graphicImage url="/images/cardemo.jpg"/>
            <h:outputText binding="#{carstore.currentModel.components.title}"/>

            <h:panelGrid columns="2" footerClass="subtitle"
                         headerClass="subtitlebig" styleClass="medium"
                         columnClasses="subtitle,medium">

                <f:facet name="header">
                    <h:outputText value="#{bundle.buyTitle}"/>
                </f:facet>

                <h:outputText value="#{bundle.Engine}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.engine}"/>

                <h:outputText value="#{bundle.Brakes}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.brake}"/>

                <h:outputText value="#{bundle.Suspension}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.suspension}"/>

                <h:outputText value="#{bundle.Speakers}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.speaker}"/>

                <h:outputText value="#{bundle.Audio}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.audio}"/>

                <h:outputText value="#{bundle.Transmission}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.transmission}"/>

                <h:outputText value="#{bundle.sunroofLabel}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.sunroof}"/>

                <h:outputText value="#{bundle.cruiseLabel}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.cruisecontrol}"/>

                <h:outputText value="#{bundle.keylessLabel}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.keylessentry}"/>

                <h:outputText value="#{bundle.securityLabel}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.securitySystem}"/>

                <h:outputText value="#{bundle.skiRackLabel}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.skiRack}"/>

                <h:outputText value="#{bundle.towPkgLabel}"/>

                <h:outputText
                      value="#{carstore.currentModel.attributes.towPackage}"/>

                <h:outputText value="#{bundle.gpsLabel}"/>

                <h:outputText value="#{carstore.currentModel.attributes.gps}"/>

                <f:facet name="footer">
                    <h:panelGroup>
                        <h:outputText value="#{bundle.yourPriceLabel}"/>
                        &nbsp;
                        <h:outputText
                              value="#{carstore.currentModel.currentPrice}"/>
                    </h:panelGroup>
                </f:facet>

            </h:panelGrid>

            <h:panelGroup>
                <h:commandButton value="#{bundle.buy}" action="customerInfo"
                                 title="#{bundle.buy}"/>
                <h:commandButton value="#{bundle.back}" action="carDetail"
                                 title="#{bundle.back}"/>
            </h:panelGroup>

        </h:panelGrid>
    </h:form>
    <jsp:include page="bottomMatter.jsp"/>
</f:view>

</body>
</html>

