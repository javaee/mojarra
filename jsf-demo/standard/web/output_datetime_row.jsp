<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="output_datetime2_label"
                     value="output_datetime pattern"/>

             </td>


             <td>

               <h:output_datetime id="output_datetime2" 
                            formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"
                               valueRef="LoginBean.date"/>

             </td>

	      <td>

		<h:output_errors id="output_datetime2_errors" 
		        for="output_datetime2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_datetime3_label" 
                     value="output_datetime dateStyle=short timeStyle=full"/>

             </td>


             <td>

               <h:output_datetime id="output_datetime3" 
                    dateStyle="short" timeStyle="full"
                               valueRef="LoginBean.date"/>

             </td>

	      <td>

		<h:output_errors id="output_datetime3_errors" 
		        for="output_datetime2" />

	      </td>

            </tr>

