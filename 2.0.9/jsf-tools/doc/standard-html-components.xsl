<?xml version="1.0" encoding="utf-8"?>

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
 
 Copyright 2005-2010 Sun Microsystems Inc. All Rights Reserved
-->

<!-- Content Stylesheet for Outstanding Issues Detail List -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="text"/>

  <xsl:template match="/">
component-type,renderer-type,component class
<xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="renderer">
<xsl:value-of select="supported-component-type/component-type" />,<xsl:value-of select="renderer-type" />,<xsl:value-of select="supported-component-class/component-class" />
  </xsl:template>


</xsl:stylesheet>
