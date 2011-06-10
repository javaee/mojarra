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

