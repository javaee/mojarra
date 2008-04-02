<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="panel3Label"
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

                    <h:command_button id="submit1" type="SUBMIT" label="Login">
                    </h:command_button>

                    <h:command_button id="reset1" type="RESET" label="Reset">
                    </h:command_button>

                </h:panel_grid>
 
             </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="panel4Label"
                     value="Grid with model data and stylesheets "/>

             </td>


             <td>
             
             <h:panel_grid id="logonPanel2" columns="2"
               footerClass="form-footer"
               headerClass="form-header"
                panelClass="form-background"
             columnClasses="form-prompt,form-field">

            <f:facet name="header">
	      <h:panel_group>
		<h:output_text id="A2" value="Logon&nbsp;"/>
		<h:output_text id="B2" value="Form"/>
	      </h:panel_group>
            </f:facet>

            <!-- Panel data elements -->

              <h:output_text id="text3" value="Username:"/>

            <h:input_text id="username2"
              valueRef="LoginBean.userName"/>

             <h:output_text id="text4" value="Password:"/>

            <h:input_secret id="password2"
                valueRef="LoginBean.password"/>

            <h:command_button id="submit2" type="SUBMIT" label="Login">
            </h:command_button>

            <h:command_button id="reset2" type="RESET" label="Reset">
            </h:command_button>

            <!-- Panel footer element -->
            <f:facet name="footer">
	      <h:panel_group>
		      <h:output_text
			     value="Enter username and password to Login" />
	      </h:panel_group>
            </f:facet>
         </h:panel_grid>
             </td>

            </tr>

