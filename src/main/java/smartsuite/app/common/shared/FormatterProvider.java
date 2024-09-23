package smartsuite.app.common.shared;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import smartsuite.app.bp.admin.formatter.service.FormatterManagerService;

/**
 * FormatterProvider Class 입니다.
 *
 */
@SuppressWarnings ({ "rawtypes","unchecked"})
@Service
public class FormatterProvider {
	
	/** The formatter service. */
	@Inject
	private FormatterManagerService formatterManagerService;
	
	/** The Constant LOG. */
	static final Logger LOG = LoggerFactory.getLogger(FormatterProvider.class);
	
	/**
	 * prec format의 값을 반환한다.
	 *
	 * @param formatNm the format nm
	 * @param value the value
	 * @param dtlCd the dtl cd
	 * @return prec format
	 */
	public BigDecimal getPrecFormat(String formatterName, BigDecimal originValue, String dtlCd){
		BigDecimal replaceFormatValue = originValue;

		if(formatterName == null || "".equals(formatterName)){
			return replaceFormatValue;
		}

		if (replaceFormatValue != null && !(BigDecimal.ZERO).equals(replaceFormatValue)) {
			Map<String, Object> displayFormatInfo = findDisplayFormat(formatterName);

			if (MapUtils.isEmpty(displayFormatInfo)) {
				return null;
			} else {
				String decimalPointUseCcd= (String) displayFormatInfo.getOrDefault("decpt_use_ccd","");

				if (StringUtils.isEmpty(decimalPointUseCcd)) {

					BigDecimal decimalLengthLimit= (BigDecimal) displayFormatInfo.getOrDefault("decpt_len_lmt",0);
					String decimalPointTruncationTypeCcd = (String) displayFormatInfo.getOrDefault("decpt_trunc_typ_ccd","");

					if (null != decimalLengthLimit) {
						int attributeValueInt = Integer.parseInt(String.valueOf(decimalLengthLimit));
						int roundHalfUp = BigDecimal.ROUND_HALF_UP;
						if (StringUtils.isNotEmpty(decimalPointTruncationTypeCcd)) {
							if ("ROUND".equals(decimalPointTruncationTypeCcd)) {
								roundHalfUp = BigDecimal.ROUND_HALF_UP;
							} else if ("FLOOR".equals(decimalPointTruncationTypeCcd)) {
								roundHalfUp = BigDecimal.ROUND_FLOOR;
							}
						}
						replaceFormatValue = replaceFormatValue.setScale(attributeValueInt, roundHalfUp);
					}
				} else {
					return getPrecFormat(decimalPointUseCcd, dtlCd, replaceFormatValue);
				}
			}
		}
		return replaceFormatValue;
	}
	
	/**
	 * list prec format zero의 값을 반환한다.
	 *
	 * @param formatNames the format names
	 * example: 
	 * Map<String, Object> formatFields = Maps.newHashMap(); this is key = field, value = formatType
	 * formatFields.put(formatField,formatType);
	 * 
	 * formatFields.put("item_qty", "qty");
	 * formatFields.put("rfq_price", "price");
	 * formatFields.put("tgt_price", "price");
	 * formatFields.put("rfq_amt", "amt");
	 * 
	 * @param items the items
	 * @param fixZero the fix zero
	 * @return list prec format zero
	 */
	public List<Map<String, Object>> getListPrecFormatZero(Map<String, Object> formatFields, List<Map<String, Object>> items,Boolean fixZero){
		Map<String, Object> formatInfo = Maps.newHashMap();
		for (Entry<String, Object> entry : formatFields.entrySet()) {
			String field = entry.getKey();
    		String format = (String)entry.getValue();
    		Map<String, Object> displayFormatInfo  = findDisplayFormat(format);
			formatInfo.put(field, displayFormatInfo);
		}
			List<Map<String, Object>> userOptions = formatterManagerService.findListCurrentUserDisplayFormat();
			for(Map<String, Object> item: items){
				for (Entry<String, Object> fieldEntry : formatInfo.entrySet()) {
					String field = fieldEntry.getKey();
		    		Object formatInfoData = fieldEntry.getValue();
					Map<String, Object> formatInfoDataMap = (Map<String, Object>)formatInfoData;
					BigDecimal bdValue = BigDecimal.ZERO;
					if(item.get(field) != null){
						bdValue = (BigDecimal)item.get(field);
					}
					item.put(field, getPrecFormatZeroByList(formatInfoDataMap,userOptions, bdValue, fixZero));
				}
			}
			return items;
	}
	
