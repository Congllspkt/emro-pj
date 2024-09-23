package smartsuite.app.bp.approval.arbitrary.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
public class ArbitraryManagerRepository {


	@Inject
	private SqlSession sqlSession;


	private static final String NAMESPACE = "arbitrary-manage.";


	/**
	 * 전결규정 헤더를 등록한다.
	 *
	 */
	public void insertArbitraryHeader(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertArbitraryHeader", param);
	}

	/**
	 * 전결규정 상세테이블을 등록한다.
	 *
	 */
	public void insertArbitraryDetailTable(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertArbitraryDetailTable", param);
	}

	/**
	 * 전결규정 상세테이블을 복사한다.
	 *
	 */
	public void insertCopyArbitraryDetailTable(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertCopyArbitraryDetailTable", param);
	}

	/**
	 * 전결규정 상세항목을 등록한다.
	 *
	 */
	public void insertArbitraryDetailFactor(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertArbitraryDetailFactor", param);
	}

	/**
	 * 전결규정 상세항목을 복사한다.
	 *
	 */
	public void insertCopyArbitraryDetailFactor(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertCopyArbitraryDetailFactor", param);
	}

	/**
	 * 전결규정 전결결재선을 등록한다.
	 *
	 */
	public void insertArbitraryAprvLine(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertArbitraryAprvLine", param);
	}

	/**
	 * 전결규정 전결결재선을 복사한다.
	 *
	 */
	public void insertCopyArbitraryAprvLine(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertCopyArbitraryAprvLine", param);
	}

	/**
	 * 전결규정 전결결재선 상세를 등록한다.
	 *
	 */
	public void insertArbitraryAprvLineDetail(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertArbitraryAprvLineDetail", param);
	}

	/**
	 * 전결규정 전결결재선 상세를 복사한다.
	 *
	 */
	public void insertCopyArbitraryAprvLineDetail(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertCopyArbitraryAprvLineDetail", param);
	}

	/**
	 * 전결규정 전결결재선 조건을 등록한다.
	 *
	 */
	public void insertArbitraryAprvLineCondition(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertArbitraryAprvLineCondition", param);
	}

	/**
	 * 전결규정 전결결재선 조건을 복사한다.
	 *
	 */
	public void insertCopyArbitraryAprvLineCondition(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertCopyArbitraryAprvLineCondition", param);
	}


	public Map<String, Object> getArbitraryAprvType(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getArbitraryAprvType",param);
	}

	public void updateArbitraryAprvType(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateArbitraryAprvType",param);
	}

