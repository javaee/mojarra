           <tr>

             <td>

               <faces:output_text id="input_text1_label" 
                     value="input_text readonly with PAGE_START label"/>

             </td>


             <td>

               <faces:input_text id="input_text1" 
                                 value="Text Value 1" 
                                 readonly="true"
                                 size="12" maxlength="20"
                                 alt="input_text readonly"
                                 accesskey="D" labelAlign="PAGE_START"
                                 title="input_text readonly">

                                <faces:output_text 
                                      id="input_text1_label_page_start" 
                           value="input_text readonly"/>

               </faces:input_text>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_text2_label" 
                     value="input_text with PAGE_END label"/>

             </td>


             <td>

               <faces:input_text id="input_text2"
                                 value="Text Value 2" 
                                 alt="input_text"
                                 labelAlign="PAGE_END"
                                  title="input_text">

                                <faces:output_text 
                                      id="input_text2_label_page_end" 
                           value="input_text"/>

               </faces:input_text>


             </td>

	      <td>

		<faces:output_errors id="input_text2_errors" 
			  compoundId="/standardRenderKitForm/input_text2" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_text3_label" 
                     value="input_text with LINE_START label"/>

             </td>


             <td>

               <faces:input_text id="input_text3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="input_text"
                                 labelAlign="LINE_START"
                                  title="input_text">

                                <faces:output_text 
                                      id="input_text3_label_line_start" 
                           value="input_text"/>

               </faces:input_text>


             </td>

	      <td>

		<faces:output_errors id="input_text3_errors" 
			  compoundId="/standardRenderKitForm/input_text3" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_text4_label" 
                     value="input_text with LINE_END label"/>

             </td>


             <td>

               <faces:input_text id="input_text4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="input_text with LINE_END label"
                                 accesskey="d"
                               title="input_text with LINE_END label"
                                 labelAlign="LINE_END">

                                <faces:output_text 
                                      id="input_text4_label_line_end" 
                           value="input_text"/>

               </faces:input_text>


             </td>

	      <td>

		<faces:output_errors id="input_text4_errors" 
			  compoundId="/standardRenderKitForm/input_text4" />

	      </td>

            </tr>

