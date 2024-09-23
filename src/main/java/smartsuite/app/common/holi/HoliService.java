package smartsuite.app.common.holi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.common.holi.repository.HoliRepository;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * Holi 관련 처리하는 서비스 Class입니다.
 *
 * @see 
 * @FileName HoliService.java
 * @package smartsuite.app.common.holi
 * @Since 2020. 12. 9
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class HoliService {

	@Inject
	HoliRepository holiRepository;
	
	/**
	 * list hd 조회한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2020. 12. 9
	 * @Method Name : findListHd
	 */
	public List<Map<String, Object>> findListHd(Map<String, Object> param) {
		return holiRepository.findListHd(param);
	}

	/**
	 * list hd 저장한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2020. 12. 9
	 * @Method Name : saveListHd
	 */
	public Map<String, Object> saveListHd(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, Object>> inserts = param.get("insertHds") == null ? Lists.newArrayList() : (List < Map < String, Object >>)param.get("insertHds");
		List<Map<String, Object>> updates = param.get("updateHds") == null ? Lists.newArrayList() : (List < Map < String, Object >>)param.get("updateHds");

		if (inserts.size() > 0) {
			boolean exist = false;
			for (Map<String, Object> row : inserts) {
				if (this.existHoliHdByDate(row)) { // 중복체크
					exist = true;
					break;
				}
			}
			if (exist) {
				throw new CommonException(ErrorCode.DUPLICATED);
			}

			for (Map<String, Object> row : inserts) {
				this.insertHd(row);
			}
		}
		if (updates != null && !updates.isEmpty()) {
			for (Map<String, Object> row : updates) {
				this.updateHd(row);
			}
		}
		return resultMap;
	}

	/**
	 * count hd by date의 값을 반환한다.
	 *
	 * @param row the row
	 * @return count hd by date
	 */
	private boolean existHoliHdByDate(Map<String, Object> row) {
		return (holiRepository.getCountHdByDate(row) > 0);
	}

	/**
	 * hd 수정한다.
	 *
	 * @param row the row
	 * @Date : 2020. 12. 9
	 * @Method Name : updateHd
	 */
	private void updateHd(Map<String, Object> row) {
		holiRepository.updateHd(row);
	}

	/**
	 * hd 입력한다.
	 *
	 * @param row the row
	 * @Date : 2020. 12. 9
	 * @Method Name : insertHd
	 */
	private void insertHd(Map<String, Object> row) {
		holiRepository.insertHd(row);
	}

	/**
	 * list hd 삭제한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2020. 12. 9
	 * @Method Name : deleteListHd
	 */
	public Map<String, Object> deleteListHd(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, Object>> deletes = (List<Map<String, Object>>)param.get("deleteHds");

		if (deletes != null && !deletes.isEmpty()) {
			for (Map<String, Object> row : deletes) {
				this.deleteHd(row);
			}
		}
		return resultMap;
	}

	/**
	 * hd 삭제한다.
	 *
	 * @param row the row
	 * @Date : 2020. 12. 9
	 * @Method Name : deleteHd
	 */
	private void deleteHd(Map<String, Object> row) {
		holiRepository.deleteHd(row);
	}
}

