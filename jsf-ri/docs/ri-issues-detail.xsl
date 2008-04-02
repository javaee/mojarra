<?xml version="1.0" encoding="utf-8"?>

<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!-- Content Stylesheet for Outstanding Issues Detail List -->
<!-- $Id: ri-issues-detail.xsl,v 1.2 2003/02/20 22:48:29 ofung Exp $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"/>

  <xsl:template match="/">
    <html>
      <head>
        <title>JavaServer Faces 1.0 Reference Implementation - Outstanding Issues Detail</title>
      </head>
      <body bgcolor="#FFFFFF">

<p><a href="ri-issues-summary.html">ri-issues-summary</a></p>

        <xsl:apply-templates/>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="issue">

    <table border="1" width="100%" cellpadding="5">
      <tr>
        <th width="5%">Issue:</th>
        <td width="5%"><xsl:value-of select="id"/></td>
        <th width="5%">Summary:</th>
        <td><xsl:value-of select="summary"/></td>
        <th width="5%">Priority:</th>
        <td><xsl:value-of select="priority"/></td>

      </tr>
      <tr><th colspan="6">
        Description
      </th></tr>
      <tr><td colspan="6">
        <xsl:apply-templates select="description"/>
      </td></tr>
      <tr><th colspan="6">
        Resolution
      </th></tr>
      <tr><td colspan="6">
        <xsl:apply-templates select="resolution"/>
      </td></tr>
      <tr>
        <th width="5%">Status:</th>
        <td width="5%"><xsl:value-of select="status"/></td>
        <th width="5%">Date:</th>
        <td width="75%"><xsl:value-of select="status-date"/></td>
        <th width="5%">Source:</th>
        <td width="5%"><xsl:value-of select="originator"/></td>
      </tr>
    </table>
    <br/><br/><br/>
  </xsl:template>

  <xsl:template match="description">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="resolution">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="*|@*">
    <xsl:copy>
      <xsl:apply-templates select="@*|*|text()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
