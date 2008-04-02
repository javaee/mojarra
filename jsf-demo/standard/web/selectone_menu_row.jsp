<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
<tr>

  <td>
    <h:outputText             id="disabledsMenuLabel"
                           value="Menu with even numbered options disabled"/>
  </td>

  <td>
    <h:selectOneMenu          id="disabledsMenu"
                           value="#{SelectItemsData.disabled}">
      <f:selectItems       value="#{SelectItemsData.disableds}"/>
    </h:selectOneMenu>
  </td>

</tr>


<tr>

  <td>
    <h:outputText             id="nestedsMenuLabel"
                           value="Menu with nested options"/>
  </td>

  <td>
    <h:selectOneMenu          id="nestedsMenu"
                           value="#{SelectItemsData.nested}">
      <f:selectItems       value="#{SelectItemsData.nesteds}"/>
    </h:selectOneMenu>
  </td>

</tr>
