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
        <h:input_text readonly="true" id="buttonStatus" 
                      valueRef="methodRef.buttonPressedOutcome"/>
        <h:command_button id="button1" value="button1"
                           actionRef="methodRef.button1Pressed"/>
        <h:command_link id="button2" actionRef="methodRef.button2Pressed">
          <h:output_text value="button2"/>
        </h:command_link>
        <h:command_button id="button3" value="button3"
                           actionListenerRef="methodRef.button3Pressed"/>
        <hr>

	<hr>
	<p>the only valid value is batman</p>
        <h:input_text id="toValidate" 
                      valueRef="methodRef.validateOutcome"
                      validateRef="methodRef.validateInput"/>
        <h:command_button id="validate" value="validate"/>
        <h:input_text id="validateStatus" readonly="true" 
                      valueRef="methodRef.validateOutcome"/>
        <h:output_errors for="toValidate"/>
        <hr>

      </h:form>
    </f:view>



    <hr>
    <address><a href="mailto:Ed Burns <ed.burns@sun.com>"></a></address>
<!-- Created: Fri Oct 31 10:49:23 Eastern Standard Time 2003 -->
<!-- hhmts start -->
Last modified: Fri Oct 31 15:56:27 Eastern Standard Time 2003
<!-- hhmts end -->
  </body>
</html>
