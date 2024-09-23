package smartsuite.security.account;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import smartsuite.security.account.service.AccountService;
import smartsuite.security.account.info.AccountSettings;

import smartsuite.security.authentication.AuthenticationPostService;

@Controller
@RequestMapping("**/account/")
public class AccountController {
	
	@Inject
	AccountService accountService;

	@Inject
	AuthenticationPostService authenticationPostService;
	
	@RequestMapping("load.do")
	public @ResponseBody AccountSettings load() {
		return accountService.load();
	}
	
	@RequestMapping("save.do")
	@ResponseStatus(value = HttpStatus.OK)
	public void save(@RequestBody AccountSettings accountManagement) {
		accountService.update(accountManagement);
	}
	
	@RequestMapping("ipAddressLoad.do")
	public @ResponseBody List<Map<String,String>> ipAddressLoad() {
		return accountService.ipAddressLoad();
	}
	
	@RequestMapping("ipAddressDelete.do")
	@ResponseStatus(value = HttpStatus.OK)
	public void ipAddressDelete(@RequestBody Map<String,List<Map<String,String>>> param) {
		List<Map<String,String>> deleteItems = param.getOrDefault("deleteItems", Lists.newArrayList());
		accountService.ipAddressDelete(deleteItems);
	}
	
	@RequestMapping("ipAddressSave.do")
	@ResponseStatus(value = HttpStatus.OK)
	public void ipAddressSave(@RequestBody Map<String,List<Map<String,String>>> param) {
		List<Map<String,String>> newItems = param.getOrDefault("newItems",Lists.newArrayList());
		accountService.ipAddressSave(newItems);
	}


	@RequestMapping(value="**/checkRoleAdminAuthenticate.do")
	public @ResponseBody Map<String,Object> checkRoleAdminAuthenticate(HttpServletRequest request, @RequestBody Map<String,String> param) {
		return authenticationPostService.checkRoleAdminAuthenticate(request, param);
	}
}
