package smartsuite.app.bp.admin.exchange.scheduler.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.exchange.scheduler.repository.ExchangeSchedulerRepository;
import smartsuite.app.common.rest.RestService;


@Service
@Transactional
@Deprecated
public class ExchangeSchedulerService {

	/** The Constant authkey. */
	private static final String AUTH_KEY = "gYqctSrNCQgVx9B0fp54BHeAuMFEGCPY";
	
	/** The Constant serviceUrl. */
	private static final String SERVICE_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";

	//AP01 : 환율, AP02 : 대출금리, AP03 : 국제금리
	private static final String API_TYPE_EXCHANGE = "AP01";
	private static final String API_TYPE_LOAN_INTEREST = "AP02";
	private static final String API_TYPE_INTERNATIONAL_INTEREST = "AP03";

	@Inject
	ExchangeSchedulerRepository exchangeSchedulerRepository;

	@Inject
	RestService restService;

	/**
	 * 환율 업데이트
	 * @param paramMap
	 * @throws Exception
	 */
	@Deprecated
	public void updateListExchange(HashMap<String,Object> paramMap) throws Exception{
		HashMap<String,Object> param = (paramMap == null? Maps.newHashMap() : paramMap);

		JsonArray resultList = this.requestListExchange(param);

		for(JsonElement jsonElements : resultList){
			this.insertExchangeKrApi(jsonElements);
		}
	}

	/**
	 * 환율 스케줄러 API 값 Replace 및 환율 API 등록
	 * @param result
	 */
	private void insertExchangeKrApi(JsonElement result) {
		Map<String,Object> apiDataMap = this.replaceExchangeKrApiString(result);

		exchangeSchedulerRepository.insertExchangeKrApi(apiDataMap);
	}

	/**
	 * API 데이터 staring replace 처리
	 * @param result
	 * @return
	 */
	private Map<String, Object> replaceExchangeKrApiString(JsonElement result) {
		Map<String,Object> replaceApiStringMap = Maps.newHashMap();

		JsonObject jsonObject = result.getAsJsonObject();

		String curUnit = jsonObject.get("cur_unit").isJsonNull() ? "" : jsonObject.get("cur_unit").getAsString().replace(",","");
		String ttb = jsonObject.get("ttb").isJsonNull() ? "" : jsonObject.get("ttb").getAsString().replace(",","");
		String tts = jsonObject.get("tts").isJsonNull() ? "" : jsonObject.get("tts").getAsString().replace(",","");
		String dealBasR = jsonObject.get("deal_bas_r").isJsonNull() ? "" : jsonObject.get("deal_bas_r").getAsString().replace(",","");
		String bkpr = jsonObject.get("bkpr").isJsonNull() ? "" : jsonObject.get("bkpr").getAsString().replace(",","");
		String yyEfeeR = jsonObject.get("yy_efee_r").isJsonNull() ? "" : jsonObject.get("yy_efee_r").getAsString().replace(",","");
		String tenDdEfeeR = jsonObject.get("ten_dd_efee_r").isJsonNull() ? "" : jsonObject.get("ten_dd_efee_r").getAsString().replace(",","");
		String kftcDealBasR = jsonObject.get("kftc_deal_bas_r").isJsonNull() ? "" : jsonObject.get("kftc_deal_bas_r").getAsString().replace(",","");
		String kftcBkpr = jsonObject.get("kftc_bkpr").isJsonNull() ? "" : jsonObject.get("kftc_bkpr").getAsString().replace(",","");

		replaceApiStringMap.put("cur_unit",curUnit);
		replaceApiStringMap.put("ttb",ttb);
		replaceApiStringMap.put("tts",tts);
		replaceApiStringMap.put("deal_bas_r",dealBasR);
		replaceApiStringMap.put("bkpr",bkpr);
		replaceApiStringMap.put("yy_efee_r",yyEfeeR);
		replaceApiStringMap.put("ten_dd_efee_r",tenDdEfeeR);
		replaceApiStringMap.put("kftc_deal_bas_r",kftcDealBasR);
		replaceApiStringMap.put("kftc_bkpr",kftcBkpr);
		return replaceApiStringMap;
	}


	/**
	 * ExChange List 요청 시.
	 *
	 * @param param the param
	 * @return the list
	 * @throws Exception the exception
	 */
	public JsonArray requestListExchange(Map<String, Object> param) throws Exception {
		
		StringBuffer sendUrl = new StringBuffer();

		Map<String,Object> parameterMap = Maps.newHashMap();
		parameterMap.put("authkey",AUTH_KEY); //OpenAPI 신청시 발급된 인증키
		parameterMap.put("data",API_TYPE_EXCHANGE); //AP01 : 환율, AP02 : 대출금리, AP03 : 국제금리


		if(param.containsKey("search_date")){
			String searchDate = param.getOrDefault("search_date","") == null? "" :(String) param.getOrDefault("search_date","");
			parameterMap.put("searchdate",searchDate); //String , ex) 2015-01-01, 20150101, (DEFAULT)현재일
		}

		RestService.RestApiBean restApiBean = new RestService.RestApiBean();
		restApiBean.setApiUrl(SERVICE_URL);
		restApiBean.setParameterMap(parameterMap);
		restApiBean.setParameterType(RestService.PARAMETER_TYPE_STRING);
		restApiBean.setRequestMethodType(RestService.REQUEST_METHOD_GET);


		StringBuffer restApiGetData = restService.restApiUrlConnection(restApiBean);

		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = null;
		if(StringUtils.isNotEmpty(restApiGetData.toString())){
			Object objectData = jsonParser.parse( restApiGetData.toString() );
			jsonArray = (JsonArray) objectData;
		}

		return jsonArray;
	}
}
