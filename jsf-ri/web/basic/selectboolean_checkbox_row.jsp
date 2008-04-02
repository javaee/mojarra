<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="checkbox1_label" 
                     value="checkbox disabled "/>

             </td>


             <td>

               <h:selectboolean_checkbox id="checkbox1" 
                                 checked="true" 
                                 disabled="true"
                                 size="10"
                                 alt="checkbox disabled"
                                 accesskey="C" 
                               title="checkbox disabled" />

                                <h:output_text 
                                      id="checkbox_label1" 
                           value="checkbox disabled"/>

             </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="checkbox3_label_model"
                     value="checkbox with modelReference "/>

             </td>


             <td>

               <h:selectboolean_checkbox id="checkbox3"
                                 checked="true"
                                 modelReference="LoginBean.validUser"
                                 size="10"
                                 alt="checkbox"
                                 accesskey="C" 
                               title="checkbox with modelReference " />

                                <h:output_text
                                      id="checkbox_label_model"
                           value="checkbox with modelReference"/>

             </td>

             <td>

                <h:output_errors id="checkbox3_errors"
                          clientId="checkbox3" />

              </td

            </tr>

             <tr>

             <td>

               <h:output_text id="checkbox4_label"
                     value="checkbox with label from JSP"/>

             </td>


             <td>

               <h:selectboolean_checkbox id="checkbox4"
                                 checked="false"
                                 size="10"
                                 alt="checkbox"
                                 accesskey="C" 
                               title="checkbox with label from JSP" />

                   <h:output_text id="checkbox_label_jsp" 
                               value="checkbox with label from JSP " />

             </td>

            </tr>
