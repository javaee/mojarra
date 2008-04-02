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

