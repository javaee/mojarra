<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="outputDate3Label"
                     value="output_date short"/>

             </td>


             <td>

               <h:output_text id="outputDate3" value="#{LoginBean.date}">
                   <f:convertDateTime dateStyle="short" type="date"/>
               </h:output_text>


             </td>

	      <td>

		<h:message id="outputDate3Errors" 
			  for="outputDate3" />

	      </td>

            </tr>

           <tr>

             <td>
               <h:output_label id="date1id" for="output_date1">
               <h:output_text id="outputDate1Label" 
                     value="output_date medium"/>
               </h:output_label>

             </td>


             <td>

               <h:output_text id="outputDate1" value="#{LoginBean.date}">
                   <f:convertDateTime dateStyle="medium" type="date"/>
               </h:output_text>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputDate4Label" 
                     value="output_date long"/>

             </td>


             <td>

               <h:output_text id="outputDate4" value="#{LoginBean.date}">
                  <f:convertDateTime type="date" dateStyle="long"/>
               </h:output_text>


             </td>

	      <td>

		<h:message id="outputDate4Errors" 
			  for="outputDate4" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputDate2Label" 
                     value="output_date FULL"/>

             </td>


             <td>

               <h:output_text id="outputDate2" value="#{LoginBean.date}">
                   <f:convertDateTime type="date" dateStyle="full"/>
               </h:output_text>


             </td>

	      <td>

		<h:message id="outputDate2Errors" 
			  for="outputDate2" />

	      </td>

            </tr>

