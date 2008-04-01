           <tr>

             <td>

               <faces:output_text id="input_secret1_label" 
                     value="input_secret readonly"/>

             </td>


             <td>

               <faces:input_secret id="input_secret1" 
                                 value="Text Value 1" 
                                 readonly="true"
                                 size="12" maxlength="20"
                                 alt="input_secret readonly"
                                 accesskey="D" 
                                 title="input_secret readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_secret2_label" 
                     value="input_secret"/>

             </td>


             <td>

               <faces:input_secret id="input_secret2"
                                 value="Text Value 2" 
                                 alt="input_secret"
                                  title="input_secret"/>


             </td>

	      <td>

		<faces:output_errors id="input_secret2_errors" 
			  compoundId="/standardRenderKitForm/input_secret2" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_secret3_label" 
                     value="input_secret"/>

             </td>


             <td>

               <faces:input_secret id="input_secret3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="input_secret"
                                  title="input_secret"/>


             </td>

	      <td>

		<faces:output_errors id="input_secret3_errors" 
			  compoundId="/standardRenderKitForm/input_secret3" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_secret4_label" 
                     value="input_secret"/>

             </td>


             <td>

               <faces:input_secret id="input_secret4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="input_secret"
                                 accesskey="d"
                               title="input_secret"/>


             </td>

	      <td>

		<faces:output_errors id="input_secret4_errors" 
			  compoundId="/standardRenderKitForm/input_secret4" />

	      </td>

            </tr>

