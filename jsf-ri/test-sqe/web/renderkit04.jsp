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

<HTML>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

       <jsp:useBean id="TestBean" class="com.sun.faces.systest.model.TestBean" scope="session" />
       <f:view renderKitId="HTML_BASIC" >  
          <h:form id="form">
              <table>
              <tr>
                 <td><h:outputText value="RenderKit:" /></td> 
                 <td><h:outputText value="#{TestBean.renderKitInfo}" /></td>
              </tr>
              <tr>
                 <td><h:outputText value="ResponseWriter:" /></td> 
                 <td><h:outputText value="#{TestBean.responseWriterInfo}" /></td> 
              </tr>
              </table>
              <h:commandButton id="submit" action="success" value="submit"/>
          </h:form>
       </f:view>

</HTML>
