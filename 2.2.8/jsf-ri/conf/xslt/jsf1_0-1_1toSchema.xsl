<?xml version="1.0" encoding="UTF-8"?>
<!--

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

-->

<!--

  Translates a JSF 1.0/1.1 faces-config document into a JSF 1.2 faces-config
  document, using the following conversion rules:

  1. Change the <faces-config> element to read as follows:
     <taglib xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
         http://java.sun.com/xml/ns/javaee/web-facesconfig_1_1.xsd">
  2. Change the namespace of all elements to the default of
     http://java.sun.com/xml/ns/javaee

-->

<xsl:stylesheet version="1.0"                
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"               
                xmlns:old="http://java.sun.com/JSF/Configuration">
    <xsl:output method="xml"/>
    <xsl:template match="/old:faces-config">
        <xsl:element name="faces-config"
                     namespace="http://java.sun.com/xml/ns/javaee">
            <xsl:attribute name="xsi:schemaLocation"
                           namespace="http://www.w3.org/2001/XMLSchema-instance">http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_1_1.xsd</xsl:attribute>
            <xsl:attribute name="version">1.1</xsl:attribute>
            <xsl:apply-templates select="*"/>
        </xsl:element>
    </xsl:template>

    <!--
       Convert all 1.0/1.1 elements to 1.2
    -->
    <xsl:template match="old:*">
        <xsl:element name="{local-name()}"
                     namespace="http://java.sun.com/xml/ns/javaee">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>
