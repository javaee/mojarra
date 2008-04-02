<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:outputText id="outputTime1Label"
                     value="output_time medium readonly"/>

             </td>


             <td>

               <h:outputText id="outputTime1" value="#{LoginBean.date}">
                   <f:convertDateTime type="time" timeStyle="medium"/>
               </h:outputText>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputTime2Label" 
                     value="output_time long "/>

             </td>


             <td>

               <h:outputText id="outputTime2" value="#{LoginBean.date}">
                   <f:convertDateTime type="time" timeStyle="long"/>
               </h:outputText>

             </td>

	      <td>

		<h:message id="outputTime2Errors" 
		        for="outputTime2" />

	      </td>

            </tr>

