<?xml version="1.0" encoding="UTF-8"?>

<!--
  * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
  -->

<!--

  Translates a JSF 1.0/1.1 faces-config document into a JSF 1.2 faces-config
  document, using the following conversion rules:

  1. Change the <taglib> element to read as follows:
     <taglib xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
         http://java.sun.com/xml/ns/javaee/web-facesconfig_1_2.xsd">
  2. Change the namespace of all elements to the default of
     http://java.sun.com/xml/ns/javaee

-->

<xsl:stylesheet version="1.0"                
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:new="http://java.sun.com/xml/ns/javaee"
                xmlns:old="http://java.sun.com/JSF/Configuration">
    <xsl:output method="xml"/>
    <xsl:template match="/old:faces-config">
        <xsl:element name="faces-config"
                     namespace="http://java.sun.com/xml/ns/javaee">
            <xsl:attribute name="xsi:schemaLocation"
                           namespace="http://www.w3.org/2001/XMLSchema-instance">http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_1_2.xsd</xsl:attribute>
            <xsl:attribute name="version">1.2</xsl:attribute>
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

    <!--
        We may be processing a 1.2 document, just copy the elements.    
    -->
    <xsl:template match="new:*">
        <xsl:element name="{local-name()}"
                     namespace="http://java.sun.com/xml/ns/javaee">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>