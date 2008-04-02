<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="outputText1Label"
                     value="output_text"/>

             </td>


             <td>

               <h:output_text id="outputText1" 
                       valueRef="LoginBean.userName"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputText0Label" 
                     value="output_text with outputClass"/>

             </td>


             <td>

               <h:output_text id="outputText0" 
                       outputClass="output_text0"
                       valueRef="LoginBean.userName"/>


             </td>

            </tr>


           <tr>

             <td>

               <h:output_text id="outputText2Label" 
                     value="output_text from bundle"/>

             </td>


             <td>

               <h:output_text id="outputText2"
                                  key="linkLabel" bundle="standardBundle"/>


             </td>

	      <td>

		<h:output_errors id="outputText2Errors" 
			  for="outputText2" />

	      </td>

            </tr>

