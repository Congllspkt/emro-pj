package smartsuite.security.account.info;

import java.util.Date;

public class AccountSettings {

	boolean disableOnSpecifiedDate;
	
	Date accountDisableForSpecifiedDate = new Date();
	
	Date accountDisableByLastLoginDate = new Date();
	
	Date accountDisableUserNotifyDate = new Date();
	
	String accountDisableUserNotifyCode = "2W";
	
	String accountDisableCode = "1Y";
	
	int passwordExpiredPeriod = 6;
	
	boolean accountPasswordExpiredThrowException;
	
	int limitLoginInvalidPasswordCount = 5;
	
	int passwordAlphabetCharacterRule = 1;
	
	int passwordDigitCharacterRule = 1;
	
	int passwordSequenceCharacterRule = 4;
	
	int passwordRepeatCharacterRule = 4;
	
	int passwordSepecialCharacterRule = 1;
	
	int passwordMinLengthRule = 8;
	
	int passwordMaxLengthRule = 20;
	
	int passwordPhoneNoDuplicateRule = 0;
	
	boolean adminAdditionalAuthentication = false;
	
	public Date getAccountDisableUserNotifyDate() {
		return accountDisableUserNotifyDate;
	}

	public void setAccountDisableUserNotifyDate(Date accountDisableUserNotifyDate) {
		this.accountDisableUserNotifyDate = accountDisableUserNotifyDate;
	}

	public String getAccountDisableUserNotifyCode() {
		return accountDisableUserNotifyCode;
	}

	public void setAccountDisableUserNotifyCode(String accountDisableUserNotifyCode) {
		this.accountDisableUserNotifyCode = accountDisableUserNotifyCode;
	}

	public int getPasswordAlphabetCharacterRule() {
		return passwordAlphabetCharacterRule;
	}

	public void setPasswordAlphabetCharacterRule(int passwordAlphabetCharacterRule) {
		this.passwordAlphabetCharacterRule = passwordAlphabetCharacterRule;
	}

	public int getPasswordDigitCharacterRule() {
		return passwordDigitCharacterRule;
	}

	public void setPasswordDigitCharacterRule(int passwordDigitCharacterRule) {
		this.passwordDigitCharacterRule = passwordDigitCharacterRule;
	}

	public int getPasswordSequenceCharacterRule() {
		return passwordSequenceCharacterRule;
	}

	public void setPasswordSequenceCharacterRule(int passwordSequenceCharacterRule) {
		this.passwordSequenceCharacterRule = passwordSequenceCharacterRule;
	}

	public int getPasswordRepeatCharacterRule() {
		return passwordRepeatCharacterRule;
	}

	public void setPasswordRepeatCharacterRule(int passwordRepeatCharacterRule) {
		this.passwordRepeatCharacterRule = passwordRepeatCharacterRule;
	}

	public int getPasswordSepecialCharacterRule() {
		return passwordSepecialCharacterRule;
	}

	public void setPasswordSepecialCharacterRule(int passwordSepecialCharacterRule) {
		this.passwordSepecialCharacterRule = passwordSepecialCharacterRule;
	}

	public int getPasswordMinLengthRule() {
		return passwordMinLengthRule;
	}

	public void setPasswordMinLengthRule(int passwordMinLengthRule) {
		this.passwordMinLengthRule = passwordMinLengthRule;
	}

	public int getPasswordMaxLengthRule() {
		return passwordMaxLengthRule;
	}

	public void setPasswordMaxLengthRule(int passwordMaxLengthRule) {
		this.passwordMaxLengthRule = passwordMaxLengthRule;
	}
	
	public int getPasswordPhoneNoDuplicateRule() {
		return passwordPhoneNoDuplicateRule;
	}

	public void setPasswordPhoneNoDuplicateRule(int passwordPhoneNoDuplicateRule) {
		this.passwordPhoneNoDuplicateRule = passwordPhoneNoDuplicateRule;
	}

	public Date getAccountDisableForSpecifiedDate() {
		return accountDisableForSpecifiedDate;
	}

	public boolean isAdminAdditionalAuthentication() {
		return adminAdditionalAuthentication;
	}

	public void setAdminAdditionalAuthentication(boolean adminAdditionalAuthentication) {
		this.adminAdditionalAuthentication = adminAdditionalAuthentication;
	}

	public int getPasswordExpiredPeriod() {
		return passwordExpiredPeriod;
	}

	public void setPasswordExpiredPeriod(int passwordExpiredPeriod) {
		this.passwordExpiredPeriod = passwordExpiredPeriod;
	}

	public void setAccountDisableForSpecifiedDate(Date accountDisableForSpecifiedDate) {
		this.accountDisableForSpecifiedDate = accountDisableForSpecifiedDate;
	}

	public Date getAccountDisableByLastLoginDate() {
		return accountDisableByLastLoginDate;
	}

	public void setAccountDisableByLastLoginDate(Date accountDisableByLastLoginDate) {
		this.accountDisableByLastLoginDate = accountDisableByLastLoginDate;
	}
	
	public int getLimitLoginInvalidPasswordCount() {
		return limitLoginInvalidPasswordCount;
	}

	public void setLimitLoginInvalidPasswordCount(int limitLoginInvalidPasswordCount) {
		this.limitLoginInvalidPasswordCount = limitLoginInvalidPasswordCount;
	}
	
	public boolean isAccountPasswordExpiredThrowException() {
		return accountPasswordExpiredThrowException;
	}

	public void setAccountPasswordExpiredThrowException(boolean accountPasswordExpiredThrowException) {
		this.accountPasswordExpiredThrowException = accountPasswordExpiredThrowException;
	}

	public boolean isDisableOnSpecifiedDate() {
		return disableOnSpecifiedDate;
	}

	public void setDisableOnSpecifiedDate(boolean disableOnSpecifiedDate) {
		this.disableOnSpecifiedDate = disableOnSpecifiedDate;
	}
	
	public String getAccountDisableCode() {
		return accountDisableCode;
	}
	
	public void setAccountDisableCode(String accountDisableCode){
		this.accountDisableCode = accountDisableCode;
	}
	
}
