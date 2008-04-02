<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

				<tr>
					<td>Multi-select menu:</td>
					<td><h:selectmany_menu id="ManyApples" size="7">
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
					</h:selectmany_menu></td>
				</tr>
				<tr>
					<td>Multi-select menumodel:</td>
					<td><h:selectmany_menu id="menumodel" size="3">
						<f:selectitems id="menumodelitems"
							valueRef="LoginBean.options" />
					</h:selectmany_menu></td>
				</tr>

                                <tr>
					<td>Multi-select menumodel with options of the type java.lang.Long:</td>
					<td><h:selectmany_menu id="menuLongmodel" size="3"
                                               valueRef="LoginBean.currentLongOptions">
                                               
						<f:selectitems id="menumodelonglitems"
							valueRef="LoginBean.longList" />
					</h:selectmany_menu></td>
				</tr>

                                <tr>
					<td>Multi-select menumodel Group:</td>
					<td><h:selectmany_menu id="listGroup" size="3"
                                                valueRef="LoginBean.currentOptions">
						<f:selectitems id="menumodelitemsGroup"
							valueRef="LoginBean.optionsGroup" />
					</h:selectmany_menu></td>
				</tr>
