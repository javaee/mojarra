
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
    <title>Validators</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Validators</h1>

<f:view>

  <h:form>

    <h:panelGrid columns="3">

<%-- Case 1: Double Range Validator with no "label" attribute --%>

      <h:outputText value="DoubleRange1:"/>
      <h:inputText id="dr1"> 
        <f:validateDoubleRange minimum="2" maximum="5" />
      </h:inputText>
      <h:message for="dr1" showSummary="true" />

<%-- Case 2: Double Range Validator with "label" attribute --%>

      <h:outputText value="DoubleRange2:"/>
      <h:inputText id="dr2" label="DoubleRange2"> 
        <f:validateDoubleRange minimum="2" maximum="5" />
      </h:inputText>
      <h:message for="dr2" showSummary="true" />

<%-- Case 3: Length Validator with no "label" attribute --%>

      <h:outputText value="Length1:"/>
      <h:inputText id="l1"> 
        <f:validateLength minimum="2" maximum="5" />
      </h:inputText>
      <h:message for="l1" showSummary="true" />

<%-- Case 4: Length Validator with "label" attribute --%>

      <h:outputText value="Length2:"/>
      <h:inputText id="l2" label="Length2"> 
        <f:validateLength minimum="2" maximum="5" />
      </h:inputText>
      <h:message for="l2" showSummary="true" />

<%-- Case 5: Long Range Validator with no "label" attribute --%>

      <h:outputText value="LongRange1:"/>
      <h:inputText id="lr1"> 
        <f:validateLongRange minimum="2" maximum="5" />
      </h:inputText>
      <h:message for="lr1" showSummary="true" />

<%-- Case 6: Long Range Validator with "label" attribute --%>

      <h:outputText value="LongRange2:"/>
      <h:inputText id="lr2" label="LongRange2"> 
        <f:validateLongRange minimum="2" maximum="5" />
      </h:inputText>
      <h:message for="lr2" showSummary="true" />

      <h:commandButton value="submit" />

    </h:panelGrid>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
