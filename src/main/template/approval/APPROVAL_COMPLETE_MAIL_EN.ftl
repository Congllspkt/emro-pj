<!-- 컨텐츠 -->
<table style="width:100%; margin:0; padding:0; border-collapse:collapse; ">
    <tr>
        <td style="height:50px; text-align:left; font-family:malgun gothic, dotum; font-size:18px; font-weight:bold;letter-spacing: -1px;">
            Approval has been completed.
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <table style="width:100%; margin:0; padding:0; border-collapse:collapse;font-family:malgun gothic, dotum;background-color: #f5f6f7;">
                <tr style="border-top: 1px solid #ccc;">
                    <td colspan="2" style="height: 280px;text-align:left;font-family:malgun gothic, dotum;font-size:15px;line-height:1.5em;padding:30px;vertical-align:top;font-weight:bold;">
                        <table style="width:100%;margin:0 auto;padding:0;border-collapse:collapse;border: 1px solid #8f8f8f;font-family:malgun gothic,dotum;background-color: #fff;">
                            <tr>
                                <th style="width:20%;border: 1px solid #8f8f8f;text-align:left;font-family:malgun gothic, dotum;font-size:14px;padding:10px;vertical-align:middle;">Approval Title</th>
                                <td style="width:30%;border: 1px solid #8f8f8f;text-align:left;font-family:malgun gothic, dotum;font-size:14px;padding:10px;vertical-align:middle;" colspan="3">${apvl_tit?default("")}</td>
                            </tr>
                            <tr>
                                <th style="width:20%;border: 1px solid #8f8f8f;text-align:left;font-family:malgun gothic, dotum;font-size:14px;padding:10px;vertical-align:middle;">Approval DocumentNumber / RevisionNumber</th>
                                <td style="width:30%;border: 1px solid #8f8f8f;text-align:left;font-family:malgun gothic, dotum;font-size:14px;padding:10px;vertical-align:middle;" colspan="3">${apvl_docno?default("")} / ${apvl_revno?default("1")}</td>
                            </tr>
                            <tr>
                                <th style="width:20%;border: 1px solid #8f8f8f;text-align:left;font-family:malgun gothic, dotum;font-size:14px;padding:10px;vertical-align:middle;">Reporting DateTime</th>
                                <td style="width:30%;border: 1px solid #8f8f8f;text-align:left;font-family:malgun gothic, dotum;font-size:14px;padding:10px;vertical-align:middle;">${apvl_rptg_dttm?default("")}</td>
                                <th style="width:20%;border: 1px solid #8f8f8f;text-align:left;font-family:malgun gothic, dotum;font-size:14px;padding:10px;vertical-align:middle;">Final Approval DateTime</th>
                                <td style="width:30%;border: 1px solid #8f8f8f;text-align:left;font-family:malgun gothic, dotum;font-size:14px;padding:10px;vertical-align:middle;">${fnl_apvl_dttm?default("")}</td>
                            </tr>
                        </table>
                        <br/>
                        <p>The person in charge should log in to the SMARTsuite system and check the approval document.</p>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<!-- //컨텐츠 -->