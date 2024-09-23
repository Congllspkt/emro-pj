package smartsuite.app.bp.admin.manualManager.service;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.manualManager.repository.ManualManagerRepository;
import smartsuite.app.common.AttachService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.upload.StdFileService;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Manual 관련 처리하는 서비스 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @FileName ManualService.java
 * @package smartsuite.app.bp.admin.manual
 * @Since 2016. 6. 28
 * @변경이력 : [2016. 6. 28] JuEung Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ManualManagerService {
	
	static final Logger LOG = LoggerFactory.getLogger(ManualManagerService.class);

	@Inject
	StdFileService stdFileService;

	@Inject
	ManualManagerRepository manualManagerRepository;

	/**
	 * 각 언어별 다국어 조회
	 * @param param
	 * @return
	 */
	public List findListAllLanguageManual(Map param) {
		return manualManagerRepository.findListAllLanguageManual(param);
	}

	/**
	 * 마지막 revision의 메뉴얼 정보를 조회한다.
	 * @param param
	 * @return
	 */
	public Map findLastRevisionManualInfo(Map param) {
		return manualManagerRepository.findLastRevisionManualInfo(param);
	}

	/**
	 * 메뉴얼 차수를 표기하기 위한 combobox 조회용
	 * @param param
	 * @return
	 */
	public List findListRevisionManualComboboxItem(Map param){
		return manualManagerRepository.findListRevisionManualComboboxItem(param);
	}
	
	/**
	 * 메뉴얼 데이터를 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2016. 6. 29
	 * @Method Name : saveManualInfo
	 */
	public ResultMap saveManualInfo(Map<String, Object> param) {

		Map<String, Object> manualInfo = (Map<String, Object>)param.getOrDefault("manualInfo", Maps.newHashMap());
		
		String manualRevision = manualInfo.getOrDefault("mnl_revno","") == null ? "" : manualInfo.getOrDefault("mnl_revno","").toString(); // 메뉴얼 차수

		if(StringUtils.isEmpty(manualRevision)){
			manualInfo.put("mnl_revno", 1);
			this.insertNextRevisionManualInfo(manualInfo);
		}else{
			this.updateSelectionRevisionManualInfo(manualInfo);
		}

		return ResultMap.SUCCESS();
	}

	/**
	 * 이전 revision 메뉴얼 복사
	 * @param param
	 * @return
	 */
	public ResultMap copyPreviousRevisionManualInfo(Map<String, Object> param) {

		Map<String, Object> previousManualInfo = (Map<String, Object>)param.getOrDefault("manualInfo",Maps.newHashMap());

		String previousAttachNumber = previousManualInfo.getOrDefault("mnl_athg_uuid","") == null ? "" : (String) previousManualInfo.getOrDefault("mnl_athg_uuid","");
		if(StringUtils.isNotEmpty(previousAttachNumber)) { // 이전 파일 번호 존재여부 확인 후 존재 시 파일 카피
			String copyAttachNumber = stdFileService.copyFile(previousAttachNumber);
			previousManualInfo.put("mnl_athg_uuid", copyAttachNumber);
		}
		String previousOriginAttachNumber = previousManualInfo.getOrDefault("orig_mnl_athg_uuid","") == null? "" : (String) previousManualInfo.getOrDefault("orig_mnl_athg_uuid","");
		if(StringUtils.isNotEmpty(previousOriginAttachNumber)) { // 이전 메뉴얼 파일 번호 존재 여부 확인 후 존재 시 메뉴얼 카피
			String copyOriginAttachNumber = stdFileService.copyFile(previousOriginAttachNumber);
			previousManualInfo.put("orig_mnl_athg_uuid", copyOriginAttachNumber);
		}

		// 메뉴얼 추가
		this.insertNextRevisionManualInfo(previousManualInfo);

		return ResultMap.SUCCESS();
	}

	/**
	 * 메뉴얼 정보를 마지막 revision으로 추가한다.
	 * @param param
	 */
	public void insertNextRevisionManualInfo(Map<String, Object> param) {
		manualManagerRepository.insertNextRevisionManualInfo(param);
	}

	/**
	 * 선택한 revision 의 메뉴얼 정보를 수정한다.
	 * @param param
	 * @return
	 */
	public void updateSelectionRevisionManualInfo(Map<String, Object> param) {
		manualManagerRepository.updateSelectionRevisionManualInfo(param);
	}

	/**
	 * 선택한 언어의 메뉴얼 메뉴 리스트 조회
	 * @param param
	 * @return
	 */
	public List findListSelectionLanguageManualMenu(Map param) {
		return manualManagerRepository.findListSelectionLanguageManualMenu(param);
	}

	public boolean existManualInfo(Map param){
		return manualManagerRepository.getCountManualInfo(param) > 0;
	}

	public void deleteManualInfo(Map param){
		manualManagerRepository.deleteManualInfo(param);
	}
}
