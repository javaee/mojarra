<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>
                 <h:outputText id="quantityLabel" value="Option list from JSP" />

             </td>


             <td>
<%-- PENDING: accesskey not recognized
                 <h:selectoneListbox id="appleQuantity" 
                     title="Select Quantity"
                     accesskey="N" tabindex="20" >
--%>
                 <h:selectoneListbox id="appleQuantity" 
                     title="Select Quantity"
                     tabindex="20" >

                <f:selectItem  disabled="true" itemValue="0" itemLabel="0"/>
                <f:selectItem  itemValue="1" itemLabel="1" />
                <f:selectItem  itemValue="2" itemLabel="2" />
                <f:selectItem  itemValue="3" itemLabel="3" />
                <f:selectItem  itemValue="4" itemLabel="4" />
                <f:selectItem  itemValue="5" itemLabel="5" />
                <f:selectItem  itemValue="6" itemLabel="6" />
                <f:selectItem  itemValue="7" itemLabel="7" />
                <f:selectItem  itemValue="8" itemLabel="8" />
                <f:selectItem  itemValue="9" itemLabel="9" />

              </h:selectoneListbox>

             </td>

            </tr>

            <tr>

             <td>
                 <h:outputText id="optionLabel"
                   value="Listbox with Kinds of Beans from Model Object" />

             </td>


             <td>
                <h:selectoneListbox id="Listbox"
                             value="#{LoginBean.currentOption}">

                <f:selectItems id="listboxOptions"
                                   value="#{LoginBean.options}"/>

              </h:selectoneListbox>

             </td>

            </tr>

            <tr>

             <td>
                 <h:outputText id="longoptionLabel"
                   value="Listbox with options of the type java.lang.Long" />

             </td>


             <td>
                <h:selectoneListbox id="longListbox" 
                             value="#{LoginBean.currentLongOption}">
                    
                <f:selectItems id="listboxLongOptions"
                                   value="#{LoginBean.longList}"/>

              </h:selectoneListbox>

             </td>

            </tr>

