<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

--%>



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
