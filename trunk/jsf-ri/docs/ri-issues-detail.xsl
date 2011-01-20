<?xml version="1.0" encoding="utf-8"?>
<!--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 2005-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

-->

<!-- Content Stylesheet for Outstanding Issues Detail List -->

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
