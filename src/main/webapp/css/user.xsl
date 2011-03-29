<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <div>
            <xsl:apply-templates select="principal"/>
            <xsl:apply-templates select="principal/friends"/>
        </div>
    </xsl:template>

    <xsl:template match="friends">
        <div id="userFriendsList">
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
                    <xsl:for-each select="principal">
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
                    </xsl:for-each>
                </tbody>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="principal">
        <div id="userDetail">
            <table>
                <tr>
                    <td style="font-weight:bold;">
                        ID
                    </td>
                    <td>
                        <xsl:value-of select="id"/>
                    </td>
                </tr>
                <tr>
                    <td style="font-weight:bold;">
                        Nick Name
                    </td>
                    <td>
                        <xsl:value-of select="shortName"/>
                    </td>
                </tr>
                <tr>
                    <td style="font-weight:bold;">
                        Name
                    </td>
                    <td>
                        <xsl:value-of select="foreName"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="surName"/>
                    </td>
                </tr>
                <tr>
                    <td style="font-weight:bold;">
                        Phone
                    </td>
                    <td>
                        <xsl:value-of select="phoneNumber"/>
                    </td>
                </tr>
                <tr>
                    <td style="font-weight:bold;">
                        Email
                    </td>
                    <td>
                        <xsl:value-of select="email"/>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>
</xsl:stylesheet>
