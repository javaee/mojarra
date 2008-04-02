<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->


           <tr>

             <td>

               <h:output_text id="commandLink1Label"
                     value="command_link with hard coded label"/>

             </td>

             <td>

	       <h:command_link id="commandLink1" action="success">
                 <f:verbatim>Submit Form</f:verbatim>
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_link>

             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="commandLink2Label" 
                     value="command_link using the model for the label"/>

             </td>

             <td>

	      <h:command_link id="valueRefLink" action="success">
                 <h:output_text valueRef="model.label"/>
                 <f:action_listener type="standard.DefaultListener"/>
              </h:command_link>

             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="commandLink3Label" 
                     value="command_link using ResourceBundle for the label"/>

             </td>

             <td>

	      <h:command_link id="resBundleLableLink" action="success">
                 <h:output_text 
                  key="standardRenderKitSubmitLabel" bundle="standardBundle"/>
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_link>

             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="commandLink4Label" 
                     value="command_link as an image"/>

             </td>

             <td>

	      <h:command_link id="imageLink" action="success">
                 <h:graphic_image url="duke.gif"/>
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_link>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="commandLink5Label" 
                     value="command_link using ResourceBundle for image path"/>

             </td>

             <td>

	      <h:command_link id="imageResourceBundleLink" action="success">
                 <h:graphic_image  
                  key="imageurl" bundle="standardBundle" />
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_link>


             </td>

            </tr>
