package smartsuite.app.common.bid.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BidTemplateRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;




	public List findListColumnMaster(Map<String, Object> param) {
		return sqlSession.selectList("bid.findListColumnMaster", param);
	}

	public List findListColumnListNotGroup(Map<String, Object> param) {
		return sqlSession.selectList("bid.findListColumnListNotGroup", param);
	}

	public List findListGroupColumnMaster(Map<String, Object> param) {
		return sqlSession.selectList("bid.selectGroupColumnMasterList", param);
	}

	public void deleteListBidColumnMaster(Map row) {
		sqlSession.delete("bid.deleteListColumnMaster", row);
	}
	
	public int selectColumnMasterInfo(Map row) {
		return sqlSession.selectOne("bid.selectColumnMasterInfo", row);
	}

	public void updateColumnMasterInfo(Map row) {
		sqlSession.update("bid.updateColumnMasterInfo", row);
	}

	public void insertColumnMasterInfo(Map row) {
		sqlSession.insert("bid.insertColumnMasterInfo", row);
	}

	public List findListSheetMaster(Map<String, Object> param) {
	    return 	sqlSession.selectList("bid.findListSheetMaster", param);
	}

	public void deleteSheetMasterInfo(Map row) {
		sqlSession.delete("bid.deleteSheetMasterInfo", row);
	}

	public void deleteSheetMasterDataInfo(Map row) {
		sqlSession.delete("bid.deleteSheetMasterDataInfo", row);
	}

	public void deleteBidTemplateSheetDataForTmpSheetId(Map<String, Object> sheetInfo) {
		sqlSession.delete("bid.deleteBidTemplateSheetDataForTmpSheetId",sheetInfo);
	}

	public void deleteBidTemplateRowIdForTmpSheetId(Map<String, Object> sheetInfo) {
		sqlSession.delete("bid.deleteBidTemplateRowIdForTmpSheetId",sheetInfo);
	}

	public void deleteBidTemplateHeaderColumnsForTmpSheetId(Map<String, Object> sheetInfo) {
		sqlSession.delete("bid.deleteBidTemplateHeaderColumnsForTmpSheetId",sheetInfo);
	}

	public void deleteBidTemplateSheetForTmpSheetId(Map<String, Object> sheetInfo) {
		sqlSession.delete("bid.deleteBidTemplateSheetForTmpSheetId",sheetInfo);
	}

	public int selectSheetMasterInfo(Map row) {
		return sqlSession.selectOne("bid.selectSheetMasterInfo", row);
	}

	public void updateSheetMasterInfo(Map row) {
		sqlSession.update("bid.updateSheetMasterInfo", row);
	}

	public void insertSheetMasterInfo(Map row) {
		sqlSession.insert("bid.insertSheetMasterInfo", row);
	}

	public List findSheetColumnHeaders(Map<String, Object> param) {
		return sqlSession.selectList("bid.findSheetColumnHeaders", param);
	}

	public List findSheetMasterGroupColumnHeaders(Map<String, Object> param) {
		return sqlSession.selectList("bid.findSheetMasterGroupColumnHeaders", param);
	}

	public List findSheetMasterHeaderDepthGroupColumnHeaders(Map<String, Object> param) {
		return sqlSession.selectList("bid.findSheetMasterHeaderDepthGroupColumnHeaders", param);
	}

	public void updateSheetColumnInfo(Map row) {
		sqlSession.update("bid.updateSheetColumnInfo", row);
	}

	public void insertSheetColumnInfo(Map row) {
		sqlSession.insert("bid.insertSheetColumnInfo", row);
	}

	public List findListBidTemplateMaster(Map<String, Object> param) {
		return sqlSession.selectList("bid.findListBidTemplateMaster", param);
	}

	public List findListConfirmBidTemplate(Map<String, Object> param) {
		return sqlSession.selectList("bid.findListConfirmBidTemplate", param);
	}

	public List findListBidTemplateConfirm(Map<String, Object> param) {
		return sqlSession.selectList("bid.findListBidTemplateConfirm", param);
	}

	public int selectBidTemplateSheetInfo(Map row) {
		return sqlSession.selectOne("bid.selectBidTemplateSheetInfo", row);
	}

	public void updateBidTemplateInfo(Map row) {
		sqlSession.update("bid.updateBidTemplateInfo", row);
	}

	public void insertBidTemplateInfo(Map row) {
		sqlSession.insert("bid.insertBidTemplateInfo", row);
	}

	public int selectVendorTemplateAppCntForTemplateId(Map<String, Object> sheetInfo) {
		return sqlSession.selectOne("bid.selectVendorTemplateAppCntForTemplateId", sheetInfo);
	}

	public void deleteSheetColumnInfo(Map row) {
		sqlSession.delete("bid.deleteSheetColumnInfo", row);
	}

	public List findListBidTemplateSheet(Map<String, Object> param) {
		return sqlSession.selectList("bid.findListBidTemplateSheet", param);
	}

	public void confirmBidTemplateInfo(Map<String, Object> sheetInfo) {
		sqlSession.update("bid.confirmBidTemplateInfo", sheetInfo);
	}

	public void updateConfirmBidTemplate(Map row) {
		sqlSession.update("bid.updateConfirmBidTemplate", row);
	}

	public void confirmBidTemplateCancel(Map<String, Object> sheetInfo) {
		sqlSession.update("bid.confirmBidTemplateCancel", sheetInfo);
	}

	public void updateConfirmBidTemplateCancel(Map row) {
		sqlSession.update("bid.updateConfirmBidTemplateCancel", row);
	}

	public int findBidTemplateSheetCnt(Map<String, Object> sheetInfo) {
		return sqlSession.selectOne("bid.findBidTemplateSheetCnt", sheetInfo);
	}

	public void updateBidTemplateSheetInfo(Map<String, Object> sheetInfo) {
		sqlSession.update("bid.updateBidTemplateSheetInfo", sheetInfo);
	}

	public void insertBidTemplateSheetInfo(Map<String, Object> param) {
		sqlSession.insert("bid.insertBidTemplateSheetInfo", param);
	}

	public int updateCheckFindColumnInfo(Map row) {
		return  sqlSession.selectOne("bid.updateCheckFindColumnInfo", row);
	}

	public void updateBidTemplateSheetColumns(Map row) {
		sqlSession.update("bid.updateBidTemplateSheetColumns", row);
	}

	public void insertBidTemplateSheetColumns(Map row) {
		sqlSession.insert("bid.insertBidTemplateSheetColumns", row);
	}

	public void deleteBidTemplateSheetColumns(Map row) {
		sqlSession.delete("bid.deleteBidTemplateSheetColumns", row);
	}

	public List findBidTemplateSheetColumnHeaders(Map<String, Object> param) {
		return sqlSession.selectList("bid.findBidTemplateSheetColumnHeaders", param);
	}

	public List<Map<String, Object>> findTemplateSheetColumnAndDataListForSheet(Map<String, Object> param) {
		return sqlSession.selectList("bid.findTemplateSheetColumnAndDataListForSheet", param);
	}

	public List<Map<String, Object>> findBidTemplateSheetRowIdList(Map<String, Object> param) {
		return sqlSession.selectList("bid.findBidTemplateSheetRowIdList", param);
	}

	public Map findBidTemplateSheetRowInfo(Map<String, Object> param) {
		return sqlSession.selectOne("bid.findBidTemplateSheetRowInfo", param);
	}

	public int selectColumnDataCnt(Map<String, Object> sheetDataInfo) {
		return sqlSession.selectOne("bid.selectColumnDataCnt", sheetDataInfo);
	}

	public void insertSheetDataInfo(Map<String, Object> sheetDataInfo) {
		sqlSession.insert("bid.insertSheetDataInfo", sheetDataInfo);
	}

	public void updateSheetDataInfo(Map<String, Object> sheetDataInfo) {
		sqlSession.update("bid.updateSheetDataInfo", sheetDataInfo);
	}

	public void insertSheetRowInfo(Map<String, Object> rowInfo) {
		sqlSession.insert("bid.insertSheetRowInfo", rowInfo);
	}

	public Map findBidTemplateSheetColumnInfo(Map<String, Object> data) {
		return sqlSession.selectOne("bid.findBidTemplateSheetColumnInfo", data);
	}

	public void deleteBidTemplateSheetDataList(Map row) {
		sqlSession.delete("bid.deleteBidTemplateSheetDataList", row);
	}

	public void deleteBidTemplateSheetRowId(Map row) {
		sqlSession.delete("bid.deleteBidTemplateSheetRowId", row);
	}

	public List findColTypeCalcHeadersColumns(Map<String, Object> param) {
		return sqlSession.selectList("bid.findColTypeCalcHeadersColumns", param);
	}

	public List findBidTemplateHeadersColumnsForCalc(Map<String, Object> param) {
		return sqlSession.selectList("bid.findBidTemplateHeadersColumnsForCalc", param);
	}

	public List findBidTemplateSheetGroupColumnHeaders(Map<String, Object> param) {
		return sqlSession.selectList("bid.findBidTemplateSheetGroupColumnHeaders", param);
	}

	public List findBidTemplateSheetHeaderDepthGroupColumnHeaders(Map<String, Object> param) {
		return sqlSession.selectList("bid.findBidTemplateSheetHeaderDepthGroupColumnHeaders", param);
	}

	public Map<String, Object> findBidTemplateSheetInfo(Map<String, Object> setVendorSearchMap) {
		return sqlSession.selectOne("bid.findBidTemplateSheetInfo", setVendorSearchMap);
	}

	public List<Map<String, Object>> findListBidTemplateSheetForApp(Map<String, Object> setVendorSearchMap) {
		return sqlSession.selectList("bid.findListBidTemplateSheetForApp", setVendorSearchMap);
	}

	public List<Map<String, Object>> findTemplateSheetColumnAndDataListForPro(Map<String, Object> param) {
		return sqlSession.selectList("bid.findTemplateSheetColumnAndDataListForPro", param);
	}

	public List<Map<String, Object>> findBidTemplateSheetRowIdListForAppId(Map<String, Object> param) {
		return sqlSession.selectList("bid.findBidTemplateSheetRowIdListForAppId", param);
	}

	public Map<String, Object> selectVendorTemplateAppByVendorTemplId(Map<String, Object> getPrInfoMap) {
		return sqlSession.selectOne("bid.selectVendorTemplateAppByVendorTemplId", getPrInfoMap);
	}

	public int selectVendorTemplateAppCnt(Map<String, Object> setVendorSearchMap) {
		return 	sqlSession.selectOne("bid.selectVendorTemplateAppCnt", setVendorSearchMap);
	}

	public void insertBidTemplateAndAppIdRelation(Map<String, Object> setVendorSearchMap) {
		sqlSession.insert("bid.insertBidTemplateAndAppIdRelation", setVendorSearchMap);
	}

	public void updatePrHeaderVendorTemplId(Map<String, Object> setVendorSearchMap) {
		sqlSession.update("bid.updatePrHeaderVendorTemplId", setVendorSearchMap);
	}


	public int selectColumnDataBuyerCnt(Map<String, Object> sheetDataInfo) {
		return sqlSession.selectOne("bid.selectColumnDataBuyerCnt", sheetDataInfo);
	}

	public void updateSheetDataInfoForAppId(Map<String, Object> sheetDataInfo) {
		sqlSession.update("bid.updateSheetDataInfoForAppId", sheetDataInfo);
	}

	public void deleteBidTemplateSheetDataListForAppId(Map row) {
		sqlSession.delete("bid.deleteBidTemplateSheetDataListForAppId", row);
	}

	public void deleteBidTemplateSheetRowIdForAppId(Map row) {
		sqlSession.delete("bid.deleteBidTemplateSheetRowIdForAppId", row);
	}

	public void deleteBidTemplateSheetDataForAppId(Map<String, Object> getSheetInfo) {
		sqlSession.delete("bid.deleteBidTemplateSheetDataForAppId",getSheetInfo);
	}

	public void deleteBidTemplateRowIdForAppId(Map<String, Object> getSheetInfo) {
		sqlSession.delete("bid.deleteBidTemplateRowIdForAppId",getSheetInfo);
	}

	public void deleteBidTemplateSyncAppInfo(Map<String, Object> setSelectMap) {
		sqlSession.delete("bid.deleteBidTemplateSyncAppInfo",setSelectMap);
	}

	public void updateRFQItemVendorTemplId(Map<String, Object> setVendorSearchMap) {
		sqlSession.update("bid.updateRFQItemVendorTemplId", setVendorSearchMap);
	}

	public String findRFQItemVendorTemplId(Map<String, Object> setSelectMap) {
		return sqlSession.selectOne("bid.findRFQItemVendorTemplId", setSelectMap);
	}

	public void updateVendorTemplIdNull(Map<String, Object> setSelectMap) {
		sqlSession.update("bid.updateVendorTemplIdNull", setSelectMap);
	}

	public List findRFQTemplateConfirmYn(Map<String, Object> param) {
		return sqlSession.selectList("bid.findRFQTemplateConfirmYn", param);
	}
}



