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
