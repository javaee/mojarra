           <tr>

             <td>

               <faces:output_text id="input_secret1_label" 
                     value="input_secret readonly with PAGE_START label"/>

             </td>


             <td>

               <faces:input_secret id="input_secret1" 
                                 value="Text Value 1" 
                                 readonly="true"
                                 size="12" maxlength="20"
                                 alt="input_secret readonly"
                                 accesskey="D" labelAlign="PAGE_START"
                                 title="input_secret readonly">

                                <faces:output_text 
                                      id="input_secret1_label_page_start" 
                           value="input_secret readonly"/>

               </faces:input_secret>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_secret2_label" 
                     value="input_secret with PAGE_END label"/>

             </td>


             <td>

               <faces:input_secret id="input_secret2"
                                 value="Text Value 2" 
                                 alt="input_secret"
                                 labelAlign="PAGE_END"
                                  title="input_secret">

                                <faces:output_text 
                                      id="input_secret2_label_page_end" 
                           value="input_secret"/>

               </faces:input_secret>


             </td>

	      <td>

		<faces:output_errors id="input_secret2_errors" 
			  compoundId="/standardRenderKitForm/input_secret2" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_secret3_label" 
                     value="input_secret with LINE_START label"/>

             </td>


             <td>

               <faces:input_secret id="input_secret3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="input_secret"
                                 labelAlign="LINE_START"
                                  title="input_secret">

                                <faces:output_text 
                                      id="input_secret3_label_line_start" 
                           value="input_secret"/>

               </faces:input_secret>


             </td>

	      <td>

		<faces:output_errors id="input_secret3_errors" 
			  compoundId="/standardRenderKitForm/input_secret3" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_secret4_label" 
                     value="input_secret with LINE_END label"/>

             </td>


             <td>

               <faces:input_secret id="input_secret4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="input_secret with LINE_END label"
                                 accesskey="d"
                               title="input_secret with LINE_END label"
                                 labelAlign="LINE_END">

                                <faces:output_text 
                                      id="input_secret4_label_line_end" 
                           value="input_secret"/>

               </faces:input_secret>


             </td>

	      <td>

		<faces:output_errors id="input_secret4_errors" 
			  compoundId="/standardRenderKitForm/input_secret4" />

	      </td>

            </tr>

