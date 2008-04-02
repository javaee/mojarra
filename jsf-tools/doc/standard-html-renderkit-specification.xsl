<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html"/>

<xsl:template match="/">
<html>

<head>

<title>JavaServer Faces 1.0 Standard HTML RenderKit Specification</title>

</head>

<body>

  <table border="1">

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
