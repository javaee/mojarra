           <tr>

             <td>

               <faces:output_text id="input_textarea1_label" 
                     value="input_textarea readonly with PAGE_START label"/>

             </td>


             <td>

               <faces:input_textarea id="input_textarea1" 
                                 value="initial text"
                                 readonly="true"
                                 rows="5" cols="20"
                                 alt="input_textarea readonly"
                                 accesskey="D" labelAlign="PAGE_START" 
                                 title="input_textarea readonly">

                                <faces:output_text 
                                      id="input_textarea1_label_page_start" 
                           value="input_textarea readonly"/>

               </faces:input_textarea>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_textarea2_label" 
                     value="input_textarea with PAGE_END label"/>

             </td>


             <td>

               <faces:input_textarea id="input_textarea2"
                                 rows="5" cols="20"
                                 alt="input_textarea"
                                 labelAlign="PAGE_END"
                                  title="input_textarea">

                                <faces:output_text 
                                      id="input_textarea2_label_page_end" 
                           value="input_textarea"/>

               </faces:input_textarea>


             </td>

	      <td>

		<faces:output_errors id="input_textarea2_errors" 
			  compoundId="/standardRenderKitForm/input_textarea2" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_textarea3_label" 
                     value="input_textarea with LINE_START label"/>

             </td>


             <td>

               <faces:input_textarea id="input_textarea3"
                                 rows="5" cols="20"
                                 alt="input_textarea"
                                 labelAlign="LINE_START"
                                  title="input_textarea">

                                <faces:output_text 
                                      id="input_textarea3_label_line_start" 
                           value="input_textarea"/>

               </faces:input_textarea>


             </td>

	      <td>

		<faces:output_errors id="input_textarea3_errors" 
			  compoundId="/standardRenderKitForm/input_textarea3" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_textarea4_label" 
                     value="input_textarea with LINE_END label"/>

             </td>


             <td>

               <faces:input_textarea id="input_textarea4" 
                                 rows="5" cols="20"
                                 alt="input_textarea with LINE_END label"
                                 accesskey="d"
                               title="input_textarea with LINE_END label"
                                 labelAlign="LINE_END">

                                <faces:output_text 
                                      id="input_textarea4_label_line_end" 
                           value="input_textarea"/>

               </faces:input_textarea>


             </td>

	      <td>

		<faces:output_errors id="input_textarea4_errors" 
			  compoundId="/standardRenderKitForm/input_textarea4" />

	      </td>

            </tr>

