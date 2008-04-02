<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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

