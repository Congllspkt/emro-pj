package smartsuite.app.bp.admin.terms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.bp.admin.terms.service.SupplierTermsService;
import smartsuite.app.common.shared.Const;
import smartsuite.security.annotation.AuthCheck;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "**/bp/**")
public class SupplierTermsController {

	@Inject
	SupplierTermsService supplierTermsService;
	/**
	 * 약관동의 이력 목록 조회
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2021.04.06
	 * @Method Name : findListUserTermsAgreeHistory
	 */
	@AuthCheck(authCode = Const.READ)
	@RequestMapping(value = "findListUserTermsAgreeHistory.do")
	public @ResponseBody List findListUserTermsAgreeHistory(@RequestBody Map param) {
		return supplierTermsService.findListUserTermsAgreeHistory(param);
	}
}
