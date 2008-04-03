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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">


<html>

<head>
    <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1">
    <title>CarStore</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/stylesheet.css">
</head>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

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
