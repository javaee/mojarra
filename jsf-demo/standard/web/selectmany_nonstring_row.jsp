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
  <td>Multi-select with int[] values</td>
  <td>
    <h:selectManyListbox
                       id="nonstringIntArray"
                    value="#{SelectManyBean.intValuesArray}">
      <f:selectItems
                    value="#{SelectManyBean.intOptions}"/>
    </h:selectManyListbox>
  </td>
  <td>
    <h:message        for="nonstringIntArray"/>
  </td>
</tr>


<tr>
  <td>Multi-select with Integer[] values</td>
  <td>
    <h:selectManyListbox
                       id="nonstringIntegerArray"
                    value="#{SelectManyBean.integerValuesArray}">
      <f:selectItems
                    value="#{SelectManyBean.integerOptions}"/>
    </h:selectManyListbox>
  </td>
  <td>
    <h:message        for="nonstringIntegerArray"/>
  </td>
</tr>


<%-- Fails the validate() test for valid values
<tr>
  <td>Multi-select with List-of-Integer values</td>
  <td>
    <h:selectManyListbox
                       id="nonstringIntegerList"
                    value="#{SelectManyBean.integerValuesList}">
      <f:selectItems
                    value="#{SelectManyBean.integerOptions}"/>
    </h:selectManyListbox>
  </td>
  <td>
    <h:message        for="nonstringIntegerList"/>
  </td>
</tr>
--%>


<tr>
  <td>Multi-select with String[] values</td>
  <td>
    <h:selectManyListbox
                       id="nonstringStringArray"
                    value="#{SelectManyBean.stringValuesArray}">
      <f:selectItems
                    value="#{SelectManyBean.stringOptions}"/>
    </h:selectManyListbox>
  </td>
  <td>
    <h:message        for="nonstringStringArray"/>
  </td>
</tr>


<tr>
  <td>Multi-select with List-of-String values</td>
  <td>
    <h:selectManyListbox
                       id="nonstringStringList"
                    value="#{SelectManyBean.stringValuesList}">
      <f:selectItems
                    value="#{SelectManyBean.stringOptions}"/>
    </h:selectManyListbox>
  </td>
  <td>
    <h:message        for="nonstringStringList"/>
  </td>
</tr>


<tr>
  <td>Multi-select with array of registered object values</td>
  <td>
    <h:selectManyListbox
                       id="registeredArray"
                    value="#{SelectManyBean.registeredArray}">
      <f:selectItems
                    value="#{SelectManyBean.registeredOptions}"/>
    </h:selectManyListbox>
  </td>
  <td>
    <h:message        for="registeredArray"/>
  </td>
</tr>


<%-- Fails with conversion errors on form submit
<tr>
  <td>Multi-select with array of unregistered object values</td>
  <td>
    <h:selectManyListbox
                       id="unregisteredArray"
                    value="#{SelectManyBean.unregisteredArray}">
      <f:selectItems
                    value="#{SelectManyBean.unregisteredOptions}"/>
    </h:selectManyListbox>
  </td>
  <td>
    <h:message        for="unregisteredArray"/>
  </td>
</tr>
--%>


