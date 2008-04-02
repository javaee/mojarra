<?xml version="1.0" encoding="utf-8"?>

<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!-- Content Stylesheet for Outstanding Issues Summary List -->
<!-- $Id: OutstandingIssuesSummary.xsl,v 1.3 2003/02/20 22:46:06 ofung Exp $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"/>

  <xsl:template match="issues">
    <html>
      <head>
        <title>JavaServer Faces 1.0 - Outstanding Issues Summary</title>
      </head>
      <body bgcolor="#FFFFFF">
        <table border="1" width="100%" cellpadding="5">
          <tr>
            <th width="5%">Pri</th>
            <th width="10%">Issue</th>
            <th width="55%">Summary</th>
            <th width="5%">Source</th>
            <th width="5%">Status</th>
            <th width="15%">Date</th>
            <th width="5%">Effort</th>
          </tr>
          <xsl:for-each select="issue">
            <xsl:sort select="priority"/>
            <tr>
              <td align="center"><xsl:value-of select="priority"/></td>
              <td><xsl:value-of select="id"/></td>
              <td><xsl:value-of select="summary"/></td>
              <td><xsl:value-of select="originator"/></td>
              <td align="center"><xsl:value-of select="status"/></td>
              <td align="center"><xsl:value-of select="status-date"/></td>
              <td align="center"><xsl:value-of select="effort"/></td>
            </tr>
          </xsl:for-each>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="issue">
    <tr>
      <td align="center"><xsl:value-of select="priority"/></td>
      <td><xsl:value-of select="id"/></td>
      <td><xsl:value-of select="summary"/></td>
      <td><xsl:value-of select="originator"/></td>
      <td align="center"><xsl:value-of select="status"/></td>
      <td align="center"><xsl:value-of select="status-date"/></td>
      <td align="center"><xsl:value-of select="effort"/></td>
    </tr>
  </xsl:template>

</xsl:stylesheet>