	/**
	 * prec format zero by list의 값을 반환한다.
	 *
	 * @param displayFormatInfo the display format info
	 * @param userOptions the user options
	 * @param value the value
	 * @param fixZero the fix zero
	 * @return prec format zero by list
	 */
	public String getPrecFormatZeroByList(Map<String, Object> displayFormatInfo,List<Map<String, Object>> userOptions, BigDecimal value,Boolean fixZero){
		BigDecimal bd = value;
		String result = value.toString();
		if(displayFormatInfo == null){
			return result;
		}
		if(bd == null || (BigDecimal.ZERO).equals(bd)){
			return result;
		}else{			
			if(displayFormatInfo == null){
				return null;
			}else{
				if(displayFormatInfo.get("decpt_len_lmt") != null){
					int attrValInt = Integer.parseInt(String.valueOf(displayFormatInfo.get("decpt_len_lmt")));
					int roundHalfUp = BigDecimal.ROUND_HALF_UP;
					if(displayFormatInfo.get("decpt_trunc_typ_ccd") != null){
						String hndlTyp = (String) displayFormatInfo.get("decpt_trunc_typ_ccd");
						if("ROUND".equals(hndlTyp)){
							roundHalfUp = BigDecimal.ROUND_HALF_UP;
						}else if("FLOOR".equals(hndlTyp)){
							roundHalfUp = BigDecimal.ROUND_FLOOR;
						}
					}
					bd = bd.setScale(attrValInt, roundHalfUp);
					if(fixZero){
						String displayString = bd.toPlainString();
						result = displayString;
					}else {
						result = bd.stripTrailingZeros().toPlainString();
					}
					//fmt_typ_ccd
					String fmtExtStr = ",";
					Object fmtExpTyp = displayFormatInfo.get("fmt_typ_ccd");
					if(fmtExpTyp != null && "AUTO_DES".equals((String)fmtExpTyp)){
						Object fmtExt = displayFormatInfo.get("fmt_expr_1");
						if(fmtExt != null && !"".equals((String)fmtExt)) {
							fmtExtStr = (String) fmtExt;
						}
					}else{
						for(Map<String, Object> userOption: userOptions){
							if(userOption.containsKey("loc_fmt_typ_ccd")){
								String usrExpCls = (String)userOption.get("loc_fmt_typ_ccd");
								if("THOUSAND".equals(usrExpCls)){
									fmtExtStr = (String)userOption.get("loc_fmt_expr");
								}
							}
						}
					}
					
					String[] a = result.split("\\.");
					if(a[0] != null) {
						String partten = "\\B(?=(\\d{3})+(?!\\d))";
						Pattern p = Pattern.compile(partten);
						Matcher mt = p.matcher(a[0]);
						result = mt.replaceAll(fmtExtStr);
						if(a.length > 1) {
							result = result.concat(".").concat(a[1]);
						}
					}
			    	
				}
			}
			return result;
		}
	}
	
	
	/**
	 * 소수점 포맷의 값을 반환한다.(displayString용 세자리 콤마 추가 , return type check!! 단건만 처리할때 사용하도록 함,)
	 *
	 * @param formatNm the format nm
	 * @param value the value
	 * @param fixZero the fix zero : 0자리수채움여부
	 * @return prec format
	 */
	public String getPrecFormatZero(String formatNm, BigDecimal value,Boolean fixZero){
		BigDecimal bd = value;
		String result = value.toString();
		if(formatNm == null || "".equals(formatNm)){
			return result;
		}
		if(bd == null || (BigDecimal.ZERO).equals(bd)){
			return result;
		}else{			
			Map<String, Object> displayFormatInfo  = findDisplayFormat(formatNm);
			
			if(displayFormatInfo == null){
				return null;
			}else{
				if(displayFormatInfo.get("decpt_len_lmt") != null){
					int attrValInt = Integer.parseInt(String.valueOf(displayFormatInfo.get("decpt_len_lmt")));
					int roundHalfUp = BigDecimal.ROUND_HALF_UP;
					if(displayFormatInfo.get("decpt_trunc_typ_ccd") != null){
						String hndlTyp = (String) displayFormatInfo.get("decpt_trunc_typ_ccd");
						if("ROUND".equals(hndlTyp)){
							roundHalfUp = BigDecimal.ROUND_HALF_UP;
						}else if("FLOOR".equals(hndlTyp)){
							roundHalfUp = BigDecimal.ROUND_FLOOR;
						}
					}
					bd = bd.setScale(attrValInt, roundHalfUp);
					if(fixZero){
						String displayString = bd.toPlainString();
						result = displayString;
					}else {
						result = bd.stripTrailingZeros().toPlainString();
					}
					//fmt_typ_ccd
					String fmtExtStr = ",";
					Object fmtExpTyp = displayFormatInfo.get("fmt_typ_ccd");
					if(fmtExpTyp != null && "AUTO_DES".equals((String)fmtExpTyp)){
						Object fmtExt = displayFormatInfo.get("fmt_expr_1");
						if(fmtExt != null && !"".equals((String)fmtExt)) {
							fmtExtStr = (String) fmtExt;
						}
					}else{
						List<Map<String, Object>> userOptions = formatterManagerService.findListCurrentUserDisplayFormat();
						for(Map<String, Object> userOption: userOptions){
							if(userOption.containsKey("loc_fmt_typ_ccd")){
								String usrExpCls = (String)userOption.get("loc_fmt_typ_ccd");
								if("THOUSAND".equals(usrExpCls)){
									fmtExtStr = (String)userOption.get("loc_fmt_expr");
								}
							}
						}
					}
					
					String[] a = result.split("\\.");
					if(a[0] != null) {
						String partten = "\\B(?=(\\d{3})+(?!\\d))";
						Pattern p = Pattern.compile(partten);
						Matcher mt = p.matcher(a[0]);
						result = mt.replaceAll(fmtExtStr);
						if(a.length > 1) {
							result = result.concat(".").concat(a[1]);
						}
					}
			    	
				}
			}
			return result;
		}
	}
	
