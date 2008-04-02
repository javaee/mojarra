<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="output_number1_label"
                     value="output_number number"/>

             </td>


             <td>

               <h:output_number id="output_number1" numberStyle="number"
                               valueRef="LoginBean.floater"/>

             </td>
             <td>

             <h:output_errors id="output_number1_errors"
                          for="output_number1" />

             </td>
            </tr>

           <tr>

             <td>

               <h:output_text id="output_number2_label" 
                     value="output_number currency"/>

             </td>


             <td>

               <h:output_number id="output_number2" numberStyle="currency"
                          valueRef="LoginBean.floater"/>
             </td>

	      <td>

		<h:output_errors id="output_number2_errors" 
			  for="output_number2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_number3_label" 
                     value="output_number percent "/>

             </td>


             <td>

               <h:output_number id="output_number3" numberStyle="percent"
                          valueRef="LoginBean.floater" />

             </td>

	      <td>

		<h:output_errors id="output_number3_errors" 
			  for="output_number3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_number4_label" 
                     value="output_number PATTERN "/>

             </td>


             <td>

               <h:output_number id="output_number4" formatPattern="####" 
                         valueRef="LoginBean.floater" />
             </td>

	      <td>

		<h:output_errors id="output_number4_errors" 
			  for="output_number4" />

	      </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="output_number5_label"
                     value="output_number integer with valueRef"/>

             </td>


             <td>

               <h:output_number id="output_number5" numberStyle="integer"
                                 valueRef="${LoginBean.char}" />
             </td>

              <td>

                <h:output_errors id="output_number5_errors"
                          for="output_number5" />

              </td>

            </tr>


