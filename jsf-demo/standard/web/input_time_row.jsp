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

               <h:outputText id="inputTime1Label"
                     value="input_time medium readonly"/>

             </td>


             <td>

               <h:inputText id="inputTime1" 
                                 value="10:00:01 PM" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_time medium readonly"
                                 accesskey="D" 
                               title="input_time medium readonly">
                   <f:convertDateTime type="time" timeStyle="medium"/>
               </h:inputText>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputTime2Label" 
                     value="input_time long "/>

             </td>


             <td>

               <h:inputText id="inputTime2"                                  
                                 value="9:57:00 AM PST"
                                 alt="input_time long"
                               title="input_time long">
                   <f:convertDateTime type="time" timeStyle="long"/>
               </h:inputText>

             </td>

	      <td>

		<h:message id="inputTime2Errors" 
		        for="inputTime2" />

	      </td>

            </tr>

