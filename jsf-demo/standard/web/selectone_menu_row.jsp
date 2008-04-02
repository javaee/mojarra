<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

				<tr>
					<td>Single-select menu:</td>
					<td><h:selectone_menu id="HockeyHeroes">
						<h:selectitem itemValue="10" itemLabel="Guy Lafleur" />
						<h:selectitem itemValue="99" itemLabel="Wayne Gretzky" />
						<h:selectitem itemValue="4" itemLabel="Bobby Orr"  />
						<h:selectitem itemValue="2" itemLabel="Brad Park" />
						<h:selectitem itemValue="9" itemLabel="Gordie Howe" />
					</h:selectone_menu></td>
				</tr>
				<tr>
					<td>Single-select menu - modelType String:</td>
					<td><h:selectone_menu id="oneMenumodel" size="3">
						<h:selectitems id="oneMenumodelItems"
							valueRef="LoginBean.options" />
					</h:selectone_menu></td>
				</tr>

                                <tr>
					<td>Single-select menumodel - modelType Boolean:</td>
					<td><h:selectone_menu id="oneLongMenumodel" size="3" 
                                             valueRef="LoginBean.currentBooleanOption">
						<h:selectitems id="oneLongMenumodelItems"
							valueRef="LoginBean.booleanList" />
					</h:selectone_menu></td>
				</tr>
