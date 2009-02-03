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

	<TD>

	      <h:inputText id="userName" value="Default_username" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:inputSecret id="password" value="Default_password" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:commandButton id="login" value="Login" 
				    action="login"/>

	</TD>

      </TR>


      <TR>

	<TD>

	      <h:commandButton id="imageButton" image="duke.gif"
				    action="login"/>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:commandLink id="link"
				       value="link text"/>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:commandLink id="imageLink"

                       value="duke.gif"/>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:outputText id="userLabel" value="Output Text" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:selectManyCheckbox id="validUser" label="Valid User"/>

    </TD>

      </TR>

      <TR>

	<TD>

	      <h:selectOneListbox id="appleQuantity">

		<f:selectItem  value="0" itemLabel="0"/>
		<f:selectItem  value="1" itemLabel="1"/>
		<f:selectItem  value="2" itemLabel="2"/>
		<f:selectItem  value="3" itemLabel="3"/>
		<f:selectItem  value="4" itemLabel="4"/>
		<f:selectItem  value="5" itemLabel="5"/>
		<f:selectItem  value="6" itemLabel="6"/>
		<f:selectItem  value="7" itemLabel="7"/>
		<f:selectItem  value="8" itemLabel="8"/>
		<f:selectItem  value="9" itemLabel="9"/>

	      </h:selectOneListbox>

              Option List

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:selectOneRadio id="shipType">

		<f:selectItem value="nextDay" itemLabel="Next Day" />
		<f:selectItem value="nextWeek" itemLabel="Next Week" />
		<f:selectItem value="nextMonth" itemLabel="Next Month" />

              </h:selectOneRadio>

	</TD>

      </TR>

      <TR>

	<TD>
		<h:selectOneRadio id="verticalRadio" border="1">

            <f:selectItem value="nextDay" itemLabel="Next Day"/>
		<f:selectItem value="nextWeek" itemLabel="Next Week"  />
		<f:selectItem value="nextMonth" itemLabel="Next Month" />

                </h:selectOneRadio>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:inputTextarea id="address" value="Hi There"
                                        rows="10" cols="10"/>

	</TD>

      </TR>

  <TABLE>

</h:form>
</f:view>

    </BODY>
</HTML>
