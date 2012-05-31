<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns1="http://java.sun.com/xml/ns/javaee"
                xmlns:functx="http://www.functx.com"
                xmlns="http://java.sun.com/xml/ns/javaee">

    <xsl:output method="html" indent="yes" name="html"/>

    <xsl:param name="copyright"/>
    <xsl:param name="outputDirectory"/>

    <xsl:template match="/">
        <xsl:message>
            Determining library name: <xsl:value-of select="ns1:facelet-taglib/ns1:composite-library-name"/>
        </xsl:message>
        <xsl:variable name="libraryName"><xsl:value-of select="ns1:facelet-taglib/ns1:composite-library-name"/></xsl:variable>
        <xsl:message>
            Generating <xsl:value-of select="$libraryName"/>
        </xsl:message>
        <xsl:variable name="tldSummaryFile"><xsl:value-of select="$outputDirectory}"/><xsl:value-of select="$libraryName"/>/tld-summary.html</xsl:variable>
        <xsl:message>
            Generating <xsl:value-of select="$tldSummaryFile"/>
        </xsl:message>
        <xsl:result-document href="{$tldSummaryFile}">
            <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                    <link rel="stylesheet" type="text/css" href="../stylesheet.css" title="styie"/>
                </head>
                <body bgcolor="white">
                    <h1>
                        <xsl:value-of select="./ns1:facelet-taglib/ns1:composite-library-name"/>
                    </h1>
                    <table>
                        <tr>
                            <th colspan="2">Facelet Taglibrary Information</th>
                        </tr>
                        <tr>
                            <td>Composite Library Name</td>
                            <td>
                                <xsl:value-of select="./ns1:facelet-taglib/ns1:composite-library-name"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Display Name</td>
                            <td>
                                <xsl:value-of select="./ns1:facelet-taglib/ns1:display-name"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Namespace</td>
                            <td>
                                <xsl:value-of select="./ns1:facelet-taglib/ns1:namespace"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Version</td>
                            <td>
                                <xsl:value-of select="./ns1:facelet-taglib/@version"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">Description</td>
                        </tr>
                        <tr>
                            <td>
                                <xsl:value-of select="./ns1:facelet-taglib/ns1:description"/>
                            </td>
                        </tr>
                    </table>
                    <table class="functionSummary">
                        <tr class="functionSummary">
                            <th class="functionSummary" colspan="2">Function Summary</th>
                        </tr>
                        <xsl:for-each select="./ns1:facelet-taglib/ns1:function">
                            <xsl:apply-templates mode="functionSummary" select="."/>
                        </xsl:for-each>
                    </table>
                    <table class="tagSummary">
                        <tr class="tagSummary">
                            <th class="tagSummary" colspan="2">Tag Summary</th>
                        </tr>
                        <xsl:for-each select="./ns1:facelet-taglib/ns1:tag">
                            <xsl:apply-templates mode="tagSummary" select="."/>
                        </xsl:for-each>
                    </table>
                </body>
            </html>
        </xsl:result-document>
        <xsl:variable name="tldFrameFile"><xsl:value-of select="$outputDirectory}"/><xsl:value-of select="$libraryName"/>/tld-frame.html</xsl:variable>
        <xsl:message>
            Generating <xsl:value-of select="$tldFrameFile"/>
        </xsl:message>
        <xsl:result-document href="{$tldFrameFile}">
            <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                    <link rel="stylesheet" type="text/css" href="../stylesheet.css" title="styie"/>
                </head>
                <body bgcolor="white">
                    <table border="0" width="100%">
                        <tr>
                            <td nowrap="true">
                                <b>Functions</b>
                                <br/>
                                <xsl:for-each select="//ns1:function">
                                    <a target="tagFrame">
                                        <xsl:attribute name="href">fn-
                                            <xsl:value-of select="ns1:function-name"/>.html
                                        </xsl:attribute>
                                        <xsl:value-of select="ancestor::ns1:facelet-taglib/ns1:composite-library-name"/>:
                                        <xsl:value-of select="ns1:function-name"/>
                                    </a> 
                                    <br/>
                                </xsl:for-each>
                                <br/>
                                <b>Tags</b>
                                <br/>
                                <xsl:for-each select="//ns1:tag">
                                    <a target="tagFrame">
                                        <xsl:attribute name="href">tag-
                                            <xsl:value-of select="ns1:tag-name"/>.html
                                        </xsl:attribute>
                                        <xsl:value-of select="ancestor::ns1:facelet-taglib/ns1:composite-library-name"/>:
                                        <xsl:value-of select="ns1:tag-name"/>
                                    </a> 
                                    <br/>
                                </xsl:for-each>
                                <br/>
                            </td>
                        </tr>
                    </table>
                </body>
            </html>
        </xsl:result-document>
        <xsl:for-each select="./ns1:facelet-taglib/ns1:tag">
            <xsl:variable name="tagFile"><xsl:value-of select="$outputDirectory}"/><xsl:value-of select="$libraryName"/>/tag-<xsl:value-of select="./ns1:tag-name"/>.html</xsl:variable>
            <xsl:message>
                Generating <xsl:value-of select="$tagFile"/>
            </xsl:message>
            <xsl:result-document href="{$tagFile}">
                <html>
                    <head>
                        <link rel="stylesheet" type="text/css" href="../faceletdocs.css" title="Style"/>
                    </head>
                    <body class="contentBody">
                        <table border="0" class="contentNavigation" width="100%">
                            <tr>
                                <td class="contentNavigationLeftText">
                                    <a class="contentNavigationLink" href="../overview-summary.html">Overview</a> &#160;
                                    <a class="contentNavigationLink" href="tld-summary.html">Library</a>
                                </td>
                                <td class="contentNavigationRightText">
                                    <a class="contentNavigationLink" href="../index.html" target="_top">frames</a> &#160;
                                    <a class="contentNavigationLink" href="tld-summary.html" target="_top">no frames</a> &#160;
                                </td>
                            </tr>
                        </table>
                        <br/>
                        <xsl:apply-templates select="." mode="content"/>
                        <table border="0" class="contentNavigation" width="100%">
                            <tr>
                                <td class="contentNavigationLeftText">
                                    <a class="contentNavigationLink" href="../overview-summary.html">Overview</a> &#160;
                                    <a class="contentNavigationLink" href="tld-summary.html">Library</a>
                                </td>
                                <td class="contentNavigationRightText">
                                    <a class="contentNavigationLink" href="../index.html" target="_top">frames</a> &#160;
                                    <a class="contentNavigationLink" href="tld-summary.html" target="_top">no frames</a> &#160;
                                </td>
                            </tr>
                        </table>
                        <p class="copyright">
                            <xsl:value-of select="$copyright"/>
                        </p>
                    </body>
                </html>
            </xsl:result-document>
        </xsl:for-each>
        <xsl:for-each select="./ns1:facelet-taglib/ns1:function">
            <xsl:variable name="functionFile"><xsl:value-of select="$outputDirectory}"/><xsl:value-of select="$libraryName"/>/fn-<xsl:value-of select="./ns1:function-name"/>.html</xsl:variable>
            <xsl:message>
                Generating <xsl:value-of select="$functionFile"/>
            </xsl:message>
            <xsl:result-document href="{$functionFile}">
                <html>
                    <head></head>
                    <body>
                        <table border="0" width="100%">
                            <tr>
                                <td>
                                    <a href="../overview-summary.html">Overview</a> &#160;
                                    <a href="tld-summary.html">Library</a>
                                </td>
                                <td>
                                    <a href="../index.html" target="_top">Frames</a> &#160;
                                    <a href="tld-summary.html" target="_top">No Frames</a> &#160;
                                </td>
                            </tr>
                        </table>
                        <br/>
                        <xsl:apply-templates select="." mode="content"/>
                        <table border="0" width="100%">
                            <tr>
                                <td>
                                    <a href="../overview-summary.html">Overview</a> &#160;
                                    <a href="tld-summary.html">Library</a>
                                </td>
                                <td>
                                    <a href="../index.html" target="_top">Frames</a> &#160;
                                    <a href="tld-summary.html" target="_top">No Frames</a> &#160;
                                </td>
                            </tr>
                        </table>
                        <br/>
                        <xsl:value-of select="$copyright"/>
                    </body>
                </html>
            </xsl:result-document>
        </xsl:for-each>
        <xsl:variable name="indexFile"><xsl:value-of select="$outputDirectory}"/>index.html</xsl:variable>
        <xsl:message>
            Generating <xsl:value-of select="$indexFile"/>
        </xsl:message>
        <xsl:variable name="allTagsFile"><xsl:value-of select="$outputDirectory}"/>alltags-frame.html</xsl:variable>
        <xsl:message>
            Generating <xsl:value-of select="$allTagsFile"/>
        </xsl:message>
        <xsl:result-document href="{$allTagsFile}">
            <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                    <title>All Tags / Functions</title>
                    <link rel="stylesheet" type="text/css" href="stylesheet.css" title="Style"/>
                </head>
                <script>
                    function asd()
                    {
                    parent.document.title="All Tags / Functions";
                    }
                </script>
                <body bgcolor="white" onload="asd();">
                    <font size="+1" class="FrameHeadingFont">
                        <b>All Functions / Tags</b>
                    </font>
                    <br/>
                    <br/>
                    <table border="0" width="100%">
                        <tr>
                            <td nowrap="true">
                                <font class="FrameItemFont">
                                    <xsl:variable name="allFiles">
                                        <xsl:value-of select="functx:substring-before-last-match(document-uri(.),'/')"/>
                                    </xsl:variable>
                                    <xsl:message>
                                        <xsl:value-of select="$allFiles"/>
                                    </xsl:message>
                                    <b>Functions</b> 
                                    <br/>
                                    <xsl:for-each select="for $x in collection(concat($allFiles,
                                            '?select=*.taglib.xml;recurse=no;on-error=ignore')) return $x">
                                        <xsl:for-each select="//ns1:function">
                                            <a target="tagFrame">
                                                <xsl:attribute name="href">
                                                    <xsl:value-of select="ancestor::ns1:facelet-taglib/ns1:composite-library-name"/>/fn-
                                                    <xsl:value-of select="ns1:function-name"/>.html
                                                </xsl:attribute>
                                                <xsl:value-of select="ancestor::ns1:facelet-taglib/ns1:composite-library-name"/>:
                                                <xsl:value-of select="ns1:function-name"/>
                                            </a> 
                                            <br/>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                    <br/>
                                    <b>Tags</b> 
                                    <br/>
                                    <xsl:for-each select="for $x in collection(concat($allFiles,
                                            '?select=*.taglib.xml;recurse=no;on-error=ignore')) return $x">
                                        <xsl:for-each select="//ns1:tag">
                                            <a target="tagFrame">
                                                <xsl:attribute name="href">
                                                    <xsl:value-of select="ancestor::ns1:facelet-taglib/ns1:composite-library-name"/>/tag-
                                                    <xsl:value-of select="ns1:tag-name"/>.html
                                                </xsl:attribute>
                                                <xsl:value-of select="ancestor::ns1:facelet-taglib/ns1:composite-library-name"/>:
                                                <xsl:value-of select="ns1:tag-name"/>
                                            </a> 
                                            <br/>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </font>
                            </td>
                        </tr>
                    </table>
                </body>
            </html>
        </xsl:result-document>
        <xsl:variable name="overviewSummaryFile"><xsl:value-of select="$outputDirectory}"/>overview-summary.html</xsl:variable>
        <xsl:message>
            Generating <xsl:value-of select="$overviewSummaryFile"/>
        </xsl:message>
        <xsl:result-document href="{$overviewSummaryFile}">
            <html>
                <head></head>
                <body>
                    <table>
                        <tr>
                            <th colspan="2">Tag Libraries</th>
                        </tr>
                        <xsl:variable name="allFiles">
                            <xsl:value-of select="functx:substring-before-last-match(document-uri(.),'/')"/>
                        </xsl:variable>
                        <xsl:message>
                            <xsl:value-of select="$allFiles"/>
                        </xsl:message>
                        <xsl:for-each select="for $x in collection(concat($allFiles,
                            '?select=*.taglib.xml;recurse=no;on-error=ignore')) return $x">
                            <xsl:for-each select="//ns1:facelet-taglib">
                                <tr>
                                    <td>
                                        <a target="tagFrame">
                                            <xsl:attribute name="href">
                                                <xsl:value-of select="ns1:composite-library-name"/>/tld-summary.html
                                            </xsl:attribute>
                                            <xsl:value-of select="ns1:composite-library-name"/>
                                        </a>
                                    </td>
                                    <td>
                                        <xsl:value-of select="ns1:description"/>
                                    </td>
                                </tr>
                            </xsl:for-each>
                        </xsl:for-each>
                    </table>
                </body>
            </html>
        </xsl:result-document>
        <xsl:variable name="overviewFrameFile"><xsl:value-of select="$outputDirectory}"/>overview-frame.html</xsl:variable>
        <xsl:message>
            Generating <xsl:value-of select="$overviewFrameFile"/>
        </xsl:message>
        <xsl:result-document href="{$overviewFrameFile}">
            <html>
                <head></head>
                <body>
                    <xsl:variable name="allFiles">
                        <xsl:value-of select="functx:substring-before-last-match(document-uri(.),'/')"/>
                    </xsl:variable>
                    <xsl:message>
                        <xsl:value-of select="$allFiles"/>
                    </xsl:message>
                    <a href="alltags-frame.html" target="tldFrame">All Tags / Functions</a> 
                    <br/>
                    <b>Tag Libraries</b> 
                    <br/>
                    <xsl:for-each select="for $x in collection(concat($allFiles,
                        '?select=*.taglib.xml;recurse=no;on-error=ignore')) return $x">
                        <xsl:for-each select="//ns1:facelet-taglib">
                            <a target="tldFrame">
                                <xsl:attribute name="href">
                                    <xsl:value-of select="ns1:composite-library-name"/>/tld-frame.html
                                </xsl:attribute>
                                <xsl:value-of select="ns1:composite-library-name"/>
                            </a> 
                            <br/>
                        </xsl:for-each>
                    </xsl:for-each>
                </body>
            </html>
        </xsl:result-document>
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            </head>
            <frameset cols="20%,80%">
                <frameset rows="30%,70%">
                    <frame src="overview-frame.html" name="tldListFrame"/>
                    <frame src="alltags-frame.html" name="tldFrame"/>
                </frameset>
                <frame src="overview-summary.html" name="tagFrame"/>
            </frameset>
            <noframes>
                <h2>Frame Alert</h2>
                This document is designed to be viewed using the frames feature.

                If you see this message, you are using a non-frame-capable web
                client.
                <br/>
                Link to 
                <a href="overview-summary.html">Non-frame version.</a>
            </noframes>
        </html>
    </xsl:template>

    <xsl:template match="ns1:function" mode="functionSummary">
        <tr class="functionSummary">
            <td class="functionSummary">
                <a class="functionSummary">
                    <xsl:attribute name="href">fn-
                        <xsl:value-of select="ns1:function-name"/>.html
                    </xsl:attribute>
                    <xsl:value-of select="ns1:function-name"/>
                </a>
            </td>
            <td class="functionSummary">
                <xsl:value-of select="ns1:description"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="ns1:tag" mode="tagSummary">
        <tr class="tagSummary">
            <td class="tagSummary">
                <a class="tagSummary">
                    <xsl:attribute name="href">tag-
                        <xsl:value-of select="ns1:tag-name"/>.html
                    </xsl:attribute>
                    <xsl:value-of select="ns1:tag-name"/>
                </a>
            </td>
            <td class="tagSummary">
                <xsl:value-of select="ns1:description"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:function
        as="xsd:string?"
        name="functx:substring-before-last-match">
        <xsl:param name="arg" as="xsd:string?"/>
        <xsl:param name="regex" as="xsd:string"/>
        <xsl:sequence select="replace($arg,concat('^(.*)',$regex,'.*'),'$1')"/>
    </xsl:function>

    <xsl:template match="ns1:function" mode="content">
        <table>
            <tr>
                <td>Name</td>
                <td>
                    <xsl:value-of select="ns1:function-name"/>
                </td>
            </tr>
            <tr>
                <td>Class</td>
                <td>
                    <xsl:value-of select="ns1:function-class"/>
                </td>
            </tr>
            <tr>
                <td>Signature</td>
                <td>
                    <xsl:value-of select="ns1:function-signature"/>
                </td>
            </tr>
        </table>
        <p>
            <xsl:value-of select="ns1:description"/>
        </p>
    </xsl:template>

    <xsl:template match="ns1:tag" mode="content">
        <table>
            <tr>
                <td>Name</td>
                <td>
                    <xsl:value-of select="ns1:tag-name"/>
                </td>
            </tr>
            <tr>
                <td>Component Type</td>
                <td>
                    <xsl:value-of select="ns1:component/ns1:component-type"/>
                </td>
            </tr>
            <tr>
                <td>Renderer Type</td>
                <td>
                    <xsl:value-of select="ns1:component/ns1:renderer-type"/>
                </td>
            </tr>
        </table>
        <p>
            <xsl:value-of select="ns1:description"/>
        </p>
    </xsl:template>

</xsl:stylesheet>
