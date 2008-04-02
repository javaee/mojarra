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

	       <h:command_hyperlink id="command_hyperlink1" 
                  label="Submit Form"/>
             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="command_hyperlink2_label" 
                     value="command_hyperlink using the model for the label"/>

             </td>

             <td>

	      <h:command_hyperlink id="valueRefLink" 
                  valueRef="model.label"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="command_hyperlink3_label" 
                     value="command_hyperlink using ResourceBundle for the label"/>

             </td>

             <td>

	      <h:command_hyperlink id="resBundleLableLink" 
                  key="standardRenderKitSubmitLabel" bundle="standardBundle"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="command_hyperlink4_label" 
                     value="command_hyperlink as an image"/>

             </td>

             <td>

	      <h:command_hyperlink id="imageLink" 
                  image="duke.gif"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="command_hyperlink5_label" 
                     value="command_hyperlink using ResourceBundle for image path"/>

             </td>

             <td>

	      <h:command_hyperlink id="imageResourceBundleLink" 
                  imageKey="imageurl" bundle="standardBundle"/>


             </td>

            </tr>
