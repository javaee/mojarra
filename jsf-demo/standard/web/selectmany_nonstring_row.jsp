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


