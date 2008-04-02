<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="checkbox1Label"
                     value="checkbox disabled "/>

             </td>


             <td>

               <h:selectboolean_checkbox id="checkbox1" 
                                 valueRef="LoginBean.validUser"
                                 checked="true" 
                                 disabled="true"
                                 size="10"
                                 alt="checkbox disabled"
                                 accesskey="C" 
                               title="checkbox disabled" />

                                <h:output_text 
                                      id="checkboxLabel1" 
                           value="checkbox disabled"/>

             </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="checkbox3LabelModel"
                     value="checkbox with valueRef "/>

             </td>


             <td>

               <h:selectboolean_checkbox id="checkbox3"
                                 checked="true"
                                 valueRef="LoginBean.validUser"
                                 size="10"
                                 alt="checkbox"
                                 accesskey="C" 
                               title="checkbox with valueRef " />

                                <h:output_text
                                      id="checkboxLabelModel"
                           value="checkbox with valueRef"/>

             </td>

             <td>

                <h:output_errors id="checkbox3Errors"
                          for="checkbox3" />

              </td

            </tr>

             <tr>

             <td>

               <h:output_text id="checkbox4Label"
                     value="checkbox with label from JSP"/>

             </td>


             <td>

               <h:selectboolean_checkbox id="checkbox4"
                                 checked="false"
                                 size="10"
                                 alt="checkbox"
                                 accesskey="C" 
                               title="checkbox with label from JSP" />

                   <h:output_text id="checkboxLabelJsp" 
                               value="checkbox with label from JSP " />

             </td>

            </tr>
