<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

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
    <title>Test Core Tags VB Enabling</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
  </head>

  <body>
    <h1>Test Core Tags VB Enabling</h1>

<f:view>
<h:form id="validatorForm">

<table>

  <tr>

    <td>

                   <h:inputText id="doubleRange" value="1000.0">
                        <f:convertNumber type="number" integerOnly="false" 
                           maxFractionDigits="2" maxIntegerDigits="5" locale="en"
                           groupingUsed="true" />
                       <f:validateDoubleRange minimum="#{doubleMin}" 
                                             maximum="#{doubleMax}"/>
                   </h:inputText>

    </td>


  </tr>

  <tr>

    <td>
         <h:inputText id="longRange" value="1000">
                     <f:convertNumber pattern="####" 
                           minFractionDigits="0" minIntegerDigits="2" />
                     <f:validateLongRange minimum="#{longMin}" 
                                           maximum="#{longMax}"/>
         </h:inputText>

    </td>

     <h:outputText id="outputNumber2" value="$123.45">
                   <f:convertNumber type="currency" currencySymbol="$" />
               </h:outputText>

    <h:outputText id="outputDatetime3" value="7/10/96 12:31:31 PM PDT">
       <f:convertDateTime type="both" timeStyle="full" dateStyle="short" 
            locale="en"/>
    </h:outputText>

      

  </tr>

  <tr>

    <td>
            <h:inputText id="intRange" value="NorthAmerica">
                  
                     <f:validateLength minimum="#{intMin}" 
                                       maximum="#{intMax}"/>
             </h:inputText>

    </td>


  </tr>

</table>

</h:form>
</f:view>

  </body>
</html>
