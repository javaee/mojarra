<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>
                 <h:output_text id="quantityLabel" value="Option list from JSP" />

             </td>


             <td>
<%-- PENDING: accesskey not recognized
                 <h:selectone_listbox id="appleQuantity" 
                     title="Select Quantity"
                     accesskey="N" tabindex="20" >
--%>
                 <h:selectone_listbox id="appleQuantity" 
                     title="Select Quantity"
                     tabindex="20" >

                <f:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
                <f:selectitem  itemValue="1" itemLabel="1" />
                <f:selectitem  itemValue="2" itemLabel="2" />
                <f:selectitem  itemValue="3" itemLabel="3" />
                <f:selectitem  itemValue="4" itemLabel="4" />
                <f:selectitem  itemValue="5" itemLabel="5" />
                <f:selectitem  itemValue="6" itemLabel="6" />
                <f:selectitem  itemValue="7" itemLabel="7" />
                <f:selectitem  itemValue="8" itemLabel="8" />
                <f:selectitem  itemValue="9" itemLabel="9" />

              </h:selectone_listbox>

             </td>

            </tr>

            <tr>

             <td>
                 <h:output_text id="optionLabel"
                   value="Listbox with Kinds of Beans from Model Object" />

             </td>


             <td>
                <h:selectone_listbox id="Listbox"
                             valueRef="LoginBean.currentOption">

                <f:selectitems id="listboxOptions"
                                   valueRef="LoginBean.options"/>

              </h:selectone_listbox>

             </td>

            </tr>

            <tr>

             <td>
                 <h:output_text id="longoptionLabel"
                   value="Listbox with options of the type java.lang.Long" />

             </td>


             <td>
                <h:selectone_listbox id="longListbox" 
                             valueRef="LoginBean.currentLongOption">
                    
                <f:selectitems id="listboxLongOptions"
                                   valueRef="LoginBean.longList"/>

              </h:selectone_listbox>

             </td>

            </tr>

