package smartsuite.app.common.shared.service;

import cn.apiclub.captcha.Captcha;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.board.service.BoardAdminService;
import smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.service.OperationOrganizationService;
import smartsuite.app.common.AttachService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.shared.repository.SharedRepository;
import smartsuite.app.common.shared.service.onboarding.OnboardingJobTypeInfo;
import smartsuite.data.FloaterStream;
import smartsuite.security.account.service.AccountService;
import smartsuite.security.authentication.Auth;
import smartsuite.security.authentication.AuthenticationPostService;
import smartsuite.security.authentication.ProxyPasswordEncoder;
import smartsuite.security.captcha.CaptchaGenerator;
import smartsuite.security.captcha.CaptchaUtils;
import smartsuite.security.core.crypto.CipherUtil;
import smartsuite.security.userdetails.User;
import smartsuite.spring.tenancy.context.TenancyContext;
import smartsuite.spring.tenancy.context.TenancyContextHolder;
import smartsuite.spring.tenancy.core.DefaultTenant;
import smartsuite.spring.tenancy.core.Tenant;
import smartsuite.spring.tenancy.web.provider.ServiceTenantProvider;
import smartsuite.spring.tenancy.web.resolver.DefaultTenantResolver;
import smartsuite5.attachment.core.crypto.impl.BasicAthfCryptoUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 공통으로 사용하는 서비스 관련 Class입니다.
 *
 * @author hjhwang
 * @see
 * @FileName SharedService.java
 * @package smartsuite.app.shared
 * @Since 2016. 2. 2
 * @변경이력 : [2016. 2. 2] hjhwang 최초작성
 */
@SuppressWarnings({ "rawtypes" })
@Service
@Transactional
public class SharedService {

	static final Logger LOG = LoggerFactory.getLogger(SharedService.class);

	@Inject
	CaptchaGenerator captchaGenerator;

	@Inject
	CipherUtil cipherUtil;

	@Inject
	private HttpSession httpSession;

	@Inject
	public AuthenticationPostService authenticationPostService;

	@Inject
	private AccountService accountService;

	@Inject
	BasicAthfCryptoUtil encryptor;

	@Inject
	SharedRepository sharedRepository;

	@Inject
	AttachService attachService;

	@Inject
	BoardAdminService boardAdminService;

	@Inject
	OperationOrganizationService operationOrganizationService;
	
	@Inject
	ServiceTenantProvider serviceTenantProvider;

	/**
	 * 공통 코드 조회.
	 *
	 * @param code the code
	 * @return the list
	 */
	// @Cacheable(value="cmmn-code", key="#code")
	@Cacheable(value = "cmmn-code", key = "#code + '_' + T(org.springframework.context.i18n.LocaleContextHolder).getLocale().toString()")
	public List findCommonCode(String code) {
		return sharedRepository.findCommonCode(code);
	}

	/**
	 * 공통코드와 특정 속성 list 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 19
	 * @Method Name : findListCommonCodeAttributeCode
	 */
	public List findListCommonCodeAttributeCode(Map<String, Object> param) {
		return sharedRepository.findListCommonCodeAttributeCode(param);
	}

	public List<Map<String, Object>> findListCommonCodeConstraintCode(Map<String, Object> param){
		return sharedRepository.findListCommonCodeConstraintCode(param);
	}

	/**
	 * 공통코드와 속성코드값을 조합한 list 조회한다.
	 */
	public List<Map<String, Object>> findListCommonCodeAttributeValue(Map<String, Object> param) {
		return sharedRepository.findListCommonCodeAttributeValue(param);
	}

	/**
	 * 마지막 수정일자 이후에 수정된 공통 코드 목록 조회.
	 *
	 * @param lastUpdated the last updated
	 * @return the list
	 */
	public List findListModifiedCode(Date lastUpdated) {
		return sharedRepository.findListModifiedCode(lastUpdated);
	}

	/**
	 * 마지막 수정일자 이후에 수정된 공통 코드 목록 조회.
	 *
	 * @return the list
	 */
	public List findListCompanyCodeForCombobox() {
		return sharedRepository.findListCompanyCodeForCombobox();
	}

	/**
	 * 현재 생성되어있는 Formatter 리스트 조회 (공통코드용)
	 * @return
	 */
	public List findListFormatterForCombobox() {
		return sharedRepository.findListFormatterForCombobox();
	}

