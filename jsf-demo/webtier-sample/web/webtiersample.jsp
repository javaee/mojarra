<%--
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
