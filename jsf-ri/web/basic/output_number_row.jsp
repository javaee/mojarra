           <tr>

             <td>

               <faces:output_text id="output_number1_label" 
                     value="output_number NUMBER"/>

             </td>


             <td>

               <faces:output_number id="output_number1" numberStyle="NUMBER"
                               modelReference="LoginBean.floater"/>

             </td>
             <td>

             <faces:output_errors id="output_number1_errors"
                          compoundId="/standardRenderKitForm/output_number1" />

             </td>
            </tr>

           <tr>

             <td>

               <faces:output_text id="output_number2_label" 
                     value="output_number CURRENCY"/>

             </td>


             <td>

               <faces:output_number id="output_number2" numberStyle="CURRENCY"
                          modelReference="LoginBean.floater"/>
             </td>

	      <td>

		<faces:output_errors id="output_number2_errors" 
			  compoundId="/standardRenderKitForm/output_number2" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="output_number3_label" 
                     value="output_number PERCENT "/>

             </td>


             <td>

               <faces:output_number id="output_number3" numberStyle="PERCENT"
                          modelReference="LoginBean.floater" />

             </td>

	      <td>

		<faces:output_errors id="output_number3_errors" 
			  compoundId="/standardRenderKitForm/output_number3" />

	      </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="output_number4_label" 
                     value="output_number PATTERN "/>

             </td>


             <td>

               <faces:output_number id="output_number4" formatPattern="####" 
                         modelReference="LoginBean.floater" />
             </td>

	      <td>

		<faces:output_errors id="output_number4_errors" 
			  compoundId="/standardRenderKitForm/output_number4" />

	      </td>

            </tr>

            <tr>

             <td>

               <faces:output_text id="output_number5_label"
                     value="output_number INTEGER with modelReference"/>

             </td>


             <td>

               <faces:output_number id="output_number5" numberStyle="INTEGER"
                                 modelReference="${LoginBean.char}" />
             </td>

              <td>

                <faces:output_errors id="output_number5_errors"
                          compoundId="/standardRenderKitForm/output_number5" />

              </td>

            </tr>


