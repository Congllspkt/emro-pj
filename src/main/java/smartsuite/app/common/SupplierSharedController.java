package smartsuite.app.common;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import smartsuite.app.common.service.SupplierSharedService;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.ResultMap;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
public class SupplierSharedController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SupplierSharedController.class);

	/** The shared service. */
	@Inject
	SupplierSharedService supplierSharedService;

	/**
	 * list vendor master 조회를 요청한다.
	 *
	 * @author : mgPark
	 * @param param the param
	 * @return the list
	 * @Date : 2018. 7. 9
	 * @Method Name : findListVendorMaster
	 */
	@RequestMapping (value = "**/findListVendorMaster.do")
	public @ResponseBody List findListVendorMaster(@RequestBody Map param) {
		return supplierSharedService.findListVendorMaster(param);
	}

	/**
	 * 협력사 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param(oorg_cd,
	 * @return the list
	 * @Date : 2016. 5. 10
	 * @Method Name : findListVendorInfo
	 */
	@RequestMapping (value = "**/findListVendorInfo.do")
	public @ResponseBody List findListVendorInfo(@RequestBody Map param) {
		if(param.get("ounit_cd") != null && "EO".equals((String)param.get("ounit_cd"))) {
			// 협력사운영단위 기준 협력사 리스트 조회
			return supplierSharedService.findListVendorInfo(param);
		} else if (param.get("vendor_typ") == null || StringUtils.isEmpty((String)param.get("vendor_typ"))) {
			// 전체 협력사 리스트 조회
			return supplierSharedService.findListVendor(param);
		} else {
			String vendorTyp = (String)param.get("vendor_typ");
			if ("SG".equals(vendorTyp)) {
				// SG 협력사 리스트 조회
				return supplierSharedService.findListSourcingGroupVendor(param);
			} else {
				// 전체 협력사 리스트 조회
				return supplierSharedService.findListVendor(param);
			}
		}
	}


	@RequestMapping (value = "**/sp/**/findUserIdByBusinessRegistrationNumber.do")
	public @ResponseBody ResultMap findUserIdByBusinessRegistrationNumberProcess(@RequestBody Map param) {
		return supplierSharedService.findUserIdByBusinessRegistrationNumberProcess(param);
	}


	/**
	 * 운영조직에 연결된 vendor 목록을 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 10
	 * @Method Name : findListOperationOrganizationLinkVendor
	 */
	@RequestMapping (value = "**/findListOperationOrganizationLinkVendor.do")
	public @ResponseBody List findListOperationOrganizationLinkVendor(@RequestBody Map param) {
		return supplierSharedService.findListOperationOrganizationLinkVendor(param);
	}


	/**
	 * 협력사 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 7. 19
	 * @Method Name : findListVendorBasicInfo
	 */
	@RequestMapping (value = "**/findListVendorBasicInfo.do")
	public @ResponseBody List findListVendorBasicInfo(@RequestBody Map param) {
		return supplierSharedService.findListVendorBasicInfo(param);
	}

	/**
	 * Inits the pasword by user info.
	 *
	 * @param param the param
	 * @return the model and view
	 */
	@RequestMapping (value = "**/sp/**/initPassword.do", method = RequestMethod.POST)
	public ModelAndView initPaswordByUserInfo(@RequestParam Map param) {
		ModelAndView model = new ModelAndView();
		String resultPage = "login/sp/result/";

		ResultMap result = supplierSharedService.initPassword(param);

		if (Const.FAIL.equals(result.getResultStatus())) {
			resultPage += "noUser"; // 회원 정보 불일치 페이지
		} else {
			resultPage += "successMailSend"; // 성공 페이지
		}

		model.setViewName(resultPage);
		return model;
	}

}
