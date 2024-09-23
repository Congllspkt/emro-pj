package smartsuite.app.bp.approval.service;

public class ApprovalConst {

	/**
	 *  G002 : 결재 상태 (= apvl_sts_ccd )
	 */
	/** 결재 진행상태: 임시저장 */
	public static final String APPROVAL_STS_TEMP_SAVE = "CRNG";

	/** 결재 진행상태: 상신취소 */
	public static final String APPROVAL_STS_CANCEL = "RPTG_CNCL";  //RPTG_CNCL
	
	/** 결재 진행상태: 결재상신 */
	public static final String APPROVAL_STS_PROGRESSING = "PRGSG";

	/** 결재 진행상태: 결재반려 */
	public static final String APPROVAL_STS_RETURN = "RET";
	
	/** 결재 진행상태: 결재승인 */
	public static final String APPROVAL_STS_APPROVED = "APVD";


	
	/** 결재선 구분: 결재선(AL) */
	public static final String APRV_LINE = "AL";
	
	/** 결재선 구분: 참조선(RL) */
	public static final String REF_LINE = "RL";


	/**
	 * G006 : 결재 결과 처리 상태 ( = apvl_res_ccd )
	 */

	/**결재 결과 처리 : 진행중 */
	public static final String APPROVAL_RESULT_PROCESS_PROGRESSING = "APVL_RES_PRCSG_PRGSG";

	/**결재 결과 처리 : 완료 */
	public static final String APPROVAL_RESULT_PROCESS_COMPLETED = "APVL_RES_PRCSG_CMPLD";

	/**결재 결과 처리 : 에러 */
	public static final String APPROVAL_RESULT_PROCESS_ERROR = "APVL_RES_PRCSG_ERR";



	/**
	 * G005 :결재자 종류코드 ( = apvr_typ_ccd )
	 */

	/**결재자 종류 : 기안자 */
	public static final String APPROVAL_USER_TYPE_CD_DRAFT = "DFT";

	/**결재자 종류 : 결재자 */
	public static final String APPROVAL_USER_TYPE_CD_APPROVAL = "APVL";

	/**결재자 종류 : 합의 */
	public static final String APPROVAL_USER_TYPE_CD_AGREE = "AG";

	/**결재자 종류 : 참조 */
	public static final String APPROVAL_USER_TYPE_CD_REFERENCE = "REF";

	/**결재자 종류 : 병렬 합의자 */
	public static final String APPROVAL_USER_TYPE_CD_PARALLEL_AGREE = "PARLL_AG";




	/**
	 * G001 : 결재 결과코드 ( = apvlln_apvl_res_ccd )
	 */

	/** 결재 결과상태 : 미처리 */
	public static final String APPROVAL_LINE_STS_WAITING = "WTG";

	/**결재 결과상태: 열람 - 참조자(RL)인 경우 */
	public static final String APPROVAL_LINE_STS_READING = "RDG";

	/**결재 결과상태: 승인 */
	public static final String APPROVAL_LINE_STS_APPROVED = "APVD";

	/**결재 결과상태: 반려 */
	public static final String APPROVAL_LINE_STS_RETURN = "RET";





}