	/**
	 * 문서번호 생성 독립 트랜잭션 생성 및 메소드 종료 후 바로 commit To-Do : 트랜잭션 확인.
	 *
	 * @param documentNumberCode the doc no cd
	 * @return doc number
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String generateDocumentNumber(String documentNumberCode) {
		Map<String, Object> documentInfo = sharedRepository.findDocumentInfo(documentNumberCode);
		return generateDocumentNumberProcess(documentInfo, documentNumberCode, null);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String generateDocumentNumber(String documentNumberCode, String oorgCd) {
		if(Strings.isNullOrEmpty(oorgCd)) {
			return this.generateDocumentNumber(documentNumberCode);
		}
		
		Map<String, Object> documentInfo = sharedRepository.findDocumentInfo(documentNumberCode);
		if("NA".equals(documentInfo.get("ren_cy_ccd"))) {
			return generateDocumentNumberProcess(documentInfo, documentNumberCode, null);
		}
		
		Map<String, Object> timezoneInfo = sharedRepository.findTimezoneByOorgCd(oorgCd);
		return generateDocumentNumberProcess(documentInfo, documentNumberCode, timezoneInfo);
	}

	/**
	 * 문서번호 생성 독립 트랜잭션 생성 및 메소드 종료 후 바로 commit To-Do : 트랜잭션 확인.
	 *
	 * @param documentNumberCode the doc no cd
	 * @param tenant  the tenant
	 * @return doc number
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String generateDocumentNumberByTenant(String documentNumberCode, String tenant) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("doc_no_cd", documentNumberCode);
		param.put("tenant", tenant);
		Map<String, Object> documentInfo = sharedRepository.findDocumentInfoByTenant(param);
		return generateDocumentNumberProcess(documentInfo, documentNumberCode, null);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String generateDocumentNumberByTenant(String documentNumberCode, String tenant, String oorgCd) {
		if(Strings.isNullOrEmpty(oorgCd)) {
			return this.generateDocumentNumberByTenant(documentNumberCode, tenant);
		}
		
		Map<String, Object> param = Maps.newHashMap();
		param.put("doc_no_cd", documentNumberCode);
		param.put("tenant", tenant);
		Map<String, Object> documentInfo = sharedRepository.findDocumentInfoByTenant(param);
		if("NA".equals(documentInfo.get("ren_cy_ccd"))) {
			return generateDocumentNumberProcess(documentInfo, documentNumberCode, null);
		}
		
		Map<String, Object> timezoneInfo = sharedRepository.findTimezoneByOorgCd(oorgCd);
		return generateDocumentNumberProcess(documentInfo, documentNumberCode, timezoneInfo);
	}


	/**
	 * 문서 번호 생성 프로세스
	 * @param documentInfo
	 * @param documentNumberCode
	 * @return
	 */
	private String generateDocumentNumberProcess(Map<String,Object> documentInfo, String documentNumberCode, Map<String, Object> timezoneInfo){
		String compareCurrentDate = "";
		String stringGeneratedLastDate = (String) documentInfo.getOrDefault("fnl_crn_dt","");
		String expression = (String) documentInfo.getOrDefault("doc_no_rule","");
		int currentSequence = 0;
		int preSequence = 0;
		int returnSequence = 0;
		String finalCreateDate = null;
		String templateExpression = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		
		TimeZone time = null;
		if(timezoneInfo != null) {
			String tmzOffset = this.convertOffset((String) timezoneInfo.get("tmz_ccd"));
			time = TimeZone.getTimeZone(tmzOffset);
			simpleDateFormat.setTimeZone(time);
		}

		try {
			String strCurrentDate = simpleDateFormat.format(new Date());
			stringGeneratedLastDate = stringGeneratedLastDate == null ? simpleDateFormat.format(new Date()) : stringGeneratedLastDate;
			Date generatedLastDate = simpleDateFormat.parse(stringGeneratedLastDate);

			if ("YRLY".equals(documentInfo.get("ren_cy_ccd").toString())) {
				compareCurrentDate = strCurrentDate.substring(0, 4);
				stringGeneratedLastDate = stringGeneratedLastDate.substring(0, 4);
			} else if ("MNTLY".equals(documentInfo.get("ren_cy_ccd").toString())) {
				compareCurrentDate = strCurrentDate.substring(0, 6);
				stringGeneratedLastDate = stringGeneratedLastDate.substring(0, 6);
			} else if ("WKLY".equals(documentInfo.get("ren_cy_ccd").toString())) {
				Calendar compareCalendar = Calendar.getInstance();
				compareCurrentDate = String.valueOf(compareCalendar.get(Calendar.WEEK_OF_YEAR));
				Calendar lastDateCalender = Calendar.getInstance();
				lastDateCalender.setTime(generatedLastDate);
				stringGeneratedLastDate = String.valueOf(lastDateCalender.get(Calendar.WEEK_OF_YEAR));
			} else if ("DLY".equals(documentInfo.get("ren_cy_ccd").toString())) {
				compareCurrentDate = strCurrentDate;
			}
		} catch (java.text.ParseException ex) {
			LOG.error(ex.getMessage(), ex);
		}
		
		if(documentInfo.get("curr_seqno") != null) {
			currentSequence = ((BigDecimal) documentInfo.get("curr_seqno")).intValue();
		}
		if(documentInfo.get("pre_seqno") != null) {
			preSequence = ((BigDecimal) documentInfo.get("pre_seqno")).intValue();
		}
		if(documentInfo.get("fnl_crn_dt") != null) {
			finalCreateDate = (String) documentInfo.get("fnl_crn_dt");
		}

		if (!"NA".equals(documentInfo.get("ren_cy_ccd"))) {
			if(compareCurrentDate.compareTo(stringGeneratedLastDate) > 0) {
				// 주기가 바뀐 경우 st_seqno 부터 새로 시작하기 위해 curr_seqno 초기화
				// 이전 시퀀스에 현재 시퀀스 저장
				preSequence = currentSequence;
				
				// 주기가 바뀐 경우 st_seqno 부터 새로 시작
				currentSequence = ((BigDecimal) documentInfo.get("st_seqno")).intValue();
				returnSequence = currentSequence;
				finalCreateDate = simpleDateFormat.format(new Date());
			} else if(compareCurrentDate.compareTo(stringGeneratedLastDate) < 0) {
				// 타임존 적용으로 인하여 과거 패턴이 들어온 경우 pre_seqno 사용
				// curr_seqno 변화 없음
				preSequence = preSequence + 1;
				returnSequence = preSequence;
			} else {
				// 최초 생성 이거나 주기가 바뀌지 않은 경우 curr_seqno + 1
				currentSequence = currentSequence + 1;
				returnSequence = currentSequence;
				finalCreateDate = simpleDateFormat.format(new Date());
			}
		} else {
			// 최초 생성 이거나 주기가 바뀌지 않은 경우 curr_seqno + 1
			currentSequence = currentSequence + 1;
			returnSequence = currentSequence;
			finalCreateDate = simpleDateFormat.format(new Date());
		}

		// curr_seqno 가 ed_seqno 초과 시 예외 발생
		// if (currentSeq > ((BigDecimal)docInfo.get("ed_seqno")).intValue()) {
		// To-Do : 예외 발생 ("Number is exceed.")
		// }

		// curr_seqno 업데이트
		Map<String, Object> param = Maps.newHashMap();
		param.put("curr_seqno", currentSequence);
		param.put("pre_seqno", preSequence);
		param.put("fnl_crn_dt", finalCreateDate);
		param.put("doc_no_cd", documentNumberCode);
		this.updateDocumentNumber(param);


		int firstIndex = expression.indexOf('#', 0);
		int secondIndex = expression.indexOf("#", firstIndex + 1);

		// 표현식에서 첫번째 #의 인덱스는 0이상, 두번째 #의 인덱스는 1이상인 경우 (SMARTNINE-1594)
		if (expression != null && firstIndex > -1 && secondIndex > 0) {
			// 표현식에서 #~~# 부분 추출
			templateExpression = expression.substring(firstIndex, secondIndex + 1);
		}

		if (templateExpression != null && templateExpression.length() > 0) {
			// expression = expression.replace(tempExpression, compareCurrentDate);
			String templateDateExpression = templateExpression.replace("#", "");
			SimpleDateFormat templateDateFormat = new SimpleDateFormat(templateDateExpression, Locale.getDefault());
			if(time != null) {
				templateDateFormat.setTimeZone(time);
			}
			String templateDate = templateDateFormat.format(new Date());
			expression = expression.replace(templateExpression, templateDate);

			/*
			 * 날짜패턴 치환 버그가 있습니다. expression = PR#yyMM#@SEQNO@ tempExpression = #yyMM#
			 * compareCurrentDate = '201604' expression.replace(tempExpression,
			 * compareCurrentDate); 이렇게 하면 expression = PR1604@SEQ가 되어야 하지만 PR201604@SEQNO@로 됨
			 * 그래서 아래 처럼 수정을 하면 될 것 같은데 확인 후 적용 부탁 드리겠습니다. 아울러 ESACDFM에서 expression의 날짜 패턴을
			 * JAVA FORMAT에 맞게 수정되어야 할 듯 합니다. 예)YYYYMMDD -> yyyyMMdd
			 */

			/*
			 * String tempDateExp = tempExpression.replace("#", ""); SimpleDateFormat tempFm
			 * = new SimpleDateFormat(tempDateExp); String tempDate = tempFm.format(new
			 * Date()); expression = expression.replace(tempExpression, tempDate);
			 */

		}

		// 종료번호의 자릿수에 맞추어서 LPAD 처리한다.
		int endSequenceNumberLength = new BigDecimal(documentInfo.get("ed_seqno").toString()).toString().length();
		String sequence = StringUtils.leftPad(String.valueOf(returnSequence), endSequenceNumberLength, "0");
		expression = expression.replace("@SEQNO@", sequence);

		return expression;
	}
	
