package smartsuite.app.bp.admin.code.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.code.repository.MultiLangMgtRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.messagesource.core.entity.SimpleMessage;
import smartsuite.messagesource.core.persist.MessagePersister;
import smartsuite.messagesource.web.MessageService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * MultiLangMgt 관련 처리하는 서비스 Class입니다.
 *
 * @author JuEung Kim
 * @see
 * @FileName MultiLangMgtService.java
 * @package smartsuite.app.bp.admin.code
 * @Since 2016. 6. 15
 * @변경이력 : [2016. 6. 15] JuEung Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class MultiLangMgtService {
	

	@Inject
	private MessageService messageService;
	
	@Inject
	MultiLangMgtRepository multiLangMgtRepository;
	
	@Inject
	MessagePersister messagePersister;

	/**
	 * 다국어관리 목록을 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findListMultiLang(Map<String, Object> param) {
		return multiLangMgtRepository.findListMultiLang(param);
	}

	/**
	 * 다국어관리 목록을 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 6. 15
	 * @Method Name : saveListMultiLang
	 */
	public ResultMap saveListMultiLang(Map<String, Object> param){
		List<Map<String, Object>> updateMultiLangList = (List<Map<String, Object>>)param.getOrDefault("updateList", Lists.newArrayList());
		List<Map<String, Object>> insertMultiLangList = (List<Map<String, Object>>)param.getOrDefault("insertList", Lists.newArrayList());

		// 다국어 관리 리스트 수정
		//this.updateListMultiLang(updateMultiLangList);
		
		// 다국어 관리 리스트 추가
		//this.insertListMultiLang(insertMultiLangList);
		
		//messageService.refreshByMultlangModDttm();
		
		// 임시방편 적용 http://alm.emro.co.kr/browse/NGTFT-370
		//TODO R&D 작업 완료 후 해당 내용 제거 필요
		if(updateMultiLangList != null && updateMultiLangList.size() > 0) {
			List updateListMessage = new ArrayList();
			for(Map updateMultiLang : updateMultiLangList) {
				updateListMessage.add(new SimpleMessage(updateMultiLang));
			}
			messagePersister.updateList(updateListMessage);
		}
		if(insertMultiLangList != null && insertMultiLangList.size() > 0) {
			List updateListMessage = new ArrayList();
			List insertListMessage = new ArrayList();
			for(Map insertMultiLang : insertMultiLangList) {
				String multiLangCode = insertMultiLang.get("multlang_ccd") == null? "ko_KR" :(String) insertMultiLang.get("multlang_ccd");
				Locale multiLangCcd = LocaleUtils.toLocale(multiLangCode);
				if(messagePersister.exists(multiLangCcd, (String) insertMultiLang.get("multlang_key"))) {
					updateListMessage.add(new SimpleMessage(insertMultiLang));
				} else {
					insertListMessage.add(new SimpleMessage(insertMultiLang));
				}
			}

			if(updateListMessage.size() > 0) messagePersister.updateList(updateListMessage);
			if(insertListMessage.size() > 0) messagePersister.createList(insertListMessage);
		}
		messageService.refreshAll();
		// ------------------------- 임시조치 ---------------------------------
		
		return ResultMap.SUCCESS();
	}

	/**
	 * 다국어 관리 리스트 추가
	 * @param insertMultiLangList
	 */
	public void insertListMultiLang(List<Map<String, Object>> insertMultiLangList) {
		for(Map multiLang : insertMultiLangList){
			this.insertMultiLang(multiLang);
		}
	}

	/**
	 * 다국어 관리 추가
	 * @param multiLang
	 */
	public void insertMultiLang(Map multiLang) {
		multiLangMgtRepository.insertMultiLang(multiLang);
	}

	/**
	 * 다국어 관리 리스트 수정
	 * @param updateMultiLangList
	 */
	public void updateListMultiLang(List<Map<String, Object>> updateMultiLangList) {
		for(Map multiLang : updateMultiLangList){
			this.updateMultiLang(multiLang);
		}
	}

	/**
	 * 다국어 관리 수정
	 * @param multiLang
	 */
	public void updateMultiLang(Map multiLang) {
		multiLangMgtRepository.updateMultiLang(multiLang);
	}

	/**
	 * 다국어관리 목록을 삭제요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListMultiLangRequest(Map<String, Object> param){
		List<Map<String, Object>> deleteMultiLangList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());

		// 다국어 관리 삭제
		this.deleteListMultiLang(deleteMultiLangList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 다국어 관리 목록 삭제
	 * @param deleteMultiLangList
	 */
	public void deleteListMultiLang(List<Map<String, Object>> deleteMultiLangList) {
		for(Map multiLang : deleteMultiLangList){
			this.deleteMultiLang(multiLang);
		}
		messageService.refreshByMultlangModDttm();
	}

	/**
	 * 다국어 관리 삭제
	 * @param multiLang
	 */
	public void deleteMultiLang(Map multiLang) {
		multiLangMgtRepository.deleteMultiLang(multiLang);
		
	}

	/**
	 *  다국어관리 message 목록을 조회한다
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findMessageList(Map<String, Object> param) {
		return multiLangMgtRepository.findMessageList(param);
	}
	
	/**
	 * 다국어관리 message 목록을 저장한다.
	 * @param param
	 * @return
	 */
	public Map saveMessageList(Map<String, Object> param) {
		List<Map<String, Object>> messageList = (List<Map<String, Object>>)param.getOrDefault("messageList",Lists.newArrayList());

		for(Map message : messageList){
			/*if(this.existMessage(message)){
				this.updateMultiLang(message);
			}else{
				this.insertMultiLang(message);
			}*/
			//TODO R&D 작업 완료 후 해당 내용 제거 필요
			Locale multlangCcd = LocaleUtils.toLocale((String) message.get("multlang_ccd"));
			if( messagePersister.exists(multlangCcd, (String) message.get("multlang_key"), (String) message.get("multlang_type")) ) {
				List updateListMessage = new ArrayList();
				updateListMessage.add(new SimpleMessage(message));
				messagePersister.updateList(updateListMessage);
			} else {
				List insertListMessage = new ArrayList();
				insertListMessage.add(new SimpleMessage(message));
				messagePersister.createList(insertListMessage);
			}
		}
		//messageService.refreshByMultlangModDttm();
		messageService.refreshAll();
		return Maps.newHashMap();
	}

	/**
	 * 다국어 관리 message 존재 여부
	 * @param message
	 * @return
	 */
	public boolean existMessage(Map<String,Object> message){
		int getMessageCount = multiLangMgtRepository.getMessageCount(message);
		return (getMessageCount > 0);
	}
}
