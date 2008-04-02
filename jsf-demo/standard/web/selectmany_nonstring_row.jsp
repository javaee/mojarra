<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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

