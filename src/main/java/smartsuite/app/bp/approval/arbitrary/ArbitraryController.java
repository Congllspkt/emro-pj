package smartsuite.app.bp.approval.arbitrary;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.approval.arbitrary.service.ArbitraryDataSourceService;
import smartsuite.app.bp.approval.arbitrary.service.ArbitraryFactorService;
import smartsuite.app.bp.approval.arbitrary.service.ArbitraryManageService;
import smartsuite.app.bp.approval.arbitrary.service.ArbitraryTableService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.data.FloaterStream;

/**
 * 전결테이블 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @Since 2019.02.20
 */
@Controller
@RequestMapping (value = "**/bp/approval/arbitrary/**")
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ArbitraryController {

	@Inject
	ArbitraryFactorService arbitraryFactorService;

	@Inject
	ArbitraryTableService arbitraryTableService;

	@Inject
	ArbitraryManageService arbitraryManageService;
	
	@Inject
	ArbitraryDataSourceService arbitraryDataSourceService;
	
	/**
	 * 전결설정항목 목록 조회 요청
	 */
	@RequestMapping (value = "selectListFactor.do")
	public @ResponseBody FloaterStream selectListFactor(@RequestBody Map param) {
		return arbitraryFactorService.selectListFactor(param);
	}

	/**
	 * 전결설정항목 상세정보 조회 요청
	 */
	@RequestMapping (value = "selectFactor.do")
	public @ResponseBody Map selectFactor(@RequestBody Map param) {
		return arbitraryFactorService.selectFactor(param);
	}
	
	/**
	 * 데이터소스 목록 조회 요청
	 */
	@RequestMapping (value = "selectListDataSource.do")
	public @ResponseBody List selectListDataSource(@RequestBody Map param) {
		return arbitraryDataSourceService.selectListDataSource(param);
	}
	
	/**
	 * 전결테이블 목록 조회 요청
	 */
	@RequestMapping (value = "selectListTable.do")
	public @ResponseBody FloaterStream selectListTable(@RequestBody Map param) {
		return arbitraryTableService.selectListTable(param);
	}
	
	/**
	 * 전결테이블항목 목록 조회 요청
	 */
	@RequestMapping (value = "selectListTableFactor.do")
	public @ResponseBody FloaterStream selectListTableFactor(@RequestBody Map param) {
		return arbitraryTableService.selectListTableFactor(param);
	}

	/**
	 * 결재유형 목록 조회 요청
	 */
	@RequestMapping (value = "selectListTableAprvType.do")
	public @ResponseBody FloaterStream selectListTableAprvType(@RequestBody Map param) {
		return arbitraryTableService.selectListTableAprvType(param);
	}

	/**
	 * 결재유형 목록 조회 요청
	 */
	@RequestMapping (value = "selectListArbitraryAprvType.do")
	public @ResponseBody FloaterStream selectListArbitraryAprvType(@RequestBody Map param) {
		return arbitraryManageService.selectListArbitraryAprvType(param);
	}

	/**
	 * 전결규정 헤더 목록 조회 요청
	 */
	@RequestMapping (value = "selectListArbitraryHeader.do")
	public @ResponseBody FloaterStream selectListArbitraryHeader(@RequestBody Map param) {
		return arbitraryManageService.selectListArbitraryHeader(param);
	}
	
	/**
	 * 전결규정 정보 조회 요청 - 헤더/상세테이블/가능한 적용시작일
	 */
	@RequestMapping (value = "selectArbitraryData.do")
	public @ResponseBody Map selectArbitraryData(@RequestBody Map param) {
		return arbitraryManageService.selectArbitraryData(param);
	}

	/**
	 * 전결규정 상세테이블 항목 목록 조회 요청
	 */
	@RequestMapping (value = "selectListArbitraryDetailFactor.do")
	public @ResponseBody FloaterStream selectListArbitraryDetailFactor(@RequestBody Map param) {
		return arbitraryManageService.selectListArbitraryDetailFactor(param);
	}
	
	
	/**
	 * 전결규정 전결결재선 목록 조회 요청
	 */
	@RequestMapping (value = "selectListArbitraryAprvLineWithDetail.do")
	public @ResponseBody List selectListArbitraryAprvLineWithDetail(@RequestBody Map param) {
		return arbitraryManageService.selectListArbitraryAprvLineWithDetail(param);
	}

	/**
	 * 전결규정 전결결재선 상세 목록 조회 요청
	 */
	@RequestMapping (value = "selectListArbitraryAprvLineDetail.do")
	public @ResponseBody FloaterStream selectListArbitraryAprvLineDetail(@RequestBody Map param) {
		return arbitraryManageService.selectListArbitraryAprvLineDetail(param);
	}

	/**
	 * 전결규정 전결결재선 조건 목록 조회 요청
	 */
	@RequestMapping (value = "selectListArbitraryAprvLineCondition.do")
	public @ResponseBody List selectListArbitraryAprvLineCondition(@RequestBody Map param) {
		return arbitraryManageService.selectListArbitraryAprvLineCondition(param);
	}
	
	/**
	 * 전결규정 전결결재선 정보 조회 요청
	 */
	@RequestMapping (value = "selectListApplyInfoByAprvType.do")
	public @ResponseBody Map selectListApplyInfoByAprvType(@RequestBody Map param) {
		return arbitraryManageService.selectArbitrayYnByAprvType(param);
	}
	
	/**
	 * 전결규정 전결결재선 조건과 일치하는 결재선 목록 조회 요청
	 */
	@RequestMapping (value = "selectListApplyMatchLineByCondition.do")
	public @ResponseBody List selectListApplyMatchLineByCondition(@RequestBody Map param) {
		return arbitraryManageService.selectListArbitrayLine(param);
	}
	
	
	
	/**
	 * 전결설정항목 상세정보 저장 요청
	 */
	@RequestMapping (value = "saveFactor.do")
	public @ResponseBody Map saveFactor(@RequestBody Map param) {
		return arbitraryFactorService.saveFactor(param);
	}

	/**
	 * 전결테이블 목록 저장 요청
	 */
	@RequestMapping (value = "saveListTable.do")
	public @ResponseBody Map saveListTable(@RequestBody Map param) {
		return arbitraryTableService.saveListTable(param);
	}

	/**
	 * 전결테이블항목 목록 저장 요청
	 */
	@RequestMapping (value = "saveListTableFactor.do")
	public @ResponseBody Map saveListTableFactor(@RequestBody Map param) {
		return arbitraryTableService.saveListTableFactor(param);
	}

	/**
	 * 전결규정 결재유형 정보 목록 저장 요청
	 */
	@RequestMapping (value = "saveListArbitraryAprvType.do")
	public @ResponseBody Map saveListArbitraryAprvType(@RequestBody Map param) {
		return arbitraryManageService.saveListArbitraryAprvType(param);
	}
	
	/**
	 * 전결규정 정보 저장 요청 - 헤더/상세항목 - 임시저장
	 */
	@RequestMapping (value = "saveDraftArbitraryData.do")
	public @ResponseBody Map saveDraftArbitraryData(@RequestBody Map param) {
		return arbitraryManageService.saveDraftArbitraryData(param);
	}
	
	/**
	 * 전결규정 정보 저장 요청 - 헤더/상세항목 - 확정
	 */
	@RequestMapping (value = "saveFixArbitraryData.do")
	public @ResponseBody Map saveFixArbitraryData(@RequestBody Map param) {
		return arbitraryManageService.saveFixArbitraryData(param);
	}
	
	/**
	 * 전결규정 정보 저장 요청 - 헤더 - 확정취소
	 */
	@RequestMapping (value = "saveCancelArbitraryHeader.do")
	public @ResponseBody Map saveCancelArbitraryHeader(@RequestBody Map param) {
		return arbitraryManageService.saveCancelArbitraryHeader(param);
	}
	
	/**
	 * 전결규정 정보 저장 요청 - 복사 생성
	 */
	@RequestMapping (value = "saveCopyArbitraryData.do")
	public @ResponseBody Map saveCopyArbitraryData(@RequestBody Map param) {
		return arbitraryManageService.saveCopyArbitraryData(param);
	}
	
	/**
	 * 전결규정 전결결재선 및 결재상세 목록 저장 요청
	 */
	@RequestMapping (value = "saveListArbitraryAprvLine.do")
	public @ResponseBody Map saveListArbitraryAprvLine(@RequestBody Map param) {
		return arbitraryManageService.saveListArbitraryAprvLine(param);
	}
	
	/**
	 * 전결규정 전결결재선 조건 저장 요청
	 */
	@RequestMapping (value = "saveListArbitraryAprvLineDetail.do")
	public @ResponseBody Map saveListArbitraryAprvLineDetail(@RequestBody Map param) {
		return arbitraryManageService.saveListArbitraryAprvLineDetail(param);
	}
	
	/**
	 * 전결규정 전결결재선 조건 목록 저장 요청
	 */
	@RequestMapping (value = "saveListArbitraryAprvLineCondition.do")
	public @ResponseBody ResultMap saveListArbitraryAprvLineCondition(@RequestBody Map param) {
		return arbitraryManageService.saveListArbitraryAprvLineCondition(param);
	}
	
	
	
	/**
	 * 전결설정항목 목록 삭제 요청
	 */
	@RequestMapping (value = "deleteListFactor.do")
	public @ResponseBody Map deleteListFactor(@RequestBody Map param) {
		return arbitraryFactorService.deleteListFactor(param);
	}
	
	/**
	 * 전결테이블 목록 삭제 요청
	 */
	@RequestMapping (value = "deleteListTable.do")
	public @ResponseBody Map deleteListTable(@RequestBody Map param) {
		return arbitraryTableService.deleteListTable(param);
	}

	/**
	 * 전결테이블항목 목록 삭제 요청
	 */
	@RequestMapping (value = "deleteListTableFactor.do")
	public @ResponseBody Map deleteListTableFactor(@RequestBody Map param) {
		return arbitraryTableService.deleteListTableFactor(param);
	}

	/**
	 * 전결규정 헤더 목록 삭제 요청
	 */
	@RequestMapping (value = "deleteListArbitraryHeader.do")
	public @ResponseBody Map deleteListArbitraryHeader(@RequestBody Map param) {
		return arbitraryManageService.deleteListArbitraryHeader(param);
	}
	
	/**
	 * 전결규정 상세테이블 목록 삭제 요청
	 */
	@RequestMapping (value = "deleteListArbitraryDetailTable.do")
	public @ResponseBody ResultMap deleteListArbitraryDetailTable(@RequestBody Map param) {
		return arbitraryManageService.deleteListArbitraryDetailTable(param);
	}
	
	/**
	 * 전결규정 전결결재선 목록 삭제 요청
	 */
	@RequestMapping (value = "deleteListArbitraryAprvLine.do")
	public @ResponseBody ResultMap deleteListArbitraryAprvLine(@RequestBody Map param) {
		return arbitraryManageService.deleteListArbitraryAprvLine(param);
	}
	
	/**
	 * 전결규정 전결결재선 조건 목록 삭제 요청
	 */
	@RequestMapping (value = "deleteListArbitraryAprvLineCondition.do")
	public @ResponseBody ResultMap deleteListArbitraryAprvLineCondition(@RequestBody Map param) {
		return arbitraryManageService.deleteListArbitraryAprvLineCondition(param);
	}
	
	/**
	 * 전결규정 전결결재선 상세목록 삭제 요청
	 */
	@RequestMapping (value = "deleteListArbitraryAprvLineDetail.do")
	public @ResponseBody ResultMap deleteListArbitraryAprvLineDetail(@RequestBody Map param) {
		return arbitraryManageService.deleteListArbitraryAprvLineDetail(param);
	}
	
	/**
	 * 전결규정 전결결재선 조건 초기화 요청
	 */
	@RequestMapping (value = "resetArbitraryDetailFactor.do")
	public @ResponseBody Map resetArbitraryDetailFactor(@RequestBody Map param) {
		return arbitraryManageService.resetArbitraryDetailFactor(param);
	}
	
}
