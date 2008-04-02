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

             <td>

                 <h:outputText id="shipmentLabel" 
                     value="Radio with hard-coded options laid out horizontally" />
             </td>


             <td>
                 <h:selectOneRadio id="shipType" layout="lineDirection" 
                    value="nextDay" tabindex="30" title="shipType">
                <f:selectItem itemValue="nextDay" itemLabel="Next Day"/>
                <f:selectItem itemValue="nextWeek" itemLabel="Next Week" />
                <f:selectItem itemValue="nextMonth" itemLabel="Next Month"/>
                </h:selectOneRadio>

             </td>

            </tr>

            <tr>

             <td>
                 <h:outputText id="verticalLabel" 
                     value="Radio with hard-coded options laid out vertically" />

             </td>


             <td>
                <h:selectOneRadio id="verticalRadio" layout="pageDirection" border="1" >
                  <f:selectItem itemValue="nextDay" itemLabel="Next Day"/>
                  <f:selectItem itemValue="nextWeek" itemLabel="Next Week"  />
                  <f:selectItem itemValue="nextMonth" itemLabel="Next Month" />

                </h:selectOneRadio>

             </td>

            </tr>

             <tr>

             <td>
                 <h:outputText id="modelLabel" value="Radio with options from model " />

             </td>


             <td>
                 <h:selectOneRadio id="radioFromModel"
                       value="#{LoginBean.currentOption}"
                       title="options come from model">

                 <f:selectItems id="radioOptions" value="#{LoginBean.options}"/>
                </h:selectOneRadio>

             </td>

            </tr>

             <tr>

             <td>
                 <h:outputText id="modelLongLabel" value="Radio with options 
                     from model of type java.lang.Long" />

             </td>


             <td>
                 <h:selectOneRadio id="radioLongOptions"
                       value="#{LoginBean.currentLongOption}"
                       title="options come from model">
                 
                <f:selectItems id="longItemOptions" value="#{LoginBean.longList}"/>
                </h:selectOneRadio>

             </td>

            </tr>

            <tr>
	         <td>Single-select radiomodel - modelType Boolean:</td>
	         <td><h:selectOneRadio id="oneLongradiomodel"
                            value="#{LoginBean.currentBooleanOption}">
                           
		         <f:selectItems id="oneRadiomodelitems"
				value="#{LoginBean.booleanList}" />
		    </h:selectOneRadio></td>
	   </tr>

           <tr>

             <td>
                 <h:outputText value="Radio with optionGroups from model " />

             </td>


             <td>
                 <h:selectOneRadio id="radioFromModelGroup"
                       value="#{LoginBean.currentOption}"
                       title="options come from model">

                 <f:selectItems id="radioOptionsGroup" value="#{LoginBean.optionsGroup}"/>
                </h:selectOneRadio>

             </td>

            </tr>

<tr>

  <td>
    <h:outputText             id="disabledsRadioLabel"
                           value="Radio Buttons with even numbered options disabled"/>
  </td>

  <td>
    <h:selectOneRadio         id="disabledsRadio"
                           value="#{SelectItemsData.disabled}">
      <f:selectItems       value="#{SelectItemsData.disableds}"/>
    </h:selectOneRadio>
  </td>

</tr>


<tr>

  <td>
    <h:outputText             id="nestedsRadioLabel" 
                           value="Radio Buttons with nested options"/>
  </td>

  <td>
    <h:selectOneRadio         id="nestedsRadio" layout="pageDirection"
                           value="#{SelectItemsData.nested}">
      <f:selectItems       value="#{SelectItemsData.nesteds}"/>
    </h:selectOneRadio>
  </td>

</tr>
