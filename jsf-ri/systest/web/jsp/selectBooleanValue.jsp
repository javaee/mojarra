<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
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

<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>selectBoolean test</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>selectBoolean test</h1>

<h2>How this testcase works.</h2>

<p>This is the regression test for bugtraq 5016123.</p>

<p>The system test for this page does the following</p>


	<ol>

	  <li><p>presses the button with the id "replace" twice.</p>

          <p>The first time pressed, it replaces the default
          PropertyResolver with one that logs calls to setValue in the
          "valueChanged" property of bean named test3.  The second time
          pressed, you'll actually see that the setValue was called.</p>

          </li>

	  <li><p>When the page loads from the second button press, it
	  looks for the string "setValue() called" and verifies it is
	  not in the page. </p></li>

	  <li><p>presses the button with the id "restore".
	  </p></li>

	</ol>


<f:view>

  <h:form>

    <h:commandButton id="replace" value="submit and replace PropertyResolver with Logging PropertyResolver" 
                     actionListener="#{test3.replacePropertyResolver}" />

    <h:panelGrid columns="2">

      <h:selectBooleanCheckbox value="#{test3.booleanProperty2}" />

      <h:outputText value="checkbox" />

      <h:outputText value="valueChanged:" />

      <h:outputText value="#{test3.valueChangeMessage}" />

      <h:messages />

    </h:panelGrid>

    <h:commandButton id="restore" 
                     value="submit and restore original PropertyResolver" 
                     actionListener="#{test3.restorePropertyResolver}" />


  </h:form>

</f:view>

    <hr>
  </body>
</html>