	private void updateDocumentNumber(Map<String, Object> param) {
		sharedRepository.updateDocumentNumber(param);
	}
	
	/**
	 * 타임존 코드를 토대로 offset 구성
	 * @param tmzCcd
	 * @return
	 */
	private String convertOffset(String tmzCcd) {
		String result = "";
		String prefix = "GMT";
		String seperator = ":";
		String hour = "";
		String minute = "";
		
		Double dou = Double.parseDouble(tmzCcd);
		if(dou.compareTo(Double.valueOf(0)) > 0) {
			prefix += "+";
		} else {
			prefix += "-";
		}
		dou = Math.abs(dou);
		Double floorDou = Math.floor(dou);
		hour = Integer.toString(floorDou.intValue());
		// 소수점 여부 판단
		if(Math.floor(dou) != dou) {
			Double dp = dou % 1 * 10 * 60 / 10;
			minute = Integer.toString(dp.intValue());
		} else {
			minute = "0";
		}
		
		hour = StringUtils.leftPad(hour, 2, '0');
		minute = StringUtils.rightPad(minute, 2, '0');
		result = prefix + hour + seperator + minute;
		return result;
	}

	/**
	 * 해당 운영조직 코드를 이용해, 운영조직 명을 조회
	 *
	 * @author : LMS
	 * @param Object oorg_cd
	 * @return 운영조직 명
	 * @Date : 2017. 05. 23
	 * @Method Name : findOperationOrganizationName
	 */
	public String findOperationOrganizationName(String oorgCd , String ounitCd) {
		Map<String ,Object> param = Maps.newHashMap();
		param.put("oorg_cd",oorgCd);
		param.put("ounit_cd",ounitCd);
		return sharedRepository.findOperationOrganizationName(param);
	}

	/**
	 * 로그인 사용자의 기록을 db에 남김
	 *
	 * @param param
	 */
	public void insertLoginLogInfo(Map param) {
		sharedRepository.insertLoginLogInfo(param);
	}

	/**
	 * 로그인 사용자 운영조직 목록 조회.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return 운영조직 목록
	 * @Date : 2016. 5. 2
	 * @Method Name : findListOperationOrganizationByUser
	 */
	public List findListOperationOrganizationByUser(String param) {
		return sharedRepository.findListOperationOrganizationByUser(param);
	}

