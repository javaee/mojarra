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

               <h:outputText id="outputDate3Label"
                     value="output_date short"/>

             </td>


             <td>

               <h:outputText id="outputDate3" value="#{LoginBean.date}">
                   <f:convertDateTime dateStyle="short" type="date"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDate3Errors" 
			  for="outputDate3" />

	      </td>

            </tr>

           <tr>

             <td>
               <h:outputLabel id="date1id" for="output_date1">
               <h:outputText id="outputDate1Label" 
                     value="output_date medium"/>
               </h:outputLabel>

             </td>


             <td>

               <h:outputText id="outputDate1" value="#{LoginBean.date}">
                   <f:convertDateTime dateStyle="medium" type="date"/>
               </h:outputText>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputDate4Label" 
                     value="output_date long"/>

             </td>


             <td>

               <h:outputText id="outputDate4" value="#{LoginBean.date}">
                  <f:convertDateTime type="date" dateStyle="long"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDate4Errors" 
			  for="outputDate4" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputDate2Label" 
                     value="output_date FULL"/>

             </td>


             <td>

               <h:outputText id="outputDate2" value="#{LoginBean.date}">
                   <f:convertDateTime type="date" dateStyle="full"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDate2Errors" 
			  for="outputDate2" />

	      </td>

            </tr>

