<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