	/**
	 * 운영단위 조직 연결정보 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 11
	 * @Method Name : findListOperationOrganizationByOperationOrganizationLink
	 */
	public List findListOperationOrganizationByOperationOrganizationLink(Map param) {
		return sharedRepository.findListOperationOrganizationByOperationOrganizationLink(param);
	}

	/**
	 * list userfunction 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list< map>
	 * @Date : 2016. 7. 1
	 * @Method Name : findListUserFunction
	 */
	public List<Map<String, Object>> findListUserFunction(String param) {
		return sharedRepository.findListUserFunction(param);
	}

	/**
	 * 로그인 사용자 메뉴 목록을 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param searchParam the search param
	 * @return the list< map< string, object>>
	 * @Date : 2016. 2. 11
	 * @Method Name : findListUserMenu
	 */
	public List<Map<String, Object>> findListUserMenu(Map searchParam) {
		List<Map<String, Object>> userMenu = sharedRepository.findListUserMenu(searchParam);
		// 관리자 권한이 없을 경우 회사(법인별) 게시판 필터링
		if(!Auth.getCurrentUser().getAuthorities().contains(new SimpleGrantedAuthority("A100"))){
			userMenu = filteringListOfCompanyBoardMenu(userMenu);
		}

		return userMenu;

	}

	public List<Map<String, Object>> filteringListOfCompanyBoardMenu(List<Map<String, Object>> userMenu){
		final String BBD_UUID = "bbd_uuid";
		for(int i=0; i< userMenu.size(); i++){
			Map<String, Object> menu = userMenu.get(i);
			String menuUrl = (menu.get("menu_url") == null) ? "" : menu.get("menu_url").toString();
			// 게시판 메뉴가 아니면 continue
			if("".equals(menuUrl) || !menuUrl.contains(BBD_UUID)){ continue; }

			// menu url로 보내는 parameters
			String[] menuParams = menuUrl.split("\\?")[1].split("&");

			Map<String, Object> bbdMap = Maps.newHashMap();

			for(int j=0; j < menuParams.length; j++){
				String[] keyValue = menuParams[j].split("=");
				// parameter의 key 가 BBD_UUID인 경우
				if(BBD_UUID.equals(keyValue[0])){
					// key, value 셋팅
					bbdMap.put(keyValue[0], keyValue[1]);
					// 해당 메뉴의 사용 법인 리스트
					List<Map<String, Object>> boardCompanyList = boardAdminService.findBoardCompanyList(bbdMap);
					if(boardCompanyList != null && boardCompanyList.size() != 0){
						// 회사(법인)별 게시판에 사용자가 속한 회사(법인)이 매핑 되는지
						long cnt = countMappingCampanyOnBoard(boardCompanyList, Auth.getCurrentUserInfo());
						if(cnt == 0){ // 매핑 안될 경우 메뉴 제거
							userMenu.remove(i);
						}
					}
					break;
				}
			}
		}
		return userMenu;
	}
	/**
	 * Vendor가 거래중인 회사를 조회한다.
	 *
	 * @author : smkang
	 * @param param the param
	 * @return the list< map>
	 * @Date : 2023. 7. 5
	 * @Method Name : findListCompanyCodeForVendor
	 */
	public List<Map<String, Object>> findListCompanyCodeForBulletinBoard(Map<String, Object> param){
		// 전체 Company List 조회
		List<Map<String, Object>> companyCodeListForCombobox = boardAdminService.findBoardCompanyList(param);
		String userTypeCcd = Auth.getCurrentUserInfo().getOrDefault("usr_typ_ccd","") == null ? "" : (String) Auth.getCurrentUserInfo().getOrDefault("usr_typ_ccd","");

		if("BUYER".equals(userTypeCcd)){
			return companyCodeListForCombobox;
		}

		if("VD".equals(userTypeCcd)){
			// 현재 사용자(Vendor)의 협력사 운영조직 조회
			List<Map<String, Object>> operationOrgVendorList = operationOrganizationService.findOperationOrgVendorList(Auth.getCurrentUserInfo());

			// 현재 사용자(Vendor)의 협력사 운영조직과 매핑되지 않는 Company는 List에서 제외
			for(int i=0; i<companyCodeListForCombobox.size(); i++){
				Map<String, Object> company = companyCodeListForCombobox.get(i);
				boolean isCompanyForVendor = false;
				for(Map oov : operationOrgVendorList) {
					String logicOrgCd = (String)oov.get("logic_org_cd");
					// 협력사 Logical Organization Code 가 회사코드로 시작하는 경우 ex> logicOrgCd : C100PU200,  co_cd : C100
					if (logicOrgCd.startsWith((String) company.get("co_cd"))) {
						isCompanyForVendor = true;
						break;
					}
				}
				// 현재 Vendor가 거래중인 회사가 아닐경우 remove
				if(!isCompanyForVendor) {
					companyCodeListForCombobox.remove(i);
					i--;
				}
			}
			System.out.println("operationOrgVendorList.toString() = " + operationOrgVendorList.toString());
			return companyCodeListForCombobox;
		}

		return null;
	}

