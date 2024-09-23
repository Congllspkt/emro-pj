<%@page contentType="text/html;charset=utf-8" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="smartsuite.app.util.*"%>
<%@page import="smartsuite.app.common.cert.util.*"%>
<%@page import="kica.sgic.util.DataToXml"%>
<%@page import="kica.sgic.util.SGIxLinker"%>
<%@page import="kica.sgic.util.XmlToData"%>
<%@page import="signgate.sgic.xml.util.XmlUtil"%>
<%
    response.setHeader("Pragma","no-cache"); 
    response.setDateHeader("Expires",0);
    response.setHeader("Cache-Control","no-store"); 
%>  
<%@ page language="java" import="java.util.*"%>
<%@ page import="java.net.*,java.io.*" %>
<%@ page import="smartsuite.app.common.cert.util.DocumentProperties" %>
<%@ page import="smartsuite.app.common.util.EdocStringUtil" %>
<%
	String recvXmlPath = request.getParameter("xml_path");
	String guar_type = request.getParameter("guar_type");
	String print = request.getParameter("print");
	
	/*  운영일 때 적용 */
	if("CPGUR".equals(guar_type)){
		guar_type = "CONGUA";
	}else if("APYMTPGUR".equals(guar_type)){
		guar_type = "PREGUA";
	}else if("DEFPGUR".equals(guar_type)){
		guar_type = "FLRGUA";
	}

	/* 테스트용 변수 */
	//recvXmlPath = "D:/app/signgate/data/recv/CONGUA/2018/02/22/recv20180222135338_dec.xml";
	//guar_type = "CONGUA";
	/* 테스트용 변수 */
	
	FileInputStream fis = null;
	byte[] buf = null;
	String recvXmlDoc = "";
	
	try{
		fis = new FileInputStream(recvXmlPath);
		buf = new byte[fis.available()];
		fis.read(buf);
		fis.close(); 
		recvXmlDoc = new String (buf);
	}catch(Exception e){
		System.out.println("XML 조회 실패 : " + e);
	}finally{
		if(fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				System.out.println("close 실패 : " + e);
			}
		}
	}
	
	XmlToData xmlToData = new XmlToData(DocumentProperties.get("sgic.module.templatepath"), guar_type, recvXmlDoc);
	
	String browser = "";
	String userAgent = request.getHeader("User-Agent");
    
    if (userAgent.indexOf("Trident") > 0 || userAgent.indexOf("MSIE") > 0) {
        browser = "IE";
    }
    else {
        browser = "ETC";
    }
