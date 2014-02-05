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



            <tr>
              <td><b><h:outputText id="dateconvertLabel"
                         value="Date [MMM DD, YYYY Format]: " /></b></td>
              <td><h:inputHidden id="datetextHidden" value="#{LoginBean.date}">
                      <f:convertDateTime type="date"/>
                  </h:inputHidden></td>
              <td><h:message id="dateErr"
                      for="datetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:outputText id="timeconvertLabel"
                         value="Time [HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:inputHidden id="timetextHidden" 
                      value="#{LoginBean.time}">
                      <f:convertDateTime type="time" /> 
                  </h:inputHidden></td>
              <td><h:message id="timeErr"
                      for="timetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:outputText id="datetimeconvertLabel"
                         value="Date/Time [MMM DD, YYYY HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:inputHidden id="datetimetextHidden" 
                      value="#{LoginBean.dateTime}">
                      <f:convertDateTime type="both"/>
                  </h:inputHidden></td>
              <td><h:message id="datetimeErr"
                      for="datetimetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:outputText id="boolconvertLabel"
                         value="Boolean [true/false]: " /></b></td>
              <td><h:inputHidden id="booltextHidden" value="false"/></td>
            </tr>
            <tr>
                <td><b><h:outputText id="currencylabel"
                           value="Currency Format:"/></b></td>
                <td>
                    <h:inputHidden id="currencyinputHidden"
                        value="#{LoginBean.double}">                        
                        <f:convertNumber type="currency"/>
                    </h:inputHidden>
                </td>
            </tr>
            <tr>
                <td><b><h:outputText id="percentlabel"
                           value="Percent Format:"/></b></td>
                <td>
                    <h:inputHidden id="percentinputHidden" value="54%">
                        <f:convertNumber type="percent"/>
                    </h:inputHidden>
                </td>
            </tr>
            <tr>
                <td><b><h:outputText id="patternlabel"
                           value="Pattern Format [####]:"/></b></td>
                <td>
                    <h:inputHidden id="patterninputHidden"
                        value="1999.65">
                        <f:convertNumber pattern="####"/>
                    </h:inputHidden>
                </td>
            </tr>
