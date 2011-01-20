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

<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>

<f:view>
<html>
<head>
  <title>
    Second Selection Page
  </title>
  <link rel="stylesheet" type="text/css"
    href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<body>

  <h:graphicImage url="/images/header.jpg" />

  <h2>Second Selection Page</h2>

  <p>This page displays the same data as the previous page except it
     does not include the user's first combat choice so that character
     will not be picked twice.</p>

  <p>You may now choose your second character that will be waging a
  magical combat with <h:outputText value="#{modelBean.firstSelection}" />.</p>

  <h:form prependId="false">

    <h:panelGrid columns="1">

      <h:selectOneRadio
        layout="pageDirection" 
        required="true"
        value="#{modelBean.secondSelection}">
        <f:selectItems
          value="#{modelBean.charactersToSelect}" />
      </h:selectOneRadio>

      <h:messages />

    </h:panelGrid>

    <jsp:include page="wizard-buttons.jsp"/>


  </h:form>

</body>
</html>
</f:view>
