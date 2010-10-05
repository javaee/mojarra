<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
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

--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>pgridcolumnclasses.jsp</title>
    <style type="text/css">
        .b1 {
            background-color: red;
        }

        .b2 {
            background-color: green;
        }

        .b3 {
            background-color: blue;
        }

        .b4 {
            background-color: burlywood;
        }

        .b5 {
            background-color: darkolivegreen;
        }

        .b6 {
            background-color: darkviolet;
        }

        .b7 {
            background-color: skyblue;
        }
    </style>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

</head>
<body>
<f:view>
<h:panelGrid columns="6"
             columnClasses="b1,b2">
    <h:outputText value="c1"/>
    <h:outputText value="c2"/>
    <h:outputText value="c3"/>
    <h:outputText value="c4"/>
    <h:outputText value="c5"/>
    <h:outputText value="c6"/>

    <h:outputText value="c1_1"/>
    <h:outputText value="c2_1"/>
    <h:outputText value="c3_1"/>
    <h:outputText value="c4_1"/>
    <h:outputText value="c5_1"/>
    <h:outputText value="c6_1"/>
</h:panelGrid>
<h:panelGrid columns="6"
             columnClasses="b1,b2,b3,b4,">
    <h:outputText value="c1"/>
    <h:outputText value="c2"/>
    <h:outputText value="c3"/>
    <h:outputText value="c4"/>
    <h:outputText value="c5"/>
    <h:outputText value="c6"/>

    <h:outputText value="c1_1"/>
    <h:outputText value="c2_1"/>
    <h:outputText value="c3_1"/>
    <h:outputText value="c4_1"/>
    <h:outputText value="c5_1"/>
    <h:outputText value="c6_1"/>
</h:panelGrid>
<h:panelGrid columns="6"
             columnClasses="b1,b2,b3">
    <h:outputText value="c1"/>
    <h:outputText value="c2"/>
    <h:outputText value="c3"/>
    <h:outputText value="c4"/>
    <h:outputText value="c5"/>
    <h:outputText value="c6"/>

    <h:outputText value="c1_1"/>
    <h:outputText value="c2_1"/>
    <h:outputText value="c3_1"/>
    <h:outputText value="c4_1"/>
    <h:outputText value="c5_1"/>
    <h:outputText value="c6_1"/>
</h:panelGrid>
<h:panelGrid columns="6"
             columnClasses="b1">
    <h:outputText value="c1"/>
    <h:outputText value="c2"/>
    <h:outputText value="c3"/>
    <h:outputText value="c4"/>
    <h:outputText value="c5"/>
    <h:outputText value="c6"/>

    <h:outputText value="c1_1"/>
    <h:outputText value="c2_1"/>
    <h:outputText value="c3_1"/>
    <h:outputText value="c4_1"/>
    <h:outputText value="c5_1"/>
    <h:outputText value="c6_1"/>
</h:panelGrid>
<h:panelGrid columns="6">
    <h:outputText value="c1"/>
    <h:outputText value="c2"/>
    <h:outputText value="c3"/>
    <h:outputText value="c4"/>
    <h:outputText value="c5"/>
    <h:outputText value="c6"/>

    <h:outputText value="c1_1"/>
    <h:outputText value="c2_1"/>
    <h:outputText value="c3_1"/>
    <h:outputText value="c4_1"/>
    <h:outputText value="c5_1"/>
    <h:outputText value="c6_1"/>
</h:panelGrid>
<h:panelGrid columns="6"
             columnClasses="b1,b2,b3,b4,b5,b6,b7">
    <h:outputText value="c1"/>
    <h:outputText value="c2"/>
    <h:outputText value="c3"/>
    <h:outputText value="c4"/>
    <h:outputText value="c5"/>
    <h:outputText value="c6"/>

    <h:outputText value="c1_1"/>
    <h:outputText value="c2_1"/>
    <h:outputText value="c3_1"/>
    <h:outputText value="c4_1"/>
    <h:outputText value="c5_1"/>
    <h:outputText value="c6_1"/>
</h:panelGrid>
<h:panelGrid columns="6"
             columnClasses=",b2,,,b5,b6,b7">
    <h:outputText value="c1"/>
    <h:outputText value="c2"/>
    <h:outputText value="c3"/>
    <h:outputText value="c4"/>
    <h:outputText value="c5"/>
    <h:outputText value="c6"/>

    <h:outputText value="c1_1"/>
    <h:outputText value="c2_1"/>
    <h:outputText value="c3_1"/>
    <h:outputText value="c4_1"/>
    <h:outputText value="c5_1"/>
    <h:outputText value="c6_1"/>
</h:panelGrid>
</f:view>
</body>
</html>


