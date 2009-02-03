<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/mojarra/extensions" prefix="mjx" %>
<f:view>
<html>

<head>
    <title>Welcome to CarStore</title>
    <mjx:outputStyle name="styles/stylesheet.css"/>
</head>


<body bgcolor="white">

    <h:form>

        <h:graphicImage url="#{resource['images/cardemo.jpg']}"/>

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
