<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:outputText id="commandButton1Label"
                     value="commandButton with hard coded label"/>

             </td>

             <td>

               <h:commandButton id="commandButton1" action="success"
                   value="commandButton with hard coded label">
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandButton>

              </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="commandButton6_label"
                     value="commandButton with label from model"/>

             </td>

             <td>

               <h:commandButton id="commandButton6" action="success"
                   value="#{model.label}">
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandButton>

              </td>

            </tr>

            <tr>

             <td>

               <h:outputText id="commandButton2Label"
                     value="commandButton with image from bundle "/>

             </td>

             <td>
                  <h:commandButton id="commandButton2" 
                      tabindex="50" accesskey="B"
                      action="success"
                      image="#{standardBundle.imageurl}">
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandButton>

              </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="commandButton3Label"
                     value="commandButton reset type"/>

             </td>

             <td>
                 <h:commandButton id="resetButton" action="success"
                     type="reset" value="#{standardBundle.resetButton}">
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandButton>

              </td>

            </tr>

            <tr>

             <td>

               <h:outputText id="commandButton4Label"
                     value="commandButton push type and disabled"/>

             </td>

             <td>
                 <h:commandButton id="pushButton"
                     title="button is disabled" type="button" 
                     disabled = "true" action="success"
                     value="This is a disabled push button">
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandButton>

              </td>

            </tr>

		    
            <tr>

             <td>

               <h:outputText id="commandButton5Label"
                     value="commandButton image type"/>

             </td>

             <td>
                  <h:commandButton id="button5" action="success"
                      title="click to submit form" image="duke.gif">
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandButton>
              </td>

            </tr>

