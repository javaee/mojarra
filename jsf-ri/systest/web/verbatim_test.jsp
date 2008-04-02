<%--
   Copyright 2004 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: verbatim_test.jsp,v 1.13 2006/03/29 22:39:04 rlubke Exp $ --%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>

    <html>

    <head>
        <title>Test of the Verbatim Tag</title>
    </head>

    <body>

    <h1>Test of the Verbatim Tag</h1>

    <p>
        <f:verbatim>
            [DEFAULT]
            This text <b>has angle brackets</b>.
            The angle brackets MUST NOT be escaped.
        </f:verbatim>
    </p>

    <p>
        <f:verbatim escape="false">
            [FALSE]
            This text <b>has angle brackets</b>.
            The angle brackets MUST NOT be escaped.
        </f:verbatim>
    </p>

    <p>
        <f:verbatim escape="true">
            [TRUE]
            This text <b>has angle brackets</b>.
            The angle brackets MUST be escaped.
        </f:verbatim>
    </p>

    <p><f:verbatim rendered="false">This text must not be rendered.
    </f:verbatim> This text is rendered.
    </p>

    </body>

    </html>

</f:view>

