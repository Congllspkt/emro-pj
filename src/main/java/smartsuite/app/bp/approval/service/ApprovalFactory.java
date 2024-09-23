package smartsuite.app.bp.approval.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApprovalFactory implements ApplicationContextAware {
	ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * approval strategy의 값을 반환한다.
	 *
	 * @param approvalType the approval type
	 * @return ApprovalStrategy
	 */
	public ApprovalStrategy afterApprovalProcessing(String approvalType) {
		ApprovalStrategy strategy = (ApprovalStrategy) applicationContext.getBean(AfterApprovalProcessBean.findAfterApprovalProcessBeanName(approvalType));
		return strategy;
	}


	/**
	 * Approval Stratege 에 따라, 각 업무 후 처리 메소드 호출
	 * @param approvalTypeCode
	 * @param applicationId
	 * @param statusCode
	 */
	public void afterApprovalProcessing(String approvalTypeCode, String applicationId , String statusCode) {
		ApprovalStrategy strategy = this.afterApprovalProcessing(approvalTypeCode);
		if (strategy != null) {
			if (ApprovalConst.APPROVAL_STS_APPROVED.equals(statusCode)) { // 결재승인(C)
				strategy.doApprove(approvalTypeCode, applicationId);
			} else if (ApprovalConst.APPROVAL_STS_RETURN.equals(statusCode)) { // 결재반려(B)
				strategy.doReject(approvalTypeCode, applicationId);
			} else if (ApprovalConst.APPROVAL_STS_CANCEL.equals(statusCode)) { // 상신취소(D)
				strategy.doCancel(approvalTypeCode, applicationId);
			} else if (ApprovalConst.APPROVAL_STS_PROGRESSING.equals(statusCode)) { // 결재상신(P)
				strategy.doSubmit(approvalTypeCode, applicationId);
			} else if (ApprovalConst.APPROVAL_STS_TEMP_SAVE.equals(statusCode)) { // 임시저장(T)
				strategy.doTemporary(approvalTypeCode, applicationId);
			}
		}
	}


	/**
	 * 후 처리를 당하는 Service(Bean) 를 취급하는 enum
	 */
	public enum AfterApprovalProcessBean{
		// 구매요청
		PR_APPROVAL("PR","prApprovalService"),
		// 구매요청변경
		PC_APPROVAL("PR_CHG","prModApprovalService"),
		UPCR_APPROVAL("UPCR","upcrApprovalService"),
		UPCR_CHG_APPROVAL("UPCR_CHG","upcrModApprovalService"),
		// RFx진행품의
		RFX_APPROVAL("RFX","rfxApprovalService"),
		// RFx결과품의(업체선정)
		RFX_RESULT_REQUEST_APPROVAL("RFX_VD_SLCTN","rfxResultApprovalService"),
		// 역경매진행품의
		AUCTION_PROGRESS_REQUEST_APPROVAL("RAUC","auctionProgressApprovalService"),
		// 역경매결과품의(업체선정)
		AUCTION_RESULT_REQUEST_APPROVAL("RAUC_VD_SLCTN","auctionResultApprovalService"),
		// 발주
		PO_APPROVAL("PO","poApprovalService"),
		// 발주변경
		MODIFY_PO_APPROVAL("PO_CHG","modifyPoApprovalService"),
		// 발주변경요청
		MODIFY_REQUEST_PO_APPROVAL("PO_CHG_REQ","modifyReqPoApprovalService"),
		// 단가계약진행
		PRICE_CONTRACT_APPROVAL("UPRCCNTR","unitPriceContractApprovalService"),
		// 단가계약변경(구매부서)
		MODIFY_PRICE_CONTRACT_APPROVAL("UPRCCNTR_CHG","modifyUnitPriceContractApprovalService"),
		// 단가계약변경요청(요청부서)
		MODIFY_REQUEST_PRICE_CONTRACT_APPROVAL("UPRCCNTR_CHG_REQ","modifyReqUnitPriceContractApprovalService"),
		// 검수
		GR_APPROVAL("GR_APVD","grApprovalService"),
		// 기성승인
		PAYMENT_BILL_APPROVAL("PP_APVD","progressPaymentApprovalService"),
		// 발주 협력사 등록 요청
		VENDOR_REGISTER_APPROVAL("PO_POSS_VD_REG_REQ","vendorRegistrationApprovalService"),
		// 협력사관리그룹 등록 요청
		VENDOR_MANAGEMENT_GROUP_REGISTER_APPROVAL("VMG_REG_REQ","vendorVmgRegistrationApprovalService"),
		// 발주 제한 요청
		VENDOR_PO_LIMIT_APPROVAL("PO_LMT_REQ","vendorPoLimitApprovalService"),
		// 협력사관리그룹 등록 취소 요청
		VENDOR_MANAGEMENT_GROUP_REG_CANCEL_APPROVAL("VMG_REG_CNCL_REQ","vendorVmgRegistrationCancelApprovalService"),
		// 주요 정보 변경 요청
		VENDOR_MODIFY_PROPERTIES_APPROVAL("MAIN_INFO_CHG_REQ","vendorInfoModifyApprovalService"),
		//정기평가 확정
		VENDOR_EVALUATION_APPROVAL("PRDCEVAL_CNFD","performanceEvalApprovalService"),
		//전략특성평가 확정
		VENDOR_STRATEGIC_CHARACTERISTIC_EVALUATION_APPROVAL("STRCHARCSEVAL_CNFD","srmEvalApprovalService"),
		// 공공입찰 - 사전공고
		PRE_NOTICE_APPROVAL("PPRI","priNotiApprovalService"),
		// 공공입찰 - 사전공고취소
		PRE_NOTICE_CANCEL_APPROVAL("PPRICN","priNotiApprovalService"),
		// 공공입찰 - 입찰공고
		BID_NOTICE_APPROVAL("PBID","bidApprovalService"),
		// 공공입찰 - 입찰일시변경
		BID_NOTICE_DATE_REPLACE_APPROVAL("PBDM","bidApprovalService"),
		// 공공입찰 - 공고취소
		BID_NOTICE_CANCEL_APPROVAL("PBCN","bidApprovalService"),
		// 공공입찰 - 정정공고,
		CORRECTION_NOTICE_APPROVAL("PBCR","bidApprovalService"),
		// 공공입찰 - 부정당업자
		UNFAIR_BUSINESS_CANCEL_APPROVAL("PIJ","vendorApprovalService"),
		// 공공입찰 - 기초조서
		BASIC_LEDGER_APPROVAL("PBRP","priceMgtApprovalService"),
		// 공공입찰 - 적격심사
		BID_QUALIFICATION_REVIEW_APPROVAL("QEVAL","bidQualApprovalService"),
		// 공공입찰 - 협상의뢰
		BID_NEGOTIATION_REQUEST_APPROVAL("NEGOREQ","bidNegoApprovalService"),
		// 공공입찰 - 협상결과
		BID_NEGOTIATION_RESULT_APPROVAL("NEGORES","bidNegoApprovalService"),
		// 공공입찰 - 낙찰취소
		BID_WINNING_CANCEL_APPROVAL("STLC","bidOpenResultApprovalService"),
		//이의제기
		OBJECTION_APPROVAL("OBJ_RAISE","srmEvalCmplApprovalService"),
		//차별화전략수행
		DIFFERENTIATION_STRATEGY_EXECUTION_APPROVAL("DIFF_RES_DTL_PLAN","elevApprovalService"),
		//자재구매요청
		MATERIAL_PURCHASE_REQUEST_APPROVAL("MR","mrRegistApprovalService"),
		//자재구매전략품의
		MATERIAL_PURCHASE_STRATEGY_REQUEST_APPROVAL("PSM","mrPsmApprovalService"),
		//EPC RFQ 업체 선정 품의
		EPC_REQ_SELECT_COMPANY_REQUEST_APPROVAL("EPCSV","epcRfqApprovalService"),
		//EPC PO품의
		EPC_PO_REQUEST_APPROVAL("EPCPO","epcPoApprovalService"),
		//EPC PO변경품의
		MODIFY_EPC_PO_REQUEST_APPROVAL("EPCPOM","modifyEpcPoApprovalService"),
		// Workflow RFx진행품의
		WORKFLOW_RFX_PROCEED_REQUEST_APPROVAL("WRKFW_RFX","rfxFormApprovalService"),
		// Workflow  RFx결과품의(업체선정)
		WORKFLOW_RFX_RESULT_REQUEST_APPROVAL("WRKFW_RFX_SLCTN","rfxFormResultApprovalService"),
		// 입찰공고  품의
		BID_NOTICE_REQUEST_APPROVAL("RFX_PRENTC","bidNoticeApprovalService"),
		// 전자계약 품의
		E_CONTRACT_REQUEST_APPROVAL("ECNTR","econtractApprovalService"),
		// 단가계약품의(전자계약자동생성)
		PRICE_CONTRACT_E_CONTRACT_REQUEST_APPROVAL("EPCP","econtractPriceApprovalService"),
		// 단가계약변경품의(전자계약자동생성)
		MODIFY_PRICE_CONTRACT_E_CONTRACT_REQUEST_APPROVAL("EAPCP","econtractModifyPriceApprovalService"),
		// 협상 요청
		NEGOTIATION_REQUEST_APPROVAL("NEGO","negoApprovalService"),
		PO_CNTR_APPROVAL("PO_CNTR","poApprovalService"),
		MODIFY_PO_CNTR_APPROVAL("PO_CNTR_CHG","modifyPoApprovalService"),
		QTA("QTA","qtaApprovalService"),
		PR_UPRCCNTR_SGNG_APPROVAL("PR_UPRCCNTR_SGNG","prApprovalService"),
		// 구매요청변경
		PR_UPRCCNTR_SGNG_CHG_APPROVAL("PR_UPRCCNTR_SGNG_CHG","prModApprovalService");

		private final String taskCode;
		private final String afterApprovalProcessBeanName;

		AfterApprovalProcessBean(String code,String codeName) {
			this.taskCode = code;
			this.afterApprovalProcessBeanName = codeName;
		}

		public String getTaskCode() {
			return taskCode;
		}

		public String getAfterApprovalProcessBeanName() {
			return afterApprovalProcessBeanName;
		}

		public static String findTaskCode(String findTaskCode){
			for(AfterApprovalProcessBean beanInfo : AfterApprovalProcessBean.values()){
				if(findTaskCode.equals(beanInfo.getTaskCode())) return beanInfo.getTaskCode();
			}
			return "";
		}

		public static String findAfterApprovalProcessBeanName(String findTaskCode){
			for(AfterApprovalProcessBean beanInfo : AfterApprovalProcessBean.values()){
				if(findTaskCode.equals(beanInfo.getTaskCode())) return beanInfo.getAfterApprovalProcessBeanName();
			}
			return "";
		}
	}

}
