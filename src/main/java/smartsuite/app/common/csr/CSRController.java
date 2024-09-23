package smartsuite.app.common.csr;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.common.csr.service.CSRService;

/**
 * CSR 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author WanSeob Kim
 * @since 2020. 2. 25
 * @FileName CSRController.java
 * @package smartsuite.app.common.csr
 */
@Controller
@RequestMapping (value = "**/bp/common/csr/")
public class CSRController {

	@Inject
	private CSRService csrService;

	/**
	 * [CSR 등록 및 현황] 목록 조회.
	 *
	 * @param param 조회 조건
	 * @return 조회 결과
	 */
	@RequestMapping (value = "findListCSRInfo.do")
	public @ResponseBody List<Map<String, Object>> findListCSRInfo(@RequestBody Map<String, Object> param) {
		return csrService.findListCSRInfo(param);

	}

	/**
	 * [CSR 접수 및 조치결과] 목록 조회.
	 *
	 * @param param 조회 조건
	 * @return 조회 결과
	 */
	@RequestMapping (value = "findListCSRReceipt.do")
	public @ResponseBody List<Map<String, Object>> findCSRReceiptList(@RequestBody Map<String, Object> param) {
		return csrService.findListCSRReceipt(param);

	}

	/**
	 * CSR 정보 조회 ( 요청내용 + 처리내용 ).
	 *
	 * @param param 조회 조건
	 * @return 조회 결과
	 */
	@RequestMapping (value = "findCSRInfo.do")
	public @ResponseBody Map<String, Object> findCSRInfo(@RequestBody Map<String, Object> param) {
		return csrService.findCSRInfo(param);

	}

	/**
	 * [CSR 등록 및 현황] CSR 임시저장.
	 *
	 * @param param CSR 정보
	 * @return CSR 아이디
	 */
	@RequestMapping (value = "templateSaveCSR.do")
	public @ResponseBody String templateSaveCSR(@RequestBody Map<String, Object> param) {
		return csrService.templateSaveCSR(param);
	}

	/**
	 * [CSR 등록 및 현황] CSR 요청 ( 임시저장 -> 접수대기 ).
	 *
	 * @param param CSR 정보
	 * @return CSR 아이디
	 */
	@RequestMapping (value = "requestCSR.do")
	public @ResponseBody String requestCSR(@RequestBody Map<String, Object> param) {
		return csrService.requestCSR(param);
	}

	/**
	 * [CSR 등록 및 현황] CSR 삭제 ( 임시저장 -> 삭제 ).
	 *
	 * @param param CSR 정보
	 */
	@RequestMapping (value = "deleteCSRInfo.do")
	public @ResponseBody void deleteCSRInfo(@RequestBody Map<String, Object> param) {
		csrService.deleteCSRInfoRequest(param);
	}

	/**
	 * [CSR 등록 및 현황] CSR 회수 ( 요청 -> 임시저장 ).
	 *
	 * @param param CSR 정보
	 * @return CSR 아이디
	 */
	@RequestMapping (value = "reCollectCSR.do")
	public @ResponseBody String reCollectCSR(@RequestBody Map<String, Object> param) {
		return csrService.reCollectCSR(param);
	}

	/**
	 * CSR 처리 정보 제출.
	 * 
	 * [처리담당자] 접수, 처리시작, 처리중지, 처리완료, 처리회수, 재처리시작, 재처리완료
	 * [요청자] 확인완료, 재처리요청
	 *
	 * @param param CSR 정보 및 처리 내역 정보
	 * @return CSR 아이디
	 */
	@RequestMapping (value = "processHistoryRequest.do")
	public @ResponseBody String processHistoryRequest(@RequestBody Map<String, Object> param) {
		return csrService.processHistoryRequest(param);
	}

	/**
	 * CSR 처리 내역 수정.
	 *
	 * @param param CSR 처리내역 정보
	 */
	@RequestMapping (value = "processHistoryContentModify.do")
	public @ResponseBody void processHistoryContentModify(@RequestBody Map<String, Object> param) {
		csrService.processHistoryContentModify(param);
	}

}
