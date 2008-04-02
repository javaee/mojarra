           <tr>

             <td>
               <h:output_label id="date1id" for="input_date1">
               <h:output_text id="input_date1_label" 
                     value="input_date MEDIUM readonly"/>
               </h:output_label>

             </td>


             <td>

               <h:input_date id="input_date1" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_date MEDIUM readonly"
                                 accesskey="D" 
                               title="input_date MEDIUM readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="input_date2_label" 
                     value="input_date MEDIUM"/>

             </td>


             <td>

               <h:input_date id="input_date2" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 alt="input_date MEDIUM"
                                  title="input_date MEDIUM"/>


             </td>

	      <td>

		<h:output_errors id="input_date2_errors" 
			  compoundId="/standardRenderKitForm/input_date2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="input_date3_label" 
                     value="input_date SHORT"/>

             </td>


             <td>

               <h:input_date id="input_date3" dateStyle="SHORT"
                                 value="01/12/1952" 
                                 size="10"
                                 alt="input_date SHORT"
                                  title="input_date SHORT"/>


             </td>

	      <td>

		<h:output_errors id="input_date3_errors" 
			  compoundId="/standardRenderKitForm/input_date3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="input_date4_label" 
                     value="input_date LONG"/>

             </td>


             <td>

               <h:input_date id="input_date4" dateStyle="LONG" 
                                 value="January 12, 1952" 
                                 size="20" maxlength="40"
                                 alt="input_date LONG"
                                 accesskey="d"
                               title="input_date LONG"/>


             </td>

	      <td>

		<h:output_errors id="input_date4_errors" 
			  compoundId="/standardRenderKitForm/input_date4" />

	      </td>

            </tr>

