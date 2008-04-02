<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:outputText id="inputSecret1Label"
                     value="inputSecret readonly"/>

             </td>


             <td>

               <h:inputSecret id="inputSecret1" 
                                 value="Text Value 1" 
                                 readonly="true"
                                 size="12" maxlength="20"
                                 alt="inputSecret readonly"
                                 accesskey="D" 
                                 title="inputSecret readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputSecret2Label" 
                     value="inputSecret"/>

             </td>


             <td>

               <h:inputSecret id="inputSecret2"
                                 value="Text Value 2" 
                                 alt="inputSecret"
                                  title="inputSecret"/>


             </td>

	      <td>

		<h:message id="inputSecret2Errors" 
			  for="inputSecret2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputSecret3Label" 
                     value="inputSecret"/>

             </td>


             <td>

               <h:inputSecret id="inputSecret3"
                                 value="Text Value 3" 
                                 size="12"
                                 alt="inputSecret"
                                  title="inputSecret"/>


             </td>

	      <td>

		<h:message id="inputSecret3Errors" 
			  for="inputSecret3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="inputSecret4Label" 
                     value="inputSecret"/>

             </td>


             <td>

               <h:inputSecret id="inputSecret4" 
                                 value="Text Value 4" 
                                 size="20" maxlength="40"
                                 alt="inputSecret"
                                 accesskey="d"
                               title="inputSecret"/>


             </td>

	      <td>

		<h:message id="inputSecret4Errors" 
			  for="inputSecret4" />

	      </td>

            </tr>