%>
<html>
<head>
	<title>
		<%= xmlToData.getData("head_mesg_name") %>
	</title>

    <meta content="text/html; charset=euc-kr" http-equiv="Content-Type"/>
	<style type="text/css">
		BODY  {  scrollbar-3dlight-color:595959; scrollbar-arrow-color:ffffff; scrollbar-3dlight-color:595959; scrollbar-arrow-color:ffffff; scrollbar-base-color:CFCFCF; scrollbar-darkshadow-color:FFFFFF; scrollbar-face-color:CFCFCF; scrollbar-highlight-color:FFFFF; scrollbar-shadow-color:595959; margin:0 0 0 0; -webkit-print-color-adjust: exact; } 
		BODY,TD,SELECT,TEXTAREA,FORM,INPUT { font-family: "굴림"; font-size: 9pt; color: #3C3C3C; }
		.HEADLINE	{ font-family: "굴림체"; font-size: 23pt; font-weight: bold; color: #000000; vertical-align: bottom;}
		.fontb	  {   font-family: "굴림체"; line-height: 14pt; font-weight: bold; text-align: left;  vertical-align: bottom; color: #3C3C3C}
		.fontbc	  {   font-family: "굴림체"; line-height: 14pt; font-weight: bold; text-align: center;  vertical-align: middle; color: #3C3C3C}
		.text_text {  font-family: '굴림체'; font-size: 7pt; color: #000000}
		.text5 {  font-family: '굴림체'; font-size: 6pt; color: #000000}
		.text6 {  font-family: '굴림체'; font-size: 10pt; font-weight: bold;}
		.sub_title {  font-family: '굴림체'; font-size: 12pt; font-weight: bold}
	</style>
<script language="javascript">
	function focusOutHandler(event) {
		try {
			var arrFrames = parent.document.getElementsByTagName("IFRAME");
			for ( var i = 0; i < arrFrames.length; i++) {
				if (arrFrames[i].contentWindow == window) {
					var iframe = arrFrames[i];
					var padding = iframe.style.padding;
					iframe.style.padding = "0px";
					iframe.style.padding = padding;
				}
			}
		} catch (e) {}
	}
</script>	
</head>
<body ondragstart="return false" onfocusout="focusOutHandler(event);" >
<table id="printArea" name="printArea" width="750" border="0" cellspacing="0" cellpadding="0" background="http://www.sgic.co.kr/chp/img/xsl/xsl_back.jpg">
	<tbody>
	<tr>
		<td width="35">&#160;</td>
		<td align="center">
			<table border="0" width="680" cellspacing="0" cellpading="0" height="95"> 
				<tr height="90">
					<td colspan="3" valign="top">
						<table border="0" height="55">
							<tr>
								<td>
								</td>
							</tr>
						</table>			
					</td>
				</tr>
				<tr> 
					<td width="74" rowspan="2" height="90">
						&#160; 
					</td> 
					<td align="center" class="headline" width="520" height="45">
					<%
						String head_mesg_name = xmlToData.getData("head_mesg_name");
					%>
						<% if (head_mesg_name.equals("계약보증서")){ %>
							이행(계약)보증보험증권						
						<% }else if (head_mesg_name.equals("하자보증서")){ %>
							이행(하자)보증보험증권						
						<% }else if (head_mesg_name.equals("선금급보증서")){ %>
							이행(선금급)보증보험증권
						<% } %>
					</td>
					<td widtn="70" rowspan="2">&#160;</td>
				</tr> 
				<tr>
					<td align="center" class="sub_title" valign="top" width="500">
						(인터넷조회용)
					</td>
				</tr>
				<tr> 
					<td colspan="3" height="28" valign="bottom"> 
						<table width="600" border="0" cellspacing="0" cellpadding="0"> 
							<tr> 
							<%
								String head_docu_numb = xmlToData.getData("head_docu_numb");
							%>
								<td width="560" height="23" class="sub_title" valign="bottom">
									증권번호 제 <%= head_docu_numb.substring(6,9) %>-<%= head_docu_numb.substring(9,12) %>-<%= head_docu_numb.substring(12, 25) %>	호
								 </td> 
							</tr> 
						</table> 
					</td> 
				</tr>
				<tr>
					<!--- 내용 시작 -->
					<td colspan="3">
						<table width="680" cellspacing="0" border="1" cellpadding="2" class="tr">
							<tr>
								<td width="15%" class="fontbc">보험계약자</td>
								<td width="35%" height="55">
									<%
										String appl_orps_divs = xmlToData.getData("appl_orps_divs");
									    String appl_orps_iden = xmlToData.getData("appl_orps_iden");
										if(appl_orps_divs.equals("0")){
									%>
											    &nbsp;<%=appl_orps_iden.substring(0, 3) %>-<%=appl_orps_iden.substring(3, 5) %>-<%=appl_orps_iden.substring(5, 10) %>
									<%
										}else if(appl_orps_divs.equals("P")){
									%>
											    &nbsp;<%=appl_orps_iden.substring(0, 6) %>-<%=appl_orps_iden.substring(6, 13) %>
									<% } %>
									<br />&nbsp;
									<%= xmlToData.getData("appl_orga_name") %>
									<br />&nbsp;
									<%= xmlToData.getData("appl_ownr_name") %>														
								</td>
								<td width="15%" class="fontbc">피보험자</td>
								<td width="35%" colspan="2">&nbsp;
									<%
										String cred_orps_divs = xmlToData.getData("cred_orps_divs");
										String cred_orps_iden = xmlToData.getData("cred_orps_iden");
										if(cred_orps_divs.equals("O")){
									%>
											    &nbsp;<%=cred_orps_iden.substring(0, 3) %>-<%=cred_orps_iden.substring(3, 5) %>-<%=cred_orps_iden.substring(5, 10) %>
									<%
										}else if(cred_orps_divs.equals("P")){
									%>
											    &nbsp;<%=cred_orps_iden.substring(0, 6) %>-<%=cred_orps_iden.substring(6, 13) %>
									<% } %>
									<br />&nbsp;
									<%= xmlToData.getData("cred_orga_name") %>
							 </td>
							</tr>
							<tr>
								<td class="fontbc" height="45">보험가입금액</td>
									<% if("WON".equals(xmlToData.getData("penl_curc_code"))){ %> 
										<td colspan="2">&nbsp;金 
											<%= xmlToData.getData("bond_penl_text") %> 整
											<br />&nbsp;											
												\ <%= EdocStringUtil.formatNum(xmlToData.getData("bond_penl_amnt")) %>-
										</td>
									<% }else{ %>	  
										<td colspan="2">
											<br />&nbsp;
											<%= xmlToData.getData("penl_curc_code") %>
								  	 	  		<%= EdocStringUtil.formatNum(xmlToData.getData("bond_penl_amnt")) %>-
										</td>
									<% } %>											  												  									
								<td class="fontbc">보 험 료</td>
								<td align="right">									
							  	 	  \ <%= EdocStringUtil.formatNum(xmlToData.getData("bond_prem_amnt")) %>-
								</td>
							</tr>
							<tr>
								<td class="fontbc" height="30">보 험 기 간</td>
								<td colspan="4">&nbsp;
								<%
									String bond_begn_date = xmlToData.getData("bond_begn_date");
									bond_begn_date = bond_begn_date.substring(0,4) + "년 " + bond_begn_date.substring(4,6) + "월 " + bond_begn_date.substring(6,8) + "일";
									String bond_fnsh_date = xmlToData.getData("bond_fnsh_date");
									bond_fnsh_date = bond_fnsh_date.substring(0,4) + "년 " + bond_fnsh_date.substring(4,6) + "월 " + bond_fnsh_date.substring(6,8) + "일";									
								%>
									<%= bond_begn_date %> 부터 
									<%= bond_fnsh_date %> 까지
								</td>
							</tr>
							<tr>
								<td class="fontbc" height="30">보 증 내 용</td>
								<td colspan="4">&nbsp;
									<%= xmlToData.getData("bond_stat_text") %>
								</td>
							</tr>
							<tr>
								<td class="fontbc" height="70">특 별 약 관</td>   <!--- 기존 XML 및 표준 XML을 위해 두개 존재 -->
								<td colspan="4">&nbsp;
									<%= xmlToData.getData("spcl_prov_text") %>
								</td>
							</tr>
							<tr>
								<td class="fontbc" height="70">특 기 사 항</td>
								<td colspan="4">&nbsp;
									<%= xmlToData.getData("spcl_cond_text") %>
								</td>
							</tr>
							<tr>
								<td colspan="5" align="center" height="190" valign="top">
									<!--- 주계약내용 시작 (이행계약) -->
<% 

		String cont_begn_date = xmlToData.getData("cont_begn_date");
		if(!"".equals(cont_begn_date)){
			cont_begn_date = cont_begn_date.substring(0,4) + "년 " + cont_begn_date.substring(4,6) + "월 " + cont_begn_date.substring(6,8) + "일";
		}
		
		String cont_fnsh_date = xmlToData.getData("cont_fnsh_date");
		if(!"".equals(cont_fnsh_date)){
			cont_fnsh_date = cont_fnsh_date.substring(0,4) + "년 " + cont_fnsh_date.substring(4,6) + "월 " + cont_fnsh_date.substring(6,8) + "일";
		}
		
		String cont_main_date = xmlToData.getData("cont_main_date");
		if(!"".equals(cont_main_date)){
			cont_main_date = cont_main_date.substring(0,4) + "년 " + cont_main_date.substring(4,6) + "월 " + cont_main_date.substring(6,8) + "일";
		}
%>									
									<% if (head_mesg_name.equals("계약보증서")){ %>
									<table width="97%" border="0">
										<tr>
											<td class="text6" colspan="2">주계약내용</td>
										</tr>
										<tr>
											<td class="fontb" width="100">계약명</td>
											<td>
												<%= xmlToData.getData("cont_name_text") %>
											</td>
										</tr>
										<tr>
											<td class="fontb">계약기간</td>
											<td>
											  <%= cont_begn_date %> 부터
											  <%= cont_fnsh_date %> 까지
											</td>
										</tr>
										<tr>
											<td class="fontb">계약체결일자</td>
											<td>
												<%= cont_main_date %>
											</td>
										</tr>
										<tr>
											<td class="fontb">계약금액</td>
											<td>
												<% if("WON".equals(xmlToData.getData("cont_curc_code"))){ %> 
														\  <%= EdocStringUtil.formatNum(xmlToData.getData("cont_main_amnt")) %>												  
												<% }else{ %> 
														<%= EdocStringUtil.formatNum(xmlToData.getData("cont_main_amnt")) %>
												<% } %>-
											</td>
										</tr>
										<tr>
											<td class="fontb">계약보증금율</td>
											<td>
											      <%= xmlToData.getData("bond_pric_rate") %> % 
											</td>
										</tr>
									</table>							
									<!--- 주계약내용 시작 (이행하자) -->
									<% }else if (head_mesg_name.equals("하자보증서")){ %>
									<table width="97%" border="0">
										<tr>
											<td class="text6" colspan="2">주계약내용</td>
										</tr>
										<tr>
											<td class="fontb" width="100">계약명</td>
											<td>
												<%= xmlToData.getData("cont_name_text") %>
											</td>
										</tr>
										<tr>
											<td class="fontb">하자담보기간</td>
											<td>
											  <%= cont_begn_date %> 부터
											  <%= cont_fnsh_date %> 까지
											</td>
										</tr>
										<tr>
											<td class="fontb">계약체결일자</td>
											<td>
												<%= cont_main_date %>
											</td>
										</tr>
										<tr>
											<td class="fontb">계약금액</td>
											<td>
												<% if("WON".equals(xmlToData.getData("cont_curc_code"))){ %> 
														\  <%= EdocStringUtil.formatNum(xmlToData.getData("cont_main_amnt")) %>												  
												<% }else{ %> 
														<%= EdocStringUtil.formatNum(xmlToData.getData("cont_main_amnt")) %>
												<% } %>-
											</td>
										</tr>
										<tr>
											<td class="fontb">하자보증금율</td>
											<td>
											      <%= xmlToData.getData("bond_pric_rate") %> % 
											</td>
										</tr>
									</table>							
									<!--- 주계약내용 시작 (이행선금급) -->
									<% }else if (head_mesg_name.equals("선금급보증서")){ %>
									<table width="97%" border="0">
										<tr>
											<td class="text6" colspan="2">주계약내용</td>
										</tr>
										<tr>
											<td class="fontb" width="180">계약명</td>
											<td>
												<%= xmlToData.getData("cont_name_text") %>
											</td>
										</tr>
										<tr>
											<td class="fontb">계약금액</td>
											<td>
												<% if("WON".equals(xmlToData.getData("cont_curc_code"))){ %> 
														\  <%= EdocStringUtil.formatNum(xmlToData.getData("cont_main_amnt")) %>												  
												<% }else{ %> 
														<%= EdocStringUtil.formatNum(xmlToData.getData("cont_main_amnt")) %>
												<% } %>-
											</td>
										</tr>
										<tr>
											<td class="fontb">선금(전도자재)액</td>
											<td>
												<% if("WON".equals(xmlToData.getData("penl_curc_code"))){ %> 
														\  <%= EdocStringUtil.formatNum(xmlToData.getData("cont_paym_amnt")) %>												  
												<% }else{ %> 
														<%= EdocStringUtil.formatNum(xmlToData.getData("cont_paym_amnt")) %>
												<% } %>-
											</td>
										</tr>
										<tr>
											<td class="fontb">계약체결일자</td>
											<td>
												<%= cont_main_date %>
											</td>
										</tr>
										<tr>
											<td class="fontb">계약기간</td>
											<td>										
											  <%= cont_begn_date %> 부터
											  <%= cont_fnsh_date %> 까지
											</td>
										</tr>
										<tr>
											<td class="fontb">선금(전도자재)지급(예정)일</td>
											<td>
											<%
												String cont_paym_date = xmlToData.getData("cont_paym_date");
												if(!"".equals(cont_paym_date)){
													cont_paym_date = cont_paym_date.substring(0,4) + "년 " + cont_paym_date.substring(4,6) + "월 " + cont_paym_date.substring(6,8) + "일";
												}											
											%>
												<%= cont_paym_date %>
											</td>
										</tr>
									</table>
									<% } %>																							
									<!--- 주계약내용 끝  -->
								</td>
							</tr>
						</table>
					</td>
					<!--- 내용 끝 -->
				</tr> 
				<tr>
					<td colspan="3" height="35" valign="bottom">
					<% if (head_mesg_name.equals("계약보증서")){ %>
						우리 회사는 이행(계약)보증보험 보통약관, 특별약관 및 이 증권에 기재된 내용에 따라 이행(계약)보증보험 계약을 체결하였음이 확실하므로 그 증으로 이 증권을 발행합니다.
					<% }else if (head_mesg_name.equals("하자보증서")){ %>
						우리 회사는 이행(하자)보증보험 보통약관, 특별약관 및 이 증권에 기재된 내용에 따라 이행(하자)보증보험 계약을 체결하였음이 확실하므로 그 증으로 이 증권을 발행합니다.
					<% }else if (head_mesg_name.equals("선금급보증서")){ %>
						우리 회사는 이행(선금급)보증보험 보통약관, 특별약관 및 이 증권에 기재된 내용에 따라 이행(선금급)보증보험 계약을 체결하였음이 확실하므로 그 증으로 이 증권을 발행합니다.
					<% } %>				
					</td>
				</tr>
				<tr>
					<td colspan="3" height="30">&#160;</td>
				</tr>
				<tr>
					<td colspan="3">
						<table border="0">
							<tr>
								<td width="405">
									<table border="0">
										<tr>
											<td class="sub_title">※ 증권발급 사실 확인 안내<br/></td>
										</tr>
										<tr>
											<td><br/>발 급 부 서 : 
												<%= xmlToData.getData("bond_numb_text").substring(6,9) %> 
												<%= xmlToData.getData("issu_dept_name") %>
												(<%= xmlToData.getData("chrg_phon_text") %>)
											</td>
										</tr>
										<tr>
											<td>부&#160;&#160;&#160;서&#160;&#160;&#160;장 : 
												<%= xmlToData.getData("issu_dept_ownr") %>
											</td>
										</tr>
										<tr>
											<td>담&#160;&#160;&#160;당&#160;&#160;&#160;자 : 
												<%= xmlToData.getData("chrg_name_text") %>
											</td>
										</tr>
										<tr>
											<td>주&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;소 : 
												<%= xmlToData.getData("issu_addr_txt1") %><br /> 
						      					&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
												<%= xmlToData.getData("issu_addr_txt2") %>
											</td>
										</tr>
										<tr>
											<td>모집 대리점 :
											<%
											String agnc_name = "";
											if(null != xmlToData.getData("invi_agnc_name")){
												agnc_name = xmlToData.getData("invi_agnc_name");
											}
											
											String invi_ownr_name = "";
											if(null != xmlToData.getData("invi_ownr_name")){
												invi_ownr_name = xmlToData.getData("invi_ownr_name");
											}
											
											String invi_phon_text = "";
											if(null != xmlToData.getData("invi_phon_text")){
												invi_phon_text = xmlToData.getData("invi_phon_text");
											}
											%> 
												<%= agnc_name %>
						      					&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
												<%= invi_ownr_name %>
												&#160;
												<%= invi_phon_text %>
											</td>
										</tr>
									</table>
								</td>
								<td valign="top">
									<table width="200">
										<tr>
											<td align="right">
											<%
												String docu_issu_date = xmlToData.getData("docu_issu_date");
												if(!"".equals(docu_issu_date)){
													docu_issu_date = docu_issu_date.substring(0,4) + "년 " + docu_issu_date.substring(4,6) + "월 " + docu_issu_date.substring(6,8) + "일";
												}											
											%>											
												<%= docu_issu_date %>
											</td>
										</tr>
										<tr>
											<td align="right">
												<%= xmlToData.getData("issu_addr_txt1") %>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>			
					</td>
				</tr>
				<tr>
					<td colspan="3" height="26">&#160;</td>
				</tr>
			</table>  
		</td>
		<td width="38">&#160;</td>
	</tr>
  </tbody>
</table> 
</body>
<% if(null != print && print.equals("yes")){ %>
<script language="javascript"> 
var browser = "<%=browser%>";

if(browser == "IE") {

var w = window.open('about:blank','printWin','width=800,height=500,scrollbars=yes');
var wdoc = w.document;

wdoc.open();

wdoc.writeln("<table width=\"750\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" background=\"http://www.sgic.co.kr/chp/img/xsl/xsl_back.jpg\">"+document.getElementById('printArea').innerHTML+"</table>");
wdoc.close();

w.print();
w.close();
}
else {
	this.print();
}
</script>
<% } %>
</html>