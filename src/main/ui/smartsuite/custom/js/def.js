/**
 * 공통 속성을 정의한다
 *
 * @class DEF
 */
var DEF = {
    /**
     * 성공 - 서버에서 데이터 처리 후 결과 코드
     *
     * @property SUCCESS
     * @type String
     */
    SUCCESS: "S",

    /**
     * 실패 - 서버에서 데이터 처리 후 결과 코드
     *
     * @property FAIL
     * @type String
     */
    FAIL: "E",

    /**
     * 데이터 중복 - 서버에서 데이터 처리 후 결과 코드
     *
     * @property DUPLICATED
     * @type String
     */
    DUPLICATED: "D",

    /**
     * 데이터 사용중 - 서버에서 데이터 처리 후 결과 코드
     *
     * @property USED
     * @type String
     */
    USED: "U",
    
    /**
     * 존재하지 않음 - 서버에서 데이터 처리 후 결과 코드
     *
     * @property NOT_EXIST
     * @type String
     */
    NOT_EXIST: "N",

    /**
     * 역할코드 - 조회
     *
     * @property READ
     * @type String
     */
    READ : "R",

    /**
     * 역할코드 - 저장
     *
     * @property SAVE
     * @type String
     */
    SAVE : "S",

    /**
     * 역할코드 - 결재
     *
     * @property APPROVAL
     * @type String
     */
    APPROVAL : "A",
    
    /**
     * 역할없음 - 결재
     *
     * @property UNAUTHORIZED
     * @type String
     */
    UNAUTHORIZED : "UNAUTHORIZED",
    
    /**
     * 유효하지 않은 상태
     */
    INVALID_STATUS_ERR : "INVALID_STATUS_ERR",
    
    /**
     * PR Item 진행상태 오류
     */
    PR_ITEM_STS_ERR : "PR_ITEM_STS_ERR",
    
    /**
     * PR 변경 오류
     */
    PR_MOD_ERR : "PR_MOD_ERR",
    
    /**
     * RFX 마감
     *
     */
    RFX_END : "RFX_END",
    
    /**
     * RFX 포기
     */
    RFX_REJECT : "RFX_REJECT",
    
    /**
     * 견적 제출
     */
    QTA_SUBMIT : "QTA_SUBMIT",
    
    /**
     * RFX 강제마감 오류
     */
    RFX_CLOSE_BYPASS_ERR : "RFX_CLOSE_BYPASS_ERR",
	
    /**
     * RFP 비가격평가 설정 미완료
     */
	RFP_NON_PRI_EVAL_SET_INCOMPELETED : "RFP_NON_PRI_EVAL_SET_INCOMPELETED",
	
    /**
     * RFP 평가자 미존재
     */
    RFP_NO_EVALUATOR : "RFP_NO_EVALUATOR",
    
    /**
     * RFx CostFactor 설정 미완료
     */
    RFX_PRI_FACT_SET_INCOMPELETED : "RFX_PRI_FACT_SET_INCOMPELETED",
    
    /**
     * RFP 평가 미완료
     */
    RFP_EVAL_INCOMPELETED : "RFP_EVAL_INCOMPELETED",
    
    /**
     * RFX 개찰시간 전
     */
    RFX_BEFORE_OPEN_TIME : "RFX_BEFORE_OPEN_TIME",
    
    /**
     * 역경매 금액 체크 오류
     */
    AUCTION_AMT_CHECK_ERR : "AUCTION_AMT_CHECK_ERR",
	
    /**
     * 검수/기성 중인 품목 존재
     */
    HAS_GR_ITEM : "HAS_GR_ITEM",
    
    /**
     * 검수/기성 요청 임시저장 품목 존재
     */
    HAS_TEMP_AR_ITEM : "HAS_TEMP_AR_ITEM",
    
    /**
     * 검수/기성 요청 품목 존재
     */
    HAS_AR_ITEM : "HAS_AR_ITEM",
    
    /**
     * 선급금 등록 존재
     */
    EXIST_PRE_PAY_REG : "EXIST_PRE_PAY_REG",
    
    /**
     * 선급금 등록 미완료
     */
    PRE_PAY_REG_INCOMPLETE : "PRE_PAY_REG_INCOMPLETE",
    
    /**
     * 기성요청 상태오류
     */
    VD_GR_STS_CCD_ERR :"VD_GR_STS_CCD_ERR",
    
    /**
     * 검수등록 수량 오류
     */
    GR_QTY_ERR : "GR_QTY_ERR",
    
    /**
     * 검수요청 수량 오류
     */
    ASN_QTY_ERR : "ASN_QTY_ERR",
    
    /**
     * 발주 상태오류
     */
    PO_STATE_ERR : "PO_STATE_ERR",
    
    /**
     * 발주변경중
     */
    PO_CHANGE_PROGRESS : "PO_CHANGE_PROGRESS",
    
    /**
     * 발주해지
     */
    PO_TERMINATE : "PO_TERMINATE",
    
    /**
     * 발주종료
     */
    PO_COMPLETE : "PO_COMPLETE",
    
    /**
     * 발주삭제
     */
    PO_DELETED : "PO_DELETED",
    
    /**
     * 계약삭제
     */
    PRC_CNTR_DELETED : "PRC_CNTR_DELETED",
    
    /**
     * 계약 상태오류
     */
    PRC_CNTR_STATE_ERR : "PRC_CNTR_STATE_ERR",
    
    /**
     * RFx 결재진행중
     */
    RFX_APRV_PROGRESS : "RFX_APRV_PROGRESS",
    
    /**
     * RFx 품목 복수업체 선정
     */
    RFX_ITEM_MULTI_SELECTED : "RFX_ITEM_MULTI_SELECTED",
	
    // 그리드의 dummy 컬럼명
    DUMMY: "_dummy_"
};