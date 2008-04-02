<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="outputTime1Label"
                     value="output_time medium readonly"/>

             </td>


             <td>

               <h:output_text id="outputTime1" valueRef="LoginBean.date">
                   <f:convert_datetime type="time" timeStyle="medium"/>
               </h:output_text>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputTime2Label" 
                     value="output_time long "/>

             </td>


             <td>

               <h:output_text id="outputTime2" valueRef="LoginBean.date">
                   <f:convert_datetime type="time" timeStyle="long"/>
               </h:output_text>

             </td>

	      <td>

		<h:output_errors id="outputTime2Errors" 
		        for="outputTime2" />

	      </td>

            </tr>

