<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

            <tr>
              <td><b><h:output_text id="dateconvertLabel"
                         value="Date [MMM DD, YYYY Format]: " /></b></td>
              <td><h:input_hidden id="datetextHidden" 
                      modelReference="LoginBean.date"
                      converter="date"/></td>
              <td><h:output_errors id="dateErr"
                      compoundId="/standardRenderKitForm/datetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="timeconvertLabel"
                         value="Time [HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:input_hidden id="timetextHidden" 
                      modelReference="LoginBean.time"
                      converter="time"/></td>
              <td><h:output_errors id="timeErr"
                      compoundId="/standardRenderKitForm/timetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="datetimeconvertLabel"
                         value="Date/Time [MMM DD, YYYY HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:input_hidden id="datetimetextHidden" 
                      modelReference="LoginBean.dateTime"
                      converter="datetime"/></td>
              <td><h:output_errors id="datetimeErr"
                      compoundId="/standardRenderKitForm/datetimetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="boolconvertLabel"
                         value="Boolean [true/false]: " /></b></td>
              <td><h:input_hidden id="booltextHidden" value="false"
                      converter="boolean"/></td>
            </tr>
            <tr>
                <td><b><h:output_text id="currencylabel"
                           value="Currency Format:"/></b></td>
                <td>
                    <h:input_hidden id="currencyinputHidden"
                        modelReference="LoginBean.double"
                        converter="number">
                        <f:attribute name="numberStyle" value="currency"/>
                    </h:input_hidden>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="percentlabel"
                           value="Percent Format:"/></b></td>
                <td>
                    <h:input_hidden id="percentinputHidden"
                        converter="number" value="54%">
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
                        converter="number">
                        <f:attribute name="formatPattern" value="####"/>
                    </h:input_hidden>
                </td>
            </tr>
