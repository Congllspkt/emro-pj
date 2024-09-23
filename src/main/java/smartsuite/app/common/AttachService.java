package smartsuite.app.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.ibatis.session.SqlSession;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.common.shared.Const;

/**
 * 첨부파일 관련 처리하는 서비스 Class입니다.
 *
 * @author JongKyu Kim
 * @see
 * @FileName
 * @package smartsuite.app.shared
 * @Since 2016. 2. 2
 * @변경이력 : [2016. 2. 2] JongKyu Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class AttachService {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE = "attach.";

	/**
	 * 첨부파일을 등록한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : insertAttach
	 */
	public void insertAttach(Map<String, Object> param) {
		param.put("athf_uuid", UUID.randomUUID().toString());
		sqlSession.insert(NAMESPACE + "insertAttach", param);
	}

	/**
	 * 첨부파일을 삭제한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteAttach
	 */
	public void deleteAttach(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteAttach", param);
	}

	/**
	 * 첨부파일 목록을 조회한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the list attach
	 * @Date : 2016. 2. 2
	 * @Method Name : findListAttach
	 */
	public List<Map<String, Object>> findListAttach(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListAttach", param);
	}

	/**
	 * 첨부파일 상세정보를 조회한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the attach
	 * @Date : 2016. 2. 2
	 * @Method Name : findAttach
	 */
	public Map<String, Object> findAttach(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findAttach", param);
	}

	/**
	 * 첨부파일 상세정보를 조회한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the attach
	 * @Date : 2016. 8. 11
	 * @Method Name : findAttachByFilePath
	 */
	public Map<String, Object> findAttachByFilePath(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findAttachByFilePath", param);
	}

	/**
	 * 첨부파일 목록을 등록/삭제한다.
	 *
	 * @author : JongKyu Kim
	 * @param param {"insertAttachs", "deleteAttachs"}
	 * @return the map< string, object>
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListAttach
	 */
	/*public Map<String, Object> saveListAttach(Map<String, Object> param,StandardPBEStringEncryptor encryptor) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, Object>> inserts = (List<Map<String, Object>>)param.get("insertAttachs");
		List<Map<String, Object>> deletes = (List<Map<String, Object>>)param.get("deleteAttachs");

		if (inserts != null && !inserts.isEmpty()) {
			for (Map<String, Object> row : inserts) {
				this.insertAttach(row);
			}
		}
		List<Map<String, Object>> deletedAttach = Lists.newArrayList();
		if (deletes != null && !deletes.isEmpty()) {
			for (Map<String, Object> row : deletes) {
				if(row.containsKey("athf_uuid")){
					String attId = encryptor.decrypt((String)row.get("athf_uuid"));
					row.remove("athf_uuid");
					row.put("athf_uuid", attId);
					deletedAttach.add(this.findAttach(row));
					this.deleteAttach(row);
				}
			}
		}
		resultMap.put(Const.RESULT_STATUS, Const.SUCCESS);
		resultMap.put("deletedAttach", deletedAttach);
		return resultMap;
	}*/
	
	/**
	 * 첨부파일 복사
	 *
	 * @param grpCd: 복사대상 첨부파일 그룹 키
	 * @return newGrpCd : 복사된 첨부파일 그룹 키
	 */
	/*public String copyListAttach(String grpCd) {
		if(grpCd == null) {
			return null;
		}
		
		Map<String, Object> param = Maps.newHashMap();
		param.put("athg_uuid", grpCd);
		
		String newGrpCd = UUID.randomUUID().toString();
		
		List<Map<String, Object>> attachList = findListAttach(param);
		if(!attachList.isEmpty()) {
			int ord = 0;
			for(Map<String, Object> attach : attachList) {
				attach.put("new_att_id", UUID.randomUUID().toString());
				attach.put("new_grp_cd", newGrpCd);
				attach.put("athg_uuid"    , grpCd);
				attach.put("sort"  , ++ord);
				
				sqlSession.insert(NAMESPACE + "copyAttach", attach);
			}
		}
		return newGrpCd;
	}*/
	
	/**
	 * 첨부파일 상세정보를 조회한다.
	 *
	 * @author : daesung lee
	 * @param param the param
	 * @return the attach
	 * @Date : 2019. 3. 29
	 * @Method Name : findAttachByAttId
	 */
	public Map<String, Object> findAttachByAttId(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findAttachByAttId", param);
	}

	/**
	 * 첨부파일 상세정보를 조회한다.
	 *
	 * @author : daesung lee
	 * @param param the param
	 */
	public List<Map<String, Object>> findListByIds(List<String> param) {
		return sqlSession.selectList(NAMESPACE + "findListByIds", param);
	}

	/**
	 * 첨부파일 Blob 업데이트
	 *
	 * @param : Map (athf_orig_dt, athf_uuid)
	 * @return : void
	 */
	public void updateCertFileInfo(Map param) {
		sqlSession.update(NAMESPACE + "updateCertFileInfo", param);
	}


}
