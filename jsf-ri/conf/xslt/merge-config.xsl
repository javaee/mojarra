<?xml version="1.0" encoding="UTF-8"?>

<!--
   Copyright 2004 Sun Microsystems, Inc. All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  
   $Id: merge-config.xsl,v 1.3 2005/08/15 23:36:53 rlubke Exp $
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:jsf="http://java.sun.com/xml/ns/javaee"
                version="1.0">
    <xsl:output method="xml" indent="yes"/>

    <xsl:strip-space elements="*"/>
    <xsl:namespace-alias stylesheet-prefix="jsf" result-prefix="#default"/>

    <xsl:variable name="source" select="document('../../src/com/sun/faces/jsf-ri-config.xml')"/>

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
