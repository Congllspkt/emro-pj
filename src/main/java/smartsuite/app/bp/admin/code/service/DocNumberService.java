package smartsuite.app.bp.admin.code.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.code.repository.DocNumberRepository;
import smartsuite.app.common.shared.ResultMap;

/**
 * DocNumber 관련 처리하는 서비스 Class입니다.
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class DocNumberService {
	
	@Inject
	DocNumberRepository docNumberRepository;

	/**
	 * 문서번호 목록 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findListDocNumber(Map<String, Object> param) {
		return docNumberRepository.findListDocNumber(param);
	}
	
	/**
	 * 문서번호를 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 3
	 * @Method Name : saveListDocNumber
	 */
	public ResultMap saveListDocNumber(Map<String, Object> param){
		List<Map<String, Object>> updateDocNumberList = (List<Map<String, Object>>)param.getOrDefault("updateList", Lists.newArrayList());
		List<Map<String, Object>> insertDocNumberList = (List<Map<String, Object>>)param.getOrDefault("insertList", Lists.newArrayList());
		
		// UPDATE
		this.updateListDocNumber(updateDocNumberList);

		// INSERT
		this.insertListDocNumber(insertDocNumberList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 문서 번호 리스트 채번
	 * @param insertDocNumberList
	 */
	public void insertListDocNumber(List<Map<String, Object>> insertDocNumberList) {
		for(Map docNumber : insertDocNumberList){
			this.insertDocNumber(docNumber);
		}
	}

	/**
	 * 문서 채번
	 * @param docNumber
	 */
	public void insertDocNumber(Map docNumber) {
		docNumberRepository.insertDocNumber(docNumber);
	}

	/**
	 * 문서 채번 번호 리스트 수정
	 * @param updateDocNumberList
	 */
	public void updateListDocNumber(List<Map<String, Object>> updateDocNumberList) {
		for(Map docNumber : updateDocNumberList){
			this.updateDocumentNumber(docNumber);
		}
	}

	/**
	 * 문서 채번 번호 수정
	 * @param docNumber
	 */
	public void updateDocumentNumber(Map docNumber) {
		docNumberRepository.updateDocumentNumber(docNumber);
	}

	/**
	 * 문서 채번 리스트 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListDocNumberRequest(Map<String, Object> param){
		List<Map<String, Object>> deleteDocNumberList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		// 문서 채번 리스트 삭제
		this.deleteListDocNumber(deleteDocNumberList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 문서 채번 리스트 삭제
	 * @param deleteDocNumberList
	 */
	public void deleteListDocNumber(List<Map<String, Object>> deleteDocNumberList) {
		for(Map docNumber : deleteDocNumberList){
			this.deleteDocNumber(docNumber);
		}
	}

	/**
	 * 문서 채번 삭제
	 * @param docNumber
	 */
	public void deleteDocNumber(Map docNumber) {
		docNumberRepository.deleteDocNumber(docNumber);
	}
}
