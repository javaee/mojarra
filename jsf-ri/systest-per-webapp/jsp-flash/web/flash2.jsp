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
    <title>RoR Flash Test Page 2</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  </head>

  <body>
    <h1>RoR Flash Test Page 2</h1>

<f:view>

  <p>As mentioned in the previous page, if I wanted to store something
  in the flash during this request and also access it during this
  request, <code>\#{flash.now.bar}</code> is the way to do it.  In
  reality, this just puts the value in request scope, but that's what
  "now" is, anyway.</p>

  <h:form prependId="false" id="form1">

  <h:panelGrid columns="2" border="1">

    Value of the previous request's foo

    <h:outputText id="flash2FooValueId" value="#{flash.foo}" />

    Put <code>barValue</code> in the flash.now under key
    <code>bar</code>.

    <c:set target="${flash.now}" property="bar" value="barValue" />

    <f:verbatim>
      &lt;c:set target="\${flash.now}" property="bar" value="barValue" /&gt;
    </f:verbatim>

    Value of <code>\#{flash.now.bar}</code>, should be <code>barValue</code>.

    <h:outputText id="flash2BarValueId" value="#{flash.now.bar}" />

    <h:commandButton id="reload" value="reload" />

    <h:commandButton id="back" value="back" action="back" />

    &nbsp;

    <h:commandButton id="next" value="next" action="next" />

   </h:panelGrid>

   <p><h:messages id="messages"/></p>

  </h:form>

</f:view>
  </body>
</html>
