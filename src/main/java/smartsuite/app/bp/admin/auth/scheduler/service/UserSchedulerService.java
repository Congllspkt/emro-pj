package smartsuite.app.bp.admin.auth.scheduler.service;

import com.google.common.collect.Maps;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.auth.scheduler.event.UserSchedulerEventPublisher;
import smartsuite.app.bp.admin.auth.scheduler.repository.UserSchedulerRepository;
import smartsuite.app.common.mail.MailService;
import smartsuite.security.account.info.AccountSettings;
import smartsuite.security.account.service.AccountService;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * UserScheduler 관련 처리하는 서비스 Class입니다.
 *
 * @see 
 * @FileName UserSchedulerService.java
 * @package smartsuite.app.bp.admin.auth.scheduler
 * @Since 2020. 6. 26
 */
@Service
@Transactional
public class UserSchedulerService {

	/** The mail service. */
	@Inject
	MailService mailService;

	@Inject
	AccountService accountService;

	@Inject
	UserSchedulerRepository userSchedulerRepository;

	@Inject
	UserSchedulerEventPublisher userSchedulerEventPublisher;

	/**
	 * Send mail dormant account schedule.
	 * userList
	 * acc_disabled_user_notify_cd : + 1W , 2W
	 * acc_disabled_cd : - 1Y, 2M
	 *
	 * @param paramMap the param map
	 */
	public void sendMailDormantAccountScheduleForAllUser(HashMap<String,Object> paramMap){
		this.getUserDormantAccountDate("ALL");
	}
	public void sendMailDormantAccountScheduleForSupplierUser(HashMap<String,Object> paramMap){
		this.getUserDormantAccountDate("SUPPLIER");
	}
	public void sendMailDormantAccountScheduleForBuyer(HashMap<String,Object> paramMap){
		this.getUserDormantAccountDate("BUYER");
	}

	/**
	 * 현재 시스템의 만기 대상 기간 검색 및 메일 발송
	 */
	public void getUserDormantAccountDate(String searchType){
		AccountSettings accountSettings = accountService.load();
		String accDisabledUserNotifyCd = "";
		String accDisabledCd = "";
		Calendar calendar = null;
		Calendar notifyAddDayCal = null;
		Date expiredDate = null;
		Calendar notifyCal = null;
		String expiredDateStr = "";
		String accountLockDateStr = "";

		if(accountSettings.isDisableOnSpecifiedDate()){
			Map<String, Object> userDormantInfo = Maps.newHashMap();

			accDisabledUserNotifyCd = accountSettings.getAccountDisableUserNotifyCode();
			accDisabledCd = accountSettings.getAccountDisableCode();
			//ex ) 일주일(acc_disabled_user_notify_cd) 후 만기대상
			calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar = getDefTime(accDisabledUserNotifyCd,calendar);
			expiredDate = calendar.getTime();
			notifyCal = getDefTime(("-"+accDisabledCd),calendar);

			//24시간 계산
			notifyAddDayCal = Calendar.getInstance();
			notifyAddDayCal.setTime(notifyCal.getTime());
			notifyAddDayCal.add(Calendar.DATE, 1);
			userDormantInfo.put("notic_one_day", notifyAddDayCal.getTime());
			userDormantInfo.put("notic_day", notifyCal.getTime());

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",  Locale.getDefault());
			expiredDateStr = formatter.format(expiredDate);
			calendar.add(Calendar.DATE, 1);
			accountLockDateStr = formatter.format(calendar.getTime());

			List<Map<String,Object>> userDormantList = this.noticeDormantAccounts(userDormantInfo,searchType);
			this.sendMailDormantAccountUserList(userDormantList,accDisabledCd,expiredDateStr,accountLockDateStr);
		}
	}

	/**
	 * 만기 대상자 검색
	 *
	 * @param userDormantInfo
	 * @param searchType
	 * @return
	 */
	public List<Map<String, Object>> noticeDormantAccounts(Map<String, Object> userDormantInfo, String searchType){
		if(searchType.equals("ALL")){
			return userSchedulerEventPublisher.findListDormantAccountForAllUser(userDormantInfo);
		}else if(searchType.equals("SUPPLIER")){
			return userSchedulerEventPublisher.findListDormantAccountForSupplier(userDormantInfo);
		}

		return userSchedulerRepository.findListDormantAccountForBuyer(userDormantInfo);
	}

	/**
	 * 만기 대상자 메일 발송
	 * @param noticeDormantAccounts
	 * @param accDisabledCd
	 * @param expiredDateStr
	 * @param accountLockDateStr
	 */
	public void sendMailDormantAccountUserList(List<Map<String,Object>> noticeDormantAccounts,String accDisabledCd,String expiredDateStr,String accountLockDateStr){
		//sendMail;
		for(Map<String, Object> dormantAccount: noticeDormantAccounts){
			dormantAccount.put("unused_period", accDisabledCd);
			dormantAccount.put("expired_date", expiredDateStr);
			dormantAccount.put("account_lock_date", accountLockDateStr);
			sendMailDormantAccount(dormantAccount);
		}
	}

	/**
	 * Send mail dormant accout.
	 *
	 * @param info the info
	 */
	private void sendMailDormantAccount(Map<String, Object> info){
		mailService.sendAsync("MAIL_DORMANT_ACCOUNT", null, info);
	}
	
	/**
	 * def time의 값을 반환한다.
	 *
	 * @param code the code
	 * @param cal the cal
	 * @return def time
	 */
	private  Calendar getDefTime(String code,Calendar cal){
		Integer increase = Integer.parseInt(code.replaceAll("[^-+?0-9]", ""));
		String type = code.replaceAll("[^a-zA-Z]","");
		
		if(type != null) type = type.toUpperCase(LocaleContextHolder.getLocale());
		
		if("Y".equals(type)) {
			cal.add(Calendar.YEAR, increase);
		} else if("M".equals(type)) {
			cal.add(Calendar.MONTH, increase);
		} else if("D".equals(type)) {
			cal.add(Calendar.DATE, increase);
		} else if("W".equals(type)){
			cal.add(Calendar.WEDNESDAY, increase);
		}
		//날짜로만 계산
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	/**
	 * account disabled 수정한다.
	 *
	 * @param paramMap the param map
	 * @Date : 2020. 6. 29
	 * @Method Name : updateAccountDisabled
	 */
	public void updateAccountDisabled(HashMap<String, Object> paramMap){
		AccountSettings accountSettings = accountService.load();
		//acc_disabled_cd : - 1Y, 2M 
		if(accountSettings.isDisableOnSpecifiedDate()){
			String accDisabledCd = accountSettings.getAccountDisableCode();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal = getDefTime(("-"+accDisabledCd),cal);
			userSchedulerRepository.accountDisable(cal.getTime());
		}
	}
}
