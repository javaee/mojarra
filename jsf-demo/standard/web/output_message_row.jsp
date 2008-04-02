<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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

