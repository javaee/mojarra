<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

				<tr>
					<td>Multi-select listbox:</td>
					<td><h:selectmany_listbox id="ManyApples2">
						<f:selectItem itemValue="0" itemLabel="zero" />
						<f:selectItem itemValue="1" itemLabel="one" />
						<f:selectItem itemValue="2" itemLabel="two" />
						<f:selectItem itemValue="3" itemLabel="three" />
						<f:selectItem itemValue="4" itemLabel="four"  />
						<f:selectItem itemValue="5" itemLabel="five" />
						<f:selectItem itemValue="6" itemLabel="six" />
						<f:selectItem itemValue="7" itemLabel="seven" />
						<f:selectItem itemValue="8" itemLabel="eight" />
						<f:selectItem itemValue="9" itemLabel="nine" />
					</h:selectmany_listbox></td>
				</tr>
				<tr>
					<td>Multi-select listmodel:</td>
					<td><h:selectmany_listbox id="listmodel"
						value="#{LoginBean.currentOptions}">
						<f:selectItems id="listmodelitems"
							value="#{LoginBean.options}" />
					</h:selectmany_listbox></td>
				</tr>

                                <tr>
					<td>Multi-select listmodel with options of type java.lang.Long:</td>
					<td><h:selectmany_listbox id="longlistmodel"
						value="#{LoginBean.currentLongOptions}">
						<f:selectItems id="longlistmodelitems"
							value="#{LoginBean.longList}" />
					</h:selectmany_listbox></td>
				</tr>

                                <tr>
					<td>Multi-select listmodel Group:</td>
					<td><h:selectmany_listbox id="listmodelGroup"
						value="#{LoginBean.currentOptions}">
						<f:selectItems id="listmodelitemsGroup"
							value="#{LoginBean.optionsGroup}" />
					</h:selectmany_listbox></td>
				</tr>
