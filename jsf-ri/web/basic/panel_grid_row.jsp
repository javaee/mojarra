           <tr>

             <td>

               <h:output_text id="panel1_label" 
                     value="Grid with hardcoded data and HTML attributes. "/>

             </td>
                   

             <td>

                 <h:panel_grid id="logonPanel1" columns="2" 
                    border="1" cellpadding="3" cellspacing="3"
                     summary="Grid with hardcoded data"
                     title="Grid with hardcoded data" >
                     <!-- Panel data elements -->

                    <h:output_text id="text1" value="Username:"/>

                    <h:input_text id="username1" value="JavaServerFaces" />

                    <h:output_text id="text2" value="Password:"/>

                    <h:input_secret id="password1" />

                    <h:command_button id="submit1" type="SUBMIT"
                        commandName="submit" label="Login">
                    </h:command_button>

                    <h:command_button id="reset1" type="RESET" 
                        commandName="reset" label="Reset">
                    </h:command_button>

                </h:panel_grid>
 
             </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="panel2_label"
                     value="Grid with model data and stylesheets "/>

             </td>


             <td>
             
             <h:panel_grid id="logonPanel2" columns="2"
               footerClass="form-footer"
               headerClass="form-header"
                panelClass="form-background"
             columnClasses="form-prompt,form-field">

            <h:panel_group id="form_header2">
              <h:output_text id="A2" value="Logon&nbsp;"/>
              <h:output_text id="B2" value="Form"/>
            </h:panel_group>

            <!-- Panel data elements -->

              <h:output_text id="text3" value="Username:"/>

            <h:input_text id="username2"
              modelReference="LoginBean.userName"/>

             <h:output_text id="text4" value="Password:"/>

            <h:input_secret id="password2"
                modelReference="LoginBean.password"/>

            <h:command_button id="submit2" type="SUBMIT"
                commandName="submit" label="Login">
            </h:command_button>

            <h:command_button id="reset2" type="RESET" commandName="reset"
                label="Reset">
            </h:command_button>

            <!-- Panel footer element -->

                    <h:output_text id="form_footer"
                           value="Enter username and password to Login" />
         </h:panel_grid>
             </td>

            </tr>

