<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->


           <tr>

             <td>

               <h:output_text id="command_hyperlink1_label"
                     value="command_hyperlink with hard coded label"/>

             </td>

             <td>

	       <h:command_hyperlink id="command_hyperlink1" action="success"
                  label="Submit Form">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_hyperlink>

             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="command_hyperlink2_label" 
                     value="command_hyperlink using the model for the label"/>

             </td>

             <td>

	      <h:command_hyperlink id="valueRefLink" action="success"
                  valueRef="model.label">
                 <f:action_listener type="standard.DefaultListener"/>
              </h:command_hyperlink>

             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="command_hyperlink3_label" 
                     value="command_hyperlink using ResourceBundle for the label"/>

             </td>

             <td>

	      <h:command_hyperlink id="resBundleLableLink" action="success"
                  key="standardRenderKitSubmitLabel" bundle="standardBundle">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_hyperlink>

             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="command_hyperlink4_label" 
                     value="command_hyperlink as an image"/>

             </td>

             <td>

	      <h:command_hyperlink id="imageLink" action="success"
                  image="/duke.gif">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_hyperlink>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="command_hyperlink5_label" 
                     value="command_hyperlink using ResourceBundle for image path"/>

             </td>

             <td>

	      <h:command_hyperlink id="imageResourceBundleLink" 
                  imageKey="imageurl" bundle="standardBundle" action="success">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_hyperlink>


             </td>

            </tr>
