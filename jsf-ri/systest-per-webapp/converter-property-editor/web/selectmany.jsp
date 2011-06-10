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

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <f:view>
            <h:form>

<p>This selectManyMenu is bound to a list of test.Payment instances.  There
is a converter-for-class registered for the test.Payment class.  This
will cause the EL coerceToType to be called to coerce the value from a
String to a test.Payment instance.  The EL uses JavaBeans PropertyEditor
instances to do this. </p>

<p>This test verifies that a custom converter-for-class converter is
called by the EL coerceToType via the ConverterPropertyEditor class in
Sun's JSF Impl.</p>

<p>                <h:selectManyMenu value="#{testBean.payments}">
                    <f:selectItem itemLabel="cc1" itemValue="1"/>
                    <f:selectItem itemLabel="cc2" itemValue="2"/>
                </h:selectManyMenu></p>
<p>                <h:selectManyMenu value="#{testBean2.payments}">
                    <f:selectItem itemLabel="cc3" itemValue="3"/>
                    <f:selectItem itemLabel="cc4" itemValue="4"/></p>
                </h:selectManyMenu>

<p>Messages: <h:messages /> </p>

<p>                <h:commandButton id="press" value="submit" /></p>
            </h:form>
        </f:view>
    </body>
</html>
