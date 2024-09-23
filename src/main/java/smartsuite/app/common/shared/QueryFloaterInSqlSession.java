package smartsuite.app.common.shared;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import smartsuite.app.common.shared.repository.SharedRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * QueryFloaterInSqlSession Class 입니다.
 */
@SuppressWarnings("unchecked")
@Service
public class QueryFloaterInSqlSession {

	/** The sql session. */
	@Inject
	SqlSession sqlSession;
	
	@Inject
	SharedRepository sharedRepository;
	
	/**
	 * Select list.
	 *
	 * @param statement the statement
	 * @param parameter the parameter
	 * @param inIds the in ids
	 * @return the list
	 */
	public List<Map<String, Object>> selectList(String statement, Object parameter,Object inIds){
		List<Map<String,Object>> resultList = Lists.newArrayList();
		List<String> taskIds = (List<String>)inIds;
		//대량 조회용 tempTable insert
		Map<String,Object> insertTemp = Maps.newHashMap();
		for(String taskId : taskIds){
			insertTemp.put("task_id", taskId);
			sharedRepository.insertTempQueryId(insertTemp);
		}
		
		resultList = sqlSession.selectList(statement,parameter);
		
		sharedRepository.deleteTempQueryId();
		
		return resultList; 
	}
	
	/**
	 * Select one.
	 *
	 * @param statement the statement
	 * @param parameter the parameter
	 * @param inIds the in ids
	 * @return the map
	 */
	public Map<String, Object> selectOne(String statement, Object parameter,Object inIds){
		Map<String,Object> result = Maps.newHashMap();
		List<String> taskIds = (List<String>)inIds;
		//대량 조회용 tempTable insert
		Map<String,Object> insertTemp = Maps.newHashMap();
		for(String taskId : taskIds){
			insertTemp.put("task_id", taskId);
			sharedRepository.insertTempQueryId(insertTemp);
		}
		
		result = sqlSession.selectOne(statement,parameter);
		
		sharedRepository.deleteTempQueryId();
		
		return result; 
	}
}
