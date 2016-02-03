<?xml version="1.0" encoding="UTF-8"?>

<!--
   Copyright 2004 Sun Microsystems, Inc. All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  
   $Id: merge-config.xsl,v 1.1 2004/08/17 17:05:21 rlubke Exp $
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:jsf="http://java.sun.com/JSF/Configuration"
                version="1.0">
    <xsl:output method="xml" doctype-system="http://java.sun.com/dtd/web-facesconfig_1_1.dtd"
                doctype-public="-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN"
                indent="yes"/>

    <xsl:strip-space elements="*"/>
    <xsl:namespace-alias stylesheet-prefix="jsf" result-prefix="#default"/>

    <xsl:variable name="source" select="document('../../build/classes/com/sun/faces/jsf-ri-config.xml')"/>

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
