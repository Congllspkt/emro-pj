package smartsuite.app.common.shared.service.onboarding;

public class OnboardingJobTypeInfo {

	// 공통코드 : E902
	//NEW	신규
	private static final String ONBOARDING_TYPE_NEW = "NEW";
	//PTNL	잠재
	private static final String ONBOARDING_TYPE_POTENTIAL = "PTNL";
	//OFC	정규
	private static final String ONBOARDING_TYPE_OFFICIAL = "OFC";

	//** APPEND TEST 용  ONE_TIME_COMPANY 1회성 업체
	private static final String ONBOARDING_TYPE_ONETIME = "OTC";


	public enum OnBoardingJobTypeEnum {

		// 자사정보관리 : 신규/잠재/정규
		VENDOR_PROFILE_MANAGEMENT("VDP", new String[]{ONBOARDING_TYPE_NEW, ONBOARDING_TYPE_POTENTIAL, ONBOARDING_TYPE_OFFICIAL}),

		// RFI 대상여부 : 신규/잠재/정규
		RFI_TARGET("RFI_TARGET", new String[]{ONBOARDING_TYPE_NEW, ONBOARDING_TYPE_POTENTIAL, ONBOARDING_TYPE_OFFICIAL}),

		// RFX 대상여부 : 잠재/정규
		RFX_TARGET("RFX_TARGET", new String[]{ONBOARDING_TYPE_POTENTIAL, ONBOARDING_TYPE_OFFICIAL}),

		// RFX 사전공고 : 잠재/정규
		RFX_BID_NOTICE("RFX_BID_NOTICE", new String[]{ONBOARDING_TYPE_POTENTIAL, ONBOARDING_TYPE_OFFICIAL}),

		// 현장설명회 : 잠재/정규
		FI("FI", new String[]{ONBOARDING_TYPE_POTENTIAL, ONBOARDING_TYPE_OFFICIAL}),

		// 역경매 : 잠재/정규
		R_AUCTION("R_AUCTION", new String[]{ONBOARDING_TYPE_POTENTIAL, ONBOARDING_TYPE_OFFICIAL}),

		// RFX 선정가능여부 : 정규
		RFX_SELECTION("RFX_SELECTION", new String[]{ONBOARDING_TYPE_OFFICIAL}),

		// 단가계약 : 정규
		UNIT_PRICE_CONTRACT("UNIT_PRICE_CONTRACT", new String[]{ONBOARDING_TYPE_OFFICIAL}),

		// 정산: 정규
		CALCULATE("CALCULATE", new String[]{ONBOARDING_TYPE_OFFICIAL});

		OnBoardingJobTypeEnum(String jobTypeCode,String[] onBoardingTypeArray){
			this.jobTypeCode = jobTypeCode;
			this.onBoardingTypeArray = onBoardingTypeArray;
		}

		private final String jobTypeCode;

		private final String[] onBoardingTypeArray;


		public String getJobTypeCode() {
			return jobTypeCode;
		}

		public String[] getOnBoardingTypeArray() {
			return onBoardingTypeArray;
		}

		public static String[] getFindOnboardingJobType(String onBoardingJobTypeCode) {
			for(OnBoardingJobTypeEnum codeInfo : OnBoardingJobTypeEnum.values()){
				if(onBoardingJobTypeCode.equals(codeInfo.getJobTypeCode())){
					return codeInfo.getOnBoardingTypeArray();
				}
			}
			return new String[]{};
		}
	}
}
