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

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html><head><title>test From Michael Youngstrom</title></head>
<body>
<p>Test from issue <a href="https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=159">159</a>.</p>


<p>When nestedTest is loaded you are presented with 4 links.  If you click on "test
bean: 4" then "You clicked on link: 4" is correctly printed to the console. 
However, if you click on "test bean: 1" then "You clicked on link: 3" is
INCORRECTLY displayed.</p>

<p>The problem is there are actually 2 DataModels associated with the datatable
with var="nestedBean".  But when the Action Event is fired there is no way for
the UIData to switch to the correct DataModel.</p>

<f:view>
	<h:form>
		<h:dataTable value="#{nestedTestList}" var="nestedList">
			<h:column>
				<h:dataTable value="#{nestedList}" var="nestedBean">
					<h:column>
						<h:commandLink actionListener="#{nestedBean.executeLink}"><h:outputText
value="test bean: #{nestedBean.id}"/></h:commandLink>
					</h:column>
				</h:dataTable>
			</h:column>
		</h:dataTable>
	</h:form>
	
	<p><h:outputText value="#{whichLink}" /></p>
	
</f:view>

</body>
</html>
