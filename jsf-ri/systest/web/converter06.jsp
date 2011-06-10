<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

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



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Converters</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
    <%@ taglib uri="/WEB-INF/taglib.tld"           prefix="s" %>
  </head>

  <body>
    <h1>Converters</h1>

<f:loadBundle basename="com.sun.faces.CustomMessages" var="customBundle"/>

<f:view>

  <h:form id="form">

    <h:panelGrid id="panelGrid" columns="3">


<%--
      Exercises javax.faces.webapp.ConverterELTag when ConverterException
      Expected result: FacesMessage queued;  Log message;
--%>
      <h:outputText value="Number4:" />
      <h:inputText id="number4" label="Number4" size="10" maxlength="20" value="aaa">
         <f:convertNumber type="number" />
      </h:inputText>
      <h:message for="number4" showSummary="true" />

<%--
      Exercises javax.faces.webapp.ConverterELTag when ConverterException
      Expected result: Log message;
--%>
      <h:outputText value="Number5:" />
      <h:outputText id="number5" value="aaa">
         <f:convertNumber type="number" />
      </h:outputText>
      <h:message for="number5" showSummary="true" />

<%--
      Exercises javax.faces.webapp.ConverterELTag when ConverterException
      Expected result: FacesMessage queued;  Log message; 
--%>
      <h:outputText value="Number6:" />
      <h:inputText id="number6" label="Number6" size="10" maxlength="20" value="aaa" converterMessage="My own message">
         <f:convertNumber type="number" />
      </h:inputText>
      <h:message for="number6" showSummary="false" />

<%--
      Exercises javax.faces.webapp.ConverterTag when ConverterException
      Expected result: FacesMessage queued;  Log message; 
--%>
      <h:outputText value="Number6:" />
      <h:outputText value="Number7:" />
      <h:inputText id="number7" label="Number7" size="10" maxlength="20" value="aaa">
         <s:converter converterId="javax.faces.Number" />
      </h:inputText>
      <h:message for="number7" showSummary="true" />

<%--
      Exercises javax.faces.webapp.ConverterTag when ConverterException
      Expected result: Log message;
--%>
      <h:outputText value="Number8:" />
      <h:outputText id="number8" value="aaa">
         <s:converter converterId="javax.faces.Number" />
      </h:outputText>
      <h:message for="number8" showSummary="true" />

<%--
      Exercises javax.faces.webapp.ConverterTag when ConverterException
      Expected result: FacesMessage queued;  Log message; 
--%>
      <h:outputText value="Number9:" />
      <h:inputText id="number9" label="Number6" size="10" maxlength="20" value="aaa" converterMessage="My own message">
         <s:converter converterId="javax.faces.Number" />
      </h:inputText>
      <h:message for="number9" showSummary="false" />
      <h:commandButton value="submit" /> 
    </h:panelGrid>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
