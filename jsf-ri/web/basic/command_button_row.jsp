           <tr>

             <td>

               <faces:output_text id="command_button1_label" 
                     value="command_button with hard coded label"/>

             </td>

             <td>

               <faces:command_button id="command_button1"  
                      commandName="standardRenderKitSubmit">
                   <faces:output_text id="button1_label"
                     value="command_button with hard coded label"/>
               </faces:command_button>

              </td>

            </tr>

            <tr>

             <td>

               <faces:output_text id="command_button2_label"
                     value="command_button with label from bundle and image from bundle "/>

             </td>

             <td>
                  <faces:command_button id="command_button2" 
                                tabindex="50"
                                accesskey="B"
                                title="click to submit form"
                                commandName="login">

                      <faces:output_text id="button2_label"
                     key="loginButton" bundle="${basicBundle}" />

                     <faces:graphic_image id="imageButton"
                            key="imageurl" bundle="${basicBundle}"  />
                 </faces:command_button>

              </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="command_button3_label"
                     value="command_button reset type"/>

             </td>

             <td>
                 <faces:command_button id="resetButton"  title="Click to reset form"
                                 type="reset"
                                 commandName="reset">
                     <faces:output_text id="button3_label"
                     key="resetButton" bundle="${basicBundle}" />

                 </faces:command_button>

              </td>

            </tr>

            <tr>

             <td>

               <faces:output_text id="command_button4_label"
                     value="command_button push type and disabled"/>

             </td>

             <td>
                 <faces:command_button id="pushButton" commandName="push"
                   title="button is disabled" type="button" disabled = "true" >
                     <faces:output_text id="button4_label" value="This is a disabled push button"/>
                 </faces:command_button>

              </td>

            </tr>

		    
            <tr>

             <td>

               <faces:output_text id="command_button5_label"
                     value="command_button image type"/>

             </td>

             <td>
                  <faces:command_button id="button5"
                   title="click to submit form" commandName="image" >
                       <faces:graphic_image id="buttonImage1" url="/duke.gif" />
                  </faces:command_button>
              </td>

            </tr>

