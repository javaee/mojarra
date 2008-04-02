<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>
                 <h:outputText id="quantityLabel" value="Option list from JSP" />

             </td>


             <td>
<%-- PENDING: accesskey not recognized
                 <h:selectOneListbox id="appleQuantity" 
                     title="Select Quantity"
                     accesskey="N" tabindex="20" >
--%>
                 <h:selectOneListbox id="appleQuantity" 
                     title="Select Quantity"
                     tabindex="20" >

                <f:selectItem  itemDisabled="true" itemValue="0" itemLabel="0"/>
                <f:selectItem  itemValue="1" itemLabel="1" />
                <f:selectItem  itemValue="2" itemLabel="2" />
                <f:selectItem  itemValue="3" itemLabel="3" />
                <f:selectItem  itemValue="4" itemLabel="4" />
                <f:selectItem  itemValue="5" itemLabel="5" />
                <f:selectItem  itemValue="6" itemLabel="6" />
                <f:selectItem  itemValue="7" itemLabel="7" />
                <f:selectItem  itemValue="8" itemLabel="8" />
                <f:selectItem  itemValue="9" itemLabel="9" />

              </h:selectOneListbox>

             </td>

            </tr>

            <tr>

             <td>
                 <h:outputText id="optionLabel"
                   value="Listbox with Kinds of Beans from Model Object" />

             </td>


             <td>
                <h:selectOneListbox id="Listbox"
                             value="#{LoginBean.currentOption}">

                <f:selectItems id="listboxOptions"
                                   value="#{LoginBean.options}"/>

              </h:selectOneListbox>

             </td>

            </tr>

            <tr>

             <td>
                 <h:outputText id="longoptionLabel"
                   value="Listbox with options of the type java.lang.Long" />

             </td>


             <td>
                <h:selectOneListbox id="longListbox" 
                             value="#{LoginBean.currentLongOption}">
                    
                <f:selectItems id="listboxLongOptions"
                                   value="#{LoginBean.longList}"/>

              </h:selectOneListbox>

             </td>

            </tr>


<tr>

  <td>
    <h:outputText             id="disabledsListboxLabel"
                           value="Listbox with even numbered options disabled"/>
  </td>

  <td>
    <h:selectOneListbox       id="disabledsListbox"
                           value="#{SelectItemsData.disabled}">
      <f:selectItems       value="#{SelectItemsData.disableds}"/>
    </h:selectOneListbox>
  </td>

</tr>


<tr>

  <td>
    <h:outputText             id="nestedsListboxLabel"
                           value="Listbox with nested options"/>
  </td>

  <td>
    <h:selectOneListbox       id="nestedsListbox"
                           value="#{SelectItemsData.nested}">
      <f:selectItems       value="#{SelectItemsData.nesteds}"/>
    </h:selectOneListbox>
  </td>

</tr>
