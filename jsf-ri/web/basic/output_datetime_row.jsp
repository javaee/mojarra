           <tr>

             <td>

               <h:output_text id="output_datetime2_label" 
                     value="output_datetime pattern"/>

             </td>


             <td>

               <h:output_datetime id="output_datetime2" 
                            formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"
                               modelReference="LoginBean.date"/>

             </td>

	      <td>

		<h:output_errors id="output_datetime2_errors" 
		        clientId="output_datetime2" />

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
                               modelReference="LoginBean.date"/>

             </td>

	      <td>

		<h:output_errors id="output_datetime3_errors" 
		        clientId="output_datetime2" />

	      </td>

            </tr>