	public long countMappingCampanyOnBoard(List<Map<String, Object>> companyList, Map<String, Object> userInfo){
		long cnt = 0;
		// 사용자가 협력사일 경우
		if("VD".equals((String)userInfo.get("usr_typ_ccd"))){
			// 협력사의 경우 협력사 운영조직을 활용하여 매핑정보 확인
			List<Map<String, Object>> operationOrgVendorList = operationOrganizationService.findOperationOrgVendorList(userInfo);
			if(operationOrgVendorList != null && operationOrgVendorList.size() != 0){
				for(Map oov : operationOrgVendorList){
					String logicOrgCd = (String)oov.get("logic_org_cd");
					for(Map company : companyList) {
						if(logicOrgCd.startsWith((String)company.get("co_cd"))){
							return 1;
						}
					}
				}
			}
		}

		// 사용자가 구매사일 경우
		if("BUYER".equals((String)userInfo.get("usr_typ_ccd"))){
			// 사용자가 속한 법인에 매핑되는 정보 카운트
			cnt = companyList.stream().filter( x -> x.get("co_cd").equals((String)userInfo.get("co_cd"))).count();
		}
		return cnt;
	}

	public List<Map<String, Object>> findListMenu(Map<String, Object> searchParam) {
		List<Map<String, Object>> menuList = Lists.newArrayList();
		String userTypeCommonCode = String.valueOf(httpSession.getAttribute("usrCls"));

		if (null != Auth.getCurrentUserName() && Auth.getCurrentUserName().length() > 0) {
			// 로그인 과정을 통해 세션에 userName 이 있는 경우 유저 역할의 메뉴를 조회합니다.
			menuList = this.findListUserMenu(searchParam);
		} else if (StringUtils.isNotEmpty(userTypeCommonCode)) {
			// httpSession 객체에 userTypeCommonCode 값이 존재 할 경우 userTypeCommonCode 기준으로 메뉴를 조회합니다.
			searchParam.put("usr_typ_ccd", userTypeCommonCode);
			menuList = this.findListDefaultMenu(searchParam);
		}
		return menuList;
	}

	private List<Map<String, Object>> findListDefaultMenu(Map<String, Object> searchParam) {
		return sharedRepository.findListDefaultMenu(searchParam);
	}

	/**
	 * 품목분류의 품목분류1 목록을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 2
	 * @Method Name : findListMajorCategory
	 */
	public List findListMajorCategory(Map param) {
		return sharedRepository.findListMajorCategory(param);
	}

	/**
	 * 상위분류에 대한 하위분류 목록을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 2
	 * @Method Name : findListCategoryByParentCategoryCode
	 */
	public List findListCategoryByParentCategoryCode(Map param) {
		return sharedRepository.findListCategoryByParentCategoryCode(param);
	}

	/**
	 * 수량 단위 공통코드 목록을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 4
	 * @Method Name : findListAmountUnitCode
	 */
	public List findListAmountUnitCode(Map param) {
		return sharedRepository.findListAmountUnitCode(param);
	}

	/**
	 * SP 운영단위 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 27
	 * @Method Name : findListOperationOrganizationBySupplier
	 */
	public List findListOperationOrganizationBySupplier(String param) {
		return sharedRepository.findListOperationOrganizationBySupplier(param);
	}

	public Map findHelpManualTypeProcess(Map param) {
		Map<String, Object> helpManualType = this.findHelpManualType(param);
		if (helpManualType != null) {
			String athfUuid = (String) helpManualType.getOrDefault("athf_uuid","");
			String mnlTypCcd = (String) helpManualType.getOrDefault("mnl_typ_ccd","");
			if("PDF".equals(mnlTypCcd) && StringUtils.isNotEmpty(athfUuid)){
				helpManualType.put("id", encryptor.encrypt((String) helpManualType.get("athf_uuid")));
				helpManualType.remove("athf_uuid");
			}
		}
		return helpManualType;
	}

	private Map<String, Object> findHelpManualType(Map param) {
		return sharedRepository.findHelpManualType(param);
	}

	/**
	 * 메뉴코드로 메뉴얼 정보를 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 7. 1
	 * @Method Name : findHelpManualInfo
	 */
	public Map findHelpManualInfo(Map param) {
		return sharedRepository.findHelpManualInfo(param);
	}


	/**
	 * Captcha 이미지 반환 및 검증 문자 세션에 저장
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return the list
	 * @Date : 2017. 2. 09
	 * @Method Name : captchaValidation
	 */
	public String captchaValidation(int width, int height) {
		Captcha captchaObj = captchaGenerator.createCaptcha(width, height);
		httpSession.setAttribute("captchaObj", captchaObj);
		return CaptchaUtils.encodeBase64(captchaObj);
	}

	/**
	 * 사용자가 입력값과 세션의 Captcha값 비교
	 *
	 * @author : JongHyeok Choi
	 * @param : String
	 * @return : Booelan
	 * @Date : 2017. 2. 09
	 * @Method Name : checkUserInputValueCompareSessionCaptcha
	 */
	public Boolean checkUserInputValueCompareSessionCaptcha(String captcha) {
		Captcha captchaObj = (Captcha) httpSession.getAttribute("captchaObj");
		// 한 번 체크하면(실패시) 무조건 변경해야 함, Front에서 구현
		httpSession.removeAttribute("captchaObj");
		String expect = captchaObj.getAnswer();
		return expect.equals(captcha);
	}

	/**
	 * 사용자 메뉴 기능 / 메뉴 URL 역할 목록을 조회한다.
	 *
	 * @author : mgPark
	 * @param roles the roles
	 * @return the list< map< string, object>>
	 * @Date : 2018. 5. 2
	 * @Method Name : findListMenuFunctionAndUrlByUserRoleList
	 */
	public List<Map<String, Object>> findListMenuFunctionAndUrlByUserRoleList() {
		return sharedRepository.findListMenuFunctionAndUrlByUserRoleList();
	}

