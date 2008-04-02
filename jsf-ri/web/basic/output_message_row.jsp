<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="output_message2_label" 
                     value="output_message value parameter substitution"/>

             </td>


             <td>

              <h:output_message id="userMsg" value="Param 0: {0} Param 1: {1} Param 2: {2} " >
                  <f:parameter modelReference="LoginBean.date"/>
                  <f:parameter  value="param 2"/>
                  <f:parameter  value="param 3"/>
              </h:output_message>


             </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="output_message1_label" 
                     value="output_message bundle parameter substitution"/>

             </td>


             <td>

              <h:output_message id="userMsg1" 
                       key="outputMessageKey" bundle="basicBundle">
                  <f:parameter modelReference="LoginBean.date"/>
                  <f:parameter value="param 5"/>
                  <f:parameter value="param 6"/>
              </h:output_message>


             </td>

            </tr>

