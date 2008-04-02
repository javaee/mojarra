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

                <h:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
                <h:selectitem  itemValue="1" itemLabel="1" title="One" />
                <h:selectitem  itemValue="2" itemLabel="2" title="Two" />
                <h:selectitem  itemValue="3" itemLabel="3" title="Three" />
                <h:selectitem  itemValue="4" itemLabel="4" title="Four" />
                <h:selectitem  itemValue="5" itemLabel="5" title="Five" />
                <h:selectitem  itemValue="6" itemLabel="6" title="Six" />
                <h:selectitem  itemValue="7" itemLabel="7" title="Seven" />
                <h:selectitem  itemValue="8" itemLabel="8" title="Eight" />
                <h:selectitem  itemValue="9" itemLabel="9" title="nine" />

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

                <h:selectitems id="listboxOptions"
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
                    
                <h:selectitems id="listboxLongOptions"
                                   valueRef="LoginBean.longList"/>

              </h:selectone_listbox>

             </td>

            </tr>

