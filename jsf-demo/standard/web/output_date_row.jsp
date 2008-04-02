<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:outputText id="outputDate3Label"
                     value="output_date short"/>

             </td>


             <td>

               <h:outputText id="outputDate3" value="#{LoginBean.date}">
                   <f:convertDateTime dateStyle="short" type="date"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDate3Errors" 
			  for="outputDate3" />

	      </td>

            </tr>

           <tr>

             <td>
               <h:outputLabel id="date1id" for="output_date1">
               <h:outputText id="outputDate1Label" 
                     value="output_date medium"/>
               </h:outputLabel>

             </td>


             <td>

               <h:outputText id="outputDate1" value="#{LoginBean.date}">
                   <f:convertDateTime dateStyle="medium" type="date"/>
               </h:outputText>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputDate4Label" 
                     value="output_date long"/>

             </td>


             <td>

               <h:outputText id="outputDate4" value="#{LoginBean.date}">
                  <f:convertDateTime type="date" dateStyle="long"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDate4Errors" 
			  for="outputDate4" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputDate2Label" 
                     value="output_date FULL"/>

             </td>


             <td>

               <h:outputText id="outputDate2" value="#{LoginBean.date}">
                   <f:convertDateTime type="date" dateStyle="full"/>
               </h:outputText>


             </td>

	      <td>

		<h:message id="outputDate2Errors" 
			  for="outputDate2" />

	      </td>

            </tr>

