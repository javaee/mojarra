<?xml version="1.0" encoding="utf-8"?>
<!-- Content Stylesheet for Outstanding Issues Summary List -->
<!-- $Id: OutstandingIssuesEffort.xsl,v 1.1 2002/11/15 05:25:43 eburns Exp $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"/>

  <xsl:template match="issues">
    <html>
      <head>
        <title>JavaServer Faces 1.0 - Outstanding Issues By Effort</title>
      </head>
      <body bgcolor="#FFFFFF">
        <table border="1" width="100%" cellpadding="5">
          <tr>
            <th width="10%">Issue</th>
            <th width="5%">Effort</th>
          </tr>
          <xsl:for-each select="issue">
            <xsl:sort select="id"/>
            <tr>
              <td><xsl:value-of select="id"/></td>
              <td align="center"><xsl:value-of select="effort"/></td>
            </tr>
          </xsl:for-each>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="issue">
    <tr>
      <td><xsl:value-of select="id"/></td>
      <td align="center"><xsl:value-of select="effort"/></td>
    </tr>
  </xsl:template>

</xsl:stylesheet>
