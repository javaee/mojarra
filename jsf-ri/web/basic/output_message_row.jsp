           <tr>

             <td>

               <faces:output_text id="output_message2_label" 
                     value="output_message value parameter substitution"/>

             </td>


             <td>

              <faces:output_message id="userMsg" value="Param 0: {0} Param 1: {1} Param 2: {2} " >
                  <faces:parameter id="param1" 
                      modelReference="LoginBean.date"/>
                  <faces:parameter id="param2" 
                      value="param 2"/>
                  <faces:parameter id="param3" 
                      value="param 3"/>
              </faces:output_message>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="output_message1_label" 
                     value="output_message bundle parameter substitution"/>

             </td>


             <td>

              <faces:output_message id="userMsg1" 
                       key="outputMessageKey" bundle="basicBundle">
                  <faces:parameter id="param4" 
                      modelReference="LoginBean.date"/>
                  <faces:parameter id="param5" 
                      value="param 5"/>
                  <faces:parameter id="param6" 
                      value="param 6"/>
              </faces:output_message>


             </td>

            </tr>

