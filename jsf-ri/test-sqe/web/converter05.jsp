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
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
    <html>
        <head>
            <title>Converters</title>
            <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
            <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
        </head>

        <body>
            <%
                java.util.Locale localeObject = new java.util.Locale("en_US");
                java.util.TimeZone tzObject =
                    java.util.TimeZone.getTimeZone("America/New_York");
                String localeString = "en_US";
                String timeZoneString = "America/New_York";

                request.setAttribute("localeObject", localeObject);
                request.setAttribute("timeZoneObject", tzObject);
                request.setAttribute("localeString", localeString);
                request.setAttribute("timeZoneString", timeZoneString);

            %>

            <f:view>
                <%--
                    Ensure timeZone and locale attributes can accept:
                       - literal string
                       - VE expression resolving to a String
                       - VE expression resolving to Locale or TimeZone instance
                         in the case of the locate and timeZone attributes (respectively)
                --%>
                <h:outputText id="outputDatetime1"
                              value="7/10/96 12:31:31 PM PDT">
                    <f:convertDateTime type="both" timeStyle="full"
                                       dateStyle="short"
                                       locale="en_US"
                                       timeZone="America/New_York"/>
                </h:outputText>

                <h:outputText id="outputDatetime2"
                              value="7/10/96 12:31:31 PM PDT">
                    <f:convertDateTime type="both" timeStyle="full"
                                       dateStyle="short"
                                       locale="#{requestScope.localeString}"
                                       timeZone="#{requestScope.timeZoneString}"/>
                </h:outputText>
                <h:outputText id="outputDatetime3"
                              value="7/10/96 12:31:31 PM PDT">
                    <f:convertDateTime type="both" timeStyle="full"
                                       dateStyle="short"
                                       locale="#{requestScope.localeObject}"
                                       timeZone="#{requestScope.timeZoneObject}"/>
                </h:outputText>
            </f:view>
        </body>
    </html>