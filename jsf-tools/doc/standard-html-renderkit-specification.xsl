<?xml version="1.0" encoding="utf-8"?>

<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!-- Content Stylesheet for Outstanding Issues Detail List -->
<!-- $Id: standard-html-renderkit-specification.xsl,v 1.1 2003/12/24 18:20:23 eburns Exp $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="text"/>

  <xsl:template match="/">
renderer-type,description
<xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="renderer">
<xsl:value-of select="renderer-type" />,<xsl:value-of select="description" />
  </xsl:template>


</xsl:stylesheet>
