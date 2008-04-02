<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>
               <h:outputLabel id="date1id" for="inputDate1">
               <h:outputText id="inputDate1Label" 
                     value="input_date medium readonly"/>
               </h:outputLabel>

             </td>


             <td>

               <h:inputText id="inputDate1" 
                                 value="#{model.date1}"
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_date medium readonly"
                                 accesskey="D" 
                               title="input_date medium readonly">
                   <f:convertDateTime type="date" dateStyle="medium"/>
               </h:inputText>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputDate2Label" 
                     value="input_date medium"/>

             </td>


             <td>

               <h:inputText id="inputDate2"
                                 value="#{model.date2}"
                                 alt="input_date medium"
                                  title="input_date medium">
                   <f:convertDateTime type="date" dateStyle="medium"/>
               </h:inputText>


             </td>

	      <td>

		<h:message id="inputDate2Errors" 
			  for="inputDate2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputDate3Label" 
                     value="input_date short"/>

             </td>


             <td>

               <h:inputText id="inputDate3" 
                                 value="#{model.date3}"
                                 size="10"
                                 alt="input_date short"
                                  title="input_date short">
                   <f:convertDateTime type="date" dateStyle="short"/>
               </h:inputText>


             </td>

	      <td>

		<h:message id="inputDate3Errors" 
			  for="inputDate3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputDate4Label" 
                     value="input_date long"/>

             </td>


             <td>

               <h:inputText id="inputDate4" 
                                 value="#{model.date4}"
                                 size="20" maxlength="40"
                                 alt="input_date long"
                                 accesskey="d"
                               title="input_date long">
                   <f:convertDateTime type="date" dateStyle="long"/>
               </h:inputText>


             </td>

	      <td>

		<h:message id="inputDate4Errors" 
			  for="inputDate4" />

	      </td>

            </tr>

