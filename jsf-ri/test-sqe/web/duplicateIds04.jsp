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

<%--
  - @@copyright@@
  --%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<f:view>
  <html>
    <head>
      <title>Search Criteria</title>
    </head>
    <body>
      <h2>Search for items that:</h2>
      <h:form>
        <h:panelGrid columns="3" border="1" cellpadding="5" cellspacing="0"
                     binding="#{duplicateIds04.panelGrid}" />
        <br>
        <h:commandButton value="redisplay" />
      </h:form>
    </body>
  </html>
</f:view>
