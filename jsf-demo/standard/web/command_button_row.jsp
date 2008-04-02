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
                 <f:actionListener type="standard.DefaultListener"/>
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
                   value="#{model.label}">
                 <f:actionListener type="standard.DefaultListener"/>
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
                      image="#{standardBundle.imageurl}">
                 <f:actionListener type="standard.DefaultListener"/>
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
                     type="reset" value="#{standardBundle.resetButton}">
                 <f:actionListener type="standard.DefaultListener"/>
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
                 <f:actionListener type="standard.DefaultListener"/>
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
                      title="click to submit form" image="duke.gif">
                 <f:actionListener type="standard.DefaultListener"/>
               </h:command_button>
              </td>

            </tr>

