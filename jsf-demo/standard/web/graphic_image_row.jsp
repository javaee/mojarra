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

               <h:outputText id="outputGraphic1Label"
                     value="output_graphic with hard coded image"/>

             </td>

             <td>

               <h:graphicImage id="outputGraphic1" url="duke.gif" 
	                            alt="output_graphic with hard coded image"
                                 title="output_graphic with hard coded image"
               />

             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputGraphic2Label" 
                     value="output_graphic with localized image"/>

             </td>

             <td>

               <h:graphicImage id="outputGraphic2" 
                                    url="#{standardBundle.imageurl}"
	                            alt="output_graphic with localized image"
                                title="output_graphic with localized image"
               />


             </td>

            </tr>


           <tr>

             <td>

               <h:outputText id="outputGraphic3Label" 
                     value="output_graphic with path from model"/>

             </td>

             <td>

               <h:graphicImage id="outputGraphic3" 
	                            value="#{LoginBean.imagePath}"
	                            alt="output_graphic with path from model"
                                title="output_graphic with path from model"
               />

             </td>

            </tr>

