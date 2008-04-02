<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="outputDatetime2Label"
                     value="output_datetime pattern"/>

             </td>


             <td>

               <h:output_text id="outputDatetime2" valueRef="LoginBean.date">
                   <f:convert_datetime pattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"/>
              </h:output_text>

             </td>

	      <td>

		<h:output_errors id="outputDatetime2Errors" 
		        for="outputDatetime2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputDatetime3Label" 
                     value="output_datetime dateStyle=short timeStyle=full"/>
             <td>

               <h:output_text id="outputDatetime3" valueRef="LoginBean.date">
                   <f:convert_datetime type="both" timeStyle="full" dateStyle="short"/>
               </h:output_text>


             </td>

	      <td>

		<h:output_errors id="outputDatetime3Errors" 
		        for="outputDatetime2" />

	      </td>

            </tr>

