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

               <h:outputText id="inputTextarea1Label"
                     value="inputTextarea readonly "/>

             </td>


             <td>

               <h:inputTextarea id="inputTextarea1" 
                                 value="initial text"
                                 readonly="true"
                                 rows="5" cols="20"
                                 accesskey="D" 
                                 title="inputTextarea readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputTextarea2Label" 
                     value="inputTextarea"/>

             </td>


             <td>

               <h:inputTextarea id="inputTextarea2"
                                 rows="5" cols="20"
                                  title="inputTextarea"/>


             </td>

	      <td>

		<h:message id="inputTextarea2Errors" 
			  for="inputTextarea2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputTextarea3Label" 
                     value="inputTextarea"/>

             </td>


             <td>

               <h:inputTextarea id="inputTextarea3"
                                 rows="5" cols="20"
                                  title="inputTextarea"/>


             </td>

	      <td>

		<h:message id="inputTextarea3Errors" 
			  for="inputTextarea3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputTextarea4_label" 
                     value="inputTextarea"/>

             </td>


             <td>

               <h:inputTextarea id="inputTextarea4" 
                                 rows="5" cols="20"
                                 accesskey="d"
                               title="inputTextarea"/>


             </td>

	      <td>

		<h:message id="inputTextarea4Errors" 
			  for="inputTextarea4" />

	      </td>

            </tr>

