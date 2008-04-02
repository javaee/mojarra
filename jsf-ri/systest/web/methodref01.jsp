<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
