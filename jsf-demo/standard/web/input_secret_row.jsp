<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="inputSecret1Label"
                     value="input_secret readonly"/>

             </td>


             <td>

               <h:input_secret id="inputSecret1" 
                                 value="Text Value 1" 
                                 readonly="true"
                                 size="12" maxlength="20"
                                 alt="input_secret readonly"
                                 accesskey="D" 
                                 title="input_secret readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputSecret2Label" 
                     value="inputSecret"/>

             </td>


             <td>

               <h:input_secret id="inputSecret2"
                                 value="Text Value 2" 
                                 alt="input_secret"
                                  title="input_secret"/>


             </td>

	      <td>

		<h:message id="inputSecret2Errors" 
			  for="inputSecret2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputSecret3Label" 
                     value="input_secret"/>

             </td>


             <td>

               <h:input_secret id="inputSecret3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="input_secret"
                                  title="input_secret"/>


             </td>

	      <td>

		<h:message id="inputSecret3Errors" 
			  for="inputSecret3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputSecret4Label" 
                     value="input_secret"/>

             </td>


             <td>

               <h:input_secret id="inputSecret4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="input_secret"
                                 accesskey="d"
                               title="input_secret"/>


             </td>

	      <td>

		<h:message id="inputSecret4Errors" 
			  for="inputSecret4" />

	      </td>

            </tr>

