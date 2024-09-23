package smartsuite.mybatis;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import smartsuite.module.ModuleManager;
import smartsuite.mybatis.plugin.parameter.ParameterInjector;
import smartsuite.security.authentication.Auth;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class DefaultParameterInjector implements ParameterInjector {

	ApplicationContext actx;
	
	private final static long ONE_DAY_MILLISECOND = 1000 * 60 * 60 * 24;
	
	private final static char ESCAPE_CHARACTER = '\\';
	
	static final Logger LOG = LoggerFactory.getLogger(DefaultParameterInjector.class);

	@Override
	public String getName() {
		return "g";
	}

	@Override
	public Object getValue() {
		return new g(getUsername(), getLocale(), getTenant(), getCompcd(), getDeptcd(), getUsercls(), getVdsn(), getVdcd(),getLblSortTypCcd(), getRoles());
	}

	protected String getUsername() {
		return Auth.getCurrentUserName();
	}
	
	protected String getCompcd() {
		if(Auth.getCurrentUserInfo() == null)
			return null;
		else
			return (String)Auth.getCurrentUserInfo().get("co_cd");
	}
	
	protected String getDeptcd() {
		if(Auth.getCurrentUserInfo() == null)		
			return null;
		else
			return (String)Auth.getCurrentUserInfo().get("dept_cd");
	}
	
	protected String getUsercls() {
		if(Auth.getCurrentUserInfo() == null)
			return null;
		else 
			return (String)Auth.getCurrentUserInfo().get("usr_typ_ccd");
	}
	
	protected String getVdsn() {
		if(Auth.getCurrentUserInfo() == null)		
			return null;
		else 
			return String.valueOf(Auth.getCurrentUserInfo().get("vd_sn"));
	}

	protected String getLblSortTypCcd() {
		if(Auth.getCurrentUserInfo() == null)
			return null;
		else
			return String.valueOf(Auth.getCurrentUserInfo().get("lbl_sort_typ_ccd"));
	}

	protected String getVdcd(){
		if(Auth.getCurrentUserInfo() == null)		
			return null;
		else 
			return String.valueOf(Auth.getCurrentUserInfo().get("vd_cd"));
	}
	protected Locale getLocale() {
		Locale targetLocale = LocaleContextHolder.getLocale();
		if(targetLocale == null || "ko".equals(targetLocale.toString())){
			targetLocale = new Locale("ko","KR");
			LocaleContextHolder.setLocale(targetLocale);
		}
		return targetLocale;
	}
	
	protected String getTenant() {
		return Auth.getCurrentTenantId();
	}
	
	protected List<String> getRoles() {
		return Auth.getCurrentUserAuthorities();
	}

	@SuppressWarnings("unused")
	private class g {

		String username;

		Locale locale;

		String tenant;

		String co_cd;    //NOPMD

		String dept_cd;    //NOPMD

		String usr_typ_ccd;    //NOPMD

		String vd_sn; //NOPMD

		String vd_cd; //NOPMD

		List<String> roles;

		String lbl_sort_typ_ccd; //NOPMD

		public g(String username, Locale locale, String tenant, String compcd, String deptcd, String usercls, String vdsn, String vdcd, String lbl_sort_typ_ccd, List<String> roles) {
			this.username = username;
			this.locale = locale;
			this.tenant = tenant;
			this.co_cd = compcd;
			this.dept_cd = deptcd;
			this.usr_typ_ccd = usercls;
			this.vd_sn = vdsn;
			this.vd_cd = vdcd;
			this.lbl_sort_typ_ccd = lbl_sort_typ_ccd;
			this.roles = roles;
		}

		public String getLbl_sort_typ_ccd() {
			return lbl_sort_typ_ccd;
		}

		public String getUsername() {
			return username;
		}

		public String getTenant() {
			return tenant;
		}

		public String getComp_cd() {
			return co_cd;
		}

		public String getDept_cd() {
			return dept_cd;
		}

		public String getUsr_cls() {
			return usr_typ_ccd;
		}

		public String getVd_sn() {
			return vd_sn;
		}

		public String getVd_cd() {
			return vd_cd;
		}

		public List<String> getRoles() {
			return this.roles;
		}

		public boolean existModule(String module) {
			return ModuleManager.exist(module);
		}

		public String getLocale() {
			return locale.getLanguage() + "_" + locale.getCountry();
		}

		public Date getNow() {
			return new Date();
		}

		public Date getDateWithNoTime(Date date) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
			String dt = formatter.format(date);
			Calendar cal = Calendar.getInstance();

			try {
				cal.setTime(formatter.parse(dt));
			} catch (ParseException e) {
				LOG.error(e.getMessage());
			}
			return cal.getTime();
		}

		public String getUuid() {
			return UUID.randomUUID().toString();
		}

		public Date getPlusDays(Date date, int days) {
			return new Date(date.getTime() + (ONE_DAY_MILLISECOND * days));
		}

		public Date increaseDate(Date date, String typeParam, Integer increaseParam) {
			Integer increase = increaseParam;
			String type = typeParam;

			if (!StringUtils.isEmpty(type)) type = type.toUpperCase(LocaleContextHolder.getLocale());
			if (increase == null) {
				increase = 1;
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			if ("Y".equals(type)) {
				cal.add(Calendar.YEAR, increase);
			} else if ("M".equals(type)) {
				cal.add(Calendar.MONTH, increase);
			} else if ("D".equals(type)) {
				cal.add(Calendar.DATE, increase);
			}
			return cal.getTime();
		}

		public String escape(String value) {
			String escaped = value.replace("%", ESCAPE_CHARACTER + "%");
			escaped = escaped.replace("_", ESCAPE_CHARACTER + "_");
			return value;
		}

		public String trim(String value) {
			String trimVal = value;
			if (value != null) {
				trimVal = value.trim();
			}
			return trimVal;
		}

		public String getFormatNow(Date date, String format) {
			SimpleDateFormat sd = new SimpleDateFormat(StringUtils.isEmpty(format) ? "yyyyMMdd" : format, Locale.getDefault());
			return sd.format(date);
		}

		public boolean contains(List<?> list, Object val) {
			return list.contains(val);
		}

		public boolean contains(String str, String val) {
			return str.contains(val);
		}

		public String[] split(String str, String regex) {
			return str.split(regex);
		}

		public boolean isEmpty(Object obj) {
			if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
			else return obj == null;
		}

		public boolean isNotEmpty(Object obj) {
			return !isEmpty(obj);
		}

		/**
		 * 소수점 처리 값 반환
		 *
		 * @param cur     the cur
		 * @param amt     the amt
		 * @param curList the cur list
		 * @return cur amt
		 */
		public BigDecimal getPrecFormat(String dtlCd, BigDecimal val, Map<String, Object> displayFormatInfo, List<Map<String, Object>> precFormats) {
			BigDecimal value = val;
			if (value == null || (BigDecimal.ZERO).equals(value)) {
				return value;
			} else {
				if (displayFormatInfo.get("decpt_use_ccd") == null) {
					if (displayFormatInfo.get("decpt_len_lmt") != null) {
						BigDecimal prec = (BigDecimal) displayFormatInfo.get("decpt_len_lmt");
						int attrValInt = prec.intValueExact();
						int roundHalfUp = BigDecimal.ROUND_HALF_UP;
						if (displayFormatInfo.get("decpt_trunc_typ_ccd") != null) {
							String hndlTyp = (String) displayFormatInfo.get("decpt_trunc_typ_ccd");
							if ("ROUND".equals(hndlTyp)) {
								roundHalfUp = BigDecimal.ROUND_HALF_UP;
							} else if ("FLOOR".equals(hndlTyp)) {
								roundHalfUp = BigDecimal.ROUND_FLOOR;
							}
						}
						value = value.setScale(attrValInt, roundHalfUp);
					}
					return value;
				} else {

					for (Map<String, Object> precFormat : precFormats) {
						if (dtlCd.equals(precFormat.get("decpt_use_dtlcd"))) {
							String attrVal = (String) precFormat.get("prec");
							int attrValInt = Integer.parseInt(attrVal, 10);
							int roundHalfUp = BigDecimal.ROUND_HALF_UP;
							if (precFormat.get("decpt_trunc_typ_ccd") != null) {
								String hndlTemrsTyp = (String) precFormat.get("decpt_trunc_typ_ccd");
								if ("ROUND".equals(hndlTemrsTyp)) {
									roundHalfUp = BigDecimal.ROUND_HALF_UP;
								} else if ("FLOOR".equals(hndlTemrsTyp)) {
									roundHalfUp = BigDecimal.ROUND_FLOOR;
								}
							}
							value = value.setScale(attrValInt, roundHalfUp);
							break;
						}
					}
					return value;
				}
			}
		}

		/**
		 * mybatis xml에서 $를 이용해야 하는 특수한 경우에 이 함수를 사용합니다
		 */
		public String sqlInjectionEscape(String value) {
			Pattern pattern = Pattern.compile("['\"\\-#()@;=*/+]");
			return pattern.matcher(value).replaceAll("");
		}
		
		public Boolean getModule(String module) {
			return ModuleManager.exist(module);
		}
	}
}
