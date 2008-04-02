<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="inputTextarea1Label"
                     value="input_textarea readonly "/>

             </td>


             <td>

               <h:input_textarea id="inputTextarea1" 
                                 value="initial text"
                                 readonly="true"
                                 rows="5" cols="20"
                                 alt="input_textarea readonly"
                                 accesskey="D" 
                                 title="input_textarea readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputTextarea2Label" 
                     value="input_textarea"/>

             </td>


             <td>

               <h:input_textarea id="inputTextarea2"
                                 rows="5" cols="20"
                                 alt="input_textarea"
                                  title="input_textarea"/>


             </td>

	      <td>

		<h:messages id="inputTextarea2Errors" 
			  for="inputTextarea2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputTextarea3Label" 
                     value="inputTextarea"/>

             </td>


             <td>

               <h:input_textarea id="inputTextarea3"
                                 rows="5" cols="20"
                                 alt="input_textarea"
                                  title="input_textarea"/>


             </td>

	      <td>

		<h:messages id="inputTextarea3Errors" 
			  for="inputTextarea3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputTextarea4_label" 
                     value="input_textarea"/>

             </td>


             <td>

               <h:input_textarea id="inputTextarea4" 
                                 rows="5" cols="20"
                                 alt="input_textarea"
                                 accesskey="d"
                               title="input_textarea"/>


             </td>

	      <td>

		<h:messages id="inputTextarea4Errors" 
			  for="inputTextarea4" />

	      </td>

            </tr>

