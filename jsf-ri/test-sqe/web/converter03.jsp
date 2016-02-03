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
    <title>Converters</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Converters</h1>

<f:view>

  <h:form>

    <h:panelGrid columns="2">

<%-- Case 1: Custom Converter with "converterId" attribute --%>

      <h:inputText id="text1"> 
        <f:converter converterId="TestConverter01" />
      </h:inputText>

      <h:message for="text1" />

<%-- Case 2: Custom Converter with "binding" attribute --%>

      <h:inputText id="text2"> 
        <f:converter binding="#{converterBean.converter}" />
      </h:inputText>

      <h:message for="text2" />

<%-- Case 3: "converterId" and "binding" specified                        --%>
<%--         "binding" will set the instance (created from "converterId") --%>
<%--         to a property on the backing bean                     --%>

      <h:inputText id="text3"> 
        <f:converter converterId="TestConverter01"
           binding="#{converterBean.converter}" />
      </h:inputText>

      <h:message for="text3" />

<%-- Bind the converter we created (Case 3) to the component --%>

      <h:inputText id="text4">
        <f:converter binding="#{converterBean.converter}" />
      </h:inputText>

      <h:message for="text4" />

<%-- DateTime Converter with "binding" attribute --%>

      <h:inputText id="text5" label="text5" value="10:00:01 PM" size="10" maxlength="20">
         <f:convertDateTime binding="#{converterBean.dateTimeConverter}"
            type="time" timeStyle="medium"/>
      </h:inputText>

      <h:message for="text5" />

<%-- Case 1: Double Converter with "converterId" attribute --%>

      <h:inputText id="text6" value="100" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Double" />
      </h:inputText>

      <h:message for="text6" />

<%-- Case 2: Double Converter with "binding" attribute --%>

      <h:inputText id="text7" value="100" size="10" maxlength="20">
         <f:converter binding="#{converterBean.doubleConverter}" />
      </h:inputText>

      <h:message for="text7" />

<%-- Case 3: Double Converter "converterId" and "binding" specified       --%> 
<%--         "binding" will set the instance (created from "converterId") --%>
<%--         to a property on the backing bean                            --%>

      <h:inputText id="text8"> 
        <f:converter converterId="javax.faces.Double"
           binding="#{converterBean.doubleConverter}" />
      </h:inputText>

      <h:message for="text8" />

<%-- Number Converter with "binding" attribute --%>

      <h:inputText id="text9" value="9" size="10" maxlength="20">
         <f:convertNumber binding="#{converterBean.numberConverter}" />
      </h:inputText>

      <h:message for="text9" />

      <h:commandButton value="submit" /> <h:messages />

    </h:panelGrid>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
