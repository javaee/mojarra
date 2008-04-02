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
<html>
<head>
   <title>CarStore</title>
   <link rel="stylesheet" type="text/css"
        href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/blueprints/ee5/components/ui" prefix="d" %>

     <f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>

<f:view>
    <h:form>

        <h:panelGrid columns="1" 
                      footerClass="form-footer"
                      headerClass="form-header"
                      styleClass="main-background"
                      columnClasses="single-column"
                      summary="#{bundle.chooseLocale}" 
                      title="#{bundle.chooseLocale}" >

            <h:graphicImage  url="/images/cardemo.jpg" /> 

            <h:outputText styleClass="maintitle"
                           value="#{bundle.chooseLocale}" />

            <h:graphicImage id="mapImage" url="/images/world.jpg" 
                             alt="#{bundle.chooseLocale}"
                             usemap="#worldMap" />

            <d:map id="worldMap" current="NAmericas" immediate="true"
                   action="storeFront"
                   actionListener="#{carstore.chooseLocaleFromMap}">
                    <d:area id="NAmerica" value="#{NA}" 
                            onmouseover="/images/world_namer.jpg" 
                            onmouseout="/images/world.jpg" 
                            targetImage="mapImage" />
                    <d:area id="SAmerica" value="#{SA}" 
                            onmouseover="/images/world_samer.jpg"
                            onmouseout="/images/world.jpg" 
                            targetImage="mapImage" />
                    <d:area id="Germany" value="#{gerA}" 
                            onmouseover="/images/world_germany.jpg" 
                            onmouseout="/images/world.jpg" 
                            targetImage="mapImage" />
                    <d:area id="France" value="#{fraA}" 
                            onmouseover="/images/world_france.jpg" 
                            onmouseout="/images/world.jpg" 
                            targetImage="mapImage" />
            </d:map>

        </h:panelGrid>

    </h:form>

    <h:form>

        <!-- For non graphical browsers  -->

        <p>Example of <code>commandLink</code></p>

        <h:panelGrid id="links" columns="4" 
                      summary="#{bundle.chooseLocale}" 
                      title="#{bundle.chooseLocale}" >

          <h:commandLink id="NAmerica" action="storeFront"
                          actionListener="#{carstore.chooseLocaleFromLink}">
  
            <h:outputText value="#{bundle.english}" />

          </h:commandLink>

          <h:commandLink id="Germany" action="storeFront"
                          actionListener="#{carstore.chooseLocaleFromLink}">

            <h:outputText value="#{bundle.german}" />

          </h:commandLink>

          <h:commandLink id="France" action="storeFront"
                          actionListener="#{carstore.chooseLocaleFromLink}">

            <h:outputText value="#{bundle.french}" />

          </h:commandLink>

          <h:commandLink id="SAmerica" action="storeFront"
                          actionListener="#{carstore.chooseLocaleFromLink}">

            <h:outputText value="#{bundle.spanish}" />

          </h:commandLink>

        </h:panelGrid>

    </h:form>

    <h:form>

        <p>Example of <code>commandButton</code></p>

        <h:panelGrid id="buttons" columns="4" 
                      summary="#{bundle.chooseLocale}" 
                      title="#{bundle.chooseLocale}" >

          <h:commandButton id="NAmerica" action="storeFront" value="#{bundle.english}" 
                          actionListener="#{carstore.chooseLocaleFromLink}">
  
          </h:commandButton>

          <h:commandButton id="Germany" action="storeFront" value="#{bundle.german}" 
                          actionListener="#{carstore.chooseLocaleFromLink}">

          </h:commandButton>

          <h:commandButton id="France" action="storeFront" value="#{bundle.french}" 
                          actionListener="#{carstore.chooseLocaleFromLink}">

          </h:commandButton>

          <h:commandButton id="SAmerica" action="storeFront" value="#{bundle.spanish}" 
                          actionListener="#{carstore.chooseLocaleFromLink}">

          </h:commandButton>

        </h:panelGrid>

    
    </h:form>

 <jsp:include page="bottomMatter.jsp"/>

</f:view>

</html>
