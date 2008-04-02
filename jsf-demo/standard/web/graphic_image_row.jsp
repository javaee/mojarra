<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="outputGraphic1Label"
                     value="output_graphic with hard coded image"/>

             </td>

             <td>

               <h:graphic_image id="outputGraphic1" url="duke.gif" 
	                            alt="output_graphic with hard coded image"
                                 title="output_graphic with hard coded image"
               />

             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputGraphic2Label" 
                     value="output_graphic with localized image"/>

             </td>

             <td>

               <h:graphic_image id="outputGraphic2" 
                                    url="#{standardBundle.imageurl}"
	                            alt="output_graphic with localized image"
                                title="output_graphic with localized image"
               />


             </td>

            </tr>


           <tr>

             <td>

               <h:output_text id="outputGraphic3Label" 
                     value="output_graphic with path from model"/>

             </td>

             <td>

               <h:graphic_image id="outputGraphic3" 
	                            value="#{LoginBean.imagePath}"
	                            alt="output_graphic with path from model"
                                title="output_graphic with path from model"
               />

             </td>

            </tr>

