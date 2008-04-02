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

               <h:outputText id="inputNumber1Label"
                     value="input_number number readonly"/>

             </td>


             <td>

               <h:inputText id="inputNumber1" 
                                 value="1239989.6079" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_number number readonly"
                                 accesskey="N"
                               title="input_number number readonly">
                   <f:convertNumber type="number"/>
               </h:inputText>
                           

             </td>
             <td>

             <h:message id="inputNumber1Errors"
                          for="inputNumber1" />

             </td>
            </tr>

           <tr>

             <td>

               <h:outputText id="inputNumber2Label" 
                     value="input_number currency"/>

             </td>


             <td>

               <h:inputText id="inputNumber2" 
                                 value="$1,234,789.60" 
                                 alt="input_number currency"
                                  title="input_number currency">
                   <f:convertNumber type="currency"/>
               </h:inputText>
             </td>

	      <td>

		<h:message id="inputNumber2Errors" 
			  for="inputNumber2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputNumber3Label" 
                     value="input_number percent "/>

             </td>


             <td>

               <h:inputText id="inputNumber3" 
                                 value="45%" 
                                 size="10"
                                 alt="input_number percent"
                                  title="input_number percent">
                   <f:convertNumber type="percent"/>
              </h:inputText>

             </td>

	      <td>

		<h:message id="inputNumber3Errors" 
			  for="inputNumber3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputNumber4Label" 
                     value="input_number PATTERN "/>

             </td>


             <td>

               <h:inputText id="inputNumber4" 
                                 value="9999.987651" 
                                 size="20" maxlength="40"
                                 alt="input_number PATTERN "
                                 accesskey="d"
                               title="input_number PATTERN">
                   <f:convertNumber pattern="####"/>
               </h:inputText>
             </td>

	      <td>

		<h:message id="inputNumber4Errors" 
			  for="inputNumber4" />

	      </td>

            </tr>

            <tr>

             <td>

               <h:outputText id="inputNumber5Label"
                     value="input_number integer with valueRef"/>

             </td>


             <td>

               <h:inputText id="inputNumber5" 
                                 size="2" maxlength="10"
                                 alt="input_number integer with valueRef"
                                 accesskey="d"
                               title="input_number integer with valueRef">
                   <f:convertNumber integerOnly="true"/>
               </h:inputText>
                               
             </td>

              <td>

                <h:message id="inputNumber5Errors"
                          for="inputNumber5" />

              </td>

            </tr>


