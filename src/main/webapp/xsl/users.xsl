<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <div>
            <table>
                <thead>
                    <tr>
                        <td style="font-weight:bold;">
                            ID
                        </td>
                        <td style="font-weight:bold;">
                            Nick Name
                        </td>
                        <td style="font-weight:bold;">
                            Forename
                        </td>
                        <td style="font-weight:bold;">
                            Surname
                        </td>
                        <td style="font-weight:bold;">
                            Phone
                        </td>
                        <td style="font-weight:bold;">
                            Email
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <xsl:for-each select="resources/principal">
                        <xsl:apply-templates select="current()"/>
                    </xsl:for-each>
                </tbody>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="principal">
        <tr>
            <td>
                <xsl:value-of select="id"/>
            </td>
            <td>
                <xsl:value-of select="shortName"/>
            </td>
            <td>
                <xsl:value-of select="foreName"/>
            </td>
            <td>
                <xsl:value-of select="surName"/>
            </td>
            <td>
                <xsl:value-of select="phoneNumber"/>
            </td>
            <td>
                <xsl:value-of select="email"/>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
