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

\<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">



<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head> <title>Test SelectItem with escape true and false</title> </head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <body bgcolor="white">
    <f:view>

      <h:form prependId="false">

<h:panelGrid columns="2">

  SelectOneMenu with no escape value

<h:selectOneMenu id="menu1">
						<f:selectItem itemValue="10" itemLabel="menu1_Guy <Lafleur>" />
						<f:selectItem itemValue="99" itemLabel="menu1_Wayne <Gretzky>" />
						<f:selectItem itemValue="4" itemLabel="menu1_Bobby +Orr+"  />
						<f:selectItem itemValue="2" itemLabel="menu1_Brad &{Park}" />
						<f:selectItem itemValue="9" itemLabel="menu1_Gordie &Howe&" />
					</h:selectOneMenu>
SelectOneMenu with true escape value

<h:selectOneMenu id="menu2">
						<f:selectItem escape="true" itemValue="10" itemLabel="menu2_Guy <Lafleur>" />
						<f:selectItem escape="true" itemValue="99" itemLabel="menu2_Wayne <Gretzky>" />
						<f:selectItem escape="true" itemValue="4" itemLabel="menu2_Bobby +Orr+"  />
						<f:selectItem escape="true" itemValue="2" itemLabel="menu2_Brad &{Park}" />
						<f:selectItem escape="true" itemValue="9" itemLabel="menu2_Gordie &Howe&" />
					</h:selectOneMenu>

SelectOneMenu with false escape value

<h:selectOneMenu id="menu3">
						<f:selectItem escape="false" itemValue="10" itemLabel="menu3_Guy <Lafleur>" />
						<f:selectItem escape="false" itemValue="99" itemLabel="menu3_Wayne <Gretzky>" />
						<f:selectItem escape="false" itemValue="4" itemLabel="menu3_Bobby +Orr+"  />
						<f:selectItem escape="false" itemValue="2" itemLabel="menu3_Brad &{Park}" />
						<f:selectItem escape="false" itemValue="9" itemLabel="menu3_Gordie &Howe&" />
					</h:selectOneMenu>


</h:panelGrid>


      </h:form>

    </f:view>
    </body>
</html>  
