<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

				<tr>
					<td>Multi-select listbox:</td>
					<td><h:selectmany_listbox id="ManyApples2">
						<h:selectitem itemValue="0" itemLabel="zero" />
						<h:selectitem itemValue="1" itemLabel="one" />
						<h:selectitem itemValue="2" itemLabel="two" />
						<h:selectitem itemValue="3" itemLabel="three" />
						<h:selectitem itemValue="4" itemLabel="four"  />
						<h:selectitem itemValue="5" itemLabel="five" />
						<h:selectitem itemValue="6" itemLabel="six" />
						<h:selectitem itemValue="7" itemLabel="seven" />
						<h:selectitem itemValue="8" itemLabel="eight" />
						<h:selectitem itemValue="9" itemLabel="nine" />
					</h:selectmany_listbox></td>
				</tr>
				<tr>
					<td>Multi-select listmodel:</td>
					<td><h:selectmany_listbox id="listmodel"
						valueRef="LoginBean.currentOptions">
						<h:selectitems id="listmodelitems"
							valueRef="LoginBean.options" />
					</h:selectmany_listbox></td>
				</tr>

                                <tr>
					<td>Multi-select listmodel with options of type java.lang.Long:</td>
					<td><h:selectmany_listbox id="longlistmodel"
						valueRef="LoginBean.currentLongOptions" converter="Number">
                                                <f:attribute name="numberStyle" value="INTEGER"/>
						<h:selectitems id="longlistmodelitems"
							valueRef="LoginBean.longList" />
					</h:selectmany_listbox></td>
				</tr>
