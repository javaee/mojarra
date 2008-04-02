<?xml version="1.0" encoding="utf-8"?>

<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!-- Content Stylesheet for Outstanding Issues Detail List -->
<!-- $Id: standard-html-components.xsl,v 1.2 2003/12/17 15:16:35 rkitain Exp $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="text"/>

  <xsl:template match="/">
component-type,renderer-type,component class
<xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="renderer">
<xsl:value-of select="supported-component-type/component-type" />,<xsl:value-of select="renderer-type" />,<xsl:value-of select="supported-component-class/component-class" />
  </xsl:template>


</xsl:stylesheet>
