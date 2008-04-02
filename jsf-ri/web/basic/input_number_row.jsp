           <tr>

             <td>

               <h:output_text id="input_number1_label" 
                     value="input_number number readonly"/>

             </td>


             <td>

               <h:input_number id="input_number1" numberStyle="number"
                                 value="1239989.6079" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_number number readonly"
                                 accesskey="N"
                               title="input_number number readonly" />

             </td>
             <td>

             <h:output_errors id="input_number1_errors"
                          clientId="input_number1" />

             </td>
            </tr>

           <tr>

             <td>

               <h:output_text id="input_number2_label" 
                     value="input_number currency"/>

             </td>


             <td>

               <h:input_number id="input_number2" numberStyle="currency"
                                 value="$1234789.60" 
                                 alt="input_number currency"
                                  title="input_number currency" />
             </td>

	      <td>

		<h:output_errors id="input_number2_errors" 
			  clientId="input_number2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="input_number3_label" 
                     value="input_number percent "/>

             </td>


             <td>

               <h:input_number id="input_number3" numberStyle="percent"
                                 value="45%" 
                                 size="10"
                                 alt="input_number percent"
                                  title="input_number percent" />

             </td>

	      <td>

		<h:output_errors id="input_number3_errors" 
			  clientId="input_number3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="input_number4_label" 
                     value="input_number PATTERN "/>

             </td>


             <td>

               <h:input_number id="input_number4" formatPattern="####" 
                                 value="9999.987651" 
                                 size="20" maxlength="40"
                                 alt="input_number PATTERN "
                                 accesskey="d"
                               title="input_number PATTERN " />
             </td>

	      <td>

		<h:output_errors id="input_number4_errors" 
			  clientId="input_number4" />

	      </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="input_number5_label"
                     value="input_number integer with modelReference"/>

             </td>


             <td>

               <h:input_number id="input_number5" numberStyle="integer"
                                 value="9"
                                 size="2" maxlength="10"
                                 modelReference="LoginBean.char"
                                 alt="input_number integer with modelReference"
                                 accesskey="d"
                               title="input_number integer with modelReference" />
             </td>

              <td>

                <h:output_errors id="input_number5_errors"
                          clientId="input_number5" />

              </td>

            </tr>


