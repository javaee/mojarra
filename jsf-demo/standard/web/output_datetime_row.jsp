<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="outputDatetime2Label"
                     value="output_datetime pattern"/>

             </td>


             <td>

               <h:output_text id="outputDatetime2" value="#{LoginBean.date}">
                   <f:convertDateTime pattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"/>
              </h:output_text>

             </td>

	      <td>

		<h:message id="outputDatetime2Errors" 
		        for="outputDatetime2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputDatetime3Label" 
                     value="output_datetime dateStyle=short timeStyle=full"/>
             <td>

               <h:output_text id="outputDatetime3" value="#{LoginBean.date}">
                   <f:convertDateTime type="both" timeStyle="full" dateStyle="short"/>
               </h:output_text>


             </td>

	      <td>

		<h:message id="outputDatetime3Errors" 
		        for="outputDatetime2" />

	      </td>

            </tr>

