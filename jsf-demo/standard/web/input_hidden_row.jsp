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
