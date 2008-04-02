<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="output_text1_label"
                     value="output_text"/>

             </td>


             <td>

               <h:output_text id="output_text1" 
                       valueRef="LoginBean.userName"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_text0_label" 
                     value="output_text with outputClass"/>

             </td>


             <td>

               <h:output_text id="output_text0" 
                       outputClass="output_text0"
                       valueRef="LoginBean.userName"/>


             </td>

            </tr>


           <tr>

             <td>

               <h:output_text id="output_text2_label" 
                     value="output_text from bundle"/>

             </td>


             <td>

               <h:output_text id="output_text2"
                                  key="linkLabel" bundle="standardBundle"/>


             </td>

	      <td>

		<h:output_errors id="output_text2_errors" 
			  for="output_text2" />

	      </td>

            </tr>

