package smartsuite.app.common.rest;


import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class RestService {

	private static final Logger logger = LoggerFactory.getLogger(RestService.class);

	// Request Type
	public static final String REQUEST_METHOD_POST = "POST";
	public static final String REQUEST_METHOD_GET = "GET";
	public static final String REQUEST_METHOD_DELETE = "DELETE";

	// Content Type
	public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
	public static final String CONTENT_TYPE_URL_ENCODING = "application/x-www-form-urlencoded;charset=UTF-8";

	// Authorization
	public static final String AUTHORIATION_BASIC ="basic";
	public static final String AUTHORIATION_BEARER ="Bearer";

	// parameter Type
	public static final String PARAMETER_TYPE_JSON = "json";
	public static final String PARAMETER_TYPE_STRING = "string";

	private static final String URL_CONTEXT_SLASH = "/";


	/**
	 * 연결 완료 후 Bufferd Reader로 result 처리
	 * JsonObject or JsonArray parser영역은 각 service 단에서 처리 하며, 해당 부분은 연결 처리만 담당
	 *
	 * @param restApiBean
	 * @return
	 * @throws Exception
	 */
	public StringBuffer restApiUrlConnection(RestApiBean restApiBean) throws Exception{
		InputStream is = null;
		HttpURLConnection http = null;
		BufferedReader reader = null;
		StringBuffer stringResultData = new StringBuffer();
		try{
			http = restApiUrlConnectionCore(restApiBean);
			if(null != http.getInputStream()){
				try{
					reader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
					String line = reader.readLine();
					while (line != null) {
						stringResultData.append(line);
						line = reader.readLine();
					}
				}catch (Exception e){
					throw new CommonException(ErrorCode.FAIL, e);
				}
			}else{
				logger.info(" Connection error ");
			}
		}finally {
			if(null != is) try{
				is.close();
			}catch (Exception e){
				logger.error(e.getMessage());
			}

			if(null != reader) try{
				reader.close();
			}catch (Exception e){
				logger.error(e.getMessage());
			}

			if(null != http) try{
				http.getInputStream().close();
			}catch (Exception e){
				logger.error(e.getMessage());
			}
		}

		return stringResultData;
	}


	/**
	 * REST API 연결 메소드
	 * @param restApiBean
	 * @return
	 */
	private HttpURLConnection restApiUrlConnectionCore(RestApiBean restApiBean){
		URL url = null;
		HttpURLConnection httpConnection = null;
		StringBuffer stringResultData = new StringBuffer();
		StringBuilder postData = new StringBuilder();
		byte[] postDataByte = null;
		JsonObject jsonObject = new JsonObject();

		try{
			String parameterType = restApiBean.getParameterType();
			Map<String,Object> parameterMap = restApiBean.getParameterMap();
			String requestMethodType = restApiBean.getRequestMethodType();
			String apiUrl = restApiBean.getApiUrl();
			String contentType = restApiBean.getContentType();
			String authorization = restApiBean.getAuthorization();
			String accessToken = restApiBean.getAccessToken();

			if(parameterType.equals(PARAMETER_TYPE_STRING)) {
				for (Map.Entry<String, Object> param : parameterMap.entrySet()) {
					if (postData.length() != 0) postData.append('&');
					postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
					postData.append('=');
					postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
				}
				postDataByte = postData.toString().getBytes(StandardCharsets.UTF_8);
			}else if(parameterType.equals(PARAMETER_TYPE_JSON)){
				//json object 에 밀어넣은 값은 URL Encoding 하면 받지못함.
				for (Map.Entry<String, Object> param : parameterMap.entrySet()) {
					jsonObject.addProperty(param.getKey(),String.valueOf(param.getValue()));
				}
				postDataByte = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
			}

			if(requestMethodType.equals(REQUEST_METHOD_GET)) apiUrl += "?"+postData;

			url = new URL(apiUrl);

			httpConnection = (HttpURLConnection) url.openConnection();

			if(StringUtils.isEmpty(requestMethodType)) requestMethodType =REQUEST_METHOD_POST;
			httpConnection.setRequestMethod(requestMethodType);
			httpConnection.setRequestProperty("Content-Type", contentType);
			if(authorization.equals(AUTHORIATION_BASIC)){
				httpConnection.setRequestProperty("Authorization", AUTHORIATION_BASIC +" " +accessToken);
			}else if(authorization.equals(AUTHORIATION_BEARER)){
				httpConnection.setRequestProperty("Authorization", AUTHORIATION_BEARER +" " +accessToken);
			}

			// GET Method인 경우, callback url로 수신받음.
			if(requestMethodType.equals(REQUEST_METHOD_POST)) {
				// Parameter 입력
				httpConnection.setDoOutput(true);
				httpConnection.getOutputStream().write(postDataByte);
			}

			return httpConnection;
		}catch (Exception e){
			logger.error(e.getMessage());
		}

		return httpConnection;
	}


	/**
	 * RestApi 호출을 위한 인자값 Bean
	 */
	public static class RestApiBean{
		public String apiUrl = "";   // API URL Address
		public String parameterType = RestService.PARAMETER_TYPE_JSON; // 기본 Json  ( request parameter type json / string)
		public Map<String, Object> parameterMap = Maps.newHashMap();  // parameter data map
		public String accessToken = "";  // access token 이 있을 경우 , authorization 같이 넘길 때 사용한다.
		public String contentType = RestService.CONTENT_TYPE_JSON; // 기본 Json ( content type json / url encoding ) 필요에 의하여, 지역변수를 추가 하여 사용한다.
		public String authorization = ""; // authorization 은 기본값은 정하지 않는다 ( 없을수도 있기에 )
		public String requestMethodType = RestService.REQUEST_METHOD_GET; // 기본 get type

		public String getApiUrl() {
			return apiUrl;
		}

		public void setApiUrl(String apiUrl) {
			this.apiUrl = apiUrl;
		}

		public String getParameterType() {
			return parameterType;
		}

		public void setParameterType(String parameterType) {
			this.parameterType = parameterType;
		}

		public Map<String, Object> getParameterMap() {
			return parameterMap;
		}

		public void setParameterMap(Map<String, Object> parameterMap) {
			this.parameterMap = parameterMap;
		}

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getAuthorization() {
			return authorization;
		}

		public void setAuthorization(String authorization) {
			this.authorization = authorization;
		}

		public String getRequestMethodType() {
			return requestMethodType;
		}

		public void setRequestMethodType(String requestMethodType) {
			this.requestMethodType = requestMethodType;
		}
	}

}
