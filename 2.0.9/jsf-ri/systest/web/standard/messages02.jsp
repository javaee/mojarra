<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
--%>

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
