<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

                 <h:output_text id="shipmentLabel" 
                     value="Radio with hard-coded options laid out horizontally" />
             </td>


             <td>
                 <h:selectone_radio id="shipType" layout="LINE_DIRECTION" >

                <h:selectitem itemValue="nextDay" itemLabel="Next Day"
                      tabindex="30" title="Next day shipment"/>

                <h:selectitem itemValue="nextWeek" itemLabel="Next Week" 
                   title="Next week shipment" tabindex="40" selected="true" />

                <h:selectitem itemValue="nextMonth" itemLabel="Next Month"
                        tabindex="50" title="Next month shipment"/>

              </h:selectone_radio>

             </td>

            </tr>

            <tr>

             <td>
                 <h:output_text id="verticalLabel" 
                     value="Radio with hard-coded options laid out vertically" />

             </td>


             <td>
                <h:selectone_radio id="verticalRadio" layout="PAGE_DIRECTION" border="1" >
                 <h:selectitem itemValue="nextDay" itemLabel="Next Day"
                                  selected="true" />
                <h:selectitem itemValue="nextWeek" itemLabel="Next Week"  />
                <h:selectitem itemValue="nextMonth" itemLabel="Next Month" />

                </h:selectone_radio>

             </td>

            </tr>

             <tr>

             <td>
                 <h:output_text id="modelLabel" value="Radio with options from model " />

             </td>


             <td>
                 <h:selectone_radio id="radioFromModel"
                       valueRef="LoginBean.currentOption"
                       layout="LINE_DIRECTION" >

                <h:selectitems id="listboxOptions"
                                   title="options come from model"
                                   valueRef="LoginBean.options"/>

              </h:selectone_radio>

             </td>

            </tr>



