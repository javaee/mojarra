<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="input_secret1_label" 
                     value="input_secret readonly"/>

             </td>


             <td>

               <h:input_secret id="input_secret1" 
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

               <h:output_text id="input_secret2_label" 
                     value="input_secret"/>

             </td>


             <td>

               <h:input_secret id="input_secret2"
                                 value="Text Value 2" 
                                 alt="input_secret"
                                  title="input_secret"/>


             </td>

	      <td>

		<h:output_errors id="input_secret2_errors" 
			  for="input_secret2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="input_secret3_label" 
                     value="input_secret"/>

             </td>


             <td>

               <h:input_secret id="input_secret3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="input_secret"
                                  title="input_secret"/>


             </td>

	      <td>

		<h:output_errors id="input_secret3_errors" 
			  for="input_secret3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="input_secret4_label" 
                     value="input_secret"/>

             </td>


             <td>

               <h:input_secret id="input_secret4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="input_secret"
                                 accesskey="d"
                               title="input_secret"/>


             </td>

	      <td>

		<h:output_errors id="input_secret4_errors" 
			  for="input_secret4" />

	      </td>

            </tr>

