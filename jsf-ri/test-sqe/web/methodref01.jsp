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
    <title>Test Method References</title>
  </head>

  <body>
    <h1>Test Method References</h1>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <f:view>  
      <h:form id="form">

	<hr>
	<p>Press a button, see some text.</p>
        <h:inputText readonly="true" id="buttonStatus" 
                      value="#{methodRef.buttonPressedOutcome}"/>
        <h:commandButton id="button1" value="button1"
                           action="#{methodRef.button1Pressed}"/>
        <h:commandLink id="button2" action="#{methodRef.button2Pressed}">
          <h:outputText value="button2"/>
        </h:commandLink>
        <h:commandButton id="button3" value="button3"
                           actionListener="#{methodRef.button3Pressed}"/>
                            <h:message for="buttonStatus"/>
        <hr>
	<p>the only valid value is batman</p>
        <h:inputText id="toValidate" 
                      validator="#{methodRef.validateInput}"/>
        <h:commandButton id="validate" value="validate"/>
        <h:message for="toValidate"/>

        <hr>
	<p>test value change</p>
        <h:inputText id="toChange" 
                      valueChangeListener="#{methodRef.valueChange}"/>
        <h:commandButton id="changeValue" value="changeValue"/>
        <h:message for="toChange"/>
      </h:form>
    </f:view>



    <hr>
    <address><a href="mailto:Ed Burns <ed.burns@sun.com>"></a></address>
<!-- Created: Fri Oct 31 10:49:23 Eastern Standard Time 2003 -->
<!-- hhmts start -->
Last modified: Mon Jan 19 10:03:14 Pacific Standard Time 2004
<!-- hhmts end -->
  </body>
</html>
