<?xml version="1.0" encoding="utf-8"?>

<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!-- Content Stylesheet for Outstanding Issues Summary List -->
<!-- $Id: api-issues-owners.xsl,v 1.3 2004/02/04 23:40:42 ofung Exp $ -->

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
            <th width="5%">Date</th>
            <th width="5%">Effort</th>
            <th width="5%">RI-Effort</th>
            <th width="5%">RI-Owner</th>            
          </tr>
          <xsl:for-each select="issue">
            <xsl:sort select="priority"/>
            <tr>
              <td align="center"><xsl:value-of select="priority"/></td>
              <xsl:variable name="issueId" select="./id[.]"/>
              <td><xsl:value-of select="$issueId"/></td>
              <td><xsl:value-of select="summary"/></td>
              <td><xsl:value-of select="originator"/></td>
              <td align="center"><xsl:value-of select="status"/></td>
              <td align="center"><xsl:value-of select="status-date"/></td>
              <td align="center"><xsl:value-of select="effort"/></td>
              <xsl:for-each select="document('api-issues-owners.xml')/issues/issue">
                 <xsl:if test="$issueId=./id[.]">
                      <td> <xsl:value-of select="./ri-effort[.]" /> </td>
                      <td> <xsl:value-of select="./ri-owner[.]" /> </td>
                 </xsl:if>
              </xsl:for-each>
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
