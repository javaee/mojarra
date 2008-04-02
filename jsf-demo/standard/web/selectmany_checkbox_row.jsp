<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

				<tr>
					<td>Multi-select checkbox:</td>
					<td><h:selectmany_checkboxlist id="ManyApples3">
						<h:selectitem itemValue="0" itemLabel="zero" />
						<h:selectitem itemValue="1" itemLabel="one" />
						<h:selectitem itemValue="2" itemLabel="two" />
						<h:selectitem itemValue="3" itemLabel="three" />
						<h:selectitem itemValue="4" itemLabel="four" />
						<h:selectitem itemValue="5" itemLabel="five" />
						<h:selectitem itemValue="6" itemLabel="six" />
						<h:selectitem itemValue="7" itemLabel="seven" />
						<h:selectitem itemValue="8" itemLabel="eight" />
						<h:selectitem itemValue="9" itemLabel="nine" />
					</h:selectmany_checkboxlist></td>
				</tr>
				<tr>
					<td>Multi-select checklistmodel:</td>
					<td><h:selectmany_checkboxlist id="checklistmodel"
						valueRef="LoginBean.currentOptions">
						<h:selectitems id="checklistmodelitems"
							valueRef="LoginBean.options" />
					</h:selectmany_checkboxlist></td>
				</tr>

                                <tr>
					<td>Multi-select checklistmodel with options of the type java.lang.Long:</td>
					<td><h:selectmany_checkboxlist id="checkLonglistmodel"
						valueRef="LoginBean.currentLongOptions">
                                                 
						<h:selectitems id="checkLonglistmodelitems"
							valueRef="LoginBean.longList" />
					</h:selectmany_checkboxlist></td>
				</tr>
