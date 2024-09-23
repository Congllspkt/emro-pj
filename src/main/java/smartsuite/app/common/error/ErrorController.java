package smartsuite.app.common.error;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.common.error.service.ErrorService;

import smartsuite.app.common.shared.ResultMap;
import smartsuite.mybatis.data.impl.PageResult;

/**
 * 시스템 에러 발생 시 관리를 위한 Controller
 *
 * @author jonghyeok
 * @see
 * @since 2018.07.11
 * @FileName ErrorController.java
 * @package smartsuite.app.common.error
 * @변경이력 : [2018. 7. 11] jonghyeok 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
public class ErrorController {
	
	@Inject
	ErrorService errorService;
	
	/**
	 * Browser Exception 저장, 역할없는 사용자도 저장이 필요
	 */
	@RequestMapping (value = "/**/saveBrowserErrorInfo.do", method = RequestMethod.POST)
	public @ResponseBody void saveBrowserErrorInfo(HttpServletRequest request, @RequestBody Map param) {
		errorService.saveBrowserErrorInfo(request, param);
	}
	
	/**
	 * 서버 에러 리스트 조회
	 */
	@RequestMapping (value = "/**/findListServerError.do", method = RequestMethod.POST)
	public @ResponseBody PageResult findListError(@RequestBody Map param) {
		param.put("err_occr_typ", "S");
		return new PageResult(errorService.findListError(param));
	}
	
	/**
	 * 브라우저 에러 리스트 조회 
	 */
	@RequestMapping (value = "/**/findListBrowserError.do", method = RequestMethod.POST)
	public @ResponseBody PageResult findListBrowserError(@RequestBody Map param) {
		param.put("err_occr_typ", "B");
		return new PageResult(errorService.findListError(param));
	}
	
	/**
	 * Error 조회 
	 */
	@RequestMapping (value = "/**/findErrorInfo.do", method = RequestMethod.POST)
	public @ResponseBody Map findErrorInfo(@RequestBody Map param) {
		return errorService.findErrorInfo(param);
	}
	
	/**
	 * Error 리스트 삭제
	 */
	@RequestMapping(value = "/**/deleteListError.do")
	public @ResponseBody ResultMap deleteListError(@RequestBody Map saveParam){
		return errorService.deleteListError(saveParam);
	}
	
	/**
	 * Error 강제 발생
	 */
	@RequestMapping(value = "/**/occurError.do")
	public @ResponseBody void occurError(@RequestBody Map saveParam){
		errorService.occurError(saveParam);
	}
	
	/**
	 * Error 업데이트
	 */
	@RequestMapping(value = "/**/updateError.do")
	public @ResponseBody ResultMap updateError(@RequestBody Map saveParam){
		return errorService.updateError(saveParam);
	}
}
