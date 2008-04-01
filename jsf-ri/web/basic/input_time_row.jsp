           <tr>

             <td>

               <faces:output_text id="input_time1_label" 
                     value="input_time MEDIUM readonly"/>

             </td>


             <td>

               <faces:input_time id="input_time1" timeStyle="MEDIUM"
                                 value="10:00:01 PM" 
                                 readonly="true"

                                 size="10" maxlength="20"
                                 alt="input_time MEDIUM readonly"
                                 accesskey="D" 
                               title="input_time MEDIUM readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_time2_label" 
                     value="input_time LONG "/>

             </td>


             <td>

               <faces:input_time id="input_time2" 
                                 timeStyle="LONG"
                                 value="9:57:00 AM PST"
                                 alt="input_time LONG"
                               title="input_time LONG"/>

             </td>

	      <td>

		<faces:output_errors id="input_time2_errors" 
		        compoundId="/standardRenderKitForm/input_time2" />

	      </td>

            </tr>

