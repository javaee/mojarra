<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
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

<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

    <BODY>
        <H3> JSF Basic Components Test Page </H3>

<f:view>
<h:form id="basicForm">

  <TABLE BORDER="1">

      <TR>
      <td>
                    <h:inputText id="userName" 
                                     value="JavaServerFaces" >
		     <f:validateLength minimum="6" maximum="10"/>
		     <f:validateRequired/>
                   </h:inputText>

              </td>


	<TD>

	      <h:commandLink id="link" href="hello.html"
                           styleClass="hyperlinkClass"
				       value="link text"/>

	</TD>

      </TR>

      <TR>

	<TD>

            <h:selectManyCheckbox id="validUser" 
                   styleClass="selectbooleanClass"/>
	</TD>

      </TR>

      <TR>

	<TD>

	     <h:selectOneListbox id="appleQuantity" 
                     title="Select Quantity"
                     accesskey="N" tabindex="20" >

                <f:selectItem  disabled="true" itemValue="0" itemLabel="0"/>
                <f:selectItem  itemValue="4" itemLabel="4" title="Four" selected="true"/>
                <f:selectItem  itemValue="9" itemLabel="9" title="nine" />

              </h:selectOneListbox>

	</TD>

      </TR>

					<TD><h:selectManyMenu id="ManyApples">
						<f:selectItem itemValue="4" itemLabel="four" selected="true" />
						<f:selectItem itemValue="6" itemLabel="six" />
						<f:selectItem itemValue="7" itemLabel="seven" selected="true" />
					</h:selectManyMenu></TD>

</tr>

  </TABLE>

</h:form>
</f:view>

    </BODY>
</HTML>
