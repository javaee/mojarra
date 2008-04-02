<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

				<tr>
					<td>Multi-select listbox:</td>
					<td><h:selectmany_listbox id="ManyApples2">
						<f:selectitem itemValue="0" itemLabel="zero" />
						<f:selectitem itemValue="1" itemLabel="one" />
						<f:selectitem itemValue="2" itemLabel="two" />
						<f:selectitem itemValue="3" itemLabel="three" />
						<f:selectitem itemValue="4" itemLabel="four"  />
						<f:selectitem itemValue="5" itemLabel="five" />
						<f:selectitem itemValue="6" itemLabel="six" />
						<f:selectitem itemValue="7" itemLabel="seven" />
						<f:selectitem itemValue="8" itemLabel="eight" />
						<f:selectitem itemValue="9" itemLabel="nine" />
					</h:selectmany_listbox></td>
				</tr>
				<tr>
					<td>Multi-select listmodel:</td>
					<td><h:selectmany_listbox id="listmodel"
						valueRef="LoginBean.currentOptions">
						<f:selectitems id="listmodelitems"
							valueRef="LoginBean.options" />
					</h:selectmany_listbox></td>
				</tr>

                                <tr>
					<td>Multi-select listmodel with options of type java.lang.Long:</td>
					<td><h:selectmany_listbox id="longlistmodel"
						valueRef="LoginBean.currentLongOptions">
						<f:selectitems id="longlistmodelitems"
							valueRef="LoginBean.longList" />
					</h:selectmany_listbox></td>
				</tr>

                                <tr>
					<td>Multi-select listmodel Group:</td>
					<td><h:selectmany_listbox id="listmodelGroup"
						valueRef="LoginBean.currentOptions">
						<f:selectitems id="listmodelitemsGroup"
							valueRef="LoginBean.optionsGroup" />
					</h:selectmany_listbox></td>
				</tr>
