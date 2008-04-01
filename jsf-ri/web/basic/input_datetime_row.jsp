           <tr>

             <td>

               <faces:output_text id="input_datetime1_label" 
                     value="input_datetime MEDIUM readonly with PAGE_START label"/>

             </td>


             <td>

               <faces:input_datetime id="input_datetime1" dateStyle="MEDIUM"
                                 timeStyle="MEDIUM"
                                 value="10:00:01 PM" 
                                 readonly="true"

                                 size="10" maxlength="20"
                                 alt="input_datetime MEDIUM readonly"
                                 accesskey="D" labelAlign="PAGE_START"
                               title="input_datetime MEDIUM readonly">

                                <faces:output_text 
                                      id="input_datetime1_label_page_start" 
                           value="input_datetime MEDIUM readonly"/>

               </faces:input_datetime>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_datetime2_label" 
                     value="input_datetime pattern with no label"/>

             </td>


             <td>

               <faces:input_datetime id="input_datetime2" 
                            formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"
                                 value="Wed, Aug 21, 2002 AD at 10:57:00 AM" 
                                 size="30" 
                                 alt="input_datetime pattern"
                               title="input_datetime pattern"/>

             </td>

	      <td>

		<faces:output_errors id="input_datetime2_errors" 
		        compoundId="/standardRenderKitForm/input_datetime2" />

	      </td>

            </tr>

