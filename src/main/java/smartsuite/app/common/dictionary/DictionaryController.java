package smartsuite.app.common.dictionary;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.common.dictionary.service.DictionaryService;

import smartsuite.app.common.shared.ResultMap;

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/bp/**/")
public class DictionaryController {
	
	@Inject
	DictionaryService dictionaryService;
	
	/**
	 * 용어집 정보 조회
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListDictionary.do")
	public @ResponseBody List findListDictionary(@RequestBody Map param) {
		return dictionaryService.findListDictionary(param);
	}
	
	/**
	 * 용어집 정보 저장
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListDictionary.do")
	public @ResponseBody ResultMap saveListDictionary(@RequestBody Map<String, Object> param) {
		return dictionaryService.saveListDictionary(param);
	}
	
	/**
	 * 용어집 정보 삭제
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteDictionary.do")
	public @ResponseBody ResultMap deleteDictionary(@RequestBody Map<String, Object> param) {
		return dictionaryService.deleteDictionary(param);
	}
}
