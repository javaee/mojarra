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

               <h:outputText id="commandLink1Label"
                     value="commandLink with hard coded label"/>

             </td>

             <td>

	       <h:commandLink id="commandLink1" action="success">
                 <f:verbatim>Submit Form</f:verbatim>
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandLink>

             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="commandLink2Label" 
                     value="commandLink using the model for the label"/>

             </td>

             <td>

	      <h:commandLink id="valueRefLink" action="success">
                 <h:outputText value="#{model.label}"/>
                 <f:actionListener type="standard.DefaultListener"/>
              </h:commandLink>

             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="commandLink3Label" 
                     value="commandLink using ResourceBundle for the label"/>

             </td>

             <td>

	      <h:commandLink id="resBundleLableLink" action="success">
                 <h:outputText 
                  value="#{standardBundle.standardRenderKitSubmitLabel}"/>
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandLink>

             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="commandLink4Label" 
                     value="commandLink as an image"/>

             </td>

             <td>

	      <h:commandLink id="imageLink" action="success">
                 <h:graphicImage url="/duke.gif"/>
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandLink>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="commandLink5Label" 
                     value="commandLink using ResourceBundle for image path"/>

             </td>

             <td>

	      <h:commandLink id="imageResourceBundleLink" action="success">
                 <h:graphicImage  
                  url="#{standardBundle.imageurl}" />
                 <f:actionListener type="standard.DefaultListener"/>
               </h:commandLink>


             </td>

            </tr>
