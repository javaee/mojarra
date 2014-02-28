<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Simple Webtier Sample</title>
        <style type="text/css">
            .standard {
                background-color: ${Color.linen.hex};
            }
            .evenRow {
                background-color: ${Color.ivory.hex};
            }
            .oddRow { 
                background-color: ${Color.LightGrey.hex};
            }
            fieldset {                
                border: #26a solid 1px;
                background-color: ${Color.snow.hex};
            }
            table {
                margin: auto;
            }
            body {
                background-color: ${Color.OldLace.hex};
            }
        </style>
    </head>
    <body>                   
  
        <f:view>  
            <h:form id="form">
                <fieldset class="centerSet">
                    <legend>Sample Output</legend>

                    <table border="1">
                        <c:forTokens items="Title,Author,ISBN,Price,Quantity"
                                     delims=","
                                     var="headerValue">
                            <th class="standard">${headerValue}</th>
                        </c:forTokens>
                        
                        <c:forEach items="#{BooksBean.books}" var="book"
                                   varStatus="stat">

                            <tr class="${(stat.index % 2) == 0 ? "evenRow" : "oddRow"}">
                                <td>
                                    <h:outputText id="title"
                                                  value="#{book.title}"/>
                                </td>
                                <td>
                                    <h:outputText id="author"
                                                  value="#{book.author}"/>
                                </td>
                                <td>
                                    <h:outputText id="isbn"
                                                  value="#{book.isbn}"/>
                                </td>
                                <td>
                                    <h:outputText id="price"
                                                  value="#{book.price}"/>
                                </td>
                                <td>
                                    <h:inputText id="quantity" 
                                                 size="3"
                                                 value="#{book.quantity}"
                                                 validator="#{book.validateQuantity}"/>                                                                                                                
                                </td>                            
                            </tr>

                        </c:forEach>
                            <tr class="standard">
                                <td colspan="5" >
                                    Total Cost: <h:outputText id="totalCost"
                                                              value="#{BooksBean.totalCost}">
                                                   <f:convertNumber minFractionDigits="2" maxFractionDigits="2" />
                                                </h:outputText>
                                </td>
                            </tr>
                            <tr class="standard">
                                <td colspan="5" >
                                    <h:commandButton id="update" 
                                                     type="submit"
                                                     value="Update"/>                            
                                </td>
                            </tr>
                    </table>  
                    <h:messages showSummary="true"/>
                </fieldset>
            </h:form>
        </f:view>
    </body>
</html>
