<%--

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.

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
