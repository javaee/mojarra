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
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>form-input-02</title>
</head>
<body>

<h:form id="formInput02_form">

  <h:panelGrid columns="3">

    <h:outputText value="booleanProperty"/>
    <h:inputText id="booleanProperty" value="#{formInput02.booleanProperty}"/>
    <h:message for="booleanProperty"/>

    <h:outputText value="byteProperty"/>
    <h:inputText id="byteProperty" value="#{formInput02.byteProperty}"/>
    <h:message for="byteProperty"/>

    <h:outputText value="doubleProperty"/>
    <h:inputText id="doubleProperty" value="#{formInput02.doubleProperty}"/>
    <h:message for="doubleProperty"/>

    <h:outputText value="floatProperty"/>
    <h:inputText id="floatProperty" value="#{formInput02.floatProperty}"/>
    <h:message for="floatProperty"/>

    <h:outputText value="intProperty"/>
    <h:inputText id="intProperty" value="#{formInput02.intProperty}"/>
    <h:message for="intProperty"/>

    <h:outputText value="longProperty"/>
    <h:inputText id="longProperty" value="#{formInput02.longProperty}"/>
    <h:message for="longProperty"/>

    <h:outputText value="shortProperty"/>
    <h:inputText id="shortProperty" value="#{formInput02.shortProperty}"/>
    <h:message for="shortProperty"/>

    <h:outputText value="stringProperty"/>
    <h:inputText id="stringProperty" value="#{formInput02.stringProperty}"/>
    <h:message for="stringProperty"/>

    <h:commandButton id="submit" type="submit" value="Submit"/>
    <h:commandButton id="reset"  type="reset"  value="Reset"/>
    <h:outputText value=""/>

  </h:panelGrid>

</h:form>

</body>
</html>
</f:view>
