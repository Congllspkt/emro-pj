package smartsuite.app.common.template.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.TemplateException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.template.service.TemplateService;
import smartsuite.app.common.shared.Const;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class TemplateGeneratorService {
	
	private static final Log LOG = LogFactory.getLog(TemplateGeneratorService.class);

	@Inject
	TemplateService templateService;

	
	/**
	 * Mail generate.
	 *
	 * @param mailKey the mail key
	 * @param data the data
	 * @return the string
	 * @throws Exception the exception
	 */
	public String mailTemplateGenerate(String mailKey, Object data) throws TemplateException, IOException{
 
		//메일 설정 키를 사용하는 템플릿 본문 정보 검색
		Map<String, Object> template = templateService.findTemplateContentByMailSetKey(mailKey);

		// 본문 내용
		String templateContent = template.getOrDefault("ctmpl_cont","") == null? "" :(String) template.getOrDefault("ctmpl_cont","");
		
		return freemarkerTemplateGenerate(mailKey,templateContent,data);
	}
	
	/**
	 * Parameter check.
	 * 
	 * TODO : 임시 param_class_nm에 param을 열로 나열하고, 주어진 dataParam과 실제로 선언된 param을 비교하여
	 * </br>
	 *  체크하여 값이 없는 경우 빈값으로 넣고 결과값을 알려줌.
	 *
	 * @author : Yeon-u Kim
	 * @param template the template
	 * @param data the data
	 * @return the map
	 */
	public Map<String,Object> parameterCheck(Map<String,Object> template,Map<String,Object> data){
		Map<String, Object> resultMap = Maps.newHashMap();

		String paramClassName = template.getOrDefault("ctmpl_parm_info","") == null ? "" : (String) template.getOrDefault("ctmpl_parm_info","");

		if(StringUtils.isEmpty(paramClassName)){
			resultMap.put("data", data);
			resultMap.put(Const.RESULT_STATUS, Const.SUCCESS);
		}else if(StringUtils.isNotEmpty(paramClassName)){
			String[] fields = paramClassName.split("\\,");
			Map<String, Object> orgData = Maps.newHashMap();
			ArrayList<String> failFieldList = Lists.newArrayList();
			for(String field : fields){
				if(data == null || !data.containsKey(field)){
					failFieldList.add(field);
				}
				orgData.put(field, field);
			}
			if(data != null){
				orgData.putAll((Map<? extends String, ? extends Object>)data);
			}
			if(failFieldList.isEmpty()){
				resultMap.put(Const.RESULT_STATUS, Const.SUCCESS);
			}else{
				resultMap.put(Const.RESULT_STATUS, Const.FAIL);
			}
			resultMap.put("data", orgData);
			resultMap.put("failFieldList", failFieldList);
		}
		return resultMap;
	}
	/**
	 * Generate.
	 *
	 * @param appId the app id
	 * @param content the content
	 * @param data the data
	 * @return the string
	 * @throws Exception the exception
	 */
	public String freemarkerTemplateGenerate(String appId, String content, Object data) throws TemplateException, IOException{
		StringWriter writer = new StringWriter();
		String replacedContent = replaceSpecialChars(content);
		freemarker.template.Template t = new freemarker.template.Template(appId, new StringReader(replacedContent), null);
		t.process(data, writer);

		String result = writer.toString();
		if (LOG.isErrorEnabled()) LOG.info("result : " + result);
		return result;
	}
	
	private String replaceSpecialChars(String str) {
		if(str == null)
		return null;

		String returnStr = str;
		returnStr = returnStr.replaceAll("<BR>", "\n");
		returnStr = returnStr.replaceAll("&amp;", "&");
		returnStr = returnStr.replaceAll("&gt;", ">");
		returnStr = returnStr.replaceAll("&lt;", "<");
		returnStr = returnStr.replaceAll("&quot;", "\"");
		returnStr = returnStr.replaceAll("&nbsp;", " ");

		return returnStr;
	}
	
	/**
     *  Template generate.
     *  @param appId
     *  @param data
     *  @return
     *  @throws TemplateException
     *  @throws IOException
     */
    public String tmpGenerate(String tmpKey, Object data) throws TemplateException, IOException {
        Map<String, Object> param = Maps.newHashMap();
        param.put("basc_ctmpl_cd", tmpKey);
        Map<String, Object> template = templateService.findTemplateBaseInfoByTemplateBaseId(param);
        if(MapUtils.isNotEmpty(template) && template.get("basc_ctmpl_cont") != null) {
            return this.freemarkerTemplateGenerate(tmpKey, (String) template.get("basc_ctmpl_cont"), data);
        }
        else {
            return null;
        }
    }

	/**
	 * 템플릿 미리보기 내용 조회
	 */
	public Map<String, Object> findTemplatePreview(Map<String, Object> param) {
		String previewHtml = "";

		try {
			// 템플릿 기초 아이디 값으로 템플릿 내용을 조회한다.
			Map templateMap = templateService.findTemplateInfoByTemplateClassAndTemplateBaseId(param);

			// 템플릿 내에 content 내용이 없을 경우 freemakerTemplateGenrate를 탈 이유가 없어서 조건 처리
			// 넘어온 언어로 1차 content를 찾은 후 존재하지 않을 경우, 한국어로 처리된 내역으로 2차 가공 진행 ( 해당 부분은 추후 의사결정에 따라 변경 가능성 존재 )
			String multiLangTemplateContent = templateMap.get("basc_ctmpl_cont") == null ? "" : (String) templateMap.get("basc_ctmpl_cont");
			if(StringUtils.isEmpty(multiLangTemplateContent)) multiLangTemplateContent =  templateMap.get("display_basc_ctmpl_cont") == null ? "" : (String) templateMap.get("display_basc_ctmpl_cont");
			if(StringUtils.isNotEmpty(multiLangTemplateContent)){
				previewHtml = this.freemarkerTemplateGenerate((String) param.get("task_uuid"),multiLangTemplateContent,param);
			}
		} catch (TemplateException e) {
			throw new CommonException(ErrorCode.FAIL, e);
		} catch (IOException e){
			throw new CommonException(ErrorCode.FAIL, e);
		}

		Map<String, Object> result = Maps.newHashMap();
		result.put("previewHtml", previewHtml);
		return result;
	}

	/**
	 * 결재 템플릿 상세 조회 ( 조회 조건 결재구분코드)  // 순환참조로 의하여, templateGeneratorService -> templateservice 호출
	 * @param param
	 * @return
	 */
	public Map<String, Object> findApprovalTemplateByApprovalTypeCode(Map<String, Object> param) {
		return templateService.findApprovalTemplateByApprovalTypeCode(param);
	}

	/**
	 * 템플리 정보 조회 ( 조회조건 템플릿 기본 아이디 & 템플릿 구분 )  // 순환참조로 의하여, templateGeneratorService -> templateservice 호출
	 * @param templateData
	 * @return
	 */
	public Map findTemplateInfoByTemplateClassAndTemplateBaseId(Map<String, Object> templateData) {
		return templateService.findTemplateInfoByTemplateClassAndTemplateBaseId(templateData);
	}

	/**
	 * 템플릿 기초정보 조회 ( 조회조건 template base id )
	 * @param param
	 * @return
	 */
	public Map findTemplateBaseInfoByTemplateBaseId(Map param) {
		return templateService.findTemplateBaseInfoByTemplateBaseId(param);
	}
}
