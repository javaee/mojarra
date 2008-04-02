<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

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

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head><title>Test Enum Converter</title></head>
<%@ page contentType="application/xhtml+xml" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<body bgcolor="white">
<f:view>

    <h:form prependId="false">

        Suit (Hearts): <h:inputText id="suit" value="#{test1.suit}"/><p/>
        Color (Blue): <h:inputText id="color" value="#{test1.color}"/><p/>
        <h:commandButton value="reload"/><p/>
        messages: <h:messages showDetail="true"/> <p/>

    </h:form>

</f:view>
</body>
</html>  
