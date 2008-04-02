<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="output_time1_label" 
                     value="output_time medium readonly"/>

             </td>


             <td>

               <h:output_time id="output_time1" timeStyle="medium"
                         valueRef="LoginBean.date"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_time2_label" 
                     value="output_time long "/>

             </td>


             <td>

               <h:output_time id="output_time2" 
                                 timeStyle="long"
                         valueRef="LoginBean.date"/>

             </td>

	      <td>

		<h:output_errors id="output_time2_errors" 
		        for="output_time2" />

	      </td>

            </tr>

