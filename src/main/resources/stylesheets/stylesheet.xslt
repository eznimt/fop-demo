<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:param name="name"/>

    <xsl:template match="/document">
        <fo:root xml:lang="de">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4"
                                       page-height="297mm" page-width="210mm" margin="20mm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:declarations>
                <x:xmpmeta xmlns:x="adobe:ns:meta/">
                    <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
                        <rdf:Description xmlns:dc="http://purl.org/dc/elements/1.1/" rdf:about="">
                            <!-- Dublin Core properties go here -->
                            <dc:title>
                                <xsl:value-of select="Titel"/>
                            </dc:title>
                            <dc:creator>
                                <xsl:value-of select="Ersteller"/>
                            </dc:creator>
                            <dc:description>
                                <xsl:value-of select="Beschreibung"/>
                            </dc:description>
                        </rdf:Description>
                        <rdf:Description xmlns:xmp="http://ns.adobe.com/xap/1.0/" rdf:about="">
                            <!-- XMP properties go here -->
                            <xmp:CreatorTool>Apache(tm) FOP Version 2.9</xmp:CreatorTool>
                        </rdf:Description>
                    </rdf:RDF>
                </x:xmpmeta>

                <pdf:catalog xmlns:pdf="http://xmlgraphics.apache.org/fop/extensions/pdf">
                    <pdf:dictionary type="normal" key="ViewerPreferences">
                        <pdf:boolean key="DisplayDocTitle">true</pdf:boolean>
                    </pdf:dictionary>
                </pdf:catalog>
            </fo:declarations>

            <xsl:apply-templates select="body"/>

        </fo:root>
    </xsl:template>

    <xsl:template match="body">
        <fo:page-sequence master-reference="A4">
            <fo:flow flow-name="xsl-region-body">

                <xsl:apply-templates/>

            </fo:flow>
        </fo:page-sequence>
    </xsl:template>

    <xsl:template match="title">
        <fo:block font-family="plex">
            <xsl:value-of select="."/>
        </fo:block>
    </xsl:template>

    <xsl:template match="content">
        <fo:block font-family="plex">
            <xsl:apply-templates/>
        </fo:block>

    </xsl:template>

    <xsl:template match="name-from-code">
            <xsl:value-of select="$name"/>
    </xsl:template>

    <xsl:template match="table">
        <fo:table table-layout="fixed" width="100%" border=".25pt solid black" margin-top="1em" margin-bottom="1em">
            <fo:table-header>
                <xsl:apply-templates select="thead/tr"/>
            </fo:table-header>
            <fo:table-body>
                <xsl:apply-templates select="tbody/tr"/>
            </fo:table-body>
        </fo:table>
        <fo:block font-family="plex">
            <xsl:value-of select="caption"/>
        </fo:block>
    </xsl:template>

    <xsl:template match="tr">
        <fo:table-row>
            <xsl:apply-templates select="th | td"/>
        </fo:table-row>
    </xsl:template>

    <xsl:template match="th | td">
        <fo:table-cell border=".25pt solid black" padding="3pt">
            <fo:block font-family="plex">
                <xsl:value-of select="."/>
            </fo:block>
        </fo:table-cell>
    </xsl:template>

</xsl:stylesheet>
