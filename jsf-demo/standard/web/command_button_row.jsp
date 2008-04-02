<!--
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
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

