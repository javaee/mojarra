           <tr>

             <td>

               <faces:output_text id="output_time1_label" 
                     value="output_time MEDIUM readonly"/>

             </td>


             <td>

               <faces:output_time id="output_time1" timeStyle="MEDIUM"
                         modelReference="LoginBean.date"/>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="output_time2_label" 
                     value="output_time LONG "/>

             </td>


             <td>

               <faces:output_time id="output_time2" 
                                 timeStyle="LONG"
                         modelReference="LoginBean.date"/>

             </td>

	      <td>

		<faces:output_errors id="output_time2_errors" 
		        compoundId="/standardRenderKitForm/output_time2" />

	      </td>

            </tr>

