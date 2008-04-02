<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="inputDatetime2Label"
                     value="input_datetime pattern"/>

             </td>


             <td>

               <h:input_text id="inputDatetime2" 
                                 value="Wed, Aug 21, 2002 AD at 10:57:00 AM" 
                                 size="30" 
                                 alt="input_datetime pattern"
                               title="input_datetime pattern">
                   <f:convertDateTime pattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"/>
              </h:input_text>

             </td>

	      <td>

		<h:message id="inputDatetime2Errors" 
		        for="inputDatetime2" />

	      </td>

            </tr>

