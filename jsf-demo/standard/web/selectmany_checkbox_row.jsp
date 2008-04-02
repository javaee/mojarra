	<!--
	 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
	 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
    <h:message               for="nestedssCheckbox"/>
  </td>

</tr>
