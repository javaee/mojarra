<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="inputTime1Label"
                     value="input_time medium readonly"/>

             </td>


             <td>

               <h:input_text id="inputTime1" 
                                 value="10:00:01 PM" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_time medium readonly"
                                 accesskey="D" 
                               title="input_time medium readonly">
                   <f:convert_datetime type="time" timeStyle="medium"/>
               </h:input_text>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="inputTime2Label" 
                     value="input_time long "/>

             </td>


             <td>

               <h:input_text id="inputTime2"                                  
                                 value="9:57:00 AM PST"
                                 alt="input_time long"
                               title="input_time long">
                   <f:convert_datetime type="time" timeStyle="long"/>
               </h:input_text>

             </td>

	      <td>

		<h:messages id="inputTime2Errors" 
		        for="inputTime2" />

	      </td>

            </tr>

