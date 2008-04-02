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

<%@ page contentType="text/html"%>

<HTML>
<HEAD>
<link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/html.css" %>'>
</HEAD>
<body bgcolor="#c1cdc1">

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

       <f:view renderKitId="HTML_BASIC" >  
          <f:loadBundle basename="demo.model.Resources" var="phaseBundle"/>
          <h:form id="form">
              <h:outputText styleClass="headerClass" value="Multiple RenderKits Demo" />
              <h:panelGrid columns="1">
                 <h:outputText styleClass="sectionClass" value="Background" />
              </h:panelGrid>
              <h:panelGrid columns="1">
                 <h:outputText value="#{phaseBundle.background1}" escape="false" />
                 <h:graphicImage url="/lifecycle-pages.gif" />
                 <h:outputText value="#{phaseBundle.background2}" escape="false" />
              </h:panelGrid>
              <h:panelGrid columns="1">
                 <h:outputText styleClass="sectionClass" value="Design Detail" />
                 <h:outputText value="#{phaseBundle.detail1}" escape="false" />
                 <h:graphicImage url="/life-demo.gif" />
                 <h:commandButton type="submit" value="Next" action="success" />
              </h:panelGrid>
          </h:form>
       </f:view>

</body>
</HTML>
