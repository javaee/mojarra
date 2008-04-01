           <tr>

             <td>

               <faces:output_text id="panel1_label" 
                     value="Grid with hardcoded data and HTML attributes. "/>

             </td>
                   

             <td>

                 <faces:panel_grid id="logonPanel1" columns="2" 
                    border="1" cellpadding="3" cellspacing="3"
                     summary="Grid with hardcoded data"
                     title="Grid with hardcoded data" >
                     <!-- Panel data elements -->

                    <faces:output_text id="text1" value="Username:"/>

                    <faces:input_text id="username1" value="JavaServerFaces" />

                    <faces:output_text id="text2" value="Password:"/>

                    <faces:input_secret id="password1" />

                    <faces:command_button id="submit1" type="SUBMIT"
                             commandName="submit">
                      <faces:output_text id="submit1_label" value="Login" />
                    </faces:command_button>

                    <faces:command_button id="reset1" type="RESET" commandName="reset">
                      <faces:output_text id="reset1_label" value="Reset" />
                    </faces:command_button>

                </faces:panel_grid>
 
             </td>

            </tr>

            <tr>

             <td>

               <faces:output_text id="panel2_label"
                     value="Grid with model data and stylesheets "/>

             </td>


             <td>
             
             <faces:panel_grid id="logonPanel2" columns="2"
               footerClass="form-footer"
               headerClass="form-header"
                panelClass="form-background"
             columnClasses="form-prompt,form-field">

            <faces:panel_group id="form_header2">
              <faces:output_text id="A2" value="Logon&nbsp;"/>
              <faces:output_text id="B2" value="Form"/>
            </faces:panel_group>

            <!-- Panel data elements -->

              <faces:output_text id="text3" value="Username:"/>

            <faces:input_text id="username2"
              modelReference="LoginBean.userName"/>

             <faces:output_text id="text4" value="Password:"/>

            <faces:input_secret id="password2"
                modelReference="LoginBean.password"/>

            <faces:command_button id="submit2" type="SUBMIT"
                     commandName="submit">
              <faces:output_text id="submit2_label" value="Login" />
            </faces:command_button>

            <faces:command_button id="reset2" type="RESET" commandName="reset">
              <faces:output_text id="reset2_label" value="Reset" />
            </faces:command_button>

            <!-- Panel footer element -->

                    <faces:output_text id="form_footer"
                           value="Enter username and password to Login" />
         </faces:panel_grid>
             </td>

            </tr>

