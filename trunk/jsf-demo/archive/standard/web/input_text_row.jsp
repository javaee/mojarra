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

