<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:outputText id="inputText1Label"
                     value="inputText readonly"/>

             </td>


             <td>

               <h:inputText id="inputText1" 
                                 value="Text Value 1" 
                                 readonly="true"
                                 size="12" maxlength="20"
                                 alt="inputText readonly"
                                 accesskey="D" 
                                 title="inputText readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputText2Label" 
                     value="inputText"/>

             </td>


             <td>

               <h:inputText id="inputText2"
                                 value="Text Value 2" 
                                 alt="inputText"
                                  title="inputText"/>


             </td>

	      <td>

		<h:message id="inputText2Errors" 
			  for="inputText2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputText3Label" 
                     value="inputText"/>

             </td>


             <td>

               <h:inputText id="inputText3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="inputText"
                                  title="inputText"/>


             </td>

	      <td>

		<h:message id="inputText3Errors" 
			  for="inputText3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputText4Label" 
                     value="inputText"/>

             </td>


             <td>

               <h:inputText id="inputText4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="inputText"
                                 accesskey="d"
                               title="inputText"/>


             </td>

	      <td>

		<h:message id="inputText4Errors" 
			  for="inputText4" />

	      </td>

            </tr>

