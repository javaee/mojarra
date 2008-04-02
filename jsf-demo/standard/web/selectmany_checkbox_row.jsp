<!--
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
-->

					<tr>
						<td>Multi-select checkbox:</td>
						<td><h:selectManyCheckbox id="ManyApples3">
							<f:selectItem itemValue="0" itemLabel="zero" />
							<f:selectItem itemValue="1" itemLabel="one" />
							<f:selectItem itemValue="2" itemLabel="two" />
							<f:selectItem itemValue="3" itemLabel="three" />
							<f:selectItem itemValue="4" itemLabel="four" />
							<f:selectItem itemValue="5" itemLabel="five" />
							<f:selectItem itemValue="6" itemLabel="six" />
							<f:selectItem itemValue="7" itemLabel="seven" />
							<f:selectItem itemValue="8" itemLabel="eight" />
							<f:selectItem itemValue="9" itemLabel="nine" />
						</h:selectManyCheckbox></td>
                                                <td><h:message id="Error1" for="ManyApples3"/></td>
					</tr>
					<tr>
						<td>Multi-select checkbox [Vertical]:</td>
						<td><h:selectManyCheckbox id="ManyApples4" 
						  layout="pageDirection">
							<f:selectItem itemValue="0" itemLabel="zero" />
							<f:selectItem itemValue="1" itemLabel="one" />
							<f:selectItem itemValue="2" itemLabel="two" />
							<f:selectItem itemValue="3" itemLabel="three" />
							<f:selectItem itemValue="4" itemLabel="four" />
							<f:selectItem itemValue="5" itemLabel="five" />
							<f:selectItem itemValue="6" itemLabel="six" />
							<f:selectItem itemValue="7" itemLabel="seven" />
							<f:selectItem itemValue="8" itemLabel="eight" />
							<f:selectItem itemValue="9" itemLabel="nine" />
						</h:selectManyCheckbox></td>
                                                <td><h:message id="Error2" for="ManyApples4"/></td>
					</tr>
					<tr>
						<td>Multi-select checklistmodel:</td>
						<td><h:selectManyCheckbox id="checklistmodel"
							value="#{LoginBean.currentOptions}">
							<f:selectItems id="checklistmodelitems"
								value="#{LoginBean.options}" />
						</h:selectManyCheckbox></td>
                                                <td><h:message id="Error3" for="checklistmodel"/></td>
					</tr>


					<tr>
						<td>Multi-select checklistmodel with options of the type java.lang.Long:</td>
						<td><h:selectManyCheckbox id="checkLonglistmodel"
							value="#{LoginBean.currentLongOptions}">
                                                 
							<f:selectItems id="checkLonglistmodelitems"
								value="#{LoginBean.longList}" />
						</h:selectManyCheckbox></td>
                                                <td><h:message id="Error4" for="checkLonglistmodel"/></td>
					</tr>


<tr>

  <td>
    <h:outputText             id="checklistmodelGroupLabel"
                           value="Multi-select checkbox list:"/>
  </td>

  <td>
    <h:selectManyCheckbox     id="checklistmodelGroup"
                           value="#{LoginBean.currentOptions}">
      <f:selectItems          id="checklistmodelitemsGroup"
                           value="#{LoginBean.optionsGroup}" />
    </h:selectManyCheckbox>
  </td>

  <td>
    <h:message                id="Error5" for="checklistmodelGroup"/>
  </td>

</tr>

<tr>

  <td>
    <h:outputText             id="disabledsCheckboxLabel"
                           value="Checkboxes with even numbered options disabled"/>
  </td>

  <td>
    <h:selectManyCheckbox     id="disabledsCheckbox"
                           value="#{SelectItemsData.disabledSelected}">
      <f:selectItems       value="#{SelectItemsData.disableds}"/>
    </h:selectManyCheckbox>
  </td>

  <td>
    <h:message               for="disabledsCheckbox"/>
  </td>

</tr>


<tr>

  <td>
    <h:outputText             id="nestedsCheckboxLabel"
                           value="Checkboxes with nested options"/>
  </td>

  <td>
    <h:selectManyCheckbox     id="nestedsCheckbox" layout="pageDirection"
                           value="#{SelectItemsData.nestedSelected}">
      <f:selectItems       value="#{SelectItemsData.nesteds}"/>
    </h:selectManyCheckbox>
  </td>

  <td>
    <h:message               for="nestedsCheckbox"/>
  </td>

</tr>
