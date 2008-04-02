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

<%@ page contentType="text/html" %>
<%@ page import="javax.faces.application.FacesMessage"%>
<%@ page import="javax.faces.context.FacesContext"%>

<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
                                                                                    
<%
  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/messages02.jsp FAILED - No FacesContext returned");
    return;
  }
  FacesMessage imsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
      "Information Summary", "Informational Detail");
  FacesMessage wmsg = new FacesMessage(FacesMessage.SEVERITY_WARN,
      "Warning Summary", "Warning Detail");
  FacesMessage emsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
      "Error Summary", "Error Detail");
  FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
      "Fatal Summary", "Fatal Detail");
  facesContext.addMessage("1", imsg);
  facesContext.addMessage("1", wmsg);
  facesContext.addMessage("1", emsg);
  facesContext.addMessage("1", fmsg);
%>
                                                                                    
<html>
<STYLE TYPE="text/css" MEDIA=screen>
<!--
.errors {
  background-color: #7171A5;
  border: 5px outset #71A5A5;
  border-collapse: collapse;
  font-family: sans-serif;
  font-size: 14pt;
  padding: 10px;
  left: 48px;
  top: 300px;
  position: absolute;
}
-->
</STYLE>

<f:view>
      <h:panelGrid columns="1"> 
         <h:messages layout="list" 
            style="left: 48px; top: 100px; position: absolute"
            dir="LTR"
            infoStyle="color: yellow"
            errorStyle="color: red"
            fatalStyle="color: blue"
            showSummary="true" showDetail="true" tooltip="true"/>

         <h:messages layout="table" 
            style="left: 48px; top: 200px; position: absolute"
            lang="en"
            infoStyle="color: yellow"
            errorStyle="color: red"
            fatalStyle="color: blue"
            showSummary="true" showDetail="true" tooltip="true"/>

         <h:messages  
            styleClass="errors"
            showSummary="true" showDetail="true" />

         <h:messages layout="table" 
            style="left: 48px; top: 500px; position: absolute"
            infoStyle="color: yellow"
            errorStyle="color: red"
            fatalStyle="color: blue"
            showDetail="true" />
       </h:panelGrid>
</f:view>
</html>
