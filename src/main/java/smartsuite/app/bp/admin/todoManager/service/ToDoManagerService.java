package smartsuite.app.bp.admin.todoManager.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.todoManager.event.ToDoManagerEventPublisher;
import smartsuite.app.bp.admin.todoManager.repository.ToDoManagerRepository;
import smartsuite.app.common.shared.ResultMap;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 할일 관리 Service
 */
@SuppressWarnings("unchecked")
@Service
@Transactional
public class ToDoManagerService {

	@Inject
	ToDoManagerRepository toDoManagerRepository;

	@Inject
	ToDoManagerEventPublisher toDoManagerEventPublisher;
	

	/**
	 * 할일 그룹 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoGroup(Map<String, Object> param) {
		return toDoManagerRepository.findListTodoGroup(param);
	}
	
	/**
	 * 할일 그룹 목록 저장
	 * @param param
	 * @return
	 */
	public Map<String, Object> saveListTodoGroup(Map<String, Object> param) {
		// to-do 그룹 리스트 추가
		List<Map<String, Object>> insertTodoGroupList = (List<Map<String, Object>>)param.getOrDefault("insertList", Lists.newArrayList());
		this.insertListTodoGroup(insertTodoGroupList);
		
		// to-do 그룹 리스트 수정
		List<Map<String, Object>> updateTodoGroupList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		this.updateListTodoGroup(updateTodoGroupList);
		
		Map<String, Object> resultMap = Maps.newHashMap();
		return resultMap;
	}

	/**
	 * 할일 그룹 목록 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListTodoGroupRequest(Map<String, Object> param) {
		// 삭제
		List<Map<String, Object>> deleteTodoGroupList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		this.deleteListTodoGroup(deleteTodoGroupList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 할일 그룹 목록 삭제
	 * @param deleteTodoGroupList
	 */
	public void deleteListTodoGroup(List<Map<String, Object>> deleteTodoGroupList) {
		for(Map<String, Object> todoInfo : deleteTodoGroupList) {
			this.deleteTodoUser(todoInfo);
			this.deleteTodoFactor(todoInfo);
			this.deleteTodoGroup(todoInfo);
		}
	}


