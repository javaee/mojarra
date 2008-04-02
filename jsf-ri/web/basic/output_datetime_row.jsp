           <tr>

             <td>

               <faces:output_text id="output_datetime2_label" 
                     value="output_datetime pattern"/>

             </td>


             <td>

               <faces:output_datetime id="output_datetime2" 
                            formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"
                               modelReference="LoginBean.date"/>

             </td>

	      <td>

		<faces:output_errors id="output_datetime2_errors" 
		        compoundId="/standardRenderKitForm/output_datetime2" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="output_datetime3_label" 
                     value="output_datetime dateStyle=SHORT timeStyle=FULL"/>

             </td>


             <td>

               <faces:output_datetime id="output_datetime3" 
                    dateStyle="SHORT" timeStyle="FULL"
                               modelReference="LoginBean.date"/>

             </td>

	      <td>

		<faces:output_errors id="output_datetime3_errors" 
		        compoundId="/standardRenderKitForm/output_datetime2" />

	      </td>

            </tr>

