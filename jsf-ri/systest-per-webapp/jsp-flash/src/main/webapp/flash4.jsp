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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>RoR Flash Test Page 4</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  </head>

  <body>
    <h1>RoR Flash Test Page 4</h1>

<f:view>

  <h:form id="form1">

  <h:panelGrid columns="2" border="1">

    Show that buckaroo is still here.

    <h:outputText value="#{flash.buckaroo}" />

    <h:commandButton value="reload" />

    <h:commandButton value="back" action="back" />

   </h:panelGrid>

  </h:form>

</f:view>
  </body>
</html>
