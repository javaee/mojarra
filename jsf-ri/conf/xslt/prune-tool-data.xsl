<?xml version="1.0"?>

<!--
   Copyright 2004 Sun Microsystems, Inc. All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
                                                                                                                                                      
   $Id: prune-tool-data.xsl,v 1.2 2004/11/08 19:23:08 rlubke Exp $
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:jsf="http://java.sun.com/xml/ns/j2ee"
                version="1.0">
    <xsl:output method="xml" indent="yes"/>

    <xsl:strip-space elements="*"/>
    <xsl:namespace-alias stylesheet-prefix="jsf" result-prefix="#default"/>

    <xsl:template match="jsf:attribute"/>
    <xsl:template match="jsf:component-extension"/>
    <xsl:template match="jsf:description"/>
    <xsl:template match="jsf:display-name"/>
    <xsl:template match="jsf:facet"/>
    <xsl:template match="jsf:large-icon"/>
    <xsl:template match="jsf:property"/>
    <xsl:template match="jsf:renderer-extension"/>
    <xsl:template match="jsf:small-icon"/>
    <xsl:template match="*|@*|text()">
       <xsl:copy>
           <xsl:apply-templates select="*|@*|text()"/>
       </xsl:copy> 
    </xsl:template>
</xsl:stylesheet>
