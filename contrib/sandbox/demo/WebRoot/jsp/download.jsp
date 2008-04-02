<%@ include file="header.inc" %>
    <f:view>
        <table>
            <tr>
                <td colspan="2">
                    <h3>
                        Method: None
                    </h3>
                </td>
            </tr>
            <tr>
                <td>
                    <h4>Default url variable</h4>
                    <risb:download mimeType="image/png" fileName="sample.png" data="#{testBean.image}">
                        <h:graphicImage url="#{downloadUrl}" width="250px" />
                    </risb:download>
                    <h4>User-defined url variable</h4>
                    <risb:download mimeType="image/png" fileName="sample.png" data="#{testBean.image}" urlVar="foo">
                        <h:graphicImage url="#{foo}" width="250px" />
                    </risb:download>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <h3>
                        Method: Download
                    </h3>
                </td>
            </tr>
            <tr>
                <td>
                    <risb:download method="download" mimeType="application/pdf" fileName="sample.pdf"
                        data="#{testBean.pdf}">
                        <h:graphicImage alt="Download" url="/download.jpg">
                        </h:graphicImage>
                    </risb:download>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <h3>
                        Method: Inline
                    </h3>
                </td>
            </tr>
            <tr>
                <td>
                    <risb:download method="inline" mimeType="application/pdf" fileName="HelloWorld.pdf"
                        data="#{testBean.pdf}" width="750px" height="500px" />
                </td>
            </tr>
        </table>
    </f:view>
<%@ include file="footer.inc" %>