	public void insertArbitraryAprvType(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE+"insertArbitraryAprvType",param);
	}


	/**
	 * 전결규정 헤더를 수정한다.
	 *
	 */
	public void updateArbitraryHeader(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateArbitraryHeader", param);
	}

	/**
	 * 전결규정 헤더를 수정한다. - 적용종료일
	 *
	 */
	public void updateArbitraryHeaderApplyEd(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateArbitraryHeaderApplyEd", param);
	}

	/**
	 * 전결규정 상세테이블을 수정한다.
	 *
	 */
	public void updateArbitraryDetailTable(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateArbitraryDetailTable", param);
	}

	/**
	 * 전결규정 전결결재선을 수정한다.
	 *
	 */
	public void updateArbitraryAprvLine(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateArbitraryAprvLine", param);
	}

	/**
	 * 전결규정 전결결재선을 수정한다. - 기본결재선 여부 선택 해재
	 *
	 */
	public void updateArbitraryAprvLineBasAprvlnOff(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateArbitraryAprvLineBasAprvlnOff", param);
	}

	/**
	 * 전결규정 전결결재선 상세를 수정한다.
	 *
	 */
	public void updateArbitraryAprvLineDetail(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateArbitraryAprvLineDetail", param);
	}

	/**
	 * 전결규정 헤더를 삭제한다. - 상태값 수정 (sts -> 'D')
	 *
	 */
	public void deleteArbitraryHeader(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "deleteArbitraryHeader", param);
	}

	/**
	 * 전결규정 상세테이블을 삭제한다.
	 *
	 */
	public void deleteArbitraryDetailTable(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryDetailTable", param);
	}

	/**
	 * 전결규정 상세테이블 항목을 삭제한다. - by dapvl_tbl_id
	 *
	 */
	public void deleteArbitraryDetailFactorByTableId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryDetailFactorByTableId", param);
	}

	/**
	 * 전결규정 전결결재선을 삭제한다. - 개별
	 *
	 */
	public void deleteArbitraryAprvLine(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryAprvLine", param);
	}

	/**
	 * 전결규정 전결결재선을 삭제한다. - by dapvl_tbl_id
	 *
	 */
	public void deleteArbitraryAprvLineByTableId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryAprvLineByTableId", param);
	}

	/**
	 * 전결규정 전결결재선 상세를 삭제한다. - 개별
	 *
	 */
	public void deleteArbitraryAprvLineDetail(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryAprvLineDetail", param);
	}

	/**
	 * 전결규정 전결결재선 상세를 삭제한다. - by dapvl_apvlln_id
	 *
	 */
	public void deleteArbitraryAprvLineDetailByLineId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryAprvLineDetailByLineId", param);
	}

	/**
	 * 전결규정 전결결재선 상세를 삭제한다. - by dapvl_tbl_id
	 *
	 */
	public void deleteArbitraryAprvLineDetailByTableId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryAprvLineDetailByTableId", param);
	}

	/**
	 * 전결규정 전결결재선 조건을 삭제한다. - 개별
	 *
	 */
	public void deleteArbitraryAprvLineCondition(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryAprvLineCondition", param);
	}

	/**
	 * 전결규정 전결결재선 조건을 삭제한다. - by dapvl_apvlln_id
	 *
	 */
	public void deleteArbitraryAprvLineConditionByLineId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryAprvLineConditionByLineId", param);
	}

	/**
	 * 전결규정 전결결재선 조건을 삭제한다. - by dapvl_tbl_id
	 *
	 */
	public void deleteArbitraryAprvLineConditionByTableId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteArbitraryAprvLineConditionByTableId", param);
	}

	/**
	 * 전결규정 결재유형 목록을 조회한다.
	 *
	 */
	public FloaterStream selectListArbitraryAprvType(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListArbitraryAprvType", param);
	}

	/**
	 * 전결규정 헤더 - 가능한 적용 시작 일자를 조회한다.
	 *
	 */
	public Map<String, Object> selectArbitraryHeaderAvailApplySd(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectArbitraryHeaderAvailApplySd", param);
	}

	/**
	 * 전결규정 헤더 정보를 조회한다.
	 *
	 */
	public Map<String, Object> selectArbitraryHeader(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectArbitraryHeader", param);
	}



	/**
	 * 전결규정 헤더 목록을 조회한다.
	 *
	 */
	public FloaterStream selectListArbitraryHeader(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListArbitraryHeader", param);
	}

	/**
	 * 전결규정 상세 테이블 목록을 조회한다.
	 *
	 */
	public List<Map<String, Object>> selectListArbitraryDetailTable(Map<String, Object> param) {
		//return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListArbitraryDetailTable", param);
		return sqlSession.selectList(NAMESPACE + "selectListArbitraryDetailTable", param);
	}

	/**
	 * 전결규정 상세 테이블 항목 목록을 조회한다.
	 *
	 */
	public FloaterStream selectListArbitraryDetailFactor(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListArbitraryDetailFactor", param);
	}

	/**
	 * 전결규정 헤더 정보를 조회한다. - by efct_exp_dt
	 *
	 */
	public Map<String, Object> selectArbitraryHeaderByApplyEd(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectArbitraryHeaderByApplyEd", param);
	}

	/**
	 * 전결규정 전결결재선 정보를 조회한다.
	 *
	 */
	public Map<String, Object> selectArbitraryAprvLine(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectArbitraryAprvLine", param);
	}

	/**
	 * 전결규정 전결결재선 목록을 조회한다.
	 *
	 */
	public List<Map<String, Object>> selectListArbitraryAprvLineByHeaderId(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectListArbitraryAprvLineByHeaderId", param);
	}

	/**
	 * 전결규정 전결결재선 목록을 조회한다.
	 *
	 */
	public List<Map<String, Object>> selectListArbitraryAprvLineWithDetail(Map<String, Object> param) {
		//return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListArbitraryAprvLineWithDetail", param);
		return sqlSession.selectList(NAMESPACE + "selectListArbitraryAprvLineWithDetail", param);
	}

	/**
	 * 전결규정 전결결재선 상세 목록을 조회한다.
	 *
	 */
	public FloaterStream selectListArbitraryAprvLineDetail(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListArbitraryAprvLineDetail", param);
	}

	/**
	 * 전결규정 전결결재선 조건 목록을 조회한다.
	 *
	 */
	public List<Map<String, Object>> selectListArbitraryAprvLineCondition(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectListArbitraryAprvLineCondition", param);
	}

	/**
	 * 전결규정 전결결재선 조건 목록을 조회한다. - 조건 추가시 중복 검사
	 *
	 */
	public List<Map<String, Object>> selectListCountArbitraryAprvLineCondition(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectListCountArbitraryAprvLineCondition", param);
	}


	public List<Map<String, Object>> selectListArbitrayTable(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectListArbitrayTable", param);
	}

	public List<Map<String, Object>> selectListArbitrayTableFactor(Map<String, Object> table) {
		return sqlSession.selectList(NAMESPACE + "selectListArbitrayTableFactor", table);
	}

	public List<Map<String, Object>> selectListArbitrayApprovalLines(Map<String, Object> conditions) {
		return sqlSession.selectList(NAMESPACE + "selectListArbitrayApprovalLines", conditions);
	}

	public List<Map<String, Object>> selectListDefaultArbitrayApprovalLines(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectListDefaultArbitrayApprovalLines",param);
	}

	public List<Map<String, Object>> selectListArbitrayApproverByDept(Map<String, Object> approverParam) {
		return sqlSession.selectList(NAMESPACE + "selectListArbitrayApproverByDept", approverParam);
	}

	public List<Map<String, Object>> selectListArbitrayApproverByUser(Map<String, Object> approverParam) {
		return sqlSession.selectList(NAMESPACE + "selectListArbitrayApproverByUser", approverParam);
	}

	/**
	 * 결재유형의 전결적용여부를 조회한다.
	 *
	 */
	public Map<String, Object> selectArbitrayYnByAprvType(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectArbitrayYnByAprvType", param);
	}


	/**
	 * 전결결재선 기본결재선 목록을 조회한다.
	 *
	 */
	public List<Map<String, Object>> selectListApplyDefaultLineByAprvType(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectListApplyDefaultLineByAprvType", param);
	}

	/**
	 * 결재유형의 전결항목 목록을 조회한다.
	 *
	 */
	public List<Map<String, Object>> selectListApplyFactorByAprvType(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectListApplyFactorByAprvType", param);
	}

	/**
	 * 결재유형의 전결항목 목록을 조회한다.
	 *
	 */
	public List<Map<String, Object>> selectListApplyMatchLineByCondition(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "selectListApplyMatchLineByCondition", param);
	}

}
