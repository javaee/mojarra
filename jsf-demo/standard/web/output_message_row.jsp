<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="outputMessage2Label"
                     value="output_message value parameter substitution"/>

             </td>


             <td>

              <h:output_message id="userMsg" value="Param 0: {0} Param 1: {1} Param 2: {2} " >
                  <f:param value="#{LoginBean.date}"/>
                  <f:param  value="param 2"/>
                  <f:param  value="param 3"/>
              </h:output_message>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputMessage1Label" 
                     value="output_message bundle parameter substitution"/>

             </td>


             <td>

              <h:output_message id="userMsg1" 
	          value="#{standardBundle.outputMessageKey}">
                  <f:param value="#{LoginBean.date}"/>
                  <f:param value="param 5"/>
                  <f:param value="param 6"/>
              </h:output_message>


             </td>

            </tr>

