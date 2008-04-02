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

               <h:outputText id="outputDatetime2Label"
                     value="output_datetime pattern"/>

             </td>


             <td>

               <h:outputText id="outputDatetime2" value="#{LoginBean.date}">
                   <f:convertDateTime pattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"/>
              </h:outputText>

             </td>

	      <td>

		<h:message id="outputDatetime2Errors" 
		        for="outputDatetime2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputDatetime3Label" 
                     value="output_datetime dateStyle=short timeStyle=full"/>
             <td>

               <h:outputText id="outputDatetime3" value="#{LoginBean.date}">
                   <f:convertDateTime type="both" timeStyle="full" dateStyle="short"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDatetime3Errors" 
		        for="outputDatetime2" />

	      </td>

            </tr>