	public Map findCurrntUserInfo() {
		String userTypeCcd = Auth.getCurrentUserInfo().getOrDefault("usr_typ_ccd","") == null ? "" : (String) Auth.getCurrentUserInfo().getOrDefault("usr_typ_ccd","");
		if(("VD").equals(userTypeCcd)){
			return accountService.findSupplierUserSessionInfo((String) Auth.getCurrentUserInfo().get("usr_id"));
		}else{
			return accountService.findUserSessionInfo((String) Auth.getCurrentUserInfo().get("usr_id"));
		}
	}

	/**
	 * 자신의 정보를 변경한다
	 *
	 * @author : JongHyeok
	 * @param param the param
	 * @return the int
	 * @Date : 2017. 3. 14
	 * @Method Name : saveUser
	 */
	public ResultMap saveUser(Map<String, Object> param) {
		String encPw = (String) param.get("enc_pw");
		if (encPw != null && !"".equals(encPw)) { // Password 존재여부 확인

			String decPassword = "";

			// Password Cipher Decrypt
			decPassword = cipherUtil.decrypt((String) param.get("enc_pw"));

			// Simplex Encrypt for DB Insert
			param.put("pw", new ProxyPasswordEncoder("SHA-512").encode(decPassword));
		}

		// 사용자 정보 업데이트 ( login session user )
		this.updateUserInfoByLoginSession(param);

		authenticationPostService.authenticationUpdate();

		return ResultMap.SUCCESS();
	}

	private void updateUserInfoByLoginSession(Map<String, Object> param) {
		sharedRepository.updateUserInfoByLoginSession(param);
	}


	/**
	 * 미사용 계정 기준 정보 조회
	 *
	 * @author : Joon Huh
	 * @return the map
	 * @Date : 2017. 5. 2
	 * @Method Name : findDisabledAccountInfo
	 */
	public Map findDisabledAccountInfo() {
		return sharedRepository.findDisabledAccountInfo();
	}

	/**
	 * 결재서식에서 코드값을 코드명으로 변환하기위해 사용하는 함수
	 *
	 * @author : LMS
	 * @param Object code, String groupCode
	 * @return String
	 * @Date : 2017. 05. 23
	 * @Method Name : findCodeName
	 */
	public String findCodeName(Object code, String groupCode) {
		String codeName = "";
		if (null != code && StringUtils.isNotEmpty(groupCode)) {
			Map<String, Object> param = Maps.newHashMap();
			param.put("dtlcd", code);
			param.put("ccd", groupCode);
			codeName = sharedRepository.findCodeName(param);
		}
		return codeName;
	}


	/**
	 * 운영조직 목록 조회.
	 */
	public List<Map<String, Object>> findListOperationOrganizationForCombobox(String param) {
		return sharedRepository.findListOperationOrganizationForCombobox(param);
	}

	/**
	 * zip code by DB 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the floater stream
	 * @Date : 2017. 8. 18
	 * @Method Name : findListZipCodeByDatabase
	 */
	public FloaterStream findListZipCodeByDatabase(Map param) {
		// 대용량 처리
		return sharedRepository.findListZipCodeByDatabase(param);
	}

	/**
	 * Find list oper org by tenant.
	 *
	 * @param param the param
	 * @return the list
	 */
	public List findListOperationOrganizationByTenant(Map param) {
		return sharedRepository.findListOperationOrganizationByTenant(param);
	}

	public ResultMap saveUserHomeType(Map param) {
		sharedRepository.saveUserHomeType(param);
		authenticationPostService.authenticationUpdate();
		return ResultMap.SUCCESS();
	}


	/**
	 * 페이징 정보 조회
	 *
	 * @param param the param
	 * @return map
	 * @Date : 2020. 05. 28
	 * @Method Name : getPageInfo
	 */
	public Map<String, Object> getPageInfo(Map<String, Object> param) {
		Map<String, Object> pageInfo = Maps.newHashMap();
		//default page 정보
		//page당 보여질 row수
		int pageRows = 10;
		//page단위
		int pageBlock = 5;
		int totalRow = 0;
		int totalPage = 0;
		int page = 1;
		int startRow = 0;
		int endRow = 0;
		int startPage = 0;
		int endPage = 0;

		if(param != null){
			if(param.containsKey("page") && param.get("page") != null && !("".equals(param.get("page").toString())) ){
				page = Integer.parseInt(param.get("page").toString());
			}
			if(param.containsKey("pageRows") && param.get("pageRows") != null && !("".equals(param.get("pageRows").toString())) ){
				pageRows = Integer.parseInt(param.get("pageRows").toString());
			}
			if(param.containsKey("pageBlock") && param.get("pageBlock") != null && !("".equals(param.get("pageBlock").toString()))){
				pageBlock = Integer.parseInt(param.get("pageBlock").toString());
			}
		}
		// TOTAL ROW를 가져온다.
		totalRow = Integer.parseInt(param.get("totalRow").toString());
		// TOTAL PAGE를 BLOCK으로 계산한다.
		totalPage = (int) Math.ceil((double) totalRow / pageRows);
		// 현재페이지를 통해 START ROW와 END ROW를 계산한다. PAGE_ROW와 PAGE활용 (ROW_NUM으로 리스트에서 조회할때 사용할 예정)
		startRow = (page - 1) * pageRows + 1;
		endRow = (startRow - 1) + pageRows;
		// END PAGE TOTAL PAGE보다 작을 경우와 클경우를 생각해서 계산
		endPage = (int) (Math.ceil((double) page/pageBlock) * pageBlock);
		if(totalPage < endPage){
			endPage = totalPage;
		}
		// START PAGE
		//startPage = (endPage - (pageBlock -1)) < 1? 1 : (endPage - (pageBlock -1));
		startPage = (int) ((Math.ceil((double)page/pageBlock) - 1) * pageBlock + 1);

		pageInfo.put("pageRows", pageRows);
		pageInfo.put("pageBlock", pageBlock);
		pageInfo.put("totalRow", totalRow);
		pageInfo.put("totalPage", totalPage);
		pageInfo.put("page", page);
		pageInfo.put("startRow", startRow);
		pageInfo.put("endRow", endRow);
		pageInfo.put("startPage", startPage);
		pageInfo.put("endPage", endPage);

		return pageInfo;
	}

