           <tr>

             <td>
                 <faces:output_text id="quantityLabel" value="Option list from JSP" />

             </td>


             <td>
                 <faces:selectone_listbox id="appleQuantity" size="6"
                     title="Select Quantity"
                     accesskey="N" tabindex="20" >

                <faces:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
                <faces:selectitem  itemValue="1" itemLabel="1" title="One" />
                <faces:selectitem  itemValue="2" itemLabel="2" title="Two" />
                <faces:selectitem  itemValue="3" itemLabel="3" title="Three" />
                <faces:selectitem  itemValue="4" itemLabel="4" title="Four" selected="true"/>
                <faces:selectitem  itemValue="5" itemLabel="5" title="Five" />
                <faces:selectitem  itemValue="6" itemLabel="6" title="Six" />
                <faces:selectitem  itemValue="7" itemLabel="7" title="Seven" />
                <faces:selectitem  itemValue="8" itemLabel="8" title="Eight" />
                <faces:selectitem  itemValue="9" itemLabel="9" title="nine" />

              </faces:selectone_listbox>

             </td>

            </tr>

            <tr>

             <td>
                 <faces:output_text id="optionLabel"
                   value="Listbox with Kinds of Beans from Model Object" />

             </td>


             <td>
                <faces:selectone_listbox id="Listbox"
                             modelReference="${LoginBean.currentOption}">

                <faces:selectitems id="listboxOptions"
                                   modelReference="${LoginBean.options}"/>

              </faces:selectone_listbox>

             </td>

            </tr>

