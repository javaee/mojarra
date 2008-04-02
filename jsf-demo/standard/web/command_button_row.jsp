<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="commandButton1Label"
                     value="command_button with hard coded label"/>

             </td>

             <td>

               <h:command_button id="commandButton1" action="success"
                   value="command_button with hard coded label">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_button>

              </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="commandButton6_label"
                     value="command_button with label from model"/>

             </td>

             <td>

               <h:command_button id="commandButton6" action="success"
                   valueRef="model.label">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_button>

              </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="commandButton2Label"
                     value="command_button with image from bundle "/>

             </td>

             <td>
                  <h:command_button id="commandButton2" 
                      tabindex="50" accesskey="B"
                      value="click to submit form"
                      action="success"
                      imageKey="imageurl" bundle="standardBundle">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_button>

              </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="commandButton3Label"
                     value="command_button reset type"/>

             </td>

             <td>
                 <h:command_button id="resetButton"  value="Click to reset form"                    action="success"
                     type="reset" key="resetButton" bundle="standardBundle">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_button>

              </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="commandButton4Label"
                     value="command_button push type and disabled"/>

             </td>

             <td>
                 <h:command_button id="pushButton"
                     title="button is disabled" type="button" 
                     disabled = "true" action="success"
                     value="This is a disabled push button">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_button>

              </td>

            </tr>

		    
            <tr>

             <td>

               <h:output_text id="commandButton5Label"
                     value="command_button image type"/>

             </td>

             <td>
                  <h:command_button id="button5" action="success"
                      title="click to submit form" image="/duke.gif">
                 <f:action_listener type="standard.DefaultListener"/>
               </h:command_button>
              </td>

            </tr>

