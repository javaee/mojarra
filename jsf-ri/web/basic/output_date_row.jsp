           <tr>

             <td>

               <faces:output_text id="output_date3_label" 
                     value="output_date SHORT"/>

             </td>


             <td>

               <faces:output_date id="output_date3" dateStyle="SHORT"
                                     modelReference="LoginBean.date"/>


             </td>

	      <td>

		<faces:output_errors id="output_date3_errors" 
			  compoundId="/standardRenderKitForm/output_date3" />

	      </td>

            </tr>

           <tr>

             <td>
               <faces:output_label id="date1id" for="output_date1">
               <faces:output_text id="output_date1_label" 
                     value="output_date MEDIUM"/>
               </faces:output_label>

             </td>


             <td>

               <faces:output_date id="output_date1" dateStyle="MEDIUM"
                               modelReference="LoginBean.date"/>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="output_date4_label" 
                     value="output_date LONG"/>

             </td>


             <td>

               <faces:output_date id="output_date4" dateStyle="LONG" 
                               modelReference="LoginBean.date"/>


             </td>

	      <td>

		<faces:output_errors id="output_date4_errors" 
			  compoundId="/standardRenderKitForm/output_date4" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="output_date2_label" 
                     value="output_date FULL"/>

             </td>


             <td>

               <faces:output_date id="output_date2" dateStyle="FULL"
                                  modelReference="LoginBean.date"/>


             </td>

	      <td>

		<faces:output_errors id="output_date2_errors" 
			  compoundId="/standardRenderKitForm/output_date2" />

	      </td>

            </tr>

