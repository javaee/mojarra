           <tr>

             <td>

               <h:output_text id="output_date3_label" 
                     value="output_date short"/>

             </td>


             <td>

               <h:output_date id="output_date3" dateStyle="short"
                                     modelReference="LoginBean.date"/>


             </td>

	      <td>

		<h:output_errors id="output_date3_errors" 
			  clientId="output_date3" />

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
                               modelReference="LoginBean.date"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_date4_label" 
                     value="output_date long"/>

             </td>


             <td>

               <h:output_date id="output_date4" dateStyle="long" 
                               modelReference="LoginBean.date"/>


             </td>

	      <td>

		<h:output_errors id="output_date4_errors" 
			  clientId="output_date4" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_date2_label" 
                     value="output_date FULL"/>

             </td>


             <td>

               <h:output_date id="output_date2" dateStyle="full"
                                  modelReference="LoginBean.date"/>


             </td>

	      <td>

		<h:output_errors id="output_date2_errors" 
			  clientId="output_date2" />

	      </td>

            </tr>

