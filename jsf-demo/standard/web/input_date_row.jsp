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
               <h:outputLabel id="date1id" for="inputDate1">
               <h:outputText id="inputDate1Label" 
                     value="input_date medium readonly"/>
               </h:outputLabel>

             </td>


             <td>

               <h:inputText id="inputDate1" 
                                 value="#{model.date1}"
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_date medium readonly"
                                 accesskey="D" 
                               title="input_date medium readonly">
                   <f:convertDateTime type="date" dateStyle="medium"/>
               </h:inputText>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputDate2Label" 
                     value="input_date medium"/>

             </td>


             <td>

               <h:inputText id="inputDate2"
                                 value="#{model.date2}"
                                 alt="input_date medium"
                                  title="input_date medium">
                   <f:convertDateTime type="date" dateStyle="medium"/>
               </h:inputText>


             </td>

	      <td>

		<h:message id="inputDate2Errors" 
			  for="inputDate2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputDate3Label" 
                     value="input_date short"/>

             </td>


             <td>

               <h:inputText id="inputDate3" 
                                 value="#{model.date3}"
                                 size="10"
                                 alt="input_date short"
                                  title="input_date short">
                   <f:convertDateTime type="date" dateStyle="short"/>
               </h:inputText>


             </td>

	      <td>

		<h:message id="inputDate3Errors" 
			  for="inputDate3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputDate4Label" 
                     value="input_date long"/>

             </td>


             <td>

               <h:inputText id="inputDate4" 
                                 value="#{model.date4}"
                                 size="20" maxlength="40"
                                 alt="input_date long"
                                 accesskey="d"
                               title="input_date long">
                   <f:convertDateTime type="date" dateStyle="long"/>
               </h:inputText>


             </td>

	      <td>

		<h:message id="inputDate4Errors" 
			  for="inputDate4" />

	      </td>

            </tr>

