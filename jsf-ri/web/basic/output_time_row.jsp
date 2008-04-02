           <tr>

             <td>

               <h:output_text id="output_time1_label" 
                     value="output_time MEDIUM readonly"/>

             </td>


             <td>

               <h:output_time id="output_time1" timeStyle="MEDIUM"
                         modelReference="LoginBean.date"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_time2_label" 
                     value="output_time LONG "/>

             </td>


             <td>

               <h:output_time id="output_time2" 
                                 timeStyle="LONG"
                         modelReference="LoginBean.date"/>

             </td>

	      <td>

		<h:output_errors id="output_time2_errors" 
		        compoundId="/standardRenderKitForm/output_time2" />

	      </td>

            </tr>

