<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="inputText1Label"
                     value="input_text readonly"/>

             </td>


             <td>

               <h:input_text id="inputText1" 
                                 value="Text Value 1" 
                                 readonly="true"
                                 size="12" maxlength="20"
                                 alt="input_text readonly"
                                 accesskey="D" 
                                 title="input_text readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputText2Label" 
                     value="inputText"/>

             </td>


             <td>

               <h:input_text id="inputText2"
                                 value="Text Value 2" 
                                 alt="input_text"
                                  title="input_text"/>


             </td>

	      <td>

		<h:output_errors id="inputText2Errors" 
			  for="inputText2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputText3Label" 
                     value="inputText"/>

             </td>


             <td>

               <h:input_text id="inputText3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="input_text"
                                  title="input_text"/>


             </td>

	      <td>

		<h:output_errors id="inputText3Errors" 
			  for="inputText3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputText4Label" 
                     value="inputText"/>

             </td>


             <td>

               <h:input_text id="inputText4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="input_text"
                                 accesskey="d"
                               title="input_text"/>


             </td>

	      <td>

		<h:output_errors id="inputText4Errors" 
			  for="inputText4" />

	      </td>

            </tr>

