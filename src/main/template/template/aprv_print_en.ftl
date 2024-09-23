<p style="font-family: &quot;맑은 고딕&quot;, &quot;Malgun Gothic&quot;, dotum, 돋움, Arial; font-size: 16px; line-height: 19.2px; margin-top: 0px; margin-bottom: 0px;">
    <meta charset="utf-8">
    <title>Approval template</title>
</p>
<style type="text/css">
    .report_wrap {/*word-break:keep-all;*/ word-wrap:break-all; min-width:400px}
    .report_wrap header {position:relative; background:#fafafa; font-weight:bold; text-align:center; margin-bottom:12px; padding:10px; border:1px solid #ccc; -webkit-box-shadow:4px 4px 0 rgba(0, 0, 0, .05); -moz-box-shadow:4px 4px 0 rgba(0, 0, 0, .05); box-shadow:4px 4px 0 rgba(0, 0, 0, .05);}
    .report_wrap header h2 {position:relative;font-size:24px; font-weight:bold; letter-spacing:5px;}
    .report_wrap header h4 {position:relative;font-size:15px; font-weight:bold;}
    .report_wrap .contents {margin:20px 20px 0;}
    .report_wrap .contents article {border:1px solid #ccc; padding:0 20px 20px; -webkit-box-shadow:4px 4px 0 rgba(0, 0, 0, .05); -moz-box-shadow:4px 4px 0 rgba(0, 0, 0, .05); box-shadow:4px 4px 0 rgba(0, 0, 0, .05);}
    .report_wrap .contents h3 {position:relative; line-height:30px; font-size:15px; font-weight:bold;}
    .report_wrap .contents h3 span {position:absolute; right:0; bottom:0px; font-size:12px; font-weight:normal;}
    .report_wrap .contents article section {margin-top:20px;}
    .report_wrap section table {width:100%; box-sizing:border-box; border-collapse: collapse;}
    .report_wrap section th, article td {border:1px solid #ccc; padding:5px 6px; min-height:12px; letter-spacing:-0.05em}
    .report_wrap section tr:first-child th, .report_wrap section tr:first-child td {border-top:1px solid #1e5784;}
    .report_wrap section th {text-align:left; font-weight:bold;font-size:12px; background:#ffffff}
    .report_wrap section th.head1 {background:#ffffff}
    .report_wrap section td {font-weight:bold; font-size:13px;}
    .report_wrap section .tc th {text-align:center;}
    .tc {text-align:center;}
    .tl {text-align:left;}
    .tr {text-align:right;}
    .report_wrap section .db_line {border-right:3px double #ccc;}
</style>

<div class="report_wrap" style="font-family: &quot;맑은 고딕&quot;, &quot;Malgun Gothic&quot;, dotum, 돋움, Arial; font-size: 16px; line-height: 19.2px; margin-top: 0px; margin-bottom: 0px;">
    <div class="contents" style="font-family: &quot;맑은 고딕&quot;, &quot;Malgun Gothic&quot;, dotum, 돋움, Arial; font-size: 16px; line-height: 19.2px; margin-top: 0px; margin-bottom: 0px;">
        <article>
            <section>
                <table>
                    <colgroup>
                        <col style="width:15%" />
                        <col style="width:85%" />
                    </colgroup>
                    <tr>
                        <th style="text-align:center;">Department</th>
                        <td>${drafter.apvr_dept_nm?default("")}</td>
                    </tr>
                </table>
            </section>
            <section>
                <table>
                    <colgroup>
                        <col style="width:15%"/>
                        <col style="width:14%"/>
                        <col style="width:14%"/>
                        <col style="width:14%"/>
                        <col style="width:14%"/>
                        <col style="width:14%"/>
                        <col style="width:15%"/>
                    </colgroup>
                    <tr>
                        <th style="text-align:center;">Proposal</th>
                        <th colspan="5" style="text-align:center;">Intermediate Approval</th>
                        <th style="text-align:center;">Final Approval</th>
                    </tr>
                    <tr>
                        <td class="tc">${drafter.apvr_jobtit_nm?default("")}</td>
                        <td class="tc">
                            <#if approver.approver1?exists>
                                ${approver.approver1.apvr_jobtit_nm?default("")}
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver2?exists>
                                ${approver.approver2.apvr_jobtit_nm?default("")}
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver3?exists>
                                ${approver.approver3.apvr_jobtit_nm?default("")}
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver4?exists>
                                ${approver.approver4.apvr_jobtit_nm?default("")}
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver5?exists>
                                ${approver.approver5.apvr_jobtit_nm?default("")}
                            </#if>
                        </td>
                        <td class="tc">${lastApprover.apvr_jobtit_nm?default("")}</td>
                    </tr>
                    <tr style="height: 60px;">
                        <td class="tc">${drafter.usr_nm?default("")}
                            <div>${drafter.apvlln_apvl_res_ccd_nm?default("")}</div>
                        </td>
                        <td class="tc">
                            <#if approver.approver1?exists>
                                ${approver.approver1.usr_nm?default("")}
                                <div>${approver.approver1.apvlln_apvl_res_ccd_nm?default("")}</div>
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver2?exists>
                                ${approver.approver2.usr_nm?default("")}
                                <div>${approver.approver2.apvlln_apvl_res_ccd_nm?default("")}</div>
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver3?exists>
                                ${approver.approver3.usr_nm?default("")}
                                <div>${approver.approver3.apvlln_apvl_res_ccd_nm?default("")}</div>
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver4?exists>
                                ${approver.approver4.usr_nm?default("")}
                                <div>${approver.approver4.apvlln_apvl_res_ccd_nm?default("")}</div>
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver5?exists>
                                ${approver.approver5.usr_nm?default("")}
                                <div>${approver.approver5.apvlln_apvl_res_ccd_nm?default("")}</div>
                            </#if>
                        </td>
                        <td class="tc">${lastApprover.usr_nm?default("")}
                            <div>${lastApprover.apvlln_apvl_res_ccd_nm?default("")}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="tc">${drafter.apvl_dttm?default("")}</td>
                        <td class="tc">
                            <#if approver.approver1?exists>
                                ${approver.approver1.apvl_dttm?default("")}
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver2?exists>
                                ${approver.approver2.apvl_dttm?default("")}
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver3?exists>
                                ${approver.approver3.apvl_dttm?default("")}
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver4?exists>
                                ${approver.approver4.apvl_dttm?default("")}
                            </#if>
                        </td>
                        <td class="tc">
                            <#if approver.approver5?exists>
                                ${approver.approver5.apvl_dttm?default("")}
                            </#if>
                        </td>
                        <td class="tc">${lastApprover.apvl_dttm?default("")}</td>
                    </tr>
                </table>
            </section>

            <#if agreementList?exists>
                <section>
                    <table>
                        <colgroup>
                            <col style="width: 200px;"></col>
                            <col style="width: 350px;"></col>
                            <col style="width: 150px;"></col>
                            <col></col>
                        </colgroup>
                        <#list agreementList as agreement>
                            <tr>
                                <th style="text-align:center;">[${agreement.up_usr_nm?default("")}] ${agreement.description?default("")}</th>
                                <td>${agreement.apvr_dept_nm?default("")} ${agreement.usr_nm?default("")}</td>
                                <td>${agreement.apvl_dttm?default("")}</td>
                                <td>${agreement.apvl_opn?default("")}</td>
                            </tr>
                        </#list>
                    </table>
                </section>
            </#if>

            <#if receiptList?size gt 0 || referenceList?size gt 0>
                <section>
                    <table>
                        <colgroup>
                            <col style="width: 200px;"></col>
                            <col></col>
                        </colgroup>
                        <tr>
                            <th>Reading</th>
                            <td>See below for reference</td>
                        </tr>
                    </table>
                </section>
            </#if>

            <section>
                <h3>Approval Contents</h3>
                <table>
                    <colgroup>
                        <col style="width:120px"></col>
                        <col></col>
                        <col style="width:120px"></col>
                        <col></col>
                    </colgroup>
                    <tr>
                        <th>Approval Type</th>
                        <td>
                            ${header.apvl_typ_ccd_nm?default("")}
                        </td>
                        <th>Approval Status</th>
                        <td>
                            ${header.apvl_sts_ccd_nm?default("")}
                        </td>
                    </tr>
                    <tr>
                        <th>Approval No/Rev</th>
                        <td colspan="3">
                            ${header.apvl_docno?default("")} / ${header.apvl_revno?default("")}
                        </td>
                    </tr>
                    <tr>
                        <th>Approval Title</th>
                        <td colspan="3">
                            ${header.apvl_tit?default("")}
                        </td>
                    </tr>
                    <tr>
                        <th colspan="4">Approval Contents</th>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <#if document?exists>
                                ${document.apvl_body_cont?default("")}
                            </#if>
                        </td>
                    </tr>
                </table>
            </section>

            <#if document.usr_add_cont?exists>
                <section>
                    <h3>Additional Approval Contents</h3>
                    <table>
                        <colgroup>
                            <col style="width: 100%;"></col>
                        </colgroup>
                        <tr>
                            <td>
                                ${document.usr_add_cont?default("")}
                            </td>
                        </tr>
                    </table>
                </section>
            </#if>

            <section>
                <#if receiptList?size gt 0>
                    <table>
                        <colgroup>
                            <col style="width: 200px;"></col>
                            <col></col>
                        </colgroup>
                        <tr>
                            <th>View after Approval</th>
                            <td>
                                <#list receiptList as receipt>
                                    ${receipt.usr_dept_nm?default("")} ${receipt.usr_nm?default("")}${receipt.separator?default("")}
                                </#list>
                            </td>
                        </tr>
                    </table>
                </#if>

                <#if referenceList?size gt 0>
                    <table>
                        <colgroup>
                            <col style="width: 200px;"></col>
                            <col></col>
                        </colgroup>
                        <tr>
                            <th>View anytime</th>
                            <td>
                                <#list referenceList as reference>
                                    ${reference.usr_dept_nm?default("")} ${reference.usr_nm?default("")}${reference.separator?default("")}
                                </#list>
                            </td>
                        </tr>
                    </table>
                </#if>
            </section>
        </article>
    </div>
</div>