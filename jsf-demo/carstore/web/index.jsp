<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
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
<html>
<head>
</head>
<body>

<!--  

This page allows the user to go to the context-path and get redirected
to the front page of the app.  For example,
http://localhost:8080/jsf-carstore/.  Note that we use "*.jsf" as the
page mapping.  Doing so allows us to just name our pages as "*.jsp",
refer to them as "*.jsf" and know that they will be properly picked up
by the container.

-->

<jsp:forward page="chooseLocale.faces" />
</body>
</html>
