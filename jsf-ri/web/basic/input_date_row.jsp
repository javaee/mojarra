           <tr>

             <td>

               <faces:output_text id="input_date1_label" 
                     value="input_date MEDIUM readonly with PAGE_START label"/>

             </td>


             <td>

               <faces:input_date id="input_date1" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_date MEDIUM readonly"
                                 accesskey="D" labelAlign="PAGE_START"
                               title="input_date MEDIUM readonly">

                                <faces:output_text 
                                      id="input_date1_label_page_start" 
                           value="input_date MEDIUM readonly"/>

               </faces:input_date>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_date2_label" 
                     value="input_date MEDIUM with PAGE_END label"/>

             </td>


             <td>

               <faces:input_date id="input_date2" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 alt="input_date MEDIUM"
                                 labelAlign="PAGE_END"
                                  title="input_date MEDIUM">

                                <faces:output_text 
                                      id="input_date2_label_page_end" 
                           value="input_date MEDIUM"/>

               </faces:input_date>


             </td>

	      <td>

		<faces:output_errors id="input_date2_errors" 
			  compoundId="/standardRenderKitForm/input_date2" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_date3_label" 
                     value="input_date SHORT with LINE_START label"/>

             </td>


             <td>

               <faces:input_date id="input_date3" dateStyle="SHORT"
                                 value="01/12/1952" 
                                 size="10"
                                 alt="input_date SHORT"
                                 labelAlign="LINE_START"
                                  title="input_date SHORT">

                                <faces:output_text 
                                      id="input_date3_label_line_start" 
                           value="input_date SHORT"/>

               </faces:input_date>


             </td>

	      <td>

		<faces:output_errors id="input_date3_errors" 
			  compoundId="/standardRenderKitForm/input_date3" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_date4_label" 
                     value="input_date LONG with LINE_END label"/>

             </td>


             <td>

               <faces:input_date id="input_date4" dateStyle="LONG" 
                                 value="January 12, 1952" 
                                 size="20" maxlength="40"
                                 alt="input_date LONG with LINE_END label"
                                 accesskey="d"
                               title="input_date LONG with LINE_END label"
                                 labelAlign="LINE_END">

                                <faces:output_text 
                                      id="input_date4_label_line_end" 
                           value="input_date LONG"/>

               </faces:input_date>


             </td>

	      <td>

		<faces:output_errors id="input_date4_errors" 
			  compoundId="/standardRenderKitForm/input_date4" />

	      </td>

            </tr>

