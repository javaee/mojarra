           <tr>

             <td>

               <h:output_text id="output_graphic1_label" 
                     value="output_graphic with hard coded image"/>

             </td>

             <td>

               <h:graphic_image id="output_graphic1" url="/duke.gif" 
	                            alt="output_graphic with hard coded image"
                                  title="output_graphic with hard coded image"
                           />

             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_graphic2_label" 
                     value="output_graphic with localized image"/>

             </td>

             <td>

               <h:graphic_image id="output_graphic2" key="imageurl"
                                    bundle="basicBundle"
	                            alt="output_graphic with localized image"
                                  title="output_graphic with localized image"
                           />


             </td>

            </tr>

