package smartsuite.app.bp.admin.printout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.printout.repository.PrintoutRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.upload.StdFileService;
import smartsuite.upload.entity.FileList;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Printout 관련 처리를 하는 서비스 Class입니다.
 *
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class PrintoutService {
	
	@Inject
	PrintoutRepository printoutRepository;
	
	@Inject
	StdFileService stdFileService;
	
	private static Logger LOG = LoggerFactory.getLogger(PrintoutService.class);
	
	@Value("#{globalProperties['ubiformUrl']}")
	private String ubiformUrl;
	
	/**
	 * 문서 출력 관리 (DOC_OUTP_MGMT) 정보 조회
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListDocumentOutputManagement(Map<String, Object> param) {
		List<Map<String, Object>> documentOutputDataList = printoutRepository.findListDocumentOutputManagement(param);
		return documentOutputDataList;
	}
	
	/**
	 * 문서 출력 관리 (DOC_OUTP_MGMT) 정보 저장
	 * 
	 * @param param
	 * @return
	 */
	public ResultMap saveListDocumentOutputManagement(Map<String, Object> param) {
		List<Map<String, Object>> updateList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		List<Map<String, Object>> insertList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		
		//UPDATE
		for(Map<String, Object> row : updateList) {
			this.updateListDocumentOutputManagement(row);
		}
		
		//INSERT
		for(Map<String, Object> row : insertList) {
			String documentOutputUuid = (String)UUID.randomUUID().toString();
			row.put("doc_outp_uuid", documentOutputUuid);
			this.insertListDocumentOutputManagement(row);
		}
		
		return ResultMap.SUCCESS();
	}
	
	public void updateListDocumentOutputManagement(Map<String, Object> param) {
		printoutRepository.updateListDocumentOutputManagement(param);
	}
	
	public void insertListDocumentOutputManagement(Map<String, Object> param) {
		printoutRepository.insertListDocumentOutputManagement(param);
	}
	
	/**
	 * 문서 출력 관리 (DOC_OUTP_MGMT) 삭제
	 * 
	 * @param param
	 * @return
	 */
	public ResultMap deleteListDocumentOutputManagement(Map<String, Object> param) {
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		
		// DELETE
		for(Map<String, Object> row : deleteList){
			this.deleteInfoDocumentOutputManagement(row);
		}
		return ResultMap.SUCCESS();
	}
	
	public void deleteInfoDocumentOutputManagement(Map<String, Object> param) {
		this.deleteDocumentOutputDataParamByManagement(param);
		this.deleteDocumentOutputDataSetupByManagement(param);
		this.deleteDocumentOutputManagment(param);
	}
	
	public void deleteDocumentOutputDataParamByManagement(Map<String, Object> param) {
		printoutRepository.deleteDocumentOutputDataParamByManagement(param);
	}
	
	public void deleteDocumentOutputDataSetupByManagement(Map<String, Object> param) {
		printoutRepository.deleteDocumentOutputDataSetupByManagement(param);
	}
	
	public void deleteDocumentOutputManagment(Map<String, Object> param) {
		printoutRepository.deleteDocumentOutputManagment(param);
	}
	
	/**
	 * 문서 출력 데이터 설정 정보(DOC_OUTP_DAT_SETUP) 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListDocumentOutputSetup(Map<String, Object> param) {
		List<Map<String, Object>> documentOutputDataSetupList = printoutRepository.findListDocumentOutputSetup(param);
		return documentOutputDataSetupList;
	}
	
	/**
	 * 문서 출력 데이터 설정 정보(DOC_OUTP_DAT_SETUP) 정보 저장
	 * 
	 * @param param
	 * @return
	 */
	public ResultMap saveListDocumentOutputSetup(Map<String, Object> param) {
		List<Map<String, Object>> updateList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		List<Map<String, Object>> insertList = (List<Map<String, Object>>) param.getOrDefault("insertList",Lists.newArrayList());
		
		//UPDATE
		for(Map<String, Object> row : updateList) {
			this.updateListDocumentOutputSetup(row);
		}
		
		//INSERT
		for(Map<String, Object> row : insertList) {
			String documentOutputDataSetupUuid = (String)UUID.randomUUID().toString();
			row.put("doc_outp_dat_setup_uuid", documentOutputDataSetupUuid);
			this.insertListDocumentOutputSetup(row);
		}
		return ResultMap.SUCCESS();
	}
	
	public void updateListDocumentOutputSetup(Map<String, Object> param) {
		printoutRepository.updateListDocumentOutputSetup(param);
	}
	
	public void insertListDocumentOutputSetup(Map<String, Object> param) {
		printoutRepository.insertListDocumentOutputSetup(param);
	}
	
	/**
	 * 문서 출력 데이터 설정 정보(DOC_OUTP_DAT_SETUP) 정보 삭제
	 * @param param
	 * @return
	 */
	public ResultMap deleteListDocumentOutputDataSetup(Map<String, Object> param) {
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		
		// DELETE
		for(Map<String, Object> row : deleteList){
			this.deleteInfoDocumentOutputDataSetup(row);
		}
		
		return ResultMap.SUCCESS();
	}
	
	public void deleteInfoDocumentOutputDataSetup(Map<String, Object> param) {
		this.deleteDocumentOutputDataParamByDataSetupUuid(param);
		this.deleteDocumentOutputDataSetup(param);
	}
	
	public void deleteDocumentOutputDataParamByDataSetupUuid(Map<String, Object> param) {
		printoutRepository.deleteDocumentOutputDataParamByDataSetupUuid(param);
	}
	
	public void deleteDocumentOutputDataSetup(Map<String, Object> param) {
		printoutRepository.deleteDocumentOutputDataSetup(param);
	}
	
	/**
	 * 문서 출력 데이터 파라미터(DOC_OUTP_DAT_PARM) 조회
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findListDocumentOutputDataParam(Map<String, Object> param) {
		List<Map<String,Object>> documentOutputDataParamList = printoutRepository.findListDocumentOutputDataParam(param);
		return documentOutputDataParamList;
	}
	
	/**
	 * 문서 출력 데이터 파라미터(DOC_OUTP_DAT_PARM) 저장
	 * @param param
	 * @return
	 */
	public ResultMap saveListDocumentOutputDataParam(Map<String, Object> param) {
		List<Map<String, Object>> updateList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		List<Map<String, Object>> insertList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		
		//UPDATE
		for(Map<String, Object> row : updateList) {
			this.updateListDocumentOutputDataParam(row);
		}
		
		//INSERT
		for(Map<String, Object> row : insertList) {
			String documentOutputDataParamUuid = (String)UUID.randomUUID().toString();
			row.put("doc_outp_dat_parm_uuid", documentOutputDataParamUuid);
			this.insertListDocumentOutputDataParam(row);
		}
		return ResultMap.SUCCESS();
	}
	
	public void updateListDocumentOutputDataParam(Map<String, Object> param) {
		printoutRepository.updateListDocumentOutputDataParam(param);
	}
	
	public void insertListDocumentOutputDataParam(Map<String, Object> param) {
		printoutRepository.insertListDocumentOutputDataParam(param);
	}
	
	/**
	 * 문서 출력 데이터 파라미터(DOC_OUTP_DAT_PARM) 삭제
	 * @param param
	 * @return
	 */
	public ResultMap deleteListDocumentOutputDataParam(Map<String, Object> param) {
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		
		// DELETE
		for(Map<String, Object> row : deleteList){
			this.deleteDocumentOutputDataParam(row); 
		}
		 
		return ResultMap.SUCCESS();
	}
	
	public void deleteDocumentOutputDataParam(Map<String, Object> param) {
		printoutRepository.deleteDocumentOutputDataParam(param);
	}
	
	/**
	 * 문서 출력 관리 파라미터 조회를 위한 업무
	 * @param param
	 * @return
	 */
	public Map<String, Object> findDocumentOutputManagement(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> paramGroupInfo = Maps.newHashMap();
		//문서 출력 관리 정보 (DOC_OUTP_MGMT) 조회
		Map<String, Object> documentOutputManagmenttInfo = printoutRepository.findDocumentOutputManagement(param);
		resultMap.put("docOutpMgmtInfo", documentOutputManagmenttInfo);

		//문서 출력 데이터 설정 정보 (DOC_OUTP_DAT_SETUP) 조회
		List<Map<String, Object>> documentOutputDataSetupList = printoutRepository.findListDocumentOutputSetup(documentOutputManagmenttInfo);

		//Value To Key
		for(Map<String, Object> documentOutputDataSetup : documentOutputDataSetupList) {			
			//문서 출력 데이터 파라미터 (DOC_OUTP_DAT_PARM) 조회 
			List<Map<String,Object>> documentOutputParamGroupList = printoutRepository.findListDocumentOutputDataParam(documentOutputDataSetup);
			Map<String, Object> documentOutputDataParam = Maps.newHashMap();
			for(Map<String, Object> parameter : documentOutputParamGroupList) {
				documentOutputDataParam.put((String) parameter.get("doc_outp_dat_parm_nm"), "");
			}
			String documentDataName = documentOutputDataSetup.get("doc_outp_dat_nm") == null ? "" : documentOutputDataSetup.get("doc_outp_dat_nm").toString();
			paramGroupInfo.put(documentDataName, documentOutputDataParam);
		}

		resultMap.put("paramGroupList",paramGroupInfo);

		return resultMap;
	}
	
	/**
	 * 유비폼팝업을 띄우기 위한 인자값(Parameter) 셋팅
	 * @param param
	 * @return
	 */
	public Map<String, Object> makeUBIFormParameter(Map<String, Object> param){
		Map<String, Object> parameter = Maps.newHashMap();
		
		parameter.put("projectName", param.get("projectName"));
		parameter.put("formName", param.get("formName"));
		parameter.put("url", ubiformUrl);
		
        Map<String,Object> reponseParameter = Maps.newHashMap();
        
        reponseParameter.put("param", parameter);
        
		return reponseParameter;
	}
	
	/**
	 * 유비폼으로 IMG encoding으로 보내야할 때, Base64 인코딩으로 변환
	 * @param attachGroupId
	 * @return
	 */
	public String converterImageFileBase64Encoding(String attachGroupId) {
		FileList fileList = null;
		byte[] fileData = null;
		String encodingImgString = "";

		try{
			fileList = stdFileService.findFileListWithContents(attachGroupId);
			if(fileList.getSize() > 0){
				fileData = fileList.getItems().get(0).toByteArray();
				encodingImgString = "data:image/png;base64," + Base64.encodeBase64String(fileData);
			}
		}catch (Exception e){
			LOG.error(e.getMessage());
		}
		return encodingImgString;
	}
}
