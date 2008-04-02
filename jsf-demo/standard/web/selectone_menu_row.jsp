<!--
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
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
