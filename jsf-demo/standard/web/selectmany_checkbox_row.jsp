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
