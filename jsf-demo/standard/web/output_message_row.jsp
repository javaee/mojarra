<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:outputText id="outputMessage2Label"
                     value="outputFormat value parameter substitution"/>

             </td>


             <td>

              <h:outputFormat id="userMsg" value="Param 0: {0} Param 1: {1} Param 2: {2} " >
                  <f:param value="#{LoginBean.date}"/>
                  <f:param  value="param 2"/>
                  <f:param  value="param 3"/>
              </h:outputFormat>


             </td>

            </tr>

           <tr>

             <td>

               <h:outputText id="outputMessage1Label" 
                     value="outputFormat bundle parameter substitution"/>

             </td>


             <td>

              <h:outputFormat id="userMsg1" 
	          value="#{standardBundle.outputMessageKey}">
                  <f:param value="#{LoginBean.date}"/>
                  <f:param value="param 5"/>
                  <f:param value="param 6"/>
              </h:outputFormat>


             </td>

            </tr>

