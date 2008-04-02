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

               <h:outputText id="checkbox1Label"
                     value="checkbox disabled "/>

             </td>


             <td>

               <h:selectBooleanCheckbox id="checkbox1" 
                                 value="#{LoginBean.validUser}"
                                 disabled="true"
                                 accesskey="C" 
                               title="checkbox disabled" />

                                <h:outputText 
                                      id="checkboxLabel1" 
                           value="checkbox disabled"/>

             </td>

            </tr>

            <tr>

             <td>

               <h:outputText id="checkbox3LabelModel"
                     value="checkbox with valueRef "/>

             </td>


             <td>

               <h:selectBooleanCheckbox id="checkbox3"
                                 value="#{LoginBean.validUser}"
                                 accesskey="C" 
                               title="checkbox with valueRef " />

                                <h:outputText
                                      id="checkboxLabelModel"
                           value="checkbox with valueRef"/>

             </td>

             <td>

                <h:message id="checkbox3Errors"
                          for="checkbox3" />

              </td

            </tr>

             <tr>

             <td>

               <h:outputText id="checkbox4Label"
                     value="checkbox with label from JSP"/>

             </td>


             <td>

               <h:selectBooleanCheckbox id="checkbox4"
                                 accesskey="C" 
                               title="checkbox with label from JSP" />

                   <h:outputText id="checkboxLabelJsp" 
                               value="checkbox with label from JSP " />

             </td>

            </tr>
