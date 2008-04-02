<!--
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
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
