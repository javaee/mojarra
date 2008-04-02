<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

				<tr>
					<td>Multi-select checkbox:</td>
					<td><h:selectmany_checkboxlist id="ManyApples3">
						<f:selectitem itemValue="0" itemLabel="zero" />
						<f:selectitem itemValue="1" itemLabel="one" />
						<f:selectitem itemValue="2" itemLabel="two" />
						<f:selectitem itemValue="3" itemLabel="three" />
						<f:selectitem itemValue="4" itemLabel="four" />
						<f:selectitem itemValue="5" itemLabel="five" />
						<f:selectitem itemValue="6" itemLabel="six" />
						<f:selectitem itemValue="7" itemLabel="seven" />
						<f:selectitem itemValue="8" itemLabel="eight" />
						<f:selectitem itemValue="9" itemLabel="nine" />
					</h:selectmany_checkboxlist></td>
				</tr>
				<tr>
					<td>Multi-select checkbox [Vertical]:</td>
					<td><h:selectmany_checkboxlist id="ManyApples4" 
                                          layout="PAGE_DIRECTION">
						<f:selectitem itemValue="0" itemLabel="zero" />
						<f:selectitem itemValue="1" itemLabel="one" />
						<f:selectitem itemValue="2" itemLabel="two" />
						<f:selectitem itemValue="3" itemLabel="three" />
						<f:selectitem itemValue="4" itemLabel="four" />
						<f:selectitem itemValue="5" itemLabel="five" />
						<f:selectitem itemValue="6" itemLabel="six" />
						<f:selectitem itemValue="7" itemLabel="seven" />
						<f:selectitem itemValue="8" itemLabel="eight" />
						<f:selectitem itemValue="9" itemLabel="nine" />
					</h:selectmany_checkboxlist></td>
				</tr>
				<tr>
					<td>Multi-select checklistmodel:</td>
					<td><h:selectmany_checkboxlist id="checklistmodel"
						valueRef="LoginBean.currentOptions">
						<f:selectitems id="checklistmodelitems"
							valueRef="LoginBean.options" />
					</h:selectmany_checkboxlist></td>
				</tr>

                                <tr>
					<td>Multi-select checklistmodel with options of the type java.lang.Long:</td>
					<td><h:selectmany_checkboxlist id="checkLonglistmodel"
						valueRef="LoginBean.currentLongOptions">
                                                 
						<f:selectitems id="checkLonglistmodelitems"
							valueRef="LoginBean.longList" />
					</h:selectmany_checkboxlist></td>
				</tr>
