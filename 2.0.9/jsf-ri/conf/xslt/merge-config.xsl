<?xml version="1.0" encoding="UTF-8"?>

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
 
 Copyright 2005-2010 Sun Microsystems Inc. All Rights Reserved
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:jsf="http://java.sun.com/xml/ns/javaee"
                version="1.0">
    <xsl:output method="xml" indent="yes"/>

    <xsl:strip-space elements="*"/>
    <xsl:namespace-alias stylesheet-prefix="jsf" result-prefix="#default"/>

    <xsl:variable name="source" select="document('../../resources/jsf-ri-config.xml')"/>

    <xsl:template match="jsf:faces-config">

        <faces-config>

        <xsl:for-each select="$source/jsf:faces-config/child::*">
            <xsl:copy-of select="."/>
        </xsl:for-each>

        <xsl:for-each select="child::*">
            <xsl:copy-of select="."/>
        </xsl:for-each>

        </faces-config>

    </xsl:template>
</xsl:stylesheet>
