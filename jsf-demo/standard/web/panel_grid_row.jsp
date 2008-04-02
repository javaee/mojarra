<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>
               <h:output_text id="foo"
                     value="Grid with hardcoded values"/>
             </td>
                   

             <td>

               <h:form id="form1" formName="form1">

                  <h:panel_grid  columns="3" 
                                  border="1"
                             cellpadding="3"
                             cellspacing="3"
                                 summary="Grid with hardcoded HTML styles"
                                   title="Grid with hardcoded HTML styles" >

                    <f:facet        name="header">
                      <h:panel_group>
                        <h:output_text
                                   value="Logon "/>
                        <h:output_text
                                   value="Form"/>
                      </h:panel_group>
                    </f:facet>

                    <h:output_text value="Username:"/>

                    <h:input_text     id="username1"
                                   value="username"
                                required="true"/>

                    <h:output_errors for="form1:username1"/>

                    <h:output_text value="Password:"/>

                    <h:input_secret   id="password1"
                                   value="password"
                                required="true"/>

                    <h:output_errors for="form1:password1"/>

                    <h:command_button id="submit1"
                               actionRef="LoginForm.login"
                                    type="SUBMIT"
                                   value="Login"/>

                    <h:command_button id="reset1"
                                    type="RESET"
                                   value="Reset"/>

                    <h:output_text value=""/>

                    <f:facet         name="footer">
                      <h:output_text
                                    value="Enter username and password to login"/>
                    </f:facet>

                </h:panel_grid>
 
               </h:form>

             </td>

            </tr>

            <tr>

              <td>
                 <h:output_text id="panel4Label"
                       value="Grid with CSS stylesheets"/>
             </td>


             <td>
             
               <h:form id="form2" formName="form2">

                  <h:panel_grid  columns="3"
                             footerClass="form-footer"
                             headerClass="form-header"
                              styleClass="form-background"
                           columnClasses="form-prompt,form-field"
                                 summary="Grid with CSS stylesheet styles"
                                   title="Grid with CSS stylesheet styles" >

                    <f:facet        name="header">
                      <h:panel_group>
                        <h:output_text
                                   value="Logon "/>
                        <h:output_text
                                   value="Form"/>
                      </h:panel_group>
                    </f:facet>

                    <h:output_text value="Username:"/>

                    <h:input_text     id="username2"
                            componentRef="LoginForm.username"
                                required="true"/>

                    <h:output_errors for="form2:username2"/>

                    <h:output_text value="Password:"/>

                    <h:input_secret   id="password2"
                            componentRef="LoginForm.password"
                                required="true"/>

                    <h:output_errors for="form2:password2"/>

                    <h:command_button id="submit2"
                               actionRef="LoginForm.login"
                                    type="SUBMIT"
                                   value="Login"/>

                    <h:command_button id="reset2"
                                    type="RESET"
                                   value="Reset"/>

                    <h:output_text value=""/>

                    <f:facet         name="footer">
                      <h:output_text
                                    value="Enter username and password to login"/>
                    </f:facet>

                  </h:panel_grid>
 
                </h:form>

              </td>

            </tr>

