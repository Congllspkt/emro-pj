package smartsuite.app.bp.edu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import smartsuite.app.bp.admin.auth.service.UserService;
import smartsuite.app.bp.edu.service.EduService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.security.account.service.AccountService;


import javax.inject.Inject;
import java.util.List;
import java.util.Map;


@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/edu/")
public class EduController {

	/** The user service. */
	@Inject
	EduService eduService;

	@Inject
	AccountService accountService;

	/**
	 * user list 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 3
	 * @Method Name : findListUserInfo
	 */
	@RequestMapping (value = "findListRegReq.do")
	public @ResponseBody List findListRegReq(@RequestBody Map param) {
		return eduService.findListRegReq(param);
	}

	@RequestMapping (value = "findListItemCat.do")
	public @ResponseBody List findListItemCat(@RequestBody Map param) {
		return eduService.findListItemCat(param);
	}
	

}
