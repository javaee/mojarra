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

               <h:outputText id="inputSecret1Label"
                     value="inputSecret readonly"/>

             </td>


             <td>

               <h:inputSecret id="inputSecret1" 
                                 value="Text Value 1" 
                                 readonly="true"
                                 size="12" maxlength="20"
                                 alt="inputSecret readonly"
                                 accesskey="D" 
                                 title="inputSecret readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputSecret2Label" 
                     value="inputSecret"/>

             </td>


             <td>

               <h:inputSecret id="inputSecret2"
                                 value="Text Value 2" 
                                 alt="inputSecret"
                                  title="inputSecret"/>


             </td>

	      <td>

		<h:message id="inputSecret2Errors" 
			  for="inputSecret2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputSecret3Label" 
                     value="inputSecret"/>

             </td>


             <td>

               <h:inputSecret id="inputSecret3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="inputSecret"
                                  title="inputSecret"/>


             </td>

	      <td>

		<h:message id="inputSecret3Errors" 
			  for="inputSecret3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputSecret4Label" 
                     value="inputSecret"/>

             </td>


             <td>

               <h:inputSecret id="inputSecret4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="inputSecret"
                                 accesskey="d"
                               title="inputSecret"/>


             </td>

	      <td>

		<h:message id="inputSecret4Errors" 
			  for="inputSecret4" />

	      </td>

            </tr>

