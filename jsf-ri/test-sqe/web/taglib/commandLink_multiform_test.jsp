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
                                                                                
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
                                                                                
                                                                                
<html>
  <body>
     <f:view>
     <h:form id="form01">
       <h:commandLink id ="Link1" action="redirect">
          <h:outputText value="Link1"/>
       </h:commandLink>
       <h:commandLink id ="Link2" action="redirect">
          <h:outputText value="Link2"/>
       </h:commandLink>
    </h:form> 
    <h:form id="form02">
       <h:commandLink id ="Link3" action="redirect">
          <h:outputText value="Link3"/>
       </h:commandLink>
       <h:commandLink id ="Link4" action="forward">
          <h:outputText value="Link4"/>
       </h:commandLink>
   </h:form>
   </f:view>
  </body>
</html>
