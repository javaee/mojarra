<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

--%>



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

               <h:inputText id="inputTest2" value="#{LoginBean.long}">
                   <f:convertNumber type="currency" currencySymbol="EUR"/>
                  
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


