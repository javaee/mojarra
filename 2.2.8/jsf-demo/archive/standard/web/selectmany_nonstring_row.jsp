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
  <td>Multi-select with Map values</td>
  <td>
    <h:selectManyListbox
                       id="nonstringStringMap"
                    value="#{SelectManyBean.selectedMapValues}">
      <f:selectItems
                    value="#{SelectManyBean.stringValuesMap}"/>
    </h:selectManyListbox>
  </td>
  <td>
    <h:message        for="nonstringStringMap"/>
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


