<!--

   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

   Copyright (c) 1997-2018 Oracle and/or its affiliates. All rights reserved.

   The contents of this file are subject to the terms of either the GNU
   General Public License Version 2 only ("GPL") or the Common Development
   and Distribution License("CDDL") (collectively, the "License").  You
   may not use this file except in compliance with the License.  You can
   obtain a copy of the License at
   https://javaee.github.io/glassfish/LICENSE
   See the License for the specific
   language governing permissions and limitations under the License.

   When distributing the software, include this License Header Notice in each
   file and include the License file at packager/legal/LICENSE.txt.

   GPL Classpath Exception:
   Oracle designates this particular file as subject to the "Classpath"
   exception as provided by Oracle in the GPL Version 2 section of the License
   file that accompanied this code.

   Modifications:
   If applicable, add the following below the License Header, with the fields
   enclosed by brackets [] replaced by your own identifying information:
   "Portions Copyright [year] [name of copyright owner]"

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

-->

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:jsp="http://java.sun.com/JSP/Page"
      xml:lang="en" lang="en">
<jsp:output doctype-root-element="html"
            doctype-public="-//W3C//DTD XHTML 1.0 Trasitional//EN"
            doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>
<jsp:directive.page contentType="application/xhtml+xml; charset=UTF-8"/>
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

<jsp:forward page="guess/greeting.jsp"/>
</body>
</html>
