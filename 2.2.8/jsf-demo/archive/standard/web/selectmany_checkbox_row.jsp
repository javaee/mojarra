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
						<td>Multi-select checkbox:</td>
						<td><h:selectManyCheckbox id="ManyApples3">
							<f:selectItem itemValue="0" itemLabel="zero" />
							<f:selectItem itemValue="1" itemLabel="one" />
							<f:selectItem itemValue="2" itemLabel="two" />
							<f:selectItem itemValue="3" itemLabel="three" />
							<f:selectItem itemValue="4" itemLabel="four" />
							<f:selectItem itemValue="5" itemLabel="five" />
							<f:selectItem itemValue="6" itemLabel="six" />
							<f:selectItem itemValue="7" itemLabel="seven" />
							<f:selectItem itemValue="8" itemLabel="eight" />
							<f:selectItem itemValue="9" itemLabel="nine" />
						</h:selectManyCheckbox></td>
                                                <td><h:message id="Error1" for="ManyApples3"/></td>
					</tr>
					<tr>
						<td>Multi-select checkbox [Vertical]:</td>
						<td><h:selectManyCheckbox id="ManyApples4" 
						  layout="pageDirection">
							<f:selectItem itemValue="0" itemLabel="zero" />
							<f:selectItem itemValue="1" itemLabel="one" />
							<f:selectItem itemValue="2" itemLabel="two" />
							<f:selectItem itemValue="3" itemLabel="three" />
							<f:selectItem itemValue="4" itemLabel="four" />
							<f:selectItem itemValue="5" itemLabel="five" />
							<f:selectItem itemValue="6" itemLabel="six" />
							<f:selectItem itemValue="7" itemLabel="seven" />
							<f:selectItem itemValue="8" itemLabel="eight" />
							<f:selectItem itemValue="9" itemLabel="nine" />
						</h:selectManyCheckbox></td>
                                                <td><h:message id="Error2" for="ManyApples4"/></td>
					</tr>
					<tr>
						<td>Multi-select checklistmodel:</td>
						<td><h:selectManyCheckbox id="checklistmodel"
							value="#{LoginBean.currentOptions}">
							<f:selectItems id="checklistmodelitems"
								value="#{LoginBean.options}" />
						</h:selectManyCheckbox></td>
                                                <td><h:message id="Error3" for="checklistmodel"/></td>
					</tr>


					<tr>
						<td>Multi-select checklistmodel with options of the type java.lang.Long:</td>
						<td><h:selectManyCheckbox id="checkLonglistmodel"
							value="#{LoginBean.currentLongOptions}">
                                                 
							<f:selectItems id="checkLonglistmodelitems"
								value="#{LoginBean.longList}" />
						</h:selectManyCheckbox></td>
                                                <td><h:message id="Error4" for="checkLonglistmodel"/></td>
					</tr>


<tr>

  <td>
    <h:outputText             id="checklistmodelGroupLabel"
                           value="Multi-select checkbox list:"/>
  </td>

  <td>
    <h:selectManyCheckbox     id="checklistmodelGroup"
                           value="#{LoginBean.currentOptions}">
      <f:selectItems          id="checklistmodelitemsGroup"
                           value="#{LoginBean.optionsGroup}" />
    </h:selectManyCheckbox>
  </td>

  <td>
    <h:message                id="Error5" for="checklistmodelGroup"/>
  </td>

</tr>

<tr>

  <td>
    <h:outputText             id="disabledsCheckboxLabel"
                           value="Checkboxes with even numbered options disabled"/>
  </td>

  <td>
    <h:selectManyCheckbox     id="disabledsCheckbox"
                           value="#{SelectItemsData.disabledSelected}">
      <f:selectItems       value="#{SelectItemsData.disableds}"/>
    </h:selectManyCheckbox>
  </td>

  <td>
    <h:message               for="disabledsCheckbox"/>
  </td>

</tr>


<tr>

  <td>
    <h:outputText             id="nestedsCheckboxLabel"
                           value="Checkboxes with nested options"/>
  </td>

  <td>
    <h:selectManyCheckbox     id="nestedsCheckbox" layout="pageDirection"
                           value="#{SelectItemsData.nestedSelected}">
      <f:selectItems       value="#{SelectItemsData.nesteds}"/>
    </h:selectManyCheckbox>
  </td>

  <td>
    <h:message               for="nestedsCheckbox"/>
  </td>

</tr>