	/**
	 * 첨부파일 목록 조회
	 *
	 * @author :
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2020. 05. 28
	 * @Method Name : findListPortalNoticeForGridPaging
	 */
	public List<Map<String, Object>> findAttList(Map<String, Object> param){
		List<Map<String, Object>> attachFileList = attachService.findListAttach(param);

		for (Map<String,Object> attachInfo : attachFileList) {
			String id = encryptor.encrypt((String)attachInfo.get("athf_uuid"));
			attachInfo.put("athf_uuid", id);
		}
		return attachFileList;
	}

	//특수문자 허용을 금지하는 필드만 꺼내어서 특수문자를 제어한다.
	public ResultMap verifySpecialCharacters(Map<String, Object> param, String tgtKey){
		Map<String, Object> resultParam = (Map<String, Object>) param;

		for(String key : resultParam.keySet()) {
			if(tgtKey.indexOf(key) > -1) {
				if(null != resultParam.get(key) && !"".equals(resultParam.get(key))) {
					String orgValue = (String)resultParam.get(key);
					String tgtValue = checkSpecialCharacters((String)resultParam.get(key));
					if(!orgValue.equals(tgtValue)) {
						return ResultMap.FAIL();
					}
				}
			}
		}

		return ResultMap.SUCCESS();
	}

	//실제 특수문자가 있는지 체크 후 특수문자를 제거해준다.
	public String checkSpecialCharacters(String tgtValue) {
		String pattern = "^[a-zA-Z0-9-_]*$";
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z-_]";
		String input = tgtValue;

		boolean isSpChar = Pattern.matches(pattern, tgtValue);

		if(!isSpChar) {
			input = tgtValue.replaceAll(match, "");
		}