	/**
	 * 할일 항목 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoFactor(Map<String, Object> param) {
		return toDoManagerRepository.findListTodoFactor(param);
	}
	
	/**
	 * 할일 항목 목록 저장
	 * @param param
	 * @return
	 */
	public ResultMap saveListTodoFactor(Map<String, Object> param) {
		// 신규
		List<Map<String, Object>> insertTodoList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		this.insertListToDoFactor(insertTodoList);
		
		// 수정
		List<Map<String, Object>> updateTodoList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		this.updateListTodoFactor(updateTodoList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 할일 항목 리스트 수정
	 * @param updateTodoList
	 */
	public void updateListTodoFactor(List<Map<String, Object>> updateTodoList) {
		for(Map<String, Object> todoInfo : updateTodoList) {
			this.updateTodoFactor(todoInfo);
		}
	}


	/**
	 * 할일 항목 리스트 추가
	 * @param insertTodoList
	 */
	public void insertListToDoFactor(List<Map<String, Object>> insertTodoList) {
		for(Map<String, Object> todoInfo : insertTodoList) {
			todoInfo.put("todoitem_uuid", UUID.randomUUID().toString());
			this.insertTodoFactor(todoInfo);
		}
	}


	/**
	 * 할일 항목 목록 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListTodoFactorRequest(Map<String, Object> param) {
		// 삭제
		List<Map<String, Object>> deleteTodoFactorList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		this.deleteListTodoFactor(deleteTodoFactorList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 할일 항목 목록 삭제
	 * @param deleteTodoFactorList
	 */
	public void deleteListTodoFactor(List<Map<String, Object>> deleteTodoFactorList) {
		for(Map<String, Object> todoInfo : deleteTodoFactorList) {
			this.deleteTodoUser(todoInfo);
			this.deleteTodoFactor(todoInfo);
		}
	}

	/**
	 * 할일 항목 별 사용자 목록 저장
	 * @param param
	 * @return
	 */
	public ResultMap insertListTodoUser(Map<String, Object> param) {
		// 신규
		List<Map<String, Object>> insertTodoList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		for(Map<String, Object> todoInfo : insertTodoList) {
			this.insertTodoUser(todoInfo);
		}
		return ResultMap.SUCCESS();
	}

	/**
	 * 할일 항목 별 사용자 목록 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListTodoUserRequest(Map<String, Object> param) {
		// 삭제
		List<Map<String, Object>> deleteTodoUserList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		this.deleteListTodoUserList(deleteTodoUserList);
		return ResultMap.SUCCESS();
	}

	/**
	 * 할일 항목 별 사용자 목록 삭제
	 * @param deleteTodoUserList
	 */
	public void deleteListTodoUserList(List<Map<String, Object>> deleteTodoUserList) {
		for(Map<String, Object> todoInfo : deleteTodoUserList) {
			this.deleteTodoUser(todoInfo);
		}
	}

	
	/**
	 * 할일 사용자 별 항목 저장/삭제
	 * @param param
	 * @return
	 */
	public ResultMap saveListTodoFactorByUser(Map<String, Object> param) {
		List<String> insertFactIdList = (List<String>) param.getOrDefault("insertFactIds",Lists.newArrayList());
		List<String> deleteFactIdList = (List<String>) param.getOrDefault("deleteFactIds",Lists.newArrayList());
		List<String> factIdList = Lists.newArrayList();

		if(insertFactIdList.size() > 0) { // 보여질 항목 신규 저장
			param.put("todoitem_uuids", insertFactIdList);
			List<Map<String,Object>> findListTodoUserByCurrentUserList = this.findListTodoUserByCurrentUser(param);

			if(findListTodoUserByCurrentUserList == null || findListTodoUserByCurrentUserList.size() == 0){
				factIdList = insertFactIdList;
			}else{
				for(Map<String, Object> todoUser: findListTodoUserByCurrentUserList){
					String factId = todoUser.getOrDefault("todoitem_uuid","") == null? "" : (String) todoUser.getOrDefault("todoitem_uuid","");
					// fact id 존재여부 확인
					if(!insertFactIdList.contains(factId)) factIdList.add(factId);
				}
			}

			// 존재하지 않는 fact id만 insert
			for(String insertFactId : factIdList){
				param.put("todoitem_uuid", insertFactId);
				this.insertTodoUserByCurrentUser(param);
			}
		}

		if (deleteFactIdList.size() > 0){ // 보여지지 않을 항목 삭제
			param.put("todoitem_uuids", deleteFactIdList);
			this.deleteTodoUserByCurrentUser(param);
		}

		return ResultMap.SUCCESS();
	}


	/**
	 * to-do 그룹 리스트 수정
	 * @param updateTodoGroupList
	 */
	public void updateListTodoGroup(List<Map<String, Object>> updateTodoGroupList) {
		for(Map<String, Object> todoInfo : updateTodoGroupList) {
			this.updateTodoGroup(todoInfo);
		}
	}

	/**
	 * to-do 그룹 수정
	 * @param todoInfo
	 */
	public void updateTodoGroup(Map<String, Object> todoInfo) {
		toDoManagerRepository.updateTodoGroup(todoInfo);
	}

	/**
	 * to-do 그룹 리스트 추가
	 * @param insertTodoGroupList
	 */
	public void insertListTodoGroup(List<Map<String, Object>> insertTodoGroupList) {
		for(Map<String, Object> todoInfo : insertTodoGroupList) {
			this.insertTodoGroup(todoInfo);
		}
	}

	/**
	 * to-do 그룹 추가
	 * @param todoInfo
	 */
	public void insertTodoGroup(Map<String, Object> todoInfo) {
		toDoManagerRepository.insertTodoGroup(todoInfo);
	}

	/**
	 * 할일 그룹 삭제
	 * @param todoInfo
	 */
	public void deleteTodoGroup(Map<String, Object> todoInfo) {
		toDoManagerRepository.deleteTodoGroup(todoInfo);
	}

	/**
	 * 할일 항목 삭제
	 * @param todoInfo
	 */
	public void deleteTodoFactor(Map<String, Object> todoInfo) {
		toDoManagerRepository.deleteTodoFactor(todoInfo);
	}

	/**
	 * 할일 항목 별 사용자 삭제
	 * @param todoInfo
	 */
	public void deleteTodoUser(Map<String, Object> todoInfo) {
		toDoManagerRepository.deleteTodoUser(todoInfo);
	}

	/**
	 *  할일 항목 추가
	 * @param todoInfo
	 */
	public void insertTodoFactor(Map<String, Object> todoInfo) {
		toDoManagerRepository.insertTodoFactor(todoInfo);
	}

	/**
	 * 할일 항목 수정
	 * @param todoInfo
	 */
	public void updateTodoFactor(Map<String, Object> todoInfo) {
		toDoManagerRepository.updateTodoFactor(todoInfo);
	}

	/**
	 * 할일 항목 별 사용자 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoUser(Map<String, Object> param) {
		String userTypeCode = param.getOrDefault("usr_typ_ccd","") == null ? "" : (String) param.getOrDefault("usr_typ_ccd","");

		if(("VD").equals(userTypeCode)){
			return toDoManagerEventPublisher.findListTodoUserSupplier(param);
		}

		return toDoManagerRepository.findListTodoUser(param);
	}


	/**
	 *  할일 항목별 사용자 추가
	 * @param todoInfo
	 */
	public void insertTodoUser(Map<String, Object> todoInfo) {
		toDoManagerRepository.insertTodoUser(todoInfo);
	}

	/**
	 * 할일 사용자 별 항목 목록 조회 ( 조회 조건 현재 접속 사용자 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoFactorByCurrentUser(Map<String, Object> param) {
		return toDoManagerRepository.findListTodoFactorByCurrentUser(param);
	}

	/**
	 * 할일 항목 별 사용자에 현재 사용자 삭제
	 * @param param
	 */
	public void deleteTodoUserByCurrentUser(Map<String, Object> param) {
		toDoManagerRepository.deleteTodoUserByCurrentUser(param);
	}

	/**
	 * 할일 항목 별 사용자에 현재 사용자 신규 추가
	 * @param param
	 */
	public void insertTodoUserByCurrentUser(Map<String, Object> param) {
		toDoManagerRepository.insertTodoUserByCurrentUser(param);
	}

	/**
	 * 사용자 to-do 리스트 조회 ( 조회조건 현재 접속 사용자 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoUserByCurrentUser(Map<String, Object> param) {
		return toDoManagerRepository.findListTodoUserByCurrentUser(param);
	}
}
