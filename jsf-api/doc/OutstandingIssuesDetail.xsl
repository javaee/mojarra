<?xml version="1.0" encoding="utf-8"?>
<!-- Content Stylesheet for Outstanding Issues Detail List -->
<!-- $Id: OutstandingIssuesDetail.xsl,v 1.2 2002/10/03 00:47:02 craigmcc Exp $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"/>

  <xsl:template match="/">
    <html>
      <head>
        <title>JavaServer Faces 1.0 - Outstanding Issues Detail</title>
      </head>
      <body bgcolor="#FFFFFF">
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
        <td width="85%" colspan="7"><xsl:value-of select="summary"/></td>
      </tr>
      <tr><th colspan="10">
        Description
      </th></tr>
      <tr><td colspan="10">
        <xsl:apply-templates select="description"/>
      </td></tr>
      <tr><th colspan="10">
        Resolution
      </th></tr>
      <tr><td colspan="10">
        <xsl:apply-templates select="resolution"/>
      </td></tr>
      <tr>
        <th width="5%">Status:</th>
        <td width="5%"><xsl:value-of select="status"/></td>
        <th width="5%">Date:</th>
        <td width="55%"><xsl:value-of select="status-date"/></td>
        <th width="5%">Priority:</th>
        <td width="5%"><xsl:value-of select="priority"/></td>
        <th width="5%">Effort:</th>
        <td width="5%"><xsl:value-of select="effort"/></td>
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
