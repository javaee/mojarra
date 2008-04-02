<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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


