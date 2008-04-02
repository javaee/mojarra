<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
