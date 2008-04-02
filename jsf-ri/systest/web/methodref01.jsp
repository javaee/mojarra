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
        <h:input_text id="input" valueRef="methodRef.buttonPressedOutcome"/>
        <h:command_button id="button1" value="button1"
                           actionRef="methodRef.button1Pressed"/>
        <h:command_button id="button2" value="button1"
                          actionRef="methodRef.button2Pressed"/>
      </h:form>
    </f:view>



    <hr>
    <address><a href="mailto:Ed Burns <ed.burns@sun.com>"></a></address>
<!-- Created: Fri Oct 31 10:49:23 Eastern Standard Time 2003 -->
<!-- hhmts start -->
Last modified: Fri Oct 31 12:17:28 Eastern Standard Time 2003
<!-- hhmts end -->
  </body>
</html>
