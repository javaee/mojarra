<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:outputText id="outputText1Label"
                     value="outputText"/>

             </td>


             <td>

               <h:outputText id="outputText1" 
                       value="#{LoginBean.userName}"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputText0Label" 
                     value="outputText with outputClass"/>

             </td>


             <td>

               <h:outputText id="outputText0" 
                       styleClass="outputText0"
                       value="#{LoginBean.userName}"/>


             </td>

            </tr>


           <tr>

             <td>

               <h:outputText id="outputText2Label" 
                     value="outputText from bundle"/>

             </td>


             <td>

               <h:outputText id="outputText2"
                                  value="#{standardBundle.linkLabel}"/>


             </td>

	      <td>

		<h:message id="outputText2Errors" 
			  for="outputText2" />

	      </td>

            </tr>

