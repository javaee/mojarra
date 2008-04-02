<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="outputDate3Label"
                     value="output_date short"/>

             </td>


             <td>

               <h:output_text id="outputDate3" valueRef="LoginBean.date">
                   <f:convert_datetime dateStyle="short" type="date"/>
               </h:output_text>


             </td>

	      <td>

		<h:messages id="outputDate3Errors" 
			  for="outputDate3" />

	      </td>

            </tr>

           <tr>

             <td>
               <h:output_label id="date1id" for="output_date1">
               <h:output_text id="outputDate1Label" 
                     value="output_date medium"/>
               </h:output_label>

             </td>


             <td>

               <h:output_text id="outputDate1" valueRef="LoginBean.date">
                   <f:convert_datetime dateStyle="medium" type="date"/>
               </h:output_text>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputDate4Label" 
                     value="output_date long"/>

             </td>


             <td>

               <h:output_text id="outputDate4" valueRef="LoginBean.date">
                  <f:convert_datetime type="date" dateStyle="long"/>
               </h:output_text>


             </td>

	      <td>

		<h:messages id="outputDate4Errors" 
			  for="outputDate4" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputDate2Label" 
                     value="output_date FULL"/>

             </td>


             <td>

               <h:output_text id="outputDate2" valueRef="LoginBean.date">
                   <f:convert_datetime type="date" dateStyle="full"/>
               </h:output_text>


             </td>

	      <td>

		<h:messages id="outputDate2Errors" 
			  for="outputDate2" />

	      </td>

            </tr>

