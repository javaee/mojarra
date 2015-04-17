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
    Combat Results Page
  </title>
  <link rel="stylesheet" type="text/css"
    href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<body>

  <h:graphicImage url="/images/header.jpg" />

  <h2>Combat Results Page</h2>

  <p>The results page determines the winner of the two selections
     based on a magically simple algorithm</p>

  <p>If 
     <i><h:outputText value="#{modelBean.firstSelection}" /></i>
     and 
     <i><h:outputText value="#{modelBean.secondSelection}" /></i>
     were to wage a magical war, the winner would be: 
     <b><h:outputText value="#{modelBean.combatWinner}" /></b>
     !
  </p>

  <h:form>
    <h:commandButton value="Start Over" action="startOver" />
    <jsp:include page="wizard-buttons.jsp"/>
  </h:form>


</body>
</html>
</f:view>
