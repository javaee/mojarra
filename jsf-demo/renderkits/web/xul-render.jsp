<?xml version="1.0"?>

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


<!DOCTYPE window>

<?xml-stylesheet href="chrome://global/skin/" type="text/css"?>
<?xml-stylesheet href='<%= request.getContextPath() + "/xul.css" %>' type="text/css"?>

<%@ page contentType="application/vnd.mozilla.xul+xml"%>
<%@ taglib uri="http://java.sun.com/jsf/xul" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view renderKitId="XUL">
   <f:loadBundle basename="demo.model.Resources" var="phaseBundle"/>
   <x:form id="form">
      <x:outputLabel boxClass="header" pack="center" styleClass="header" value="#{phaseBundle.renderHeader}"/>

      <x:panelGroup styleClass="groupboxClass" captionLabel="#{phaseBundle.renderCaption}" 
          captionClass="captionClass" >
         <x:panelGroup styleClass="groupboxDetailClass">
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription1}" escape="false" />
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription2}" escape="false" />
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription3}" escape="false" />
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription4}" escape="false" />
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription5}" escape="false" />
         </x:panelGroup>
      </x:panelGroup>

      <x:panelGrid columns="2">
         <x:commandButton id="back" value="Back" action="xul-back" type="submit"/>
         <x:commandButton id="main" value="Main" action="xul-main" type="submit"/>
      </x:panelGrid>
      
   </x:form>
</f:view>
