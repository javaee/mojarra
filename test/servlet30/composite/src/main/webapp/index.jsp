<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.

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

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mojarra Agnostic Composite Component Tests</title>
    </head>
    <body>
        <h1>Mojarra Agnostic Composite Component Tests</h1>
        <h3>Simple</h3>
        <ul>
            <li><a href="faces/basic/isInsideCompositeComponent.xhtml">Test to see if we are inside a composite component</a></li>
            <li><a href="faces/basic/isInsideCompositeComponent2.xhtml">Test to see if we are inside a composite component #2</a></li>
            <li><a href="faces/basic/simple1.xhtml">A simple composite component</a></li>
            <li><a href="faces/basic/simple2.xhtml">A h:outputText inside a composite component</a></li>
            <li><a href="faces/basic/simple3.xhtml">A h:outputText inside a composite component (using EL)</a></li>
        </ul>
        <h3>Nested</h3>
        <ul>
            <li><a href="faces/nested/nested1.xhtml">A nested composite component test</a></li>
            <li><a href="faces/nested/nested2.xhtml">A h:outputText inside a nested composite component</a></li>
            <li><a href="faces/nested/nested3.xhtml">A h:outputText inside a nested composite component (using EL)</a></li>
        </ul>
        <h3>cc:actionSource</h3>
        <ul>
            <li><a href="faces/actionSource/actionSource1.xhtml">A cc:actionSource test</a></li>
        </ul>
        <h3>cc:attribute and # {cc.attrs}</h3>
        <ul>
            <li><a href="faces/attribute/attribute1.xhtml">A composite component using # {cc.attrs.text}</a></li>
            <li><a href="faces/attribute/attribute2.xhtml">A composite component using cc:attribute with a default and overriding the default</a></li>
        </ul>
        <h3>cc:attribute and actions</h3>
        <ul>
            <li><a href="faces/action/action1.xhtml">A custom action</a></li>
        </ul>
        <h3>cc:insertChildren</h3>
        <ul>
            <li><a href="faces/insertChildren/insertChildren1.xhtml">A cc:insertChildren inside a composite component</a></li>
            <li><a href="faces/insertChildren/insertChildren2.xhtml">A cc:insertChildren inside a nested composite component</a></li>
            <li><a href="faces/insertChildren/insertChildren3.xhtml">A cc:insertChildren inside a composite component with an inactive facet</a></li>
            <li><a href="faces/insertChildren/insertChildren4.xhtml">A cc:insertChildren inside of panelGroup inside a composite component</a></li>
        </ul>
        <h3>cc:insertFacet</h3>
        <ul>
            <li><a href="faces/insertFacet/insertFacet1.xhtml">A composite component with cc:insertFacet in it</a></li>
        </ul>
        <h3>cc:renderFacet</h3>
        <ul>
            <li><a href="faces/renderFacet/renderFacet1.xhtml">A composite component using cc:renderFacet</a></li>
            <li><a href="faces/renderFacet/renderFacet2.xhtml">A composite component using cc:renderFacet #2</a></li>
        </ul>
        <h3>h:input</h3>
        <ul>
            <li><a href="faces/input/input1.xhtml">A composite component with a UIInput in it</a></li>
        </ul>
        <h3>ui:define</h3>
        <ul>
            <li><a href="faces/define/define1.xhtml">A composite component with template functionality in it</a></li>
        </ul>
    </ul>
</body>
</html>
