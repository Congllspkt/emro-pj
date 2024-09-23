package smartsuite.app.bp.approval.arbitrary.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import smartsuite.app.common.shared.service.SharedService;
import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

/**
 * 전결테이블 관련 처리하는 서비스 Class입니다.
 *
 * @Since 2019.02.20
 */
@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ArbitraryTableService {

	@Inject
	private SqlSession sqlSession;

	@Inject
	private SharedService sharedService;

	private static final String NAMESPACE = "arbitrary-table.";
	
	/**
	 * 전결테이블을 등록한다.
	 *
	 */
	public void insertTable(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertTable", param);
	}

	/**
	 * 전결테이블 항목을 등록한다.
	 *
	 */
	public void insertTableFactor(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertTableFactor", param);
	}
	
	/**
	 * 전결테이블을 수정한다.
	 *
	 */
	public void updateTable(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateTable", param);
	}
	
	/**
	 * 전결테이블을 항목을 수정한다.
	 *
	 */
	public void updateTableFactor(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateTableFactor", param);
	}
	
	/**
	 * 전결테이블을 삭제한다.
	 *
	 */
	public void deleteTable(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteTable", param);
	}
	
	/**
	 * 전결테이블을 항목을 삭제한다.
	 *
	 */
	public void deleteTableFactor(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteTableFactor", param);
	}
	
	/**
	 * 전결테이블을 항목을 삭제한다.
	 *
	 */
	public void deleteTableFactorByTbId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteTableFactorByTbId", param);
	}
	
	/**
	 * 전결테이블 목록을 조회한다.
	 *
	 */
	public FloaterStream selectListTable(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListTable", param);
	}
	
	/**
	 * 전결테이블항목 목록을 조회한다.
	 *
	 */
	public FloaterStream selectListTableFactor(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListTableFactor", param);
	}
	
	/**
	 * 결재유형 목록을 조회한다.
	 *
	 */
	public FloaterStream selectListTableAprvType(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListTableAprvType", param);
	}
	
	/**
	 * 전결테이블 목록을 저장(신규등록/수정)한다.
	 *
	 */
	public Map<String, Object> saveListTable(Map<String, Object> param) {
		List<Map<String, Object>> insertTables = (List<Map<String, Object>>)param.get("insertTables");
		List<Map<String, Object>> updateTables = (List<Map<String, Object>>)param.get("updateTables");
		List<Map<String, Object>> insertList = Lists.newArrayList();
		List<Map<String, Object>> updateList = Lists.newArrayList();
		final String TABLE_ID_KEY = "dapvl_tbl_id";
		
		for (Map<String, Object> data : insertTables) {
			String tableId = sharedService.generateDocumentNumber("ABDSN_TABLE_ID").replaceAll("\\{0\\}", (String)data.get("apvl_typ_ccd")); // 문서번호
			data.put(TABLE_ID_KEY, tableId);
			insertList.add(data);
		}
		for (Map<String, Object> data : updateTables) {
			updateList.add(data);
		}

		for (Map<String, Object> data : insertList) {
			this.insertTable(data);
		}
		for (Map<String, Object> data : updateList) {
			this.updateTable(data);
		}
		
		Map<String, Object> resultMap = Maps.newHashMap();
		return resultMap;
	}
	
	/**
	 * 전결테이블 항목 목록을 저장(신규등록/수정)한다.
	 *
	 */
	public Map<String, Object> saveListTableFactor(Map<String, Object> param) {
		List<Map<String, Object>> insertTableFactors = (List<Map<String, Object>>)param.get("insertTableFactors");
		List<Map<String, Object>> updateTableFactors = (List<Map<String, Object>>)param.get("updateTableFactors");
		List<Map<String, Object>> insertList = Lists.newArrayList();
		List<Map<String, Object>> updateList = Lists.newArrayList();
		
		for (Map<String, Object> data : insertTableFactors) {
			insertList.add(data);
		}
		for (Map<String, Object> data : updateTableFactors) {
			updateList.add(data);
		}
		
		for (Map<String, Object> data : insertList) {
			this.insertTableFactor(data);
		}
		for (Map<String, Object> data : updateList) {
			this.updateTableFactor(data);
		}
		
		Map<String, Object> resultMap = Maps.newHashMap();
		return resultMap;
	}

	/**
	 * 전결테이블 목록을 삭제한다.
	 *
	 */
	public Map<String, Object> deleteListTable(Map param) {
		List<Map<String, Object>> deleteTables = (List<Map<String, Object>>)param.get("deleteTables");
		List<Map<String, Object>> deleteList = Lists.newArrayList();
		
		for (Map<String, Object> data : deleteTables) {
			deleteList.add(data);
		}
		
		for (Map<String, Object> data : deleteList) {
			this.deleteTableFactorByTbId(data); // 전결테이블항목 삭제
		}
		for (Map<String, Object> data : deleteList) {
			this.deleteTable(data); // 전결테이블 삭제
		}
		
		Map<String, Object> resultMap = Maps.newHashMap();
		return resultMap;
	}
	
	/**
	 * 전결테이블항목 목록을 삭제한다.
	 *
	 */
	public Map<String, Object> deleteListTableFactor(Map param) {
		List<Map<String, Object>> deleteTableFactors = (List<Map<String, Object>>)param.get("deleteTableFactors");
		List<Map<String, Object>> deleteList = Lists.newArrayList();
		
		for (Map<String, Object> data : deleteTableFactors) {
			deleteList.add(data);
		}
		
		for (Map<String, Object> data : deleteList) {
			this.deleteTableFactor(data); // 전결테이블항목 삭제
		}
		
		Map<String, Object> resultMap = Maps.newHashMap();
		return resultMap;
	}
	
}
