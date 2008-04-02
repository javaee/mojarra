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

               <h:outputText id="outputDatetime2Label"
                     value="output_datetime pattern"/>

             </td>


             <td>

               <h:outputText id="outputDatetime2" value="#{LoginBean.date}">
                   <f:convertDateTime pattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"/>
              </h:outputText>

             </td>

	      <td>

		<h:message id="outputDatetime2Errors" 
		        for="outputDatetime2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputDatetime3Label" 
                     value="output_datetime dateStyle=short timeStyle=full"/>
             <td>

               <h:outputText id="outputDatetime3" value="#{LoginBean.date}">
                   <f:convertDateTime type="both" timeStyle="full" dateStyle="short"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDatetime3Errors" 
		        for="outputDatetime2" />

	      </td>

            </tr>

