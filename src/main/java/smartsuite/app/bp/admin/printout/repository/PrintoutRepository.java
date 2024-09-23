package smartsuite.app.bp.admin.printout.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PrintoutRepository {
	
	@Inject
	private SqlSession sqlSession;
	
	private static final String NAMESPACE = "printout.";
	
	/**
	 * 문서 출력 관리 (DOC_OUTP_MGMT) 정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListDocumentOutputManagement(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListDocumentOutputManagement", param);
	}
	
	/**
	 * 문서 출력 관리 (DOC_OUTP_MGMT) 정보 UPDATE
	 * @param param
	 */
	public void updateListDocumentOutputManagement(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateListDocumentOutputManagement", param);
	}
	
	/**
	 * 문서 출력 관리 (DOC_OUTP_MGMT) 정보 INSERT
	 * @param param
	 */
	public void insertListDocumentOutputManagement(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE+"insertListDocumentOutputManagement", param);
	}
	
	/**
	 * 문서 출력 관리 (DOC_OUTP_MGMT) 정보 DELETE
	 * @param param
	 */
	public void deleteDocumentOutputManagment(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteDocumentOutputManagment", param);
	}
	
	/**
	 * 문서 출력 데이터 설정 (DOC_OUTP_DAT_SETUP) 정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListDocumentOutputSetup(Map<String, Object> param){
		return sqlSession.selectList(NAMESPACE+"findListDocumentOutputSetup", param);
	}
	
	/**
	 * 문서 출력 데이터 설정 (DOC_OUTP_DAT_SETUP) 정보 UPDATE
	 * @param param
	 */
	public void updateListDocumentOutputSetup(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateListDocumentOutputSetup", param);
	}
	
	/**
	 * 문서 출력 데이터 설정 (DOC_OUTP_DAT_SETUP) 정보 INSERT
	 * @param param
	 */
	public void insertListDocumentOutputSetup(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE+"insertListDocumentOutputSetup", param);
	}
	
	/**
	 * 문서 출력 데이터 설정 (DOC_OUTP_DAT_SETUP) 정보 DELETE
	 * @param param
	 */
	public void deleteDocumentOutputDataSetup(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteDocumentOutputDataSetup", param);
	}

	/**
	 * 문서 출력물 데이터 설정 파라미터(ESADTPR) 정보 DELETE BY MGMT
	 * @param param
	 */
	public void deleteDocumentOutputDataSetupByManagement(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteDocumentOutputDataSetupByManagement", param);
	}
	
	/**
	 * 문서 출력 데이터 파라미터(DOC_OUTP_DAT_PARM) 정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListDocumentOutputDataParam(Map<String, Object> param){
		return sqlSession.selectList(NAMESPACE+"findListDocumentOutputDataParam", param);
	}
	
	/**
	 * 문서 출력 데이터 파라미터(DOC_OUTP_DAT_PARM) 정보 조회
	 * @param param
	 */
	public void updateListDocumentOutputDataParam(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateListDocumentOutputDataParam", param);
	}
	
	/**
	 * 문서 출력 데이터 파라미터(DOC_OUTP_DAT_PARM) 정보 INSERT
	 * @param param
	 */
	public void insertListDocumentOutputDataParam(Map<String, Object> param){
		sqlSession.insert(NAMESPACE+"insertListDocumentOutputDataParam", param);
	}
	
	/**
	 * 문서 출력 데이터 파라미터(DOC_OUTP_DAT_PARM) 정보 DELETE
	 * @param param
	 */
	public void deleteDocumentOutputDataParam(Map<String, Object> param){
		sqlSession.delete(NAMESPACE+"deleteDocumentOutputDataParam", param);
	}
	
	public void deleteDocumentOutputDataParamByDataSetupUuid(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteDocumentOutputDataParamByDataSetupUuid", param);
	}
	
	public void deleteDocumentOutputDataParamByManagement(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteDocumentOutputDataParamByManagement", param);
	}
	
	public Map<String, Object> findDocumentOutputManagement(Map<String, Object> param){
		return sqlSession.selectOne(NAMESPACE+"findListDocumentOutputManagement", param);
	}
}
