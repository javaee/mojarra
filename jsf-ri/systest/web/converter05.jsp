<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
                java.util.Locale localeObject = new java.util.Locale("en", "US");
                java.util.TimeZone tzObject =
                    java.util.TimeZone.getTimeZone("America/New_York");
                String localeString = "en";
                String timeZoneString = "America/New_York";

                request.setAttribute("localeObject", localeObject);
                request.setAttribute("timeZoneObject", tzObject);
                request.setAttribute("localeString", localeString);
                request.setAttribute("timeZoneString", timeZoneString);
                request.setAttribute("localeObjectAU", new java.util.Locale("en", "AU"));
                request.setAttribute("timeZoneStringAU", "Australia/Melbourne");

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
                                       locale="en"
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
                <h:outputText id="outputDatetime4"
                              value="7/10/96 12:31:31 PM PDT">
                    <f:convertDateTime type="both" timeStyle="full"
                                       dateStyle="short"
                                       locale="#{requestScope.localeObjectAU}"
                                       timeZone="#{requestScope.timeZoneStringAU}"/>
                </h:outputText>
            </f:view>
        </body>
    </html>