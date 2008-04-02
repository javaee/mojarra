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
    <title>test</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>test</h1>

<f:view>

    <p>Page One</p>

	<ol>

	  <li><p>Click one the "one" link below
	  </p></li>

	  <li><p>Use the browser back button to go back
	  </p></li>

	  <li><p>Click on the "two" link below
	  </p></li>

	  <li><p>Note you receive no IllegalStateException
	  </p></li>

	  <li><p>Click on the "one" link below
	  </p></li>

	  <li><p>Use the browser back button to go back
	  </p></li>

	  <li><p>Click on the "four" button" note that you go to page four
	  </p></li>

	  <li><p>Use the browser back button to go back
	  </p></li>

	  <li><p>Click on the "five" button" note that you go to page five
	  </p></li>



	</ol>


  <h:form>

    <h:commandLink id="two" action="two">
      <h:outputText value="two" />
    </h:commandLink>

<br />

    <h:commandLink id="three" action="three">
      <h:outputText value="three" />
    </h:commandLink>


  </h:form>

  <h:form>

    <h:commandLink id="four" action="four">
      <h:outputText value="four" />
    </h:commandLink>

<br />

    <h:commandLink id="five" action="five">
      <h:outputText value="five" />
    </h:commandLink>


  </h:form>


</f:view>

    <hr>
  </body>
</html>
