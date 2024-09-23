<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html;charset=utf-8" %>
<%@page import="java.util.Map"%>
<%
    response.setHeader("Pragma","no-cache"); 
    response.setDateHeader("Expires",0); 
    response.setHeader("Cache-Control","no-store"); 
%>  
<%@ page language="java" import="java.util.*"%>
<%@ page import="java.net.*,java.io.*" %>
<%
	Map<String,Object> bondInfo = (Map<String,Object>)request.getAttribute("bondInfo");
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
		<c:out value="${bondInfo.head_mesg_name}"></c:out>
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
	
</head>
<body>
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
						<c:choose>
							<c:when test="${bondInfo.head_mesg_type eq 'CONGUA'}">
							이행(계약)보증보험증권
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${bondInfo.head_mesg_type eq 'FLRGUA'}">
							이행(하자)보증보험증권
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${bondInfo.head_mesg_type eq 'PREGUA'}">
							이행(선금급)보증보험증권
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${bondInfo.head_mesg_type eq 'PAYGUA'}">
							이행(지급)보증보험증권
							</c:when>
						</c:choose>
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
							<c:set var="head_docu_numb" value="${bondInfo.head_docu_numb}"/>
								<td width="560" height="23" class="sub_title" valign="bottom">
									증권번호 제 ${fn:substring(head_docu_numb,6,9)}-${fn:substring(head_docu_numb,9,12)}-${fn:substring(head_docu_numb,12,25)}	호
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
									<c:set var="appl_orps_divs" value="${bondInfo.appl_orps_divs}"/>
									<c:set var="appl_orps_iden" value="${bondInfo.appl_orps_iden}"/>
									<c:choose>
										<c:when test="${appl_orps_divs eq 'O'}">
											&nbsp;${fn:substring(appl_orps_iden,0,3)}-${fn:substring(appl_orps_iden,3,5)}-${fn:substring(appl_orps_iden,5,10)}
										</c:when>
										<c:when test="${appl_orps_iden eq 'P'}">
											&nbsp;${fn:substring(appl_orps_iden,0,6)}-${fn:substring(appl_orps_iden,6,13)}
										</c:when>
									</c:choose>
									<br />&nbsp;
									<c:out value="${bondInfo.appl_orga_name}"></c:out>
									<br />&nbsp;
									<c:out value="${bondInfo.appl_ownr_name}"></c:out>
								</td>
								<td width="15%" class="fontbc">피보험자</td>
								<td width="35%" colspan="2">&nbsp;
									<c:set var="cred_orps_divs" value="${bondInfo.cred_orps_divs}"/>
									<c:set var="cred_orps_iden" value="${bondInfo.cred_orps_iden}"/>
									<c:choose>
										<c:when test="${cred_orps_divs eq 'O'}">
											${fn:substring(cred_orps_iden,0,3)}-${fn:substring(cred_orps_iden,3,5)}-${fn:substring(cred_orps_iden,5,10)}
										</c:when>
										<c:when test="${cred_orps_divs eq 'P'}">
											${fn:substring(cred_orps_iden,0,6)}-${fn:substring(cred_orps_iden,6,13)}
										</c:when>
									</c:choose>
									<br />&nbsp;
									<c:out value="${bondInfo.cred_orga_name}"></c:out>
							 </td>
							</tr>
							<tr>
								<td class="fontbc" height="45">보험가입금액</td>
									<c:choose>
										<c:when test="${bondInfo.penl_curc_code eq 'WON'}">
											<td colspan="2">&nbsp;金
												<c:out value="${bondInfo.bond_penl_text}"></c:out> 整 
												<br />&nbsp;
												￦ <c:out value="${bondInfo.bond_penl_amnt}"></c:out>-											
											</td>
										</c:when>
										<c:otherwise>
											<td colspan="2">
												<br />&nbsp;
												<c:out value="${bondInfo.penl_curc_code}"></c:out>
												<c:out value="${bondInfo.bond_penl_amnt}"></c:out>-
											</td>
										</c:otherwise>
									</c:choose>
																				  												  									
								<td class="fontbc">보 험 료</td>
								<td align="right">
									\ <c:out value="${bondInfo.bond_prem_amnt}"></c:out>-
								</td>
							</tr>
							<tr>
								<td class="fontbc" height="30">보 험 기 간</td>
								<td colspan="4">&nbsp;
									<c:set var="bond_begn_date" value="${bondInfo.bond_begn_date}"/>
									<c:set var="bond_fnsh_date" value="${bondInfo.bond_fnsh_date}"/>
									<c:out value="${fn:substring(bond_begn_date,0,4)}"></c:out>년 <c:out value="${fn:substring(bond_begn_date,4,6)}"></c:out>월 <c:out value="${fn:substring(bond_begn_date,6,8)}"></c:out>일 부터 
									<c:out value="${fn:substring(bond_fnsh_date,0,4)}"></c:out>년 <c:out value="${fn:substring(bond_fnsh_date,4,6)}"></c:out>월 <c:out value="${fn:substring(bond_fnsh_date,6,8)}"></c:out>일 까지
								</td>
							</tr>
							<tr>
								<td class="fontbc" height="30">보 증 내 용</td>
								<td colspan="4">&nbsp;
									<c:out value="${bondInfo.bond_stat_text}"></c:out>
								</td>
							</tr>
							<tr>
								<td class="fontbc" height="70">특 별 약 관</td>   <!--- 기존 XML 및 표준 XML을 위해 두개 존재 -->
								<td colspan="4">&nbsp;
									<c:out value="${bondInfo.spcl_prov_text}"></c:out>
								</td>
							</tr>
							<tr>
								<td class="fontbc" height="70">특 기 사 항</td>
								<td colspan="4">&nbsp;
									<c:out value="${bondInfo.spcl_cond_text}"></c:out>
								</td>
							</tr>
							<tr>
								<td colspan="5" align="center" height="190" valign="top">
									<!--- 주계약내용 시작 (이행계약) -->
									<c:choose>
										<c:when test="${bondInfo.head_mesg_type eq 'CONGUA'}">
											<table width="97%" border="0">
												<tr>
													<td class="text6" colspan="2">주계약내용</td>
												</tr>
												<tr>
													<td class="fontb" width="100">계약명</td>
													<td>
														<c:out value="${bondInfo.cont_name_text}"></c:out>
													</td>
												</tr>
												<tr>
													<td class="fontb">계약기간</td>
													<td>
														<c:out value="${bondInfo.cont_begn_date}"></c:out> 부터
														<c:out value="${bondInfo.cont_fnsh_date}"></c:out> 까지
													</td>
												</tr>
												<tr>
													<td class="fontb">계약체결일자</td>
													<td>
														<c:out value="${bondInfo.cont_main_date}"></c:out>
													</td>
												</tr>
												<tr>
													<td class="fontb">계약금액</td>
													<td>
														<c:choose>
															<c:when test="${bondInfo.cont_curc_code eq 'WON'}">
																￦ <c:out value="${bondInfo.cont_main_amnt}"></c:out>-
															</c:when>
															<c:otherwise>
																<c:out value="${bondInfo.cont_main_amnt}"></c:out>-
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="fontb">계약보증금율</td>
													<td>
														<c:out value="${bondInfo.bond_pric_rate}"></c:out> %
													</td>
												</tr>
											</table>
										</c:when>
										<c:when test="${bondInfo.head_mesg_type eq 'FLRGUA'}">
											<table width="97%" border="0">
												<tr>
													<td class="text6" colspan="2">주계약내용</td>
												</tr>
												<tr>
													<td class="fontb" width="100">계약명</td>
													<td>
														<c:out value="${bondInfo.cont_name_text}"></c:out>
													</td>
												</tr>
												<tr>
													<td class="fontb">하자담보기간</td>
													<td>
														<c:out value="${bondInfo.cont_begn_date}"></c:out> 부터
														<c:out value="${bondInfo.cont_fnsh_date}"></c:out> 까지
													</td>
												</tr>
												<tr>
													<td class="fontb">계약체결일자</td>
													<td>
														<c:out value="${bondInfo.cont_main_date}"></c:out>
													</td>
												</tr>
												<tr>
													<td class="fontb">계약금액</td>
													<td>
														<c:choose>
															<c:when test="${bondInfo.cont_curc_code eq 'WON'}">
																￦ <c:out value="${bondInfo.cont_main_amnt}"></c:out>-
															</c:when>
															<c:otherwise>
																<c:out value="${bondInfo.cont_main_amnt}"></c:out>-
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="fontb">하자보증금율</td>
													<td>
														<c:out value="${bondInfo.bond_pric_rate}"></c:out>
													</td>
												</tr>
											</table>	
										</c:when>
										<c:when test="${bondInfo.head_mesg_type eq 'PREGUA'}">
											<table width="97%" border="0">
												<tr>
													<td class="text6" colspan="2">주계약내용</td>
												</tr>
												<tr>
													<td class="fontb" width="180">계약명</td>
													<td>
														<c:out value="${bondInfo.cont_name_text}"></c:out>
													</td>
												</tr>
												<tr>
													<td class="fontb">계약금액</td>
													<td>
														<c:choose>
															<c:when test="${bondInfo.cont_curc_code eq 'WON'}">
																￦ <c:out value="${bondInfo.cont_main_amnt}"></c:out>-
															</c:when>
															<c:otherwise>
																<c:out value="${bondInfo.cont_main_amnt}"></c:out>-
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="fontb">선금(전도자재)액</td>
													<td>
														<c:choose>
															<c:when test="${bondInfo.penl_curc_code eq 'WON'}">
																￦ <c:out value="${bondInfo.cont_paym_amnt}"></c:out>-
															</c:when>
															<c:otherwise>
																<c:out value="${bondInfo.cont_paym_amnt}"></c:out>-
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="fontb">계약체결일자</td>
													<td>
														<c:out value="${bondInfo.cont_main_date}"></c:out>
													</td>
												</tr>
												<tr>
													<td class="fontb">계약기간</td>
													<td>
														<c:out value="${bondInfo.cont_begn_date}"></c:out> 부터
														<c:out value="${bondInfo.cont_fnsh_date}"></c:out> 까지										
													</td>
												</tr>
												<tr>
													<td class="fontb">선금(전도자재)지급(예정)일</td>
													<td>
														<c:out value="${bondInfo.cont_paym_date}"></c:out>
													</td>
												</tr>
											</table>
										</c:when>
									</c:choose>
									<!--- 주계약내용 끝  -->
								</td>
							</tr>
						</table>
					</td>
					<!--- 내용 끝 -->
				</tr> 
				<tr>
					<td colspan="3" height="35" valign="bottom">
						<c:choose>
							<c:when test="${bondInfo.head_mesg_type eq 'CONGUA'}">
								우리 회사는 이행(계약)보증보험 보통약관, 특별약관 및 이 증권에 기재된 내용에 따라 이행(계약)보증보험 계약을 체결하였음이 확실하므로 그 증으로 이 증권을 발행합니다.
							</c:when>
							<c:when test="${bondInfo.head_mesg_type eq 'FLRGUA'}">
								우리 회사는 이행(하자)보증보험 보통약관, 특별약관 및 이 증권에 기재된 내용에 따라 이행(하자)보증보험 계약을 체결하였음이 확실하므로 그 증으로 이 증권을 발행합니다.
							</c:when>
							<c:when test="${bondInfo.head_mesg_type eq 'PREGUA'}">
								우리 회사는 이행(선금급)보증보험 보통약관, 특별약관 및 이 증권에 기재된 내용에 따라 이행(선금급)보증보험 계약을 체결하였음이 확실하므로 그 증으로 이 증권을 발행합니다.
							</c:when>
							<c:when test="${bondInfo.head_mesg_type eq 'PAYGUA'}">
								우리 회사는 이행(지급)보증보험 보통약관, 특별약관 및 이 증권에 기재된 내용에 따라 이행(지급)보증보험 계약을 체결하였음이 확실하므로 그 증으로 이 증권을 발행합니다.
							</c:when>
						</c:choose>
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
												${fn:substring(bondInfo.bond_numb_text,6,9)} <c:out value="${bondInfo.issu_dept_name}"></c:out> (<c:out value="${bondInfo.chrg_phon_text}"></c:out>)
											</td>
										</tr>
										<tr>
											<td>부&#160;&#160;&#160;서&#160;&#160;&#160;장 : 
												<c:out value="${bondInfo.issu_dept_ownr}"></c:out>
											</td>
										</tr>
										<tr>
											<td>담&#160;&#160;&#160;당&#160;&#160;&#160;자 : 
												<c:out value="${bondInfo.chrg_name_text}"></c:out>
											</td>
										</tr>
										<tr>
											<td>주&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;소 : 
												<c:out value="${bondInfo.issu_addr_txt1}"></c:out><br /> 
						      					&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
						      					<c:out value="${bondInfo.issu_addr_txt2}"></c:out>
						      					&#160;
											</td>
										</tr>
										<tr>
											<td>모집 대리점 :
												<c:out value="${bondInfo.invi_agnc_name}"></c:out>
												&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
												<c:out value="${bondInfo.invi_ownr_name}"></c:out>
												&#160;
												<c:out value="${bondInfo.invi_phon_text}"></c:out>
											</td>
										</tr>
									</table>
								</td>
								<td valign="top">
									<table width="200">
										<tr>
											<td align="right">
												<c:out value="${bondInfo.docu_issu_date}"></c:out>
											</td>
										</tr>
										<tr>
											<td align="right">
												<c:out value="${bondInfo.issu_addr_txt1}"></c:out>
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
</html>