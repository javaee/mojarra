           <tr>

             <td>

               <faces:output_text id="checkbox1_label" 
                     value="checkbox disabled "/>

             </td>


             <td>

               <faces:selectboolean_checkbox id="checkbox1" 
                                 checked="true" 
                                 disabled="true"
                                 size="10"
                                 alt="checkbox disabled"
                                 accesskey="C" 
                               title="checkbox disabled" />

                                <faces:output_text 
                                      id="checkbox_label1" 
                           value="checkbox disabled"/>

             </td>

            </tr>

            <tr>

             <td>

               <faces:output_text id="checkbox3_label_model"
                     value="checkbox with modelReference "/>

             </td>


             <td>

               <faces:selectboolean_checkbox id="checkbox3"
                                 checked="true"
                                 modelReference="${LoginBean.validUser}"
                                 size="10"
                                 alt="checkbox"
                                 accesskey="C" 
                               title="checkbox with modelReference " />

                                <faces:output_text
                                      id="checkbox_label_model"
                           value="checkbox with modelReference"/>

             </td>

             <td>

                <faces:output_errors id="checkbox3_errors"
                          compoundId="/standardRenderKitForm/checkbox3" />

              </td

            </tr>

             <tr>

             <td>

               <faces:output_text id="checkbox4_label"
                     value="checkbox with label from JSP"/>

             </td>


             <td>

               <faces:selectboolean_checkbox id="checkbox4"
                                 checked="false"
                                 size="10"
                                 alt="checkbox"
                                 accesskey="C" 
                               title="checkbox with label from JSP" />

                   <faces:output_text id="checkbox_label_jsp" 
                               value="checkbox with label from JSP " />

             </td>

            </tr>