	/**
	 * 소수점제한 자릿수를 리턴한다.
	 * 
	 * @param formatNm
	 * @param dtlCd
	 * @return
	 */
	public Integer getDecimalPrecision(String formatNm, String dtlCd) {
		if(StringUtils.isEmpty(formatNm)){
			return null;
		}else{
			Map<String, Object> displayFormatInfo  = findDisplayFormat(formatNm);
			if(displayFormatInfo == null){
				return null;
			}else{
				if(displayFormatInfo.get("decpt_use_ccd") == null){
					if(displayFormatInfo.get("decpt_len_lmt") != null){
						int prec = Integer.parseInt(String.valueOf(displayFormatInfo.get("decpt_len_lmt")));
						return prec;
					}else{
						return null;
					}
				}else{
					Integer result = null;
					String precGrpCd = (String) displayFormatInfo.get("decpt_use_ccd");
					List<Map<String, Object>> precFormats = findListPrecisionFormatByGroupCode(precGrpCd);
					for(Map<String, Object> precFormat: precFormats){
						if(dtlCd.equals(precFormat.get("dtl_cd"))){
							if(precFormat.get("decpt_use_dtlcd") != null && precFormat.get("prec") != null){
								int prec = Integer.parseInt(String.valueOf(precFormat.get("prec")));
								result = prec;
							}
							break;
						}
					}
					return result;
				}
			}
		}
	}
	
	/**
	 * prec format의 값을 반환한다.
	 *
	 * @param code the code
	 * @param dtlCd the dtl cd
	 * @param amt the amt
	 * @return prec format
	 */
	private BigDecimal getPrecFormat(String code, String dtlCd, BigDecimal amt){
		BigDecimal result = amt;
		if(code == null || "".equals(code)){
			return result;
		}
		if(result == null || (BigDecimal.ZERO).equals(result)){
			return result;
		}else{
			List<Map<String, Object>> precFormats = findListPrecisionFormatByGroupCode(code);
			for(Map<String, Object> precFormat: precFormats){
				if(dtlCd.equals(precFormat.get("dtl_cd"))){
					if(precFormat.get("decpt_use_dtlcd") != null && precFormat.get("prec") != null){
						int attrValInt = Integer.parseInt(String.valueOf(precFormat.get("prec")));
						int roundHalfUp = BigDecimal.ROUND_HALF_UP;
						if(precFormat.get("decpt_trunc_typ_ccd") != null){
							String hndlTemrsTyp = (String) precFormat.get("decpt_trunc_typ_ccd");
							if("ROUND".equals(hndlTemrsTyp)){
								roundHalfUp = BigDecimal.ROUND_HALF_UP;
							}else if("FLOOR".equals(hndlTemrsTyp)){
								roundHalfUp = BigDecimal.ROUND_FLOOR;
							}
						}
						result = amt.setScale(attrValInt, roundHalfUp);
					}else{
						result = amt;
					}
					break;
				}
			}
			return result;
		}
	}
	
	public Map<String, Object> findDisplayFormat(String formatNm){
		return formatterManagerService.findDisplayFormat(formatNm);
	}
	/**
	 * Find list prec format by grp cd.
	 *
	 * @param precGrpCd the prec grp cd
	 * @return the list
	 */
	public List<Map<String, Object>> findListPrecisionFormatByGroupCode(String precGrpCd) {
		return formatterManagerService.findListPrecisionFormatByGroupCode(precGrpCd);
	}

