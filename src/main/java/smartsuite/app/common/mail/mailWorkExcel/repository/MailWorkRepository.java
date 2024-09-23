package smartsuite.app.common.mail.mailWorkExcel.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.excel.bean.CellInfoBean;
import smartsuite.app.common.excel.bean.ExcelInfoBean;
import smartsuite.app.common.excel.bean.RowInfoBean;
import smartsuite.app.common.excel.bean.SheetInfoBean;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MailWorkRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE =  "mail-work.";


	public Map<String, Object> findListMailTemplate(Map<String, Object> param) {
		return sqlSession.selectOne( NAMESPACE+ "findListMailTemplate", param);
	}

	public String findCloseYnExcelMailAP(Map<String, Object> row) {
		return sqlSession.selectOne(NAMESPACE+ "findCloseYnExcelMailAP", row);
	}

	public void insertExcelWorkSendMail(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE +"insertExcelWorkSendMail", param);
	}
	
	public void updateExcelWorkSendMail(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateExcelWorkSendMail", param);
	}

	public void insertExcelWorkApplicationProcess(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE +"insertExcelWorkApplicationProcess", param);
	}
	
	public Map<String, Object> findExcelSendMailById(Map<String, Object> mail) {
		return sqlSession.selectOne(NAMESPACE + "findExcelSendMailById", mail);
	}

	public void updateEmailWorkSendComplete(Map<String, Object> mailParam) {
		sqlSession.update(NAMESPACE + "updateEmailWorkSendComplete", mailParam);
	}

	public void updateEmailWorkSendSuccess(Map<String, Object> mailParam) {
		sqlSession.update(NAMESPACE + "updateEmailWorkSendSuccess", mailParam);
	}

	public void updateUseYnSaveExcel(Map<String, Object> excelInfo) {
		sqlSession.update(NAMESPACE + "updateUseYnSaveExcel", excelInfo);
	}

	public void updateExcelInfoConfirmYn(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateExcelInfoConfirmYn", param);
	}

	public void updateExcelInfo(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateExcelInfo", param);
	}

	public void deleteExcelCellInfo(Map<String, Object> deleteExcelInfo) {
		sqlSession.delete(NAMESPACE + "deleteExcelCellInfo",deleteExcelInfo);
	}

	public void deleteExcelRowInfo(Map<String, Object> deleteExcelInfo) {
		sqlSession.delete(NAMESPACE + "deleteExcelRowInfo",deleteExcelInfo);
	}

	public void deleteExcelSheetInfo(Map<String, Object> deleteExcelInfo) {
		sqlSession.delete(NAMESPACE + "deleteExcelSheetInfo",deleteExcelInfo);
	}

	public List<Map<String, Object>> findListEmailWorkMailInfo(Map<String, Object> deleteExcelInfo) {
		return sqlSession.selectList(NAMESPACE + "findListEmailWorkMailInfo",deleteExcelInfo);
	}

	public List<String> findListEmailWorkTaskSubjectUUID(Map<String, Object> deleteExcelInfo) {
		return sqlSession.selectList(NAMESPACE + "findListEmailWorkTaskSubjectUUID",deleteExcelInfo);
	}

	public void deleteEmailWorkSendMailList(Map<String, Object> emailWorkProcMap) {
		sqlSession.delete(NAMESPACE + "deleteEmailWorkSendMailList",emailWorkProcMap);
	}

	public void deleteEmailWorkMailInfo(Map<String, Object> emailWorkProcMap) {
		sqlSession.delete(NAMESPACE + "deleteEmailWorkMailInfo",emailWorkProcMap);
	}

	public void deleteExcelInfo(Map<String, Object> deleteExcelInfo) {
		sqlSession.delete(NAMESPACE + "deleteExcelInfo",deleteExcelInfo);
	}

	public void insertMailWorkExcelInfo(ExcelInfoBean excelInfoBean) {
		sqlSession.insert(NAMESPACE + "insertMailWorkExcelInfo", excelInfoBean);
	}


	public void insertExcelSheetInfo(SheetInfoBean sheetInfo) {
		sqlSession.insert(NAMESPACE + "insertExcelSheetInfo", sheetInfo);
	}

	public void insertExcelRowInfo(RowInfoBean row) {
		sqlSession.insert(NAMESPACE + "insertExcelRowInfo", row);
	}

	public void insertExcelCellInfo(CellInfoBean cellInfo) {
		sqlSession.insert(NAMESPACE + "insertExcelCellInfo", cellInfo);
	}

	public List<Map<String, Object>> findListWorkExcelList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListWorkExcelList", param);
	}

	public List<Map<String, Object>> findExcelRowListBySheetId(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findExcelRowListBySheetId", param);
	}

	public List<Map<String, Object>> findExcelCellListByRowId(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findExcelCellListByRowId", param);
	}

	public List<CellInfoBean> findListWorkExcelSheetCellList(RowInfoBean param) {
		return sqlSession.selectList(NAMESPACE + "findListWorkExcelSheetCellList", param);
	}

	public List<Map<String, Object>> findListExcelInfoList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListExcelInfoList", param);
	}


	public List<Map<String, Object>> findListSheetInfoListCombo(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListSheetInfoListCombo", param);
	}

	public List<SheetInfoBean> findWorkExcelSheet(ExcelInfoBean excelInfoBean) {
		return sqlSession.selectList(NAMESPACE + "findWorkExcelSheet", excelInfoBean);
	}

	public List<RowInfoBean> findListWorkExcelSheetRow(SheetInfoBean sheetInfoBean) {
		return sqlSession.selectList(NAMESPACE + "findListWorkExcelSheetRow", sheetInfoBean);
	}

	public ExcelInfoBean findWorkExcelInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findWorkExcelInfo", param);
	}

	public ExcelInfoBean findWorkExcelInfoByWorkCd(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findWorkExcelInfoByWorkCd", param);
	}

	public List<Map<String, Object>> findListEmailWorkSendHistory(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListEmailWorkSendHistory", param);
	}

	public List<Map<String, Object>> getMailWorkList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "getMailWorkList", param);
	}

	public void insertExcelRowInfo(Map<String, Object> insert) {
		sqlSession.insert(NAMESPACE + "insertExcelRowInfo", insert);
	}
}
