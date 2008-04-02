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
<body onLoad="initValue('form')"> 
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

       <jsp:useBean id="TestBean" class="com.sun.faces.systest.model.TestBean" scope="session" />
       <f:view >  
       <script type="text/javascript" language="Javascript">
       <!--
       function setValue(curFormName) {
           var curForm = document.forms[curFormName];
           curForm.elements['form:init'].value = "Hello";
       }
       //-->
       </script>
       <script type="text/javascript" language="Javascript">
       <!--
       function initValue(curFormName) {
           var curForm = document.forms[curFormName];
           curForm.elements['form:_idcl'].value = "Goodbye";
       }
       //-->
       </script>
               
          <h:form id="form">
              <table>
              <tr>
                 <td><h:outputText value="Value:" /></td> 
                 <td><h:inputText id="init" value="initial value" /></td>
              </tr>
              </table>
              <h:commandLink id="submit" onclick="setValue('form'); return false;" value="submit"/>
          </h:form>
       </f:view>
</body>
</HTML>
