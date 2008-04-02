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

               <h:outputText id="outputDate3Label"
                     value="output_date short"/>

             </td>


             <td>

               <h:outputText id="outputDate3" value="#{LoginBean.date}">
                   <f:convertDateTime dateStyle="short" type="date"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDate3Errors" 
			  for="outputDate3" />

	      </td>

            </tr>

           <tr>

             <td>
               <h:outputLabel id="date1id" for="output_date1">
               <h:outputText id="outputDate1Label" 
                     value="output_date medium"/>
               </h:outputLabel>

             </td>


             <td>

               <h:outputText id="outputDate1" value="#{LoginBean.date}">
                   <f:convertDateTime dateStyle="medium" type="date"/>
               </h:outputText>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputDate4Label" 
                     value="output_date long"/>

             </td>


             <td>

               <h:outputText id="outputDate4" value="#{LoginBean.date}">
                  <f:convertDateTime type="date" dateStyle="long"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDate4Errors" 
			  for="outputDate4" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputDate2Label" 
                     value="output_date FULL"/>

             </td>


             <td>

               <h:outputText id="outputDate2" value="#{LoginBean.date}">
                   <f:convertDateTime type="date" dateStyle="full"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDate2Errors" 
			  for="outputDate2" />

	      </td>

            </tr>

