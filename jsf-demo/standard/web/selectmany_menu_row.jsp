<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

				<tr>
					<td>Multi-select menu:</td>
					<td><h:selectmany_menu id="ManyApples" size="7">
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
					</h:selectmany_menu></td>
				</tr>
				<tr>
					<td>Multi-select menumodel:</td>
					<td><h:selectmany_menu id="menumodel" size="3">
						<h:selectitems id="menumodelitems"
							valueRef="LoginBean.options" />
					</h:selectmany_menu></td>
				</tr>

                                <tr>
					<td>Multi-select menumodel with options of the type java.lang.Long:</td>
					<td><h:selectmany_menu id="menuLongmodel" size="3"
                                               valueRef="LoginBean.currentLongOptions" converter="Number">
                                               
						<h:selectitems id="menumodelonglitems"
							valueRef="LoginBean.longList" />
					</h:selectmany_menu></td>
				</tr>
