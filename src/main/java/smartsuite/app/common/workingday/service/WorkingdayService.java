package smartsuite.app.common.workingday.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.service.OperationOrganizationService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.workingday.repository.WorkingdayRepository;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class WorkingdayService {

	private static final Logger LOG = LoggerFactory.getLogger(WorkingdayService.class);
	@Inject
	WorkingdayRepository workingdayRepository;

	@Inject
	OperationOrganizationService operationOrganizationService;

	/**
	 * 근무일 목록 조회
	 * 데이터가 없으면 만들어서 조회
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> findListWorkingdayAtFirst(Map<String, Object> param) {
		if(!this.checkCntWorkingday(param)){
			this.makeListWorkingday(param);
		}

		return this.findListWorkingday(param);
	}

	/**
	 * 데이터 있는지 check
	 *
	 * @param
	 * @return the boolean
	 */
	public Boolean checkCntWorkingday(Map<String, Object> param) {
		return workingdayRepository.findCntWorkingday(param) > 0;
	}

	/**
	 * 근무일 목록 조회
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> findListWorkingday(Map<String, Object> param) {
		return workingdayRepository.findListWorkingday(param);
	}

	/**
	 * 1. 1월 1일부터 12월 31일까지 데이터 만들어 주말 체크
	 * 2. 공공데이터 api를 통해 공휴일 가져오기
	 * 3. 1과 2의 데이터를 merge 하여 insert
	 *
	 * @param
	 * @return void
	 */
	private void makeListWorkingday(Map<String, Object> param) {
		List<Map<String, Object>> fullDayList = this.makeListFullDay(param);
		List<Map<String, Object>> holidayList = this.makeListCallApi(param);
		List<Map<String, Object>> resultList = this.mergeListFullAndHoli(fullDayList, holidayList);

		this.insertListWorkingday(resultList);
	}

	/**
	 * 근무일 목록 insert
	 *
	 * @param
	 * @return void
	 */
	public void insertListWorkingday(List<Map<String, Object>> param) {
		for(Map<String, Object> rowMap : param) {
			this.insertInfoWorkingday(rowMap);
		}
	}

	/**
	 * 근무일 단건 insert
	 *
	 * @param
	 * @return void
	 */
	public void insertInfoWorkingday(Map<String, Object> param) {
		workingdayRepository.insertInfoWorkingday(param);
	}

	/**
	 * 1월 1일 부터 12월 31일까지 데이터를 만들어 주말을 체크한다
	 *
	 * @param
	 * @return the list
	 */
	private List<Map<String, Object>> makeListFullDay(Map<String, Object> param) {
		List<Map<String, Object>> resultList = Lists.newArrayList();

		Calendar targetCal = Calendar.getInstance();
		Calendar initCal = Calendar.getInstance();
		Calendar lastCal = Calendar.getInstance();

		int wrkgdayYr = param.get("wrkgday_yr") == null ? 0 : (Integer) param.get("wrkgday_yr");
		if(0 != wrkgdayYr) {
			initCal.set(wrkgdayYr, 0, 1, 0, 0, 0);
			lastCal.set(wrkgdayYr+1, 0, 1, 0, 0, 0);
			targetCal.setTime(initCal.getTime());

			do {
				Map<String, Object> rowMap = Maps.newHashMap();
				rowMap.put("co_cd", param.get("co_cd"));
				rowMap.put("wrkgday_dt", targetCal.getTime());
				rowMap.put("dow", targetCal.get(Calendar.DAY_OF_WEEK));
				if(1 == targetCal.get(Calendar.DAY_OF_WEEK) || 7 == targetCal.get(Calendar.DAY_OF_WEEK)) {
					rowMap.put("ctry_hol_yn", "Y");
					rowMap.put("ctry_hol_rsn", "주말");
					rowMap.put("co_hol_yn", "Y");
					rowMap.put("co_hol_rsn", "주말");
				}
				resultList.add(rowMap);
				targetCal.add(Calendar.DAY_OF_WEEK, 1);
			} while (targetCal.compareTo(lastCal) < 0);
		}

		return resultList;
	}

	/**
	 * 공공데이터 api를 통해 국가공휴일을 가져와서 arraylist로 변환한다
	 *
	 * @param
	 * @return the list
	 */
	private List<Map<String, Object>> makeListCallApi(Map<String, Object> param) {
		JSONArray jsonHoliday = callApiHolidayList(param);
		List<Map<String, Object>> holidayList =  this.makeJsonToList(jsonHoliday);
		return this.makeListHoliday(holidayList);
	}

	/**
	 * 공공데이터 api를 통해 국가공휴일을 가져와서 JSONARRAY를 return
	 *
	 * @param
	 * @return the JSONArray
	 */
	private JSONArray callApiHolidayList(Map<String, Object> param) {
		JSONArray returnArray = new JSONArray();
		try {
			String wrkgdayYr = param.get("wrkgday_yr") == null ? "" : Integer.toString((Integer)param.get("wrkgday_yr"));

			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
			urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=OAdvB9Q7UqOdqA9klH9pdfVTjafECNxwGyeB0NDne6xvpLGkstcy6C43UvTjezYGyFh4C9QOd80sXvNnnkR5Cg%3D%3D"); /*Service Key*/
			urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("30", "UTF-8")); /*한 페이지 결과 수*/
			urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(wrkgdayYr, "UTF-8")); /*연*/
			urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*연*/
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			BufferedReader rd;
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			conn.disconnect();

			JSONObject jsonObject = new JSONObject(sb.toString());
			if(!jsonObject.isNull("response") && jsonObject.has("response") && !("").equals(jsonObject.get("response"))) {
				JSONObject response = jsonObject.getJSONObject("response");
				if(!response.isNull("body") && response.has("body") && !("").equals(response.get("body"))) {
					JSONObject body = response.getJSONObject("body");
					if(!body.isNull("items") && body.has("items") && !("").equals(body.get("items"))) {
						JSONObject items = body.getJSONObject("items");
						if(!items.isNull("item") && items.has("item") && !("").equals(items.get("item"))) {
							returnArray = items.getJSONArray("item");
						}
					}
				}
			}

		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return returnArray;
	}

	/**
	 * JSONARRAY를 ARRAYLIST로 변환
	 *
	 * @param
	 * @return the list
	 */
	private List<Map<String, Object>> makeJsonToList(JSONArray array) {
		List<Map<String, Object>> resultList = Lists.newArrayList();
		for(int i = 0 ; i < array.length() ; i++) {
			JSONObject obj = array.getJSONObject(i);
			Map<String, Object> map = Maps.newHashMap();
			Set keys = obj.keySet();
			Iterator<String> ketsltr = keys.iterator();

			while(ketsltr.hasNext()) {
				String key = ketsltr.next();
				Object value = obj.get(key);

				map.put(key, value);
			}
			resultList.add(map);
		}
		return resultList;
	}

	/**
	 * 공공데이터 api로 가져온 list를 정제한다
	 *
	 * @param
	 * @return the list
	 */
	private List<Map<String, Object>> makeListHoliday(List<Map<String, Object>> holiday) {
		List<Map<String, Object>> returnList = Lists.newArrayList();

		for(Map<String, Object> rowMap : holiday) {
			Map<String, Object> returnMap = Maps.newHashMap();

			String locDate = rowMap.get("locdate") == null ? "" : String.valueOf(rowMap.get("locdate"));
			if(!Strings.isNullOrEmpty(locDate)) {
				String targetYear = locDate.substring(0, 4);
				String targetMonth = locDate.substring(4, 6);
				String targetDate = locDate.substring(6, 8);

				Calendar targetCal = Calendar.getInstance();
				targetCal.set(Integer.parseInt(targetYear), Integer.parseInt(targetMonth)-1, Integer.parseInt(targetDate), 0, 0, 0);

				returnMap.put("wrkgday_dt", targetCal.getTime());
				returnMap.put("dow", targetCal.get(Calendar.DAY_OF_WEEK));
				returnMap.put("ctry_hol_yn", rowMap.get("isHoliday"));
				returnMap.put("ctry_hol_rsn", rowMap.get("dateName"));

				returnList.add(returnMap);
			}
		}

		return returnList;
	}

	/**
	 * 1월1일부터 12월31일까지의 데이터와 공휴일 데이터를 merge
	 *
	 * @param
	 * @return the list
	 */
	private List<Map<String, Object>> mergeListFullAndHoli(List<Map<String, Object>> fullday, List<Map<String, Object>> holiday) {

		for(Map<String, Object> holidayMap : holiday) {
			for(Map<String, Object> fulldayMap : fullday) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String fullDayStr = sdf.format((Date) fulldayMap.get("wrkgday_dt"));
				String holiDayStr = sdf.format((Date) holidayMap.get("wrkgday_dt"));

				if(fullDayStr.equals(holiDayStr)) {
					fulldayMap.put("ctry_hol_yn", "Y");
					String rsn = fulldayMap.get("ctry_hol_rsn") == null ? (String) holidayMap.get("ctry_hol_rsn") : fulldayMap.get("ctry_hol_rsn") + ", " + holidayMap.get("ctry_hol_rsn");
					fulldayMap.put("ctry_hol_rsn", rsn);
					fulldayMap.put("co_hol_yn", "Y");
					fulldayMap.put("co_hol_rsn", rsn);
					break;
				}
			}
		}

		return fullday;
	}

	/**
	 * 일자를 초기화
	 * 해당 년도 일자 삭제
	 * 일자 생성 후 조회
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> resetListWorkingday(Map<String, Object> param) {
		this.deleteListWorkingday(param);
		this.makeListWorkingday(param);
		return this.findListWorkingday(param);
	}

	/**
	 * 해당 년도 일자 삭제
	 *
	 * @param
	 * @return void
	 */
	public void deleteListWorkingday(Map<String, Object> param) {
		workingdayRepository.deleteListWorkingday(param);
	}

	/**
	 * 수정 한 근무일 목록을 저장
	 *
	 * @param
	 * @return ResultMap
	 */
	public ResultMap saveListWorkingday(Map<String, Object> param) {
		List<Map<String, Object>> updateList = param.get("updateList") == null ? Lists.newArrayList() : (List<Map<String, Object>>) param.get("updateList");

		for(Map<String, Object> rowMap : updateList) {
			this.updateInfoWorkingday(rowMap);
		}

		return ResultMap.SUCCESS();
	}

	/**
	 * 수정 한 근무일 단건 저장
	 *
	 * @param
	 * @return void
	 */
	public void updateInfoWorkingday(Map<String, Object> param) {
		workingdayRepository.updateInfoWorkingday(param);
	}

	/**
	 * 현재 이후의 휴일을 조회
	 *
	 * @param
	 * @return list
	 */
	public List<String> findListHolidayFromNow(String coCd) {
		Map<String,Object> param = Maps.newHashMap();
		param.put("co_cd",coCd);
		return workingdayRepository.findListHolidayFromNow(param);
	}

	public List<String> findListHolidayFromNowByOorgCd(String oorgCd) {
		Map<String,Object> coParam = Maps.newHashMap();
		coParam.put("oorg_cd",oorgCd);
		String coCd = operationOrganizationService.findCompanyCodeByOorgCd(coParam);

		Map<String,Object> param = Maps.newHashMap();
		param.put("co_cd",coCd);
		return workingdayRepository.findListHolidayFromNow(param);
	}

	/**
	 * 납품일자 단건 계산
	 * return 값은
	 * 1) 납품 요청일자
	 * 2) 납품 소요기간 + 휴무일(오늘 기준)
	 * 1) 과 2)를 비교하여 더 미래의 날짜를 반환한다.
	 *
	 * @param
	 * @return String
	 */
	public ResultMap calculateWorkingday(List<String> holiList, String reqDt, String dlvyLdtm) {
		// null Check
		if(Strings.isNullOrEmpty(reqDt) || Strings.isNullOrEmpty(dlvyLdtm)) {
			return ResultMap.FAIL("STD.E0000");//유효하지 않은 입력값이 있습니다. 오류 정보를 확인해 주세요.
		}

		// 1) 납품요청일자 validate
		ResultMap dtResult = this.validateDlvyDt(reqDt);
		if(dtResult.isFail()) {
			return ResultMap.FAIL(dtResult.getResultMessage());
		}

		// 2) 오늘 부터 납품소요일자까지의 휴일을 뺀 값을 계산
		String ldtmDt = "";
		ResultMap ldtmResult = this.validateDlvyLdtm(holiList, dlvyLdtm);
		if(ldtmResult.isFail()) {
			return ResultMap.FAIL(ldtmResult.getResultMessage());
		} else {
			Map ldtmReturn = ldtmResult.getResultData();
			ldtmDt = (String) ldtmReturn.get("ldtm_dt");
		}

		// 날짜 비교 후 더 미래의 값으로 리턴
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date ldtmDate = sdf.parse(ldtmDt);
			Date dvlyDate = sdf.parse(reqDt);

			if(ldtmDate.compareTo(dvlyDate) >= 0) {
				resultMap.put("dlvy_date", ldtmDt);
			} else {
				resultMap.put("dlvy_date", reqDt);
			}
		} catch (ParseException e) {
			LOG.error(e.getMessage(), e);
			return ResultMap.FAIL("STD.E1022");//납품 일자 계산시 오류가 발생하였습니다.
		}

		return ResultMap.SUCCESS(resultMap);
	}

	/**
	 * 납품요청일자와
	 * 오늘과의 차이를 구한다.
	 *
	 * @param
	 * @return resultmap
	 */
	private ResultMap validateDlvyDt(String dlvyDt) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate.parse(dlvyDt, formatter);
		} catch (DateTimeException e) {
			LOG.error(e.getMessage(), e);
			return ResultMap.FAIL("STD.WRKG1002"); //잘못된 납품요청일자 형식입니다.
		}

		return ResultMap.SUCCESS();
	}

	/**
	 * 납품소요일에서
	 * 휴무일을 제외한 날짜를 구한다.
	 *
	 * @param
	 * @return resultmap
	 */
	private ResultMap validateDlvyLdtm(List<String> holiList, String dlvyLdtm) {
		Integer dlvy_ldtm = 0;
		try {
			dlvy_ldtm = Integer.parseInt(dlvyLdtm);
		} catch (NumberFormatException e) {
			LOG.error(e.getMessage(), e);
			return ResultMap.FAIL("STD.WRKG1002");    //잘못된 납품소요일 형식입니다.
		}

		Calendar now = Calendar.getInstance();
		String nowDt = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		if(holiList != null && !holiList.isEmpty()) {
			Integer j = 0;
			for(Integer i = 0 ; i < dlvy_ldtm ; i++) {
				String holiDt = holiList.get(j);
				now.add(Calendar.DAY_OF_WEEK, 1);
				nowDt = sdf.format(now.getTime());

				if(nowDt.equals(holiDt)) {//휴일 체크
					i--;//휴일은 제외
					if(j < holiList.size()-1) {
						j++;//다음 휴일을 조회
					}
				}
			}
		} else {
			now.add(Calendar.DAY_OF_WEEK, dlvy_ldtm);
			nowDt = sdf.format(now.getTime());
		}

		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("ldtm_dt", nowDt);
		return ResultMap.SUCCESS(resultMap);
	}
}

