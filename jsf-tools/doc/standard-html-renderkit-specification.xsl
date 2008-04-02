<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml"/>

<xsl:template match="/">
  <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <fo:layout-master-set>
      <fo:simple-page-master master-name="content"
	page-width="210mm" page-height="297mm">
	<fo:region-body/>
      </fo:simple-page-master>
    </fo:layout-master-set>
    <fo:page-sequence master-reference="content">
      <fo:flow flow-name="xsl-region-body">
	<fo:table border-width="0.5pt" border-color="black">
	  <fo:table-column column-width="30mm"/>
	  <fo:table-column column-width="100mm"/>
	  <fo:table-body>
	    <fo:table-row>
	      <fo:table-cell>
		<fo:block>renderer-type</fo:block>
	      </fo:table-cell>
	      <fo:table-cell>
		<fo:block>description</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

            <xsl:for-each select="render-kit/renderer">
	    <fo:table-row>
	      <fo:table-cell>
		<fo:block><xsl:value-of select="renderer-type"/></fo:block>
	      </fo:table-cell>
	      <fo:table-cell>
		<fo:block><xsl:value-of select="description"/></fo:block>
	      </fo:table-cell>
	    </fo:table-row>
	    <fo:table-row>
	      <fo:table-cell>
		<fo:block>__</fo:block>
	      </fo:table-cell>
	      <fo:table-cell>
		<fo:block>__</fo:block>
	      </fo:table-cell>
	    </fo:table-row>
            </xsl:for-each>
	  </fo:table-body>
	</fo:table>
      </fo:flow>
    </fo:page-sequence>
  </fo:root>

</xsl:template>

</xsl:stylesheet>
