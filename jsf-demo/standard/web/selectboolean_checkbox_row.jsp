<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:outputText id="checkbox1Label"
                     value="checkbox disabled "/>

             </td>


             <td>

               <h:selectbooleanCheckbox id="checkbox1" 
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

               <h:selectbooleanCheckbox id="checkbox3"
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

               <h:selectbooleanCheckbox id="checkbox4"
                                 accesskey="C" 
                               title="checkbox with label from JSP" />

                   <h:outputText id="checkboxLabelJsp" 
                               value="checkbox with label from JSP " />

             </td>

            </tr>
