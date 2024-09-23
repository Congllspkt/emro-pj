package smartsuite.app.bp.approval.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import smartsuite.app.bp.admin.validator.Validator;
import smartsuite.app.bp.admin.validator.ValidatorUtil;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

@Service
public class ApprovalValidator implements Validator {
	@Inject
	SqlSession sqlSession;

	@Override
	public ResultMap validate(Map<String, Object> param) {
		String aprvId = param.get("apvl_uuid") == null ? null : param.get("apvl_uuid").toString();

		if (StringUtils.isEmpty(aprvId)) {
			// 최초 결재 생성(상신) 시
			// appId(업무ID)로 결재상신 건이 존재하는 체크한다.
			
			String dupCheckAppId = sqlSession.selectOne("approval-master.checkAppMasterDupAppId", param);
			if(!StringUtils.isEmpty(dupCheckAppId)){
				throw new CommonException(ErrorCode.DUPLICATED);
			} 
			
			
		} else {
			// 결재 수정/상신/삭제 시
			// 결제모듈 호출 시점의 결재진행상태와 서버상의 결재진행상태가 일치하는지 체크한다.
			Map<String, Object> checkResult = sqlSession.selectOne("approval-master.compareAprvSts", param);
			ValidatorUtil.handleResultMapByCheckResult(checkResult);
		}
		
		return ResultMap.SUCCESS();
	}

}
