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
    <title>Validators</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Validators</h1>

<f:view>

  <h:form>

    <h:panelGrid columns="2">

<%-- Case 1: Custom Validator with "validatorId" attribute --%>

      <h:inputText id="text1"> 
        <f:validator validatorId="TestValidator01" />
      </h:inputText>

      <h:message for="text1" />

<%-- Case 2: Custom Validator with "binding" attribute --%>

      <h:inputText id="text2"> 
        <f:validator binding="#{validatorBean.validator}" />
      </h:inputText>

      <h:message for="text2" />

<%-- Case 3: "validatorId" and "binding" specified                        --%>
<%--         "binding" will set the instance (created from "validatorId") --%>
<%--         to a property on the backing bean                     --%>

      <h:inputText id="text3"> 
        <f:validator validatorId="TestValidator01"
           binding="#{validatorBean.validator}" />
      </h:inputText>

      <h:message for="text3" />

<%-- Bind the validator we created (Case 3) to the component --%>

      <h:inputText id="text4">
        <f:validator binding="#{validatorBean.validator}" />
      </h:inputText>

      <h:message for="text4" />

<%-- Double Range Validator with "binding" attribute --%>
                                                                                     
      <h:inputText id="text5">
        <f:validateDoubleRange binding="#{validatorBean.doubleValidator}" 
           maximum="2" />
      </h:inputText>
                                                                                     
      <h:message for="text5" />
                                                                                     
<%-- Length Validator with "binding" attribute --%>
                                                                                     
      <h:inputText id="text6">
        <f:validateLength binding="#{validatorBean.lengthValidator}" 
           maximum="5" />
      </h:inputText>
                                                                                     
      <h:message for="text6" />
                                                                                     
<%-- Long Range Validator with "binding" attribute --%>
                                                                                     
      <h:inputText id="text7">
        <f:validateLongRange binding="#{validatorBean.longRangeValidator}"
           maximum="4" />
      </h:inputText>
                                                                                     
      <h:message for="text7" />
                                                                                     

      <h:commandButton value="submit" /> <h:messages />

    </h:panelGrid>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