		return input;
	}


	//mdi에서 locale을 변경하고 user에 변경된 locale의 정보를 set한다.
	public void afterChangeLocale() {
		User user = Auth.getCurrentUser();
		String username = user.getUsername();
		Map<String,Object> userInfo = accountService.findUserSessionInfo(username);
		user.setUserInfo(userInfo);
	}


	/**
	 * info auto manual 조회한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2021. 12. 8
	 * @Method Name : findInfoAutoManual
	 */
	public Map<String, Object> findInfoAutoManualProcess(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String,Object> info = this.findInfoAutoManual(param);

		if(info != null){
			String manualAutoPostingYn = info.get("mnl_auto_pstg_yn") == null? "N" : (String)info.get("mnl_auto_pstg_yn");

			if("Y".equals(manualAutoPostingYn)){
				//자동표시여부
				//날짜 체크
				Date todate = new Date();
				String autoDispYn = "N";
				if(info.get("mnl_auto_pstg_st_dttm") != null && info.get("mnl_auto_pstg_exp_dttm") != null){
					Date dispStartDt = (Date)info.get("mnl_auto_pstg_st_dttm");
					Date closedt = (Date)info.get("mnl_auto_pstg_exp_dttm");
					Date dispCloseDt = new Date(closedt.getTime() + ((1000 * 60 * 60 * 24) * 1));
					if(dispStartDt.before(todate) && todate.before(dispCloseDt)){
						autoDispYn = "Y";
					}
				}
				info.put("mnl_auto_pstg_yn", autoDispYn);

			}
			resultMap = info;
		}
		return resultMap;
	}

	private Map<String, Object> findInfoAutoManual(Map<String, Object> param) {
		return sharedRepository.findInfoAutoManual(param);
	}

	/**
	 * Client 브라우저 정보 가져오기
	 *
	 * @author :
	 * @param request to param
	 * @return the map
	 * @Date : 2021. 10. 01
	 * @Method Name : getClientBrowser
	 */
	public String getClientBrowser(HttpServletRequest request){
		String browser = "";
		String userAgent = request.getHeader("User-Agent");

		// 브라우저 구분
		if(userAgent.indexOf("Trident") > -1 || userAgent.indexOf("MSIE") > -1) { //IE
			if(userAgent.indexOf("Trident/7") > -1) {
				browser = "IE 11";
			}else if(userAgent.indexOf("Trident/6") > -1) {
				browser = "IE 10";
			}else if(userAgent.indexOf("Trident/5") > -1) {
				browser = "IE 9";
			}else if(userAgent.indexOf("Trident/4") > -1) {
				browser = "IE 8";
			}else if(userAgent.indexOf("edge") > -1) {
				browser = "IE edge";
			}
		}else if(userAgent.indexOf("Whale") > -1){ //네이버 WHALE
			browser = "WHALE " + userAgent.split("Whale/")[1].toString().split(" ")[0].toString();
		}else if(userAgent.indexOf("Opera") > -1 || userAgent.indexOf("OPR") > -1){ //오페라
			if(userAgent.indexOf("Opera") > -1) {
				browser = "OPERA " + userAgent.split("Opera/")[1].toString().split(" ")[0].toString();
			}else if(userAgent.indexOf("OPR") > -1) {
				browser = "OPERA " + userAgent.split("OPR/")[1].toString().split(" ")[0].toString();
			}
		}else if(userAgent.indexOf("Firefox") > -1){ //파이어폭스
			browser = "FIREFOX " + userAgent.split("Firefox/")[1].toString().split(" ")[0].toString();
		}else if(userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Chrome") == -1 ){ //사파리
			browser = "SAFARI " + userAgent.split("Safari/")[1].toString().split(" ")[0].toString();
		}else if(userAgent.indexOf("Chrome") > -1){ //크롬
			browser = "CHROME " + userAgent.split("Chrome/")[1].toString().split(" ")[0].toString();
		}

		// 모바일 구분
		if (userAgent.indexOf("iPhone") > -1 && userAgent.indexOf("Mobile") > -1) {
			browser = "iPhone " + browser;
		} else if (userAgent.indexOf("Android") > -1 && userAgent.indexOf("Mobile") > -1) {
			browser = "Android " + browser;
		} else if (userAgent.indexOf("Mobile") > -1) {
			browser = "Etc Mobile " + browser;
		}
		return browser;
	}

	public List findListLatelyExchange(String param) {
		return sharedRepository.findListLatelyExchange(param);
	}

	/**
	 * 공통코드 제약 조건 값 조회
	 * @param codeInfo
	 * @return
	 */
	public String getCommonCodeConstraintConditionValue(Map<String, Object> codeInfo) {
		return sharedRepository.getCommonCodeConstraintConditionValue(codeInfo);
	}
	
	/**
	 * temp table task_id 저장
	 * @param param
	 */
	public void insertTempQueryId(Map param) {
		sharedRepository.insertTempQueryId(param);
	}
	
	/**
	 * 사용자 temp data 삭제
	 */
	public void deleteTempQueryId() {
		sharedRepository.deleteTempQueryId();
	}
	
	/**
	 * tenant id를 가져온다.
	 * @param tenId
	 * @return
	 */
	public String getTenantId(String tenId) throws RuntimeException {
		Tenant tenant = TenancyContextHolder.getContext().getTenant();
		if(null == tenant){
			tenant = TenancyContextHolder.getDefaultTenant();
		}
		
		String tenantId = tenant.getId();
		if(StringUtils.isNotBlank(tenId) && !tenId.equals(tenantId)) {
			tenant = serviceTenantProvider.findTenant(tenId);
			tenantId = tenant.getId();
		}
		
		return tenantId;
	}
	
	/**
	 * tenant를 설정한다.
	 * @param tenId
	 */
	public void setTenant(String tenId, HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isBlank(tenId)) {
			return;
		}
		
		Tenant tenant = new DefaultTenant().createInstance(tenId);
		TenancyContext tenancyContext = TenancyContextHolder.createEmptyContext();
		tenancyContext.setTenant(tenant);
		TenancyContextHolder.setContext(tenancyContext);
		new DefaultTenantResolver().setTenant(request, response, tenant);
	}


	/**
	 * 모듈 별 첨부파일 리스트를 조회한다.
	 * @param param
	 * @return
	 */
	public List findListModuleAttach(Map param) {
		return sharedRepository.findListModuleAttach(param);
	}

	/**
	 * 그룹 별 모듈 첨부파일을 조회한다.
	 * @param param
	 * @return
	 */
	public List findListGroupModuleAttach(Map param) {
		return sharedRepository.findListGroupModuleAttach(param);
	}

	public void saveListModuleAttachCode(Map param) {

		List<Map<String, Object>> updateList = (List<Map<String, Object>>)param.get("updateList");
		List<Map<String, Object>> insertList = (List<Map<String, Object>>)param.get("insertList");

		// UPDATE
		for(Map updateModuleAttachInfo : updateList){
			this.updateModuleAttachCode(updateModuleAttachInfo);
		}

		// INSERT
		for(Map insertModuleAttache : insertList){
			if(this.existModuleAttachCode(insertModuleAttache)){
				this.updateModuleAttachCode(insertModuleAttache);
			}else{
				this.insertModuleAttachCode(insertModuleAttache);
			}
		}
	}

	public void insertModuleAttachCode(Map insertModuleAttache) {
		sharedRepository.insertModuleAttachCode(insertModuleAttache);
	}

	public void updateModuleAttachCode(Map updateModuleAttachInfo) {
		sharedRepository.updateModuleAttachCode(updateModuleAttachInfo);
	}

	private boolean existModuleAttachCode(Map moduleAttacheInfo) {
		int getCount = sharedRepository.getModuleAttachCode(moduleAttacheInfo);
		return ( getCount > 0);
	}

	public void deleteListModuleAttachCode(Map param) {
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.get("deleteList");

		for(Map deleteModuleAttachInfo : deleteList){
			this.deleteModuleAttachCode(deleteModuleAttachInfo);
		}
	}

	public void deleteModuleAttachCode(Map deleteModuleAttachInfo) {
		sharedRepository.deleteModuleAttachCode(deleteModuleAttachInfo);
	}

	public Map findModuleAttach(Map<String, Object> param) {
		return sharedRepository.findModuleAttach(param);
	}

	/**
	 * 온보딩 유형에 따른 사용 코드 넘기기
	 * @param onBoardingJobTypeCode
	 * @return
	 */
	public String[] findListOnboardingTypeCodeByJob(String onBoardingJobTypeCode) {
		return OnboardingJobTypeInfo.OnBoardingJobTypeEnum.getFindOnboardingJobType(onBoardingJobTypeCode);
	}
}
