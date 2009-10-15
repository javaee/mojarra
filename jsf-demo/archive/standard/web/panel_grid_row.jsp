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

           <tr>

             <td>
               <h:outputText id="foo"
                     value="Grid with hardcoded values"/>
             </td>
                   

             <td>

               <h:form id="form1">

                  <h:panelGrid  columns="3" 
                                  border="1"
                             cellpadding="3"
                             cellspacing="3"
                                 summary="Grid with hardcoded HTML styles"
                                   title="Grid with hardcoded HTML styles" >

                    <f:facet        name="header">
                      <h:panelGroup>
                        <h:outputText
                                   value="Logon "/>
                        <h:outputText
                                   value="Form"/>
                      </h:panelGroup>
                    </f:facet>

                    <h:outputText value="Username:"/>

                    <h:inputText     id="username1"
                                   value="username"
                                required="true"/>

                    <h:message for="form1:username1"/>

                    <h:outputText value="Password:"/>

                    <h:inputSecret   id="password1"
                                   value="password"
                                required="true"/>

                    <h:message for="form1:password1"/>

                    <h:commandButton id="submit1"
                               action="#{LoginForm.login}"
                                    type="SUBMIT"
                                   value="Login"/>

                    <h:commandButton id="reset1"
                                    type="RESET"
                                   value="Reset"/>

                    <h:outputText value=""/>

                    <f:facet         name="footer">
                      <h:outputText
                                    value="Enter username and password to login"/>
                    </f:facet>

                </h:panelGrid>
 
               </h:form>

             </td>

            </tr>

            <tr>

              <td>
                 <h:outputText id="panel4Label"
                       value="Grid with CSS stylesheets"/>
             </td>


             <td>
             
               <h:form id="form2">

                  <h:panelGrid  columns="3"
                             footerClass="form-footer"
                             headerClass="form-header"
                              styleClass="form-background"
                           columnClasses="form-prompt,form-field"
                                 summary="Grid with CSS stylesheet styles"
                                   title="Grid with CSS stylesheet styles" >

                    <f:facet        name="header">
                      <h:panelGroup>
                        <h:outputText
                                   value="Logon "/>
                        <h:outputText
                                   value="Form"/>
                      </h:panelGroup>
                    </f:facet>

                    <h:outputText value="Username:"/>

                    <h:inputText     id="username2"
                                 binding="#{LoginForm.username}"
                                required="true"/>

                    <h:message for="form2:username2"/>

                    <h:outputText value="Password:"/>

                    <h:inputSecret   id="password2"
                                 binding="#{LoginForm.password}"
                                required="true"/>

                    <h:message for="form2:password2"/>

                    <h:commandButton id="submit2"
                               action="#{LoginForm.login}"
                                    type="SUBMIT"
                                   value="Login"/>

                    <h:commandButton id="reset2"
                                    type="RESET"
                                   value="Reset"/>

                    <h:outputText value=""/>

                    <f:facet         name="footer">
                      <h:panelGroup   id="footerGroup">
                        <h:outputText
                                    value="Enter username and password "/>
                        <h:outputText
                                    value="to login"/>
                      </h:panelGroup>
                    </f:facet>

                  </h:panelGrid>
 
                </h:form>

              </td>

            </tr>

