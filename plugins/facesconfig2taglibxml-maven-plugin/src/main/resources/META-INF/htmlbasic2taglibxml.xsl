<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ee="http://java.sun.com/xml/ns/javaee">
        
    <xsl:output method="xml" encoding="UTF-8"/>
    <xsl:param name="namespace" select="'http://mojarra.java.net/ns/default'"/>
  
    <xsl:template match="/">
        <facelet-taglib 
            xmlns="http://java.sun.com/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facelettaglibrary_2_0.xsd"
            version="2.0">
            <namespace>
                <xsl:value-of select="$namespace"/>
            </namespace>
            <xsl:apply-templates select="//ee:render-kit"/>
        </facelet-taglib>
    </xsl:template>
    
    <xsl:template match="ee:attribute">
        <attribute>
            <xsl:apply-templates select="./ee:description"/>
            <display-name>
                <xsl:apply-templates select="./ee:display-name"/>
            </display-name>
            <name>
                <xsl:apply-templates select="./ee:attribute-name"/>
            </name>
            <type>
                <xsl:apply-templates select="./ee:attribute-class"/>
            </type>
        </attribute>
    </xsl:template>
    
    <xsl:template match="ee:description">
        <description>
            <xsl:apply-templates/>
        </description>
    </xsl:template>
    
    <xsl:template match="ee:render-kit">
        <xsl:apply-templates select="ee:renderer"/>
    </xsl:template>
    
    <xsl:template match="ee:renderer">
        <tag>
            <xsl:apply-templates select="./ee:description"/>
            <tag-name>
                <xsl:choose>
                    <xsl:when test="./ee:renderer-extension/ee:tag-name != ''">
                        <xsl:value-of select="./ee:renderer-extension/ee:tag-name"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="./ee:display-name"/>                            
                    </xsl:otherwise>
                </xsl:choose>
            </tag-name>
            <xsl:apply-templates select="ee:attribute"/>
            <component>
                <component-type>
                    <xsl:value-of select="./ee:component-family"/>
                </component-type>
                <renderer-type>
                    <xsl:value-of select="./ee:renderer-type"/>
                </renderer-type>
            </component>
        </tag>
    </xsl:template>
    
</xsl:stylesheet>
