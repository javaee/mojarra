<?xml version="1.0" encoding="ISO-8859-1"?>

<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html"/>

<xsl:template match="/">
<html>

<head>

<title>JavaServer Faces 1.0 Standard HTML RenderKit Specification</title>

<STYLE TYPE="text/css" MEDIA="screen">

<xsl:comment>

  TABLE  { border-width: medium; border-style: groove }

  TD  { border-width: thin; border-style: groove }

  TH { background-color: #dcdcdc }

</xsl:comment>

</STYLE>

</head>

<!-- Note that you need to modify the standard-html-renderkit.xml file
to make it so that render-kit is the root XML element in order for this
XSL file to work.  -->

<body>

  <h2>Passthru attributes</h2>

  <p>The following attributes are passed thru without interpretation
  directly to the rendered markup.</p>

  <table>

    <tr>

      <th>attribute</th>

      <th>renderers supporting that attribute</th>

    </tr>

    <tr>

      <td>disabled</td>

    </tr>

    <tr>

      <td>readonly</td>

    </tr>

    <tr>

      <td>ismap</td>

    </tr>

    <tr>

      <td>accesskey</td>

    </tr>

    <tr>

      <td>alt</td>

    </tr>

    <tr>

      <td>cols</td>

    </tr>

    <tr>

      <td>height</td>

    </tr>

    <tr>

      <td>lang</td>

    </tr>

    <tr>

      <td>longdesc</td>

    </tr>

    <tr>

      <td>maxlength</td>

    </tr>

    <tr>

      <td>onblur</td>

    </tr>

    <tr>

      <td>onchange</td>

    </tr>

    <tr>

      <td>onclick</td>

    </tr>

    <tr>

      <td>ondblclick</td>

    </tr>

    <tr>

      <td>onfocus</td>

    </tr>

    <tr>

      <td>onkeydown</td>

    </tr>

    <tr>

      <td>onkeypress</td>

    </tr>

    <tr>

      <td>onkeyup</td>

    </tr>

    <tr>

      <td>onload</td>

    </tr>

    <tr>

      <td>onmousedown</td>

    </tr>

    <tr>

      <td>onmousemove</td>

    </tr>

    <tr>

      <td>onmouseout</td>

    </tr>

    <tr>

      <td>onmouseover</td>

    </tr>

    <tr>

      <td>onmouseup</td>

    </tr>

    <tr>

      <td>onreset</td>

    </tr>

    <tr>

      <td>onselect</td>

    </tr>

    <tr>

      <td>onsubmit</td>

    </tr>

    <tr>

      <td>onunload</td>

    </tr>

    <tr>

      <td>rows</td>

    </tr>

    <tr>

      <td>size</td>

    </tr>

    <tr>

      <td>tabindex</td>

    </tr>

    <tr>

      <td>title</td>

    </tr>

    <tr>

      <td>style</td>

    </tr>

    <tr>

      <td>width</td>

    </tr>

    <tr>

      <td>dir</td>

    </tr>

    <tr>

      <td>rules</td>

    </tr>

    <tr>

      <td>frame</td>

    </tr>

    <tr>

      <td>border</td>

    </tr>

    <tr>

      <td>cellspacing</td>

    </tr>

    <tr>

      <td>cellpadding</td>

    </tr>

    <tr>

      <td>summary</td>

    </tr>

    <tr>

      <td>bgcolor</td>

    </tr>

    <tr>

      <td>usemap</td>

    </tr>

    <tr>

      <td>enctype </td>

    </tr>

    <tr>

      <td>acceptcharset </td>

    </tr>

    <tr>

      <td>accept </td>

    </tr>

    <tr>

      <td>target </td>

    </tr>

    <tr>

      <td>onreset</td>

    </tr>

    <tr>

      <td>rel</td>

    </tr>

    <tr>

      <td>rev</td>

    </tr>

    <tr>

      <td>shape</td>

    </tr>

    <tr>

      <td>coords</td>

    </tr>

    <tr>

      <td>hreflang</td>

    </tr>

  </table>

  <h2>Renderer Descriptions</h2>

  <table>

    <tr>

      <th>renderer-type</th>

      <th>description</th>

    </tr>

    <xsl:for-each select="render-kit/renderer">

      <tr>

	<td><xsl:value-of select="renderer-type"/></td>

	<td><xsl:value-of select="description"/></td>

      </tr>

    </xsl:for-each>

  </table>

</body>

</html>

</xsl:template>

</xsl:stylesheet>
