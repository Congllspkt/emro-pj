package smartsuite.app.bp.admin.code;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.code.service.ExchangeRateService;

import smartsuite.app.common.shared.ResultMap;

/**
 * 환율 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @since 2017.06.05
 * @FileName ExchangeRateController.java
 * @package smartsuite.app.bp.admin.code
 * @변경이력 : [2016.06.05] JuEung Kim 최초작성
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/bp/**/")
public class ExchangeRateController {
	
	@Inject
	ExchangeRateService exchangeRateService;
	
	/**
	 * 환율 관리 데이터를 조회한다.
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListExchangeRate.do")
	public @ResponseBody List findListExchangeRate(@RequestBody Map param) {
		return exchangeRateService.findListExchangeRate(param);
	}
	
	/**
	 * 환율 관리 데이터를 저장한다.
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListExchangeRate.do")
	public @ResponseBody ResultMap saveListExchangeRate(@RequestBody Map param) {
		return exchangeRateService.saveListExchangeRate(param);
	}
	
	/**
	 * 환율 관리 데이터를 삭제한다.
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteListExchangeRate.do")
	public @ResponseBody ResultMap deleteListExchangeRate(@RequestBody Map param) {
		return exchangeRateService.deleteListExchangeRateRequest(param);
	}
	
}