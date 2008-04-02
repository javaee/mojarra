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

               <h:outputText id="inputText1Label"
                     value="inputText readonly"/>

             </td>


             <td>

               <h:inputText id="inputText1" 
                                 value="Text Value 1" 
                                 readonly="true"
                                 size="12" maxlength="20"
                                 alt="inputText readonly"
                                 accesskey="D" 
                                 title="inputText readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputText2Label" 
                     value="inputText"/>

             </td>


             <td>

               <h:inputText id="inputText2"
                                 value="Text Value 2" 
                                 alt="inputText"
                                  title="inputText"/>


             </td>

	      <td>

		<h:message id="inputText2Errors" 
			  for="inputText2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputText3Label" 
                     value="inputText"/>

             </td>


             <td>

               <h:inputText id="inputText3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="inputText"
                                  title="inputText"/>


             </td>

	      <td>

		<h:message id="inputText3Errors" 
			  for="inputText3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputText4Label" 
                     value="inputText"/>

             </td>


             <td>

               <h:inputText id="inputText4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="inputText"
                                 accesskey="d"
                               title="inputText"/>


             </td>

	      <td>

		<h:message id="inputText4Errors" 
			  for="inputText4" />

	      </td>

            </tr>

