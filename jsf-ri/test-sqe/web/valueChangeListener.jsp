
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
    <title>ValueChangeListeners and Validators</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>ValueChangeListeners and Validators</h1>

<f:view>

  <h:form>

    <p>Enter numbers from 1 to 10.</p>

    <h:panelGrid columns="2">

      <h:inputText id="textA" 
	       valueChangeListener="#{valueChangeListenerBean.textAChanged}">
        <f:validateLongRange minimum="1" maximum="10" />
      </h:inputText>

      <h:inputText id="textB" 
	       valueChangeListener="#{valueChangeListenerBean.textBChanged}">
        <f:validateLongRange minimum="1" maximum="10" />
      </h:inputText>

      <h:outputText value="#{valueChangeListenerBean.textAResult}" />

      <h:outputText value="#{valueChangeListenerBean.textBResult}" />

      <h:commandButton value="submit" /> <p>

      <h:messages dir="LTR" lang="en"/>

      <hr />

      <h:message for="textB" dir="RTL" lang="de"/>


    </h:panelGrid>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
