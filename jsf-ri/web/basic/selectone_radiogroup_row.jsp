           <tr>

             <td>

                 <faces:output_text id="shipmentLabel" 
                     value="Radio with hard-coded options laid out horizontally" />
             </td>


             <td>
                 <faces:selectone_radiogroup id="shipType" layout="LINE_DIRECTION" >

                <faces:selectitem itemValue="nextDay" itemLabel="Next Day"
                      tabindex="30" title="Next day shipment"/>

                <faces:selectitem itemValue="nextWeek" itemLabel="Next Week" 
                   title="Next week shipment" tabindex="40" selected="true" />

                <faces:selectitem itemValue="nextMonth" itemLabel="Next Month"
                        tabindex="50" title="Next month shipment"/>

              </faces:selectone_radiogroup>

             </td>

            </tr>

            <tr>

             <td>
                 <faces:output_text id="verticalLabel" 
                     value="Radio with hard-coded options laid out vertically" />

             </td>


             <td>
                <faces:selectone_radiogroup id="verticalRadio" layout="PAGE_DIRECTION" border="1" >
                 <faces:selectitem itemValue="nextDay" itemLabel="Next Day"
                                  selected="true" />
                <faces:selectitem itemValue="nextWeek" itemLabel="Next Week"  />
                <faces:selectitem itemValue="nextMonth" itemLabel="Next Month" />

                </faces:selectone_radiogroup>

             </td>

            </tr>

             <tr>

             <td>
                 <faces:output_text id="modelLabel" value="Radio with options from model " />

             </td>


             <td>
                 <faces:selectone_radiogroup id="radioFromModel"
                       modelReference="${LoginBean.currentOption}"
                       layout="LINE_DIRECTION" >

                <faces:selectitems id="optionListOptions"
                                   title="options come from model"
                                   modelReference="${LoginBean.options}"/>

              </faces:selectone_radiogroup>

             </td>

            </tr>



