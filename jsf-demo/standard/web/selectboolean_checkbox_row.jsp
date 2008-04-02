<!--
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
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
