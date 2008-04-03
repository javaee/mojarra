<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
--%>

<?xml version='1.0' encoding='UTF-8'?>
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/ri/sandbox" prefix="risb"%>
<html>
    <f:view>
        <h:dataTable id="books"
            columnClasses="list-column-center,list-column-right,list-column-center,list-column-right"
            headerClass="list-header" 
            rowClasses="list-row" 
            styleClass="list-background" 
            value="#{BookStore.items}"
            var="store">
            <h:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.storeNameLabel}" />
                </f:facet>
                <h:outputText value="#{store.name}">
                    <risb:contextMenu>
                        <risb:menuItem>
                            <h:commandLink action="#{testBean.edit}" value="Edit">
                                <f:setPropertyActionListener value="#{store.id}" target="#{testBean.id}" />
                            </h:commandLink>
                        </risb:menuItem>
                        <risb:menuItem>
                            <h:commandLink action="#{testBean.delete}" value="Delete">
                                <f:setPropertyActionListener value="#{store.id}" target="#{testBean.id}" />
                            </h:commandLink>
                        </risb:menuItem>
                    </risb:contextMenu>
                </h:outputText>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <Subject
                </f:facet>
                <h:outputText value="#{store.subject}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.storePriceLabel}" />
                </f:facet>
                <h:outputText value="#{store.price}" />
            </h:column>
        </h:dataTable>
    </f:view>
</html>
