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
    <title>Test Validator Tags</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
  </head>

  <body>
    <h1>Test Validator Tags</h1>

<f:view>
<h:form id="validatorForm">
<table>

  <tr>

    <td>

                   <h:inputText id="outOfBounds1" value="3.1415">
                       <f:convertNumber pattern="####"/>
                       <f:validateDoubleRange minimum="10.0" 
                                              maximum="10.5"/>
                   </h:inputText>

    </td>


  </tr>

  <tr>

    <td>

                   <h:inputText id="inBounds1" value="10.25">
                     <f:convertNumber pattern="####"/>
                     <f:validateDoubleRange minimum="10.0" 
                                            maximum="10.5"/>
                   </h:inputText>

    </td>


  </tr>

  <tr>

    <td>

                   <h:inputText id="outOfBounds2" value="fox">
                     <f:validateLength minimum="10" maximum="11"/>
                   </h:inputText>

    </td>


  </tr>

  <tr>

    <td>

                   <h:inputText id="inBounds2" value="alligator22">
                     <f:validateLength minimum="10"  maximum="12"/>
                   </h:inputText>

    </td>


  </tr>

  <tr>

    <td>

                   <h:inputText id="outOfBounds3" value="30000">
                     <f:convertNumber  />
                     <f:validateLongRange minimum="100000" maximum="110000"/>
                   </h:inputText>

    </td>


  </tr>

  <tr>

    <td>

                   <h:inputText id="inBounds3" value="1100">
                     <f:convertNumber  />
                     <f:validateLongRange minimum="1000"  maximum="1200"/>
                   </h:inputText>

    </td>


  </tr>

  <tr>

    <td>

                   <h:inputText id="required1" value="required" 
                                 required="true"/>

    </td>


  </tr>

  <tr>

    <td>

                   <h:inputText id="required2" value="required" 
                                 required="true"/>
    </td>


  </tr>

</table>
</h:form>
</f:view>

  </body>
</html>
