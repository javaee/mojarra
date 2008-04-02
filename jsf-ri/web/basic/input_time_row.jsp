<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="input_time1_label" 
                     value="input_time medium readonly"/>

             </td>


             <td>

               <h:input_time id="input_time1" timeStyle="medium"
                                 value="10:00:01 PM" 
                                 readonly="true"

                                 size="10" maxlength="20"
                                 alt="input_time medium readonly"
                                 accesskey="D" 
                               title="input_time medium readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="input_time2_label" 
                     value="input_time long "/>

             </td>


             <td>

               <h:input_time id="input_time2" 
                                 timeStyle="long"
                                 value="9:57:00 AM PST"
                                 alt="input_time long"
                               title="input_time long"/>

             </td>

	      <td>

		<h:output_errors id="input_time2_errors" 
		        for="input_time2" />

	      </td>

            </tr>

