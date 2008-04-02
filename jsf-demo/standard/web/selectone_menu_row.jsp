<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

				<tr>
					<td>Single-select menu:</td>
					<td><h:selectOneMenu id="HockeyHeroes">
						<f:selectItem itemValue="10" itemLabel="Guy Lafleur" />
						<f:selectItem itemValue="99" itemLabel="Wayne Gretzky" />
						<f:selectItem itemValue="4" itemLabel="Bobby Orr"  />
						<f:selectItem itemValue="2" itemLabel="Brad Park" />
						<f:selectItem itemValue="9" itemLabel="Gordie Howe" />
					</h:selectOneMenu></td>
				</tr>
				<tr>
					<td>Single-select menu - modelType String:</td>
					<td><h:selectOneMenu id="oneMenumodel">
						<f:selectItems id="oneMenumodelItems"
							value="#{LoginBean.options}" />
					</h:selectOneMenu></td>
				</tr>

                                <tr>
					<td>Single-select menumodel - modelType Boolean:</td>
					<td><h:selectOneMenu id="oneLongMenumodel"  
                                             value="#{LoginBean.currentBooleanOption}">
						<f:selectItems id="oneLongMenumodelItems"
							value="#{LoginBean.booleanList}" />
					</h:selectOneMenu></td>
				</tr>
