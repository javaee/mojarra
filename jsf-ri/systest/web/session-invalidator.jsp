<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test with action that invalidates a session.</title>
    <%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
    <%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
  </head>

  <body>
    <h1>Test with action that invalidates a session.</h1>

    <f:view>

     <h:form id="form">

      <table border="1">

       <tr>

	 <td>

	    Next cell's contents come from a bean in session scope.

	 </td>

	 <td>

	    <h:outputText value="#{test3.stringProperty}" style="color: red"/>

	 </td>

	 <td>

	   <h:commandButton action="#{methodRef.invalidateSession}"
                          id="button1"
                          value="Press to invalidate session and redisplay" />

	 </td>

       </tr>

      </table>
   
      </h:form>

    </f:view>


  </body>
</html>
