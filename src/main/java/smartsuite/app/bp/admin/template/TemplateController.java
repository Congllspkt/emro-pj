package smartsuite.app.bp.admin.template;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.template.service.TemplateService;

import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.template.service.TemplateGeneratorService;


@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping(value="**/bp/**/")
public class TemplateController {


	@Inject
	TemplateService templateService;

	/*
	 * 템플릿 목록 조회
	 * */
	@RequestMapping(value="findListTemplate.do")
	public @ResponseBody List findListTemplate(@RequestBody Map param){
		return templateService.findListTemplate(param);
	}
	
	
	/*
	 * 템플릿 내용 상세 조회
	 * */
	@RequestMapping(value = "findListTemplateById.do")
	public @ResponseBody Map findListTemplateById(@RequestBody Map param) {
		return templateService.findTemplateInfoByTemplateClassAndTemplateBaseId(param);
	}
	
	/*
	 * 템플릿 내용 상세 조회
	 * */
	@RequestMapping(value = "findTemplateInfoByTemplateClassMultiLangAndTemplateBaseId.do")
	public @ResponseBody Map findTemplateInfoByTemplateClassMultiLangAndTemplateBaseId(@RequestBody Map param) {
		return templateService.findTemplateInfoByTemplateClassMultiLangAndTemplateBaseId(param);
	}


	/*
	 * 템플릿 목록 삭제
	 * */ 
	@RequestMapping(value="deleteListTemplate.do")
	public @ResponseBody ResultMap deleteListTemplate(@RequestBody Map param){
		return templateService.deleteListTemplateRequest(param);
	}

	/*
	 * 템플릿 저장
	 * */
	@RequestMapping(value="saveTemplateBaseInfo.do")
	public @ResponseBody ResultMap saveTemplateBaseInfo(@RequestBody Map param){
		return templateService.saveTemplateBaseInfo(param);
	}
	

	/* 템플릿 조회 */
	@RequestMapping(value="findTemplateBaseInfoByTemplateBaseId.do")
	public @ResponseBody Map findTemplateBaseInfoByTemplateBaseId(@RequestBody Map param){
		return templateService.findTemplateBaseInfoByTemplateBaseId(param);
	}

	/**
	 * list approval tmp 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2017. 2. 20
	 * @Method Name : findListApprovalTemplate
	 */
	/* 결재 템플릿 목록 조회 */
	@RequestMapping(value="findListApprovalTemplate.do")
	public @ResponseBody List findListApprovalTemplate(@RequestBody Map param){
		return templateService.findListApprovalTemplate(param);
	}

	/**
	 * approval tmp by cd 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 2. 20
	 * @Method Name : findApprovalTemplateByApprovalTypeCode
	 */
	/* 결재 템플릿 상세 조회(단건조회) */
	@RequestMapping(value="findApprovalTemplateByApprovalTypeCode.do")
	public @ResponseBody Map findApprovalTemplateByApprovalTypeCode(@RequestBody Map param){
		return templateService.findApprovalTemplateByApprovalTypeCode(param);
	}

	/**
	 * approval tmp 저장을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 2. 20
	 * @Method Name : saveApprovalTemplate
	 */
	/* 결재 템플릿 저장 */
	@RequestMapping(value="saveApprovalTemplate.do")
	public @ResponseBody ResultMap saveApprovalTemplate(@RequestBody Map param){
		return templateService.saveApprovalTemplate(param);
	}

	/**
	 * approval tmp bas id list 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the approval tmp bas id list
	 * @Date : 2017. 2. 20
	 * @Method Name : findListApprovalTemplateComboboxItemByApprovalTemplateClass
	 */
	/* combobox 목록조회 */
	@RequestMapping(value="findListApprovalTemplateComboboxItemByApprovalTemplateClass.do")
	public @ResponseBody List findListApprovalTemplateComboboxItemByApprovalTemplateClass(@RequestBody Map param){
		return templateService.findListApprovalTemplateComboboxItemByApprovalTemplateClass(param);
	}

}