	/**
	 * Find current user all display format.
	 *
	 * @return the map
	 */
	public Map<String, Object> findCurrentUserAllDisplayFormat() {
		Map<String, Object> resultMap = Maps.newHashMap();
		//displayFormat에서 적용하기로한 List를 가져옴.
		List<Map<String, Object>> displayFormatList = formatterManagerService.findListUseDisplayFormat();
		//사용자설정과 사용자표기법설정의 default 관련 설정을 가져옴.
		List<Map<String,Object>> currentUserDispFormats = formatterManagerService.findListCurrentUserDisplayFormat();
		resultMap.put("currentUserDisplayFormats", currentUserDispFormats);
		resultMap.put("displayFormats", displayFormatList);
		return resultMap;
	}

	/**
	 * Find list prec format cur.
	 *
	 * @param param the param
	 * @return the list
	 */
	public List findListPrecisionFormatCur(Map<String, Object> param) {
		return formatterManagerService.findListPrecisionFormatCur(param);
	}

	/**
	 * Find list all prec format.
	 *
	 * @return the list
	 */
	public List findListPrecisionFormat() {
		return formatterManagerService.findListPrecisionFormat();
	}

	/**
	 * Find list formatter.
	 *
	 * @return the map
	 */
	public Map<String, Object> findListFormatter() {
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap = findCurrentUserAllDisplayFormat();
		resultMap.put("precFormats", findListPrecisionFormat());
		return resultMap;
	}

	/**
	 * Find all user exp format.
	 *
	 * @return the map
	 */
	public Map<String, Object> findAllUserExpFormat() {
		Map<String, Object> resultMap = Maps.newHashMap();

		resultMap.put("userDateExpList", findListUserDateExp());
		resultMap.put("userPrecExpList", findListPrexExp());
		resultMap.put("userThouExpList", findListThouExp());
		return resultMap;
	}

	/**
	 * Find list thou exp.
	 *
	 * @return the list
	 */
	private List findListThouExp() {
		Map<String, Object> param = Maps.newHashMap();
		param.put("loc_fmt_typ_ccd", "THOUSAND");
		return formatterManagerService.findListUserFormatByUserExpressionClass(param);
	}

	/**
	 * Find list prex exp.
	 *
	 * @return the list
	 */
	private List findListPrexExp() {
		Map<String, Object> param = Maps.newHashMap();
		param.put("loc_fmt_typ_ccd", "PREC");
		return formatterManagerService.findListUserFormatByUserExpressionClass(param);
	}

	/**
	 * Find list user date exp.
	 *
	 * @return the list
	 */
	private List findListUserDateExp() {
		Map<String, Object> param = Maps.newHashMap();
		param.put("loc_fmt_typ_ccd", "DATE");
		return formatterManagerService.findListUserFormatByUserExpressionClass(param);
	}

	public Map findCurrentUserFormatInfo() {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, Object>> formatInfo = formatterManagerService.findCurrentUserFormatInfo();
		if(formatInfo != null){
			for(Map<String, Object> result: formatInfo){
				String usrExpCls = (String) result.get("loc_fmt_typ_ccd");
				resultMap.put(usrExpCls.toLowerCase(LocaleContextHolder.getLocale()).toString(), result);
			}
		}
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveCurrentUserFormatter(Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();
		
		for(String key : param.keySet()){
			Map<String, Object> data = (Map<String, Object>) param.get(key);
			if(data.get("loc_fmt_expr_uuid") == null){
				deleteCurrentUserFormatLink(data);
			}else{
				margeCurrentUserFormatLink(data);
			}
        }
		return resultMap;
	}

	private void deleteCurrentUserFormatLink(Map<String, Object> data){
		formatterManagerService.deleteCurrentUserFormatLink(data);
	}
	private void margeCurrentUserFormatLink(Map<String, Object> data) {
		formatterManagerService.margeCurrentUserFormatLink(data);
	}

	public Map<String, Object> findUserFormatInfo(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, Object>> formatInfo = formatterManagerService.findUserFormatInfo(param);
		if(formatInfo != null){
			for(Map<String, Object> result: formatInfo){
				String usrExpCls = (String) result.get("loc_fmt_typ_ccd");
				resultMap.put(usrExpCls.toLowerCase(LocaleContextHolder.getLocale()).toString(), result);
			}
		}
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveUserFormatter(Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();
		
		for(String key : param.keySet()){
			Map<String, Object> data = (Map<String, Object>) param.get(key);
			if(data.get("loc_fmt_expr_uuid") == null){
				deleteUserFormatLinkByUserExpressionClass(data);
			}else{
				margeUserFormatLink(data);
			}
        }
		return resultMap;
	}

	private void deleteUserFormatLinkByUserExpressionClass(Map<String, Object> data) {
		formatterManagerService.deleteUserFormatLinkByUserExpressionClass(data);
	}

	private void margeUserFormatLink(Map<String, Object> data) {
		formatterManagerService.margeUserFormatLink(data);
	}
}
