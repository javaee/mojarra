<?xml version="1.0" encoding="utf-8"?>
<!-- Content Stylesheet for Outstanding Issues Summary List -->
<!-- $Id: ri-issues-summary.xsl,v 1.1 2002/09/27 00:24:09 eburns Exp $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"/>

  <xsl:template match="issues">
    <html>
      <head>
        <title>JavaServer Faces 1.0 Reference Implementation -
        Outstanding Issues Summary</title>
      </head>
      <body bgcolor="#FFFFFF">

<p><a href="ri-issues-detail.html">ri-issues-detail</a></p>

<p><xsl:value-of select="page-descriptor"/></p>

        <table border="1" width="100%" cellpadding="5">
          <tr>
            <th width="10%">Issue</th>
            <th width="55%">Summary</th>
            <th width="10%">Source</th>
            <th width="10%">Status</th>
            <th width="15%">Date</th>
            <th width="15%">Priority</th>
          </tr>
          <xsl:apply-templates select="issue"/>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="issue">
    <tr>
      <td><xsl:value-of select="id"/></td>
      <td><xsl:value-of select="summary"/></td>
      <td><xsl:value-of select="originator"/></td>
      <td align="center"><xsl:value-of select="status"/></td>
      <td align="center"><xsl:value-of select="status-date"/></td>
      <td align="center"><xsl:value-of select="priority"/></td>
    </tr>
  </xsl:template>

</xsl:stylesheet>
