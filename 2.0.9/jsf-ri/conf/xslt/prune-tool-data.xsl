<?xml version="1.0"?>

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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:jsf="http://java.sun.com/xml/ns/javaee"
                version="1.0">
    <xsl:output method="xml" indent="yes"/>

    <xsl:strip-space elements="*"/>
    <xsl:namespace-alias stylesheet-prefix="jsf" result-prefix="#default"/>

    <xsl:template match="jsf:attribute"/>
    <xsl:template match="jsf:component-extension"/>
    <xsl:template match="jsf:description"/>
    <xsl:template match="jsf:display-name"/>
    <xsl:template match="jsf:facet"/>
    <xsl:template match="jsf:large-icon"/>
    <xsl:template match="jsf:property"/>
    <xsl:template match="jsf:renderer-extension"/>
    <xsl:template match="jsf:small-icon"/>
    <xsl:template match="*|@*|text()">
       <xsl:copy>
           <xsl:apply-templates select="*|@*|text()"/>
       </xsl:copy> 
    </xsl:template>
</xsl:stylesheet>
