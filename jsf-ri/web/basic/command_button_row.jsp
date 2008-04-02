           <tr>

             <td>

               <h:output_text id="command_button1_label" 
                     value="command_button with hard coded label"/>

             </td>

             <td>

               <h:command_button id="command_button1"  
                      commandName="standardRenderKitSubmit">
                   <h:output_text id="button1_label"
                     value="command_button with hard coded label"/>
               </h:command_button>

              </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="command_button2_label"
                     value="command_button with label from bundle and image from bundle "/>

             </td>

             <td>
                  <h:command_button id="command_button2" 
                                tabindex="50"
                                accesskey="B"
                                title="click to submit form"
                                commandName="standardRenderKitSubmit">

                      <h:output_text id="button2_label"
                     key="loginButton" bundle="basicBundle" />

                     <h:graphic_image id="imageButton"
                            key="imageurl" bundle="basicBundle"  />
                 </h:command_button>

              </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="command_button3_label"
                     value="command_button reset type"/>

             </td>

             <td>
                 <h:command_button id="resetButton"  title="Click to reset form"
                                 type="reset"
                                 commandName="reset">
                     <h:output_text id="button3_label"
                     key="resetButton" bundle="basicBundle" />

                 </h:command_button>

              </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="command_button4_label"
                     value="command_button push type and disabled"/>

             </td>

             <td>
                 <h:command_button id="pushButton" commandName="push"
                   title="button is disabled" type="button" disabled = "true" >
                     <h:output_text id="button4_label" value="This is a disabled push button"/>
                 </h:command_button>

              </td>

            </tr>

		    
            <tr>

             <td>

               <h:output_text id="command_button5_label"
                     value="command_button image type"/>

             </td>

             <td>
                  <h:command_button id="button5"
                   title="click to submit form" commandName="image" >
                       <h:graphic_image id="buttonImage1" url="/duke.gif" />
                  </h:command_button>
              </td>

            </tr>

