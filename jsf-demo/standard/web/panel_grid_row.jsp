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

