<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

            <tr>
              <td><b><h:output_text id="dateconvertLabel"
                         value="Date [MMM DD, YYYY Format]: " /></b></td>
              <td><h:input_hidden id="datetextHidden" 
                      valueRef="LoginBean.date"
                      converter="Date"/></td>
              <td><h:output_errors id="dateErr"
                      for="datetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="timeconvertLabel"
                         value="Time [HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:input_hidden id="timetextHidden" 
                      valueRef="LoginBean.time"
                      converter="Time"/></td>
              <td><h:output_errors id="timeErr"
                      for="timetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="datetimeconvertLabel"
                         value="Date/Time [MMM DD, YYYY HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:input_hidden id="datetimetextHidden" 
                      valueRef="LoginBean.dateTime"
                      converter="DateTime"/></td>
              <td><h:output_errors id="datetimeErr"
                      for="datetimetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="boolconvertLabel"
                         value="Boolean [true/false]: " /></b></td>
              <td><h:input_hidden id="booltextHidden" value="false"
                      converter="Boolean"/></td>
            </tr>
            <tr>
                <td><b><h:output_text id="currencylabel"
                           value="Currency Format:"/></b></td>
                <td>
                    <h:input_hidden id="currencyinputHidden"
                        valueRef="LoginBean.double"
                        converter="Number">
                        <f:attribute name="numberStyle" value="currency"/>
                    </h:input_hidden>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="percentlabel"
                           value="Percent Format:"/></b></td>
                <td>
                    <h:input_hidden id="percentinputHidden"
                        converter="Number" value="54%">
                        <f:attribute name="numberStyle" value="percent"/>
                    </h:input_hidden>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="patternlabel"
                           value="Pattern Format [####]:"/></b></td>
                <td>
                    <h:input_hidden id="patterninputHidden"
                        value="1999.65"
                        converter="Number">
                        <f:attribute name="formatPattern" value="####"/>
                    </h:input_hidden>
                </td>
            </tr>
