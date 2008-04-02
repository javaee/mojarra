           <tr>

             <td>

               <h:output_text id="output_number1_label" 
                     value="output_number number"/>

             </td>


             <td>

               <h:output_number id="output_number1" numberStyle="number"
                               modelReference="LoginBean.floater"/>

             </td>
             <td>

             <h:output_errors id="output_number1_errors"
                          compoundId="/standardRenderKitForm/output_number1" />

             </td>
            </tr>

           <tr>

             <td>

               <h:output_text id="output_number2_label" 
                     value="output_number currency"/>

             </td>


             <td>

               <h:output_number id="output_number2" numberStyle="currency"
                          modelReference="LoginBean.floater"/>
             </td>

	      <td>

		<h:output_errors id="output_number2_errors" 
			  compoundId="/standardRenderKitForm/output_number2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_number3_label" 
                     value="output_number percent "/>

             </td>


             <td>

               <h:output_number id="output_number3" numberStyle="percent"
                          modelReference="LoginBean.floater" />

             </td>

	      <td>

		<h:output_errors id="output_number3_errors" 
			  compoundId="/standardRenderKitForm/output_number3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_number4_label" 
                     value="output_number PATTERN "/>

             </td>


             <td>

               <h:output_number id="output_number4" formatPattern="####" 
                         modelReference="LoginBean.floater" />
             </td>

	      <td>

		<h:output_errors id="output_number4_errors" 
			  compoundId="/standardRenderKitForm/output_number4" />

	      </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="output_number5_label"
                     value="output_number integer with modelReference"/>

             </td>


             <td>

               <h:output_number id="output_number5" numberStyle="integer"
                                 modelReference="${LoginBean.char}" />
             </td>

              <td>

                <h:output_errors id="output_number5_errors"
                          compoundId="/standardRenderKitForm/output_number5" />

              </td>

            </tr>


