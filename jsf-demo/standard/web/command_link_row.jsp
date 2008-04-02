<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
