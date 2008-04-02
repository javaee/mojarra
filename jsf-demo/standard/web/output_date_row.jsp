<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="output_date3_label"
                     value="output_date short"/>

             </td>


             <td>

               <h:output_date id="output_date3" dateStyle="short"
                                     valueRef="LoginBean.date"/>


             </td>

	      <td>

		<h:output_errors id="output_date3_errors" 
			  for="output_date3" />

	      </td>

            </tr>

           <tr>

             <td>
               <h:output_label id="date1id" for="output_date1">
               <h:output_text id="output_date1_label" 
                     value="output_date medium"/>
               </h:output_label>

             </td>


             <td>

               <h:output_date id="output_date1" dateStyle="medium"
                               valueRef="LoginBean.date"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_date4_label" 
                     value="output_date long"/>

             </td>


             <td>

               <h:output_date id="output_date4" dateStyle="long" 
                               valueRef="LoginBean.date"/>


             </td>

	      <td>

		<h:output_errors id="output_date4_errors" 
			  for="output_date4" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_date2_label" 
                     value="output_date FULL"/>

             </td>


             <td>

               <h:output_date id="output_date2" dateStyle="full"
                                  valueRef="LoginBean.date"/>


             </td>

	      <td>

		<h:output_errors id="output_date2_errors" 
			  for="output_date2" />

	      </td>

            </tr>

