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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
  <html>
    <head>
      <title>Nested Tables</title>
    </head>
    <body>
    
    <p>Test from issue <a href="https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=62">62</a>.</p>
      <h:form id="form">
        <h:dataTable id="outer" value="#{outer62.model}" var="yyy">
          <h:column>
            outer
            <h:dataTable id="inner" value="#{yyy.model}" var="www">
              <h:column>
                inner
                <h:commandLink action="#{outer62.action}">link</h:commandLink>
             </h:column>
           </h:dataTable>
          </h:column>
         </h:dataTable>
         
         <p>Current Status = <h:outputText value="#{outer62.curStatus}" /></p>
      </h:form>
    </body>
  </html>
</f:view>

