<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="input_datetime2_label" 
                     value="input_datetime pattern"/>

             </td>


             <td>

               <h:input_datetime id="input_datetime2" 
                            formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"
                                 value="Wed, Aug 21, 2002 AD at 10:57:00 AM" 
                                 size="30" 
                                 alt="input_datetime pattern"
                               title="input_datetime pattern"/>

             </td>

	      <td>

		<h:output_errors id="input_datetime2_errors" 
		        for="input_datetime2" />

	      </td>

            </tr>

