package smartsuite.app.bp.approval.scheduler;

import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;
import smartsuite.scheduler.core.ScheduleService;

/**
 * 결재 스케쥴러 관련 Service
 * <pre>
 * saveApprovalLineResult : 결재선 결과 저장 스케쥴러 등록
 * </pre>
 */
@Service
@Transactional
public class ApprovalSchedulerService {

	/** The schedule service. */
	@Inject
	ScheduleService scheduleService;
	
	public void saveApprovalLineResult(Map<String, Object> param) {
		if(param.get("apvlln_uuid") != null) {
			String aprvlnId = (String)param.get("apvlln_uuid");	//unique key
			Object[] args = new Object[]{param};
			
			Date startScheduleTime = new Date();
			if(param.get("startScheduleTime") != null)
				startScheduleTime = (Date)param.get("startScheduleTime");
			
			try {
				scheduleService.removeScheduleTrigger(Class.forName(ApprovalJobConst.APRV_SERVICE_CLASS_NAME),
						ApprovalJobConst.SAVE_APRV_LINE_RESULT_METHOD_NAME, 
						ApprovalJobConst.APRV_JOB_GROUP,
						aprvlnId);
				scheduleService.registSchedule(Class.forName(ApprovalJobConst.APRV_SERVICE_CLASS_NAME),
						ApprovalJobConst.SAVE_APRV_LINE_RESULT_METHOD_NAME,
						args,
						startScheduleTime,
						ApprovalJobConst.APRV_JOB_GROUP,
						aprvlnId,
						ApprovalJobConst.SAVE_APRV_LINE_RESULT_JOB_NAME,
						null);
			} catch (ClassNotFoundException e) {
				throw new CommonException(ErrorCode.FAIL, e);
			}
		}
	}
}
