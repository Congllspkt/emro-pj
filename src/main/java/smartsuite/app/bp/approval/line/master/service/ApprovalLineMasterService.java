package smartsuite.app.bp.approval.line.master.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.approval.line.master.repository.ApprovalLineMasterRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

// TODO: Auto-generated Javadoc
/**
 * ApprovalLine 관련 처리하는 서비스 Class입니다.
 *
 * @author Yeon-u Kim
 * @see 
 * @FileName ApprovalLineService.java
 * @package smartsuite.app.bp.approval.mng
 * @Since 2017. 2. 1
 * @변경이력 : [2017. 2. 1] Yeon-u Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" ,
	"PMD.FinalFieldCouldBeStatic",
	"PMD.UseCollectionIsEmpty",
	"PMD.UseConcurrentHashMap",
	"PMD.LawOfDemeter",
	"PMD.BeanMembersShouldSerialize",
	"PMD.AtLeastOneConstructor"})
public class ApprovalLineMasterService {
	
	@Inject
	ApprovalLineMasterRepository approvalLineMasterRepository;
	
	/**
	 * aprv line list 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2017. 2. 1
	 * @Method Name : findApprovalLineMasterList
	 */
	public FloaterStream findApprovalLineMasterList(final Map<String,Object> param) {
		// 대용량 처리
		return approvalLineMasterRepository.findApprovalLineMasterList(param);
	}

	/**
	 * aprv line master detail list 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2017. 2. 2
	 * @Method Name : findApprovalLineMasterDetailList
	 */
	public List findApprovalLineMasterDetailList(final Map<String,Object> param){
		return approvalLineMasterRepository.findApprovalLineMasterDetailList(param);
	}
	/**
	 * aprv line master 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 2. 2
	 * @Method Name : saveApprovalLineMaster
	 */
	public Map saveApprovalLineMaster(final Map<String,Object> param){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> insertList = param.get("insertAprvLineMasters") == null? Lists.newArrayList() : (List<Map<String, Object>>) param.get("insertAprvLineMasters");
		List<Map<String,Object>> updateList = param.get("updateAprvLineMasters") == null? Lists.newArrayList() : (List<Map<String, Object>>) param.get("updateAprvLineMasters");
		this.insertApprovalLineMaster(insertList);
		this.updateApprovalLineMaster(updateList);
		return resultMap;
	}

	private void updateApprovalLineMaster(List<Map<String, Object>> updateList) {
		for(Map<String,Object> updated : updateList){
			approvalLineMasterRepository.updateApprovalLineMaster(updateList);
		}
	}

	private void insertApprovalLineMaster(List<Map<String, Object>> insertList) {
		for(Map<String,Object> inserted : insertList){
			this.insertApprovalLineMaster(inserted, null);
		}
	}
	
	private void insertApprovalLineMaster(Map<String, Object> inserted, String key) {
		if(Strings.isNullOrEmpty(key)) {
			inserted.put("usr_apvlln_uuid", UUID.randomUUID().toString());
		} else {
			inserted.put("usr_apvlln_uuid", key);
		}
		approvalLineMasterRepository.insertApprovalLineMaster(inserted);
	}

	/**
	 * aprv line detail 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 2. 2
	 * @Method Name : saveApprovalLineDetail
	 */
	public Map saveApprovalLineDetail(final Map<String,Object> param){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> insertDetailList = param.get("insertAprvLineDetails") == null? Lists.newArrayList(): (List<Map<String, Object>>) param.get("insertAprvLineDetails");
		List<Map<String,Object>> updateDetailList = param.get("updateAprvLineDetails") == null? Lists.newArrayList(): (List<Map<String, Object>>) param.get("updateAprvLineDetails");
		
		this.insertApprovalLineDetail(insertDetailList);
		this.updateApprovalLineDetail(updateDetailList);
		
		return resultMap;
	}

	private void updateApprovalLineDetail(List<Map<String, Object>> updateDetailList) {
		for(final Map<String,Object> updated : updateDetailList){
			approvalLineMasterRepository.updateApprovalLineDetail(updated);
		}
	}

	private void insertApprovalLineDetail(List<Map<String, Object>> insertDetailList) {
		for(final Map<String,Object> inserted : insertDetailList){
			approvalLineMasterRepository.insertApprovalLineDetail(inserted);
			
		}
	}

	/**
	 * aprv line master 삭제한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 2. 1
	 * @Method Name : deleteApprovalLineMaster
	 */
	public ResultMap deleteApprovalLineMaster(final Map<String, Object> param) {
		List<Map<String,Object>> deleteApprovalLineList = param.get("deleteds") == null? Lists.newArrayList(): (List<Map<String, Object>>) param.get("deleteds");
		
		if(deleteApprovalLineList.size() > 0){
			//마스터 상세 리스트 삭제
			this.deleteApprovalLineMasterInfo(param);
			//마스터 삭제
			this.deleteApprovalLineMasterList(param);
		}
		
		return ResultMap.SUCCESS();
		
	}

	private void deleteApprovalLineMasterList(Map<String, Object> param) {
		approvalLineMasterRepository.deleteApprovalLineMasterList(param);
	}
	
	private void deleteApprovalLineMasterInfo(Map<String, Object> param) {
		approvalLineMasterRepository.deleteApprovalLineMasterInfo(param);
	}

	private void deleteApprovalLineMasterDetail(Map<String, Object> param) {
		approvalLineMasterRepository.deleteApprovalLineMasterDetail(param);
	}

	/**
	 * aprv line master detail 삭제한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 2. 1
	 * @Method Name : deleteListApprovalLineMasterDetail
	 */
	public Map deleteListApprovalLineMasterDetail(final Map<String,Object> param){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> deleteApprovalLineMasterList = param.get("deleteds") == null? Lists.newArrayList() : (List<Map<String, Object>>) param.get("deleteds");
		
		if(deleteApprovalLineMasterList.size() > 0){
			//마스터 상세 삭제
			this.deleteApprovalLineMasterDetail(param);
		}
		
		return resultMap;
	}
	
	public Map findListApprovalLineDetailForApproval(Map<String, Object> param) {
		Map result = Maps.newHashMap();
		result.put("aprvLineList", approvalLineMasterRepository.findListApprovalLineDetailForApproval(param));
		result.put("aprvLineCcList", approvalLineMasterRepository.findListReferenceListDetailForApproval(param));
		return result;
	}
	
	public ResultMap saveApprovalLineByApproval(Map<String, Object> param) {
		Map<String, Object> header = (Map<String, Object>) param.get("header");
		List<Map<String, Object>> approver = (List<Map<String, Object>>) param.get("approver");
		List<Map<String, Object>> receiver = (List<Map<String, Object>>) param.get("receiver");
		Map<String, Object> lastApprover = (Map<String, Object>) param.get("lastApprover");
		
		String usrApvllnUuid = UUID.randomUUID().toString();
		List<Map<String, Object>> approverList = this.convertApproverToApprovalLine(usrApvllnUuid, approver, lastApprover);
		List<Map<String, Object>> referenceList = this.convertReferenceToApprovalLine(usrApvllnUuid, receiver);
		approverList.addAll(referenceList);
		
		for(int i = 0; i < approverList.size(); i++) {
			Map<String, Object> approverInfo = approverList.get(i);
			approverInfo.put("apvr_sort", (i+1));
		}
		
		this.insertApprovalLineMaster(header, usrApvllnUuid);
		this.insertApprovalLineDetail(approverList);
		return ResultMap.SUCCESS();
	}
	
	protected List convertApproverToApprovalLine(String key, List<Map<String, Object>> approver, Map<String, Object> lastApprover) {
		List<Map<String, Object>> resultList = Lists.newArrayList();
		Map<String, Object> result;
		for(int i = 0; i < approver.size(); i++) {
			Map<String, Object> info = approver.get(i);
			result = Maps.newHashMap();
			result.put("usr_apvlln_uuid", key);
			result.put("apvr_id", info.get("usr_id"));
			result.put("apvr_typ_ccd", info.get("apvr_typ_ccd"));
			if("PARLL_AG".equals(info.get("apvr_typ_ccd"))) {
				result.put("seq_parll_typ_ccd", "PARLL");
			} else {
				result.put("seq_parll_typ_ccd", "SEQ");
			}
			resultList.add(result);
		}
		if(lastApprover != null && !lastApprover.isEmpty()) {
			result = Maps.newHashMap();
			result.put("usr_apvlln_uuid", key);
			result.put("apvr_id", lastApprover.get("usr_id"));
			result.put("apvr_typ_ccd", "APVL");
			result.put("seq_parll_typ_ccd", "SEQ");
			resultList.add(result);
		}
		return resultList;
	}
	
	protected List convertReferenceToApprovalLine(String key, List<Map<String, Object>> receiver) {
		List<Map<String, Object>> resultList = Lists.newArrayList();
		Map<String, Object> result;
		for(int i = 0; i < receiver.size(); i++) {
			Map<String, Object> info = receiver.get(i);
			result = Maps.newHashMap();
			result.put("usr_apvlln_uuid", key);
			result.put("apvr_id", info.get("usr_id"));
			result.put("apvr_typ_ccd", info.get("rdg_typ_ccd"));
			resultList.add(result);
		}
		return resultList;
	}
}
