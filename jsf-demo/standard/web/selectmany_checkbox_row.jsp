	<!--
	 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
	 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
	-->

					<tr>
						<td>Multi-select checkbox:</td>
						<td><h:selectmany_checkboxlist id="ManyApples3">
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
						</h:selectmany_checkboxlist></td>
                                                <td><h:message id="Error1" for="ManyApples3"/></td>
					</tr>
					<tr>
						<td>Multi-select checkbox [Vertical]:</td>
						<td><h:selectmany_checkboxlist id="ManyApples4" 
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
						</h:selectmany_checkboxlist></td>
                                                <td><h:message id="Error2" for="ManyApples4"/></td>
					</tr>
					<tr>
						<td>Multi-select checklistmodel:</td>
						<td><h:selectmany_checkboxlist id="checklistmodel"
							value="#{LoginBean.currentOptions}">
							<f:selectItems id="checklistmodelitems"
								value="#{LoginBean.options}" />
						</h:selectmany_checkboxlist></td>
                                                <td><h:message id="Error3" for="checklistmodel"/></td>
					</tr>

					<tr>
						<td>Multi-select checklistmodel with options of the type java.lang.Long:</td>
						<td><h:selectmany_checkboxlist id="checkLonglistmodel"
							value="#{LoginBean.currentLongOptions}">
                                                 
							<f:selectItems id="checkLonglistmodelitems"
								value="#{LoginBean.longList}" />
						</h:selectmany_checkboxlist></td>
                                                <td><h:message id="Error4" for="checkLonglistmodel"/></td>
					</tr>

					<tr>
						<td>Multi-select checklistmodelGroup:</td>
						<td><h:selectmany_checkboxlist id="checklistmodelGroup"
							value="#{LoginBean.currentOptions}">
							<f:selectItems id="checklistmodelitemsGroup"
								value="#{LoginBean.optionsGroup}" />
						</h:selectmany_checkboxlist></td>
                                                <td><h:message id="Error5" for="checklistmodelGroup"/></td>
				</tr
